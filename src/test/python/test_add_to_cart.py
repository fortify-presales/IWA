
import pytest
from selenium import webdriver
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.proxy import Proxy, ProxyType
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
from dotenv import load_dotenv
from time import sleep
import sys, os

directory_path = os.getcwd()
env_file = str(directory_path) + os.sep + '.env'

load_dotenv(env_file)

APP_URL = os.getenv('APP_URL')
CHROME_WEBDRIVER_PATH = os.getenv('CHROME_WEBDRIVER_PATH')
FAST_PROXY = os.getenv('FAST_PROXY')

capabilities = webdriver.DesiredCapabilities.CHROME
chrome_options = webdriver.ChromeOptions()
# comment out below in not using FAST proxy
chrome_options.add_argument('--proxy-server=%s' % FAST_PROXY)
chrome_options.add_argument('ignore-certificate-errors')


@pytest.fixture()
def setup():
    browser = webdriver.Chrome(executable_path='%s' % CHROME_WEBDRIVER_PATH,
                               desired_capabilities=capabilities,
                               options=chrome_options)
    yield browser
    browser.close()
    browser.quit()


class TestApp:
    # def test_login(self, setup):
    #    pass

    # def test_logout(self, setup):
    #    pass

    def test_add_to_cart(self, setup):
        chrome_driver = setup

        chrome_driver.get(APP_URL)
        chrome_driver.maximize_window()

        title = "IWA Pharmacy Direct - Home"
        assert title == chrome_driver.title

        shop_now_button = WebDriverWait(chrome_driver, 10).until(lambda x: x.find_element(By.LINK_TEXT, "SHOP NOW"))
        shop_now_button.click()

        search_input = WebDriverWait(chrome_driver, 10).until(lambda x: x.find_element(By.ID, "keywords"))
        search_input.click()
        search_input.send_keys('alphadex')
        search_input.send_keys(Keys.ENTER)

        search_results = WebDriverWait(chrome_driver, 10).until(
            lambda x: x.find_element(By.CSS_SELECTOR, "a:nth-child(1) .img-thumbnail"))
        search_results.click()
        add_to_cart_button = WebDriverWait(chrome_driver, 10).until(lambda x: x.find_element(By.ID, "add-to-cart"))
        add_to_cart_button.click()
        sleep(2)

        checkout_now_button = WebDriverWait(chrome_driver, 10).until(lambda x: x.find_element(By.ID, "checkout-now"))
        checkout_now_button.click()

        sleep(2)
