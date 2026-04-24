<#
.SYNOPSIS
    Unified image processing script for Slay the Spire mod.
    Tries Python (rembg) first, falls back to Node.js (sharp) if Python is unavailable.

.DESCRIPTION
    This script acts as an entry point for image processing tasks.
    Strategy:
      1. Try Python with rembg (AI-based, higher quality)
      2. If Python/rembg is unavailable, fall back to Node.js with sharp (threshold-based)

.PARAMETER File
    Path to the image file to process.

.PARAMETER Type
    Image type: card, cardback, relic, power, select, character, orb.
    Only used by the Python script. JS fallback only does background removal.

.PARAMETER KeepBg
    If set, skips background removal (only resizes/processes).

.PARAMETER ForceFallback
    If set, skips Python and goes straight to JS fallback.

.PARAMETER Threshold
    White threshold for JS fallback (0-255). Default: 240.

.PARAMETER EdgeSmooth
    Edge smoothing range for JS fallback. Default: 20.

.EXAMPLE
    .\process.ps1 -File "..\src\main\resources\jujutsumod\images\relics\SukunaFinger.png" -Type relic
    .\process.ps1 -File "image.png" -Type relic -ForceFallback
#>

param(
    [Parameter(Mandatory=$true)]
    [string]$File,

    [Parameter(Mandatory=$false)]
    [ValidateSet("card", "cardback", "relic", "power", "select", "character", "orb")]
    [string]$Type = "relic",

    [switch]$KeepBg,
    [switch]$ForceFallback,

    [int]$Threshold = 240,
    [int]$EdgeSmooth = 20
)

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ResolvedFile = Resolve-Path $File -ErrorAction SilentlyContinue

if (-not $ResolvedFile) {
    Write-Host "[ERROR] File not found: $File" -ForegroundColor Red
    exit 1
}

$FilePath = $ResolvedFile.Path
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Slay the Spire - Image Processor" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  File: $FilePath"
Write-Host "  Type: $Type"
Write-Host ""

# --- Strategy 1: Python (rembg) ---
function Try-PythonProcessing {
    Write-Host "[STEP 1] Trying Python (rembg)..." -ForegroundColor Yellow

    # Check if Python is available
    $pythonCmd = $null
    foreach ($cmd in @("python", "python3", "py")) {
        try {
            $version = & $cmd --version 2>&1
            if ($LASTEXITCODE -eq 0 -and $version -match "Python") {
                $pythonCmd = $cmd
                Write-Host "  -> Found: $version" -ForegroundColor Green
                break
            }
        } catch {
            # Command not found, continue
        }
    }

    if (-not $pythonCmd) {
        Write-Host "  -> Python not found." -ForegroundColor Red
        return $false
    }

    # Check if rembg is installed
    $rembgCheck = & $pythonCmd -c "import rembg; print('ok')" 2>&1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  -> rembg not installed. Attempting install..." -ForegroundColor Yellow
        & $pythonCmd -m pip install "rembg[cpu]" "Pillow>=10.0.0" 2>&1 | Out-Null
        
        # Re-check
        $rembgCheck = & $pythonCmd -c "import rembg; print('ok')" 2>&1
        if ($LASTEXITCODE -ne 0) {
            Write-Host "  -> Failed to install rembg." -ForegroundColor Red
            return $false
        }
    }
    Write-Host "  -> rembg is available." -ForegroundColor Green

    # Run the Python script
    $pyScript = Join-Path $ScriptDir "process_images.py"
    $pyArgs = @($pyScript, $FilePath, $Type)
    if ($KeepBg) { $pyArgs += "--keep-bg" }

    Write-Host "  -> Running: $pythonCmd $($pyArgs -join ' ')" -ForegroundColor Gray
    & $pythonCmd @pyArgs
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  -> Python processing succeeded!" -ForegroundColor Green
        return $true
    } else {
        Write-Host "  -> Python processing failed (exit code: $LASTEXITCODE)." -ForegroundColor Red
        return $false
    }
}

# --- Strategy 2: Node.js (sharp) ---
function Try-NodeFallback {
    Write-Host "[STEP 2] Trying Node.js fallback (sharp)..." -ForegroundColor Yellow

    # Check if Node.js is available
    try {
        $nodeVersion = & node --version 2>&1
        if ($LASTEXITCODE -ne 0) {
            Write-Host "  -> Node.js not found." -ForegroundColor Red
            return $false
        }
        Write-Host "  -> Found: Node.js $nodeVersion" -ForegroundColor Green
    } catch {
        Write-Host "  -> Node.js not found." -ForegroundColor Red
        return $false
    }

    # Check if sharp is available, install if needed
    $sharpCheck = & node -e "require('sharp')" 2>&1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  -> sharp not installed. Installing..." -ForegroundColor Yellow
        Push-Location $ScriptDir
        & npm install sharp 2>&1 | Out-Null
        Pop-Location

        $sharpCheck = & node -e "require('sharp')" 2>&1
        if ($LASTEXITCODE -ne 0) {
            Write-Host "  -> Failed to install sharp." -ForegroundColor Red
            return $false
        }
    }
    Write-Host "  -> sharp is available." -ForegroundColor Green

    # Run the JS fallback script
    $jsScript = Join-Path $ScriptDir "remove_bg_fallback.mjs"
    $jsArgs = @($jsScript, $FilePath, "--threshold=$Threshold", "--edge-smooth=$EdgeSmooth")

    Write-Host "  -> Running: node $($jsArgs -join ' ')" -ForegroundColor Gray
    & node @jsArgs

    if ($LASTEXITCODE -eq 0) {
        Write-Host "  -> JS fallback succeeded!" -ForegroundColor Green
        return $true
    } else {
        Write-Host "  -> JS fallback failed (exit code: $LASTEXITCODE)." -ForegroundColor Red
        return $false
    }
}

# --- Main execution ---
$success = $false

if ($ForceFallback) {
    Write-Host "[INFO] Skipping Python (--ForceFallback)." -ForegroundColor Magenta
} else {
    $success = Try-PythonProcessing
}

if (-not $success) {
    if (-not $ForceFallback) {
        Write-Host ""
        Write-Host "[INFO] Falling back to JS approach..." -ForegroundColor Magenta
    }
    $success = Try-NodeFallback
}

Write-Host ""
if ($success) {
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "  Processing complete!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
} else {
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "  All strategies failed!" -ForegroundColor Red
    Write-Host "  Install Python + rembg OR Node.js + sharp" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    exit 1
}
