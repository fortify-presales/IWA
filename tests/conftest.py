import pytest
from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from webdriver_manager.chrome import ChromeDriverManager
from dotenv import load_dotenv
import sys, os

directory_path = os.getcwd()
env_file = str(directory_path) + os.sep + '.env'

load_dotenv(env_file)
FAST_PROXY = os.getenv('FAST_PROXY')
print('FAST_PROXY: %s' %FAST_PROXY)

@pytest.fixture()
def chrome_browser():
    options = webdriver.ChromeOptions()
    options.add_argument("--start-maximized")
    options.add_argument("--ignore-certificate-errors")
    options.add_argument("--disable-popup-blocking")
    options.add_argument("--incognito")
    options.add_argument("--headless=new")
    # comment out below if not using FAST proxy
    if (FAST_PROXY):
        options.add_argument('--proxy-server=%s' % FAST_PROXY)

    #driver = webdriver.Chrome()
    # Use this line instead of the prev if you wish to download the ChromeDriver binary on the fly
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()),
                              options=options)
    driver.implicitly_wait(10)
    # Yield the WebDriver instance
    yield driver
    # Close the WebDriver instance
    driver.quit()