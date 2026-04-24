import os
import shutil
import time
from pathlib import Path
from PIL import Image
try:
    from rembg import remove, new_session
except ImportError:
    print("rembg package not found. Make sure to run 'pip install -r scripts/requirements.txt'")
    exit(1)

# Configuration
RAW_DIR = Path("raw_images")
DEST_DIR = Path("src/main/resources/jujutsumod/images")
BACKUP_DIR = Path("scripts/raw_backups")

# Sizes (width, height)
CONFIG = {
    "cards": {
        "portrait": (500, 380),
        "standard": (250, 190)
    },
    "powers": {
        "large": (84, 84),
        "standard": (32, 32)
    },
    "relics": {
        "large": (256, 256),
        "standard": (128, 128)
    },
    "character_select": {
        "portrait": (1920, 1200),
        "button": (200, 200)
    },
    "character_view": {
        "shoulder": (1920, 1136),
        "corpse": (512, 512)
    }
}

def resize_image(img, size):
    """Resizes image maintaining aspect ratio and centering it."""
    # We use LANCZOS for high quality downsampling
    img_aspect = img.width / img.height
    target_aspect = size[0] / size[1]

    if img_aspect > target_aspect:
        # Image is wider
        new_width = size[0]
        new_height = int(size[0] / img_aspect)
    else:
        # Image is taller
        new_height = size[1]
        new_width = int(size[1] * img_aspect)

    img = img.resize((new_width, new_height), Image.Resampling.LANCZOS)
    
    # Create a new transparent image with the exact target size
    new_img = Image.new("RGBA", size, (255, 255, 255, 0))
    
    # Paste the resized image into the center
    paste_x = (size[0] - new_width) // 2
    paste_y = (size[1] - new_height) // 2
    new_img.paste(img, (paste_x, paste_y), img)
    
    return new_img

def process_file(file_path: Path, session):
    print(f"\nProcessing: {file_path}")
    
    # Check if the file is in a known directory
    category = None
    sub_category = None
    
    # Path logic (e.g. raw_images/cards/attack/sword.png)
    parts = file_path.relative_to(RAW_DIR).parts
    if len(parts) >= 2 and parts[0] == "cards":
        category = "cards"
        sub_category = parts[1]
    elif parts[0] in ["relics", "powers"]:
        category = parts[0]
    elif parts[0] == "character":
        if len(parts) >= 2 and parts[1] == "select":
            category = "character_select"
            sub_category = parts[1]
        else:
            category = "character_view"
            sub_category = "main"
    
    if not category:
        print(f"Skipping {file_path}. Not in a recognized folder (cards, relics, powers).")
        return

    # 1. Remove background
    try:
        with open(file_path, 'rb') as i:
            input_data = i.read()
        print("  -> Removing background...")
        result_data = remove(
            input_data, 
            session=session, 
            post_process_mask=True,
            alpha_matting=True,
            alpha_matting_foreground_threshold=120,
            alpha_matting_background_threshold=5,
            alpha_matting_erode_size=0
        )        
        # Determine temporary path for logic
        temp_path = file_path.with_suffix('.temp.png')
        with open(temp_path, 'wb') as o:
            o.write(result_data)
            
        img = Image.open(temp_path).convert("RGBA")
    except Exception as e:
        print(f"  -> Error processing {file_path}: {e}")
        return

    # 2. Resize and Save
    base_name = file_path.stem
    if category == "cards":
        # Create standard and portrait
        portrait_img = resize_image(img, CONFIG["cards"]["portrait"])
        standard_img = resize_image(portrait_img, CONFIG["cards"]["standard"])
        
        dest_folder = DEST_DIR / "cards" / sub_category
        dest_folder.mkdir(parents=True, exist_ok=True)
        
        p_path = dest_folder / f"{base_name}_p.png"
        s_path = dest_folder / f"{base_name}.png"
        
        portrait_img.save(p_path)
        standard_img.save(s_path)
        print(f"  -> Saved Portrait: {p_path} (500x380)")
        print(f"  -> Saved Standard: {s_path} (250x190)")

    elif category in ["relics", "powers"]:
        large_img = resize_image(img, CONFIG[category]["large"])
        std_img = resize_image(large_img, CONFIG[category]["standard"])
        
        dest_large_folder = DEST_DIR / category / "large"
        dest_std_folder = DEST_DIR / category
        dest_large_folder.mkdir(parents=True, exist_ok=True)
        dest_std_folder.mkdir(parents=True, exist_ok=True)
        
        l_path = dest_large_folder / f"{base_name}.png"
        s_path = dest_std_folder / f"{base_name}.png"
        
        large_img.save(l_path)
        std_img.save(s_path)
        print(f"  -> Saved Large: {l_path}")
        print(f"  -> Saved Standard: {s_path}")

    elif category == "character_select":
        dest_folder = DEST_DIR / "character" / "select"
        dest_folder.mkdir(parents=True, exist_ok=True)
        
        if "portrait" in base_name.lower():
            img_resized = resize_image(img, CONFIG["character_select"]["portrait"])
            save_path = dest_folder / "portrait.png"
        elif "button" in base_name.lower():
            img_resized = resize_image(img, CONFIG["character_select"]["button"])
            save_path = dest_folder / "button.png"
        else:
            print(f"  -> Skipping {file_path}: Use 'portrait' or 'button' in the filename.")
            return

        img_resized.save(save_path)
        print(f"  -> Saved character select asset: {save_path}")

    elif category == "character_view":
        dest_folder = DEST_DIR / "character"
        dest_folder.mkdir(parents=True, exist_ok=True)
        
        if "shoulder" in base_name.lower():
             # Logic for shoulder1 and shoulder2
             target_name = "shoulder2.png" if "2" in base_name else "shoulder.png"
             img_resized = resize_image(img, CONFIG["character_view"]["shoulder"])
             save_path = dest_folder / target_name
        elif "corpse" in base_name.lower():
             img_resized = resize_image(img, CONFIG["character_view"]["corpse"])
             save_path = dest_folder / "corpse.png"
        else:
             print(f"  -> Skipping {file_path}: Use 'shoulder' or 'corpse' in the filename.")
             return

        img_resized.save(save_path)
        print(f"  -> Saved character view asset: {save_path}")
        
    # Cleanup temp image
    temp_path.unlink()
    
    # Move original to backup
    backup_folder = BACKUP_DIR / category
    if sub_category:
        backup_folder = backup_folder / sub_category
    backup_folder.mkdir(parents=True, exist_ok=True)
    
    shutil.move(str(file_path), str(backup_folder / file_path.name))
    print(f"  -> Moved original to backup: {backup_folder / file_path.name}")


def main():
    if not DEST_DIR.exists():
        print(f"Error: Target directory {DEST_DIR} does not exist. Are you in the project root?")
        return
        
    if not RAW_DIR.exists():
         RAW_DIR.mkdir()
         print(f"Created {RAW_DIR}. Place your AI generated images there.")
         return

    # To avoid downloading the model multiple times during a loop, initialize UI session
    print("Initializing AI Model...")
    session = new_session("isnet-general-use")    
    processed_any = False
    for root, _, files in os.walk(RAW_DIR):
        for file in files:
            if file.lower().endswith(('.png', '.jpg', '.jpeg', '.webp')):
                file_path = Path(root) / file
                process_file(file_path, session)
                processed_any = True

    if not processed_any:
         print(f"No images found in {RAW_DIR}.")
         print(f"Place image files in subfolders like {RAW_DIR}/cards/attack/ or {RAW_DIR}/relics/")
    else:
         print("\nDone! All images processed successfully.")

if __name__ == "__main__":
    main()
