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

def resize_and_center(img, size):
    img_aspect = img.width / img.height
    target_aspect = size[0] / size[1]

    if img_aspect > target_aspect:
        new_width = size[0]
        new_height = int(size[0] / img_aspect)
    else:
        new_height = size[1]
        new_width = int(size[1] * img_aspect)

    img = img.resize((new_width, new_height), Image.Resampling.LANCZOS)
    new_img = Image.new("RGBA", size, (255, 255, 255, 0))
    paste_x = (size[0] - new_width) // 2
    paste_y = (size[1] - new_height) // 2
    new_img.paste(img, (paste_x, paste_y), img)
    return new_img

def process_single_image(file_path, img_type):
    path = Path(file_path)
    if not path.exists():
        print(f"Error: File {file_path} not found.")
        return

    print(f"Processing {img_type}: {path.name}...")
    session = new_session("isnet-general-use")

    # 1. Remove background
    with open(path, 'rb') as i:
        input_data = i.read()
    
    print("  -> Removing background...")
    result_data = remove(input_data, session=session, post_process_mask=True)
    
    # Save temp and open
    temp_path = path.with_suffix('.temp.png')
    with open(temp_path, 'wb') as o:
        o.write(result_data)
    img = Image.open(temp_path).convert("RGBA")

    # 2. Resize and Save in the same folder
    if img_type == "card":
        p_img = resize_and_center(img, CONFIG["card"]["portrait"])
        s_img = resize_and_center(p_img, CONFIG["card"]["standard"])
        p_img.save(path.parent / f"{path.stem}_p.png")
        s_img.save(path.parent / f"{path.stem}.png")
        print(f"  -> Saved {path.stem}.png and {path.stem}_p.png in {path.parent}")

    elif img_type in ["relic", "power"]:
        l_img = resize_and_center(img, CONFIG[img_type]["large"])
        s_img = resize_and_center(l_img, CONFIG[img_type]["standard"])
        
        # Relics and Powers often have a 'large' subfolder, but we'll save in place or as specified
        l_path = path.parent / f"{path.stem}_large.png"
        s_path = path.parent / f"{path.stem}.png"
        l_img.save(l_path)
        s_img.save(s_path)
        print(f"  -> Saved {s_path.name} and {l_path.name}")

    else: # character or select
        size_key = "portrait" if "portrait" in path.stem.lower() else "button" if "button" in path.stem.lower() else "shoulder" if "shoulder" in path.stem.lower() else "corpse"
        cat = "select" if img_type == "select" else "character"
        size = CONFIG[cat].get(size_key, (512, 512))
        
        img_resized = resize_and_center(img, size)
        img_resized.save(path.parent / f"{path.stem}_processed.png")
        print(f"  -> Saved processed image as {path.stem}_processed.png")

    temp_path.unlink()
    print("Done!")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Process a single image for Slay the Spire")
    parser.add_argument("file", help="Path to the image file")
    parser.add_argument("type", choices=["card", "relic", "power", "select", "character"], help="Type of image")
    
    if len(sys.argv) < 3:
        parser.print_help()
        sys.exit(1)
        
    args = parser.parse_args()
    process_single_image(args.file, args.type)
