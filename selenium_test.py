from selenium import webdriver
from selenium.webdriver.chrome.options import Options
import time

# Set options for headless mode (Jenkins server usually has no GUI)
chrome_options = Options()
chrome_options.add_argument("--headless")
chrome_options.add_argument("--no-sandbox")
chrome_options.add_argument("--disable-dev-shm-usage")

# Launch browser
driver = webdriver.Chrome(options=chrome_options)

# URL of your deployed app
url = "http://54.167.8.65:8081"

# Open the app URL
driver.get(url)

# Wait to load
time.sleep(5)

# Take Screenshot
driver.save_screenshot("homepage.png")

# Close the browser
driver.quit()
