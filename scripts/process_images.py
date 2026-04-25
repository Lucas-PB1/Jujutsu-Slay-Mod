import os
import sys
import argparse
from pathlib import Path
from PIL import Image, ImageDraw, ImageFilter

try:
    from rembg import remove, new_session
except ImportError:
    print("rembg package not found. Run: pip install \"rembg[cpu]\"")
    sys.exit(1)

# Configuration for sizes
CONFIG = {
    "card": {"portrait": (500, 380), "standard": (250, 190)},
    "cardback": {"large": (1024, 1024), "standard": (512, 512)},
    "relic": {"large": (256, 256), "standard": (128, 128)},
    "power": {"large": (84, 84), "standard": (32, 32)},
    "select": {"portrait": (1920, 1200), "button": (200, 200)},
    "character": {"shoulder": (1920, 1136), "corpse": (512, 512)},
    "orb": {"large": (512, 512), "preview": (164, 164), "small": (22, 22)}
}

def clean_alpha(img, threshold=50):
    """Purge semi-transparent pixels that cause halos after downscaling"""
    if img.mode != 'RGBA':
        return img
    datas = img.getdata()
    newData = []
    for item in datas:
        if item[3] < threshold:
            newData.append((255, 255, 255, 0))
        else:
            newData.append(item)
    img.putdata(newData)
    return img

def resize_and_center(img, size, scale=1.0, anchor="center"):
    # Autocrop transparency
    bbox = img.getbbox()
    if bbox:
        img = img.crop(bbox)
        
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
    new_img = Image.new("RGBA", size, (255, 255, 255, 0))
    
    if anchor == "top-left":
        paste_x, paste_y = 36, 8
    elif anchor == "bottom-left":
        paste_x = 0
        paste_y = size[1] - new_height
    elif anchor == "bottom-left-offset":
        # Recuo específico para Corpse (profundidade)
        paste_x = int(size[0] * 0.1) 
        paste_y = size[1] - new_height - 50 
    elif anchor == "bottom-center":
        paste_x = (size[0] - new_width)
        paste_y = size[1] - new_height
    else: # center
        paste_x = (size[0] - new_width)
        paste_y = (size[1] - new_height)
        
    new_img.paste(img, (paste_x, paste_y), img)
    return new_img

def process_single_image(file_path, img_type, keep_bg=False):
    path = Path(file_path)
    if not path.exists():
        print(f"Error: File {file_path} not found.")
        return

    if img_type == "card":
        keep_bg = True

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
        if img_type != "orb":
            img = clean_alpha(img, threshold=50) 
    else:
        print("  -> Keeping original background...")
        img = Image.open(path).convert("RGBA")
        temp_path = None

    # Resize and Save
    if img_type == "orb":
        # Cardback orbs
        l_img = resize_and_center(img, CONFIG["orb"]["large"], scale=0.2, anchor="top-left")
        p_img = resize_and_center(img, CONFIG["orb"]["preview"], scale=0.9, anchor="center")
        s_img = resize_and_center(img, CONFIG["orb"]["small"], scale=1.1, anchor="center")
        
        # CREATE STRONGER RADIAL GRADIENT MASK
        mask = Image.new('L', (22, 22), 0)
        draw = ImageDraw.Draw(mask)
        # Inner core: Solid white (alpha 255) for the center 10px
        draw.ellipse((6, 6, 16, 16), fill=255)
        # Outer fade: From center to edges
        for i in range(1, 7):
            alpha = int(255 * (1 - (i / 6)))
            # Draw concentric circles with decreasing alpha
            draw.ellipse((6-i, 6-i, 16+i, 16+i), outline=alpha, width=1)
        
        # Blur only slightly
        mask = mask.filter(ImageFilter.GaussianBlur(radius=0.3))
        
        # Apply mask to the small orb
        final_small = Image.new("RGBA", (22, 22), (255, 255, 255, 0))
        final_small.paste(s_img, (0, 0), mask=mask)
        
        l_img.save(path.parent / "energy_orb.png")
        p_img.save(path.parent / "energy_orb_p.png")
        final_small.save(path.parent / "small_orb.png")
        print(f"  -> Saved energy_orb.png, energy_orb_p.png and small_orb.png (Core Preserved)")

    elif img_type == "cardback":
        l_img = resize_and_center(img, CONFIG["cardback"]["large"])
        s_img = resize_and_center(img, CONFIG["cardback"]["standard"])
        l_img.save(path.parent / f"{path.stem}_p.png")
        s_img.save(path.parent / f"{path.stem}.png")

    elif img_type == "card":
        p_img = resize_and_center(img, CONFIG["card"]["portrait"])
        s_img = resize_and_center(p_img, CONFIG["card"]["standard"])
        p_img.save(path.parent / f"{path.stem}_p.png")
        s_img.save(path.parent / f"{path.stem}.png")

    elif img_type in ["relic", "power"]:
        scale = 0.5
        l_img = resize_and_center(img, CONFIG[img_type]["large"], scale)
        s_img = resize_and_center(img, CONFIG[img_type]["standard"], scale)
        l_img.save(path.parent / f"{path.stem}_large.png")
        s_img.save(path.parent / f"{path.stem}.png")

    else: # character or select
        size_key = "portrait" if "portrait" in path.stem.lower() else "button" if "button" in path.stem.lower() else "shoulder" if "shoulder" in path.stem.lower() else "corpse"
        cat = "select" if img_type == "select" else "character"
        size = CONFIG[cat].get(size_key, (512, 512))
        
        # Shoulder: bottom-left para não cobrir a UI da fogueira
        # Corpse: bottom-left, maior e com sensação de profundidade
        if size_key == "shoulder":
            anchor_type = "bottom-left"
            current_scale = 1.0
        elif size_key == "corpse":
            anchor_type = "bottom-left-offset"
            current_scale = 0.6
        elif size_key == "button":
            anchor_type = "center"
            current_scale = 0.7
        else:
            anchor_type = "center"
            current_scale = 1.0
        
        img_resized = resize_and_center(img, size, current_scale, anchor=anchor_type)
        img_resized.save(path.parent / f"{path.stem}_processed.png")

    if temp_path and temp_path.exists():
        temp_path.unlink()
    print("Done!")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Process a single image for Slay the Spire")
    parser.add_argument("file", help="Path to the image file")
    parser.add_argument("type", choices=["card", "cardback", "relic", "power", "select", "character", "orb"], help="Type of image")
    parser.add_argument("--keep-bg", action="store_true", help="Keep the original background")
    
    if len(sys.argv) < 3:
        parser.print_help()
        sys.exit(1)
        
    args = parser.parse_args()
    process_single_image(args.file, args.type, args.keep_bg)
