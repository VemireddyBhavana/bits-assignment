# Student ID: 2024EB01570
# Python script to extract individual high-res deliverable screenshots for Java app

import os
from PIL import Image

output_dir = r"d:\projects\bits assignment\screenshots"
os.makedirs(output_dir, exist_ok=True)

first_row_path = r"C:\Users\bhava\.gemini\antigravity-ide\brain\94aa27d8-3e29-47fb-896a-f50a9d949d7f\first_row_view_1784528343833.png"
second_row_path = r"C:\Users\bhava\.gemini\antigravity-ide\brain\94aa27d8-3e29-47fb-896a-f50a9d949d7f\second_row_view_1784528333734.png"

img1 = Image.open(first_row_path)
img2 = Image.open(second_row_path)

w1, h1 = img1.size
w2, h2 = img2.size

# Process Row 1 Screens (RegisterActivity.java, LoginActivity.java, WelcomeActivity.java)
cols = 3
w_chunk1 = w1 / cols

row1_screens = [
    ("Screenshot_1_RegisterActivity.png", 0),
    ("Screenshot_2_LoginActivity.png", 1),
    ("Screenshot_3_WelcomeActivity.png", 2)
]

for filename, col in row1_screens:
    left = int(col * w_chunk1)
    right = int((col + 1) * w_chunk1)
    cropped = img1.crop((left, 0, right, h1))
    out_path = os.path.join(output_dir, filename)
    cropped.save(out_path)
    print(f"Saved: {out_path}")

# Process Row 2 Screens (NoticeActivity Staff, NoticeActivity Student, NotificationService)
w_chunk2 = w2 / cols

row2_screens = [
    ("Screenshot_4_NoticeActivity_Staff.png", 0),
    ("Screenshot_5_NoticeActivity_Student.png", 1),
    ("Screenshot_6_NotificationService.png", 2)
]

for filename, col in row2_screens:
    left = int(col * w_chunk2)
    right = int((col + 1) * w_chunk2)
    cropped = img2.crop((left, 0, right, h2))
    out_path = os.path.join(output_dir, filename)
    cropped.save(out_path)
    print(f"Saved: {out_path}")

print("All Java deliverable screenshots processed successfully!")
