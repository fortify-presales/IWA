import pytest
from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from webdriver_manager.chrome import ChromeDriverManager

@pytest.fixture()
def chrome_browser():
    #driver = webdriver.Chrome()

    FAST_PROXY="35.176.81.197:8087"

    options = webdriver.ChromeOptions()
    options.add_argument("--start-maximized")
    options.add_argument("--ignore-certificate-errors")
    options.add_argument("--disable-popup-blocking")
    options.add_argument("--incognito")
    options.add_argument("--headless=new")
    # comment out below if not using FAST proxy
    options.add_argument('--proxy-server=http://35.176.81.197:8087')

    # Use this line instead of the prev if you wish to download the ChromeDriver binary on the fly
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()),
                              options=options)
    
    driver.implicitly_wait(10)
    # Yield the WebDriver instance
    yield driver
    # Close the WebDriver instance
    driver.quit()