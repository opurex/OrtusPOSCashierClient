import os
import re
import shutil

PROJECT_ROOT = './'  # Set to your project root directory
SOURCE_DIR = os.path.join(PROJECT_ROOT, 'app/src/main/java')

# Backup original files to avoid accidental damage
BACKUP_DIR = './refactor_backup'
os.makedirs(BACKUP_DIR, exist_ok=True)

# Replacement rules (pattern, replacement)
REPLACEMENTS = [
    (r'AlertDialog\.Builder', 'MaterialAlertDialogBuilder'),
    (r'getActionBar\(\)', 'getSupportActionBar()'),
    (r'FragmentActivity', 'AppCompatActivity'),
    (r'android\.app\.Fragment', 'androidx.fragment.app.Fragment')
]

def refactor_file(filepath):
    with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
        content = f.read()

    original_content = content

    for pattern, replacement in REPLACEMENTS:
        content = re.sub(pattern, replacement, content)

    if content != original_content:
        print(f"[UPDATED] {filepath}")
        backup_path = os.path.join(BACKUP_DIR, os.path.relpath(filepath, PROJECT_ROOT))
        os.makedirs(os.path.dirname(backup_path), exist_ok=True)
        shutil.copy(filepath, backup_path)
        
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)


def walk_and_refactor():
    print("\nStarting Auto-Refactor...")
    for root, dirs, files in os.walk(SOURCE_DIR):
        for file in files:
            if file.endswith('.java') or file.endswith('.kt'):
                full_path = os.path.join(root, file)
                refactor_file(full_path)
    print("\n--- Auto-Refactor Complete ---")

if __name__ == '__main__':
    walk_and_refactor()

