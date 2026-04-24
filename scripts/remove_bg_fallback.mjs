/**
 * Fallback background removal using sharp (Node.js).
 * Used when the Python rembg approach is unavailable or fails.
 * 
 * Usage: node remove_bg_fallback.mjs <image_path> [--threshold=240] [--edge-smooth=20]
 * 
 * This uses a simple color-threshold approach: pixels near white become transparent.
 * It's less accurate than AI-based rembg, but works without Python dependencies.
 */

import sharp from 'sharp';
import { existsSync } from 'fs';
import { rename } from 'fs/promises';
import { resolve } from 'path';

const args = process.argv.slice(2);
if (args.length < 1) {
  console.error('Usage: node remove_bg_fallback.mjs <image_path> [--threshold=240] [--edge-smooth=20]');
  process.exit(1);
}

const inputPath = resolve(args[0]);

if (!existsSync(inputPath)) {
  console.error(`Error: File not found: ${inputPath}`);
  process.exit(1);
}

// Parse optional flags
let threshold = 240;
let edgeSmooth = 20;

for (const arg of args.slice(1)) {
  if (arg.startsWith('--threshold=')) {
    threshold = parseInt(arg.split('=')[1], 10);
  } else if (arg.startsWith('--edge-smooth=')) {
    edgeSmooth = parseInt(arg.split('=')[1], 10);
  }
}

async function removeWhiteBackground() {
  console.log(`[JS Fallback] Removing white background from: ${inputPath}`);
  console.log(`  -> Threshold: ${threshold}, Edge smooth: ${edgeSmooth}`);

  const image = sharp(inputPath);
  const { width, height } = await image.metadata();

  // Get raw pixel data with alpha channel
  const rawBuffer = await image.ensureAlpha().raw().toBuffer();

  let pixelsChanged = 0;

  // Process each pixel (RGBA = 4 bytes per pixel)
  for (let i = 0; i < rawBuffer.length; i += 4) {
    const r = rawBuffer[i];
    const g = rawBuffer[i + 1];
    const b = rawBuffer[i + 2];

    if (r >= threshold && g >= threshold && b >= threshold) {
      // Fully white -> fully transparent
      rawBuffer[i + 3] = 0;
      pixelsChanged++;
    } else if (
      r >= threshold - edgeSmooth &&
      g >= threshold - edgeSmooth &&
      b >= threshold - edgeSmooth
    ) {
      // Near-white -> partially transparent for smooth edges
      const avg = (r + g + b) / 3;
      const alpha = Math.round(255 * (1 - (avg - (threshold - edgeSmooth)) / edgeSmooth));
      rawBuffer[i + 3] = Math.min(rawBuffer[i + 3], alpha);
      pixelsChanged++;
    }
  }

  const tmpPath = inputPath + '.tmp';
  await sharp(rawBuffer, { raw: { width, height, channels: 4 } })
    .png()
    .toFile(tmpPath);

  await rename(tmpPath, inputPath);

  const totalPixels = width * height;
  const pct = ((pixelsChanged / totalPixels) * 100).toFixed(1);
  console.log(`  -> Done! Changed ${pixelsChanged}/${totalPixels} pixels (${pct}%)`);
}

removeWhiteBackground().catch((err) => {
  console.error(`[JS Fallback] Error: ${err.message}`);
  process.exit(1);
});
