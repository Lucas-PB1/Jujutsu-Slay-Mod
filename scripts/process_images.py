import os
import sys
import argparse
from pathlib import Path
from PIL import Image

try:
    from rembg import remove, new_session
except ImportError:
    print("rembg package not found. Run: pip install \"rembg[cpu]\"")
    sys.exit(1)

# Configuration for sizes
CONFIG = {
    "card": {"portrait": (500, 380), "standard": (250, 190)},
    "relic": {"large": (256, 256), "standard": (128, 128)},
    "power": {"large": (84, 84), "standard": (32, 32)},
    "select": {"portrait": (1920, 1200), "button": (200, 200)},
    "character": {"shoulder": (1920, 1136), "corpse": (512, 512)}
}

def resize_and_center(img, size, scale=1.0):
    # Apply padding if scale < 1.0
    work_size = (int(size[0] * scale), int(size[1] * scale))
    
    img_aspect = img.width / img.height
    target_aspect = work_size[0] / work_size[1]

    if img_aspect > target_aspect:
        new_width = work_size[0]
        new_height = int(work_size[0] / img_aspect)
    else:
        new_height = work_size[1]
        new_width = int(work_size[1] * img_aspect)

    img = img.resize((new_width, new_height), Image.Resampling.LANCZOS)
    
    # Create final image with full size
    new_img = Image.new("RGBA", size, (255, 255, 255, 0))
    paste_x = (size[0] - new_width) // 2
    paste_y = (size[1] - new_height) // 2
    new_img.paste(img, (paste_x, paste_y), img)
    return new_img

def process_single_image(file_path, img_type, keep_bg=False):
    path = Path(file_path)
    if not path.exists():
        print(f"Error: File {file_path} not found.")
        return

    print(f"Processing {img_type}: {path.name} (Keep BG: {keep_bg})...")
    
    if not keep_bg:
        session = new_session("isnet-general-use")
        with open(path, 'rb') as i:
            input_data = i.read()
        print("  -> Removing background...")
        result_data = remove(input_data, session=session, post_process_mask=True)
        temp_path = path.with_suffix('.temp.png')
        with open(temp_path, 'wb') as o:
            o.write(result_data)
        img = Image.open(temp_path).convert("RGBA")
    else:
        print("  -> Keeping original background...")
        img = Image.open(path).convert("RGBA")
        temp_path = None

    # 3. Resize and Save
    # Define scale based on type (relics, powers and select buttons need padding)
    scale = 1.0
    if img_type in ["relic", "power"]: scale = 0.7
    
    if img_type == "card":
        p_img = resize_and_center(img, CONFIG["card"]["portrait"])
        s_img = resize_and_center(p_img, CONFIG["card"]["standard"])
        p_img.save(path.parent / f"{path.stem}_p.png")
        s_img.save(path.parent / f"{path.stem}.png")
        print(f"  -> Saved {path.stem}.png and {path.stem}_p.png")

    elif img_type in ["relic", "power"]:
        l_img = resize_and_center(img, CONFIG[img_type]["large"], scale)
        s_img = resize_and_center(img, CONFIG[img_type]["standard"], scale)
        l_img.save(path.parent / f"{path.stem}_large.png")
        s_img.save(path.parent / f"{path.stem}.png")

    else: # character or select
        size_key = "portrait" if "portrait" in path.stem.lower() else "button" if "button" in path.stem.lower() else "shoulder" if "shoulder" in path.stem.lower() else "corpse"
        cat = "select" if img_type == "select" else "character"
        size = CONFIG[cat].get(size_key, (512, 512))
        
        # Apply 70% scale ONLY for the select button
        current_scale = 0.7 if size_key == "button" else 1.0
        
        img_resized = resize_and_center(img, size, current_scale)
        img_resized.save(path.parent / f"{path.stem}_processed.png")

    if temp_path and temp_path.exists():
        temp_path.unlink()
    print("Done!")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Process a single image for Slay the Spire")
    parser.add_argument("file", help="Path to the image file")
    parser.add_argument("type", choices=["card", "relic", "power", "select", "character"], help="Type of image")
    parser.add_argument("--keep-bg", action="store_true", help="Keep the original background")
    
    if len(sys.argv) < 3:
        parser.print_help()
        sys.exit(1)
        
    args = parser.parse_args()
    process_single_image(args.file, args.type, args.keep_bg)
