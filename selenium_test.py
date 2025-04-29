from selenium import webdriver
from selenium.webdriver.chrome.options import Options
import time
import tempfile

# Create a truly fresh temp dir
user_data_dir = tempfile.mkdtemp()

chrome_options = Options()
chrome_options.add_argument("--headless")
chrome_options.add_argument("--no-sandbox")
chrome_options.add_argument("--disable-dev-shm-usage")
chrome_options.add_argument(f"--user-data-dir={user_data_dir}")

driver = webdriver.Chrome(options=chrome_options)

url = "http://34.229.221.148:8081"

driver.get(url)

time.sleep(5)

driver.save_screenshot("homepage.png")

driver.quit()

