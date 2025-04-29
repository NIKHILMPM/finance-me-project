from selenium import webdriver
from selenium.webdriver.chrome.options import Options
import time
import tempfile
import os

user_data_dir = tempfile.mkdtemp()

chrome_options = Options()
chrome_options.add_argument("--headless")
chrome_options.add_argument("--no-sandbox")
chrome_options.add_argument("--disable-dev-shm-usage")
chrome_options.add_argument(f"--user-data-dir={user_data_dir}")

driver = webdriver.Chrome(options=chrome_options)

# 🔥 Get URL dynamically from environment
url = os.getenv("APP_URL", "http://localhost:8081")  # default fallback

print(f"Testing URL: {url}")
driver.get(url)

time.sleep(5)

driver.save_screenshot("homepage.png")
driver.quit()
