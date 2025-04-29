from selenium import webdriver
from selenium.webdriver.chrome.options import Options
import time

chrome_options = Options()
chrome_options.add_argument("--headless")
chrome_options.add_argument("--no-sandbox")
chrome_options.add_argument("--disable-dev-shm-usage")
chrome_options.add_argument("--user-data-dir=/tmp/selenium")

driver = webdriver.Chrome(options=chrome_options)

url = "http://54.167.8.65:8081"

driver.get(url)
time.sleep(5)

driver.save_screenshot("homepage.png")

driver.quit()
