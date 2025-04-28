from selenium import webdriver
from selenium.webdriver.common.by import By
import time

# URL of your deployed app
url = "http://54.167.8.65:8081"

# Launch browser
driver = webdriver.Chrome()

# Open the app URL
driver.get(url)

# Wait to load
time.sleep(5)

# Take Screenshot
driver.save_screenshot("homepage.png")

# Close the browser
driver.quit()
