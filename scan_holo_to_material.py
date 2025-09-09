import os
import re

# --- CONFIGURATION ---
PROJECT_ROOT = './'  # Point to your Android project root

# Deprecated XML widgets
DEPRECATED_XML_WIDGETS = [
    'ListView', 'AbsoluteLayout', 'GridView', 'Spinner', 'ProgressBar', 
    'RatingBar', 'Gallery', 'TabHost', 'ActionBar', 'SearchView'
]

# Deprecated Java/Kotlin APIs
DEPRECATED_JAVA_APIS = [
    'AlertDialog.Builder', 'ActionBar', 'getActionBar\(', 'setActionBar\(', 
    'Theme.Holo', 'AppCompatActivity.getSupportActionBar\(',
    'FragmentActivity', 'android.support.', 'android.app.Fragment'
]

# --- SCANNER FUNCTIONS ---

def scan_xml_files():
    print("\nScanning XML Layouts...")
    for root, dirs, files in os.walk(os.path.join(PROJECT_ROOT, 'app/src/main/res/layout')):
        for file in files:
            if file.endswith('.xml'):
                path = os.path.join(root, file)
                with open(path, 'r', encoding='utf-8') as f:
                    content = f.read()
                    for widget in DEPRECATED_XML_WIDGETS:
                        if widget in content:
                            print(f"[XML] {file} contains <{widget}>")

def scan_java_files():
    print("\nScanning Java/Kotlin source files...")
    for root, dirs, files in os.walk(os.path.join(PROJECT_ROOT, 'app/src/main/java')):
        for file in files:
            if file.endswith('.java') or file.endswith('.kt'):
                path = os.path.join(root, file)
                with open(path, 'r', encoding='utf-8', errors='ignore') as f:
                    content = f.read()
                    for api in DEPRECATED_JAVA_APIS:
                        if re.search(api, content):
                            print(f"[CODE] {file} contains {api}")

def scan_theme_files():
    print("\nScanning styles/themes...")
    for root, dirs, files in os.walk(os.path.join(PROJECT_ROOT, 'app/src/main/res/values')):
        for file in files:
            if file.startswith('styles') and file.endswith('.xml'):
                path = os.path.join(root, file)
                with open(path, 'r', encoding='utf-8') as f:
                    content = f.read()
                    if 'Holo' in content:
                        print(f"[THEME] {file} uses Holo theme")


def run_full_scan():
    print("Starting full Holo-to-Material scan...\n")
    scan_xml_files()
    scan_java_files()
    scan_theme_files()
    print("\n--- Scan Complete ---")

if __name__ == '__main__':
    run_full_scan()

