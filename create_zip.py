# Student ID: 2024EB01570
# Python script to package all assignment deliverables into CampusNoticeBoardApp_2024EB01570.zip

import os
import zipfile

zip_filename = r"d:\projects\bits assignment\CampusNoticeBoardApp_2024EB01570.zip"
base_dir = r"d:\projects\bits assignment"

items_to_include = [
    "app",
    "gradle",
    "screenshots",
    "build.gradle.kts",
    "settings.gradle.kts",
    "gradle.properties",
    "local.properties",
    "README.md"
]

print(f"Creating ZIP archive: {zip_filename}...")

with zipfile.ZipFile(zip_filename, 'w', zipfile.ZIP_DEFLATED) as zipf:
    for item in items_to_include:
        item_path = os.path.join(base_dir, item)
        if os.path.isdir(item_path):
            for root, dirs, files in os.walk(item_path):
                # Skip build directories or temporary caches
                if ".gradle" in root or "build" in root or "__pycache__" in root:
                    continue
                for file in files:
                    file_full_path = os.path.join(root, file)
                    arcname = os.path.relpath(file_full_path, base_dir)
                    zipf.write(file_full_path, arcname)
                    print(f"Added: {arcname}")
        elif os.path.isfile(item_path):
            arcname = os.path.relpath(item_path, base_dir)
            zipf.write(item_path, arcname)
            print(f"Added: {arcname}")

print(f"ZIP package created successfully! Size: {os.path.getsize(zip_filename)} bytes")
