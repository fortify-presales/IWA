#!/bin/python3

"""
    Example script to start FAST proxy, run selenium tests, then ScanCentral DAST scan
    Required modules:
        - selenium
        - python-dotenv
"""

from dotenv import load_dotenv
from selenium import webdriver
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
# from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from selenium.webdriver.common.proxy import Proxy, ProxyType
import sys, os, subprocess, time, getopt
from subprocess import PIPE

opts, args = getopt.getopt(sys.argv[1:], "hsv", ["help", "skip-fast", "verbose"])
skip_fast = False
verbose = False
for o in opts:
    if o in ("-v", "--verbose"):
        verbose = True
    elif o in ("-h", "--help"):
        #usage()
        sys.exit()
    elif o in ("-s", "--skip-fast"):
        skip_fast = True

directory_path = os.getcwd()
# parent_dir = os.path.abspath(os.path.join(directory_path, os.pardir))
env_file = str(directory_path) + os.sep + '.env'

print("Using environment file: " + env_file)

load_dotenv(env_file)

APP_URL = os.getenv('APP_URL')
SSC_AUTH_TOKEN = os.getenv('SSC_AUTH_TOKEN_BASE64')
SCANCENTRAL_DAST_API = os.getenv('SCANCENTRAL_DAST_API')
SCANCENTRAL_DAST_CICD_TOKEN = os.getenv('SCANCENTRAL_DAST_CICD_TOKEN')
FAST_EXE = os.getenv('FAST_EXE')
FAST_PORT = os.getenv('FAST_PORT')
FAST_PROXY = os.getenv('FAST_PROXY')
CHROME_WEBDRIVER_PATH = os.getenv('CHROME_WEBDRIVER_PATH')

if not skip_fast:
    # start FAST proxy
    print('Starting FAST proxy %s' % FAST_EXE)
    fast_proxy = subprocess.Popen(['%s' % FAST_EXE,
                                   '-CIToken', '%s' % SSC_AUTH_TOKEN,
                                   '-CICDToken', '%s' % SCANCENTRAL_DAST_CICD_TOKEN,
                                   '-u', '%s' % SCANCENTRAL_DAST_API,
                                   '-p', FAST_PORT, '-k', '-n', 'FAST-Demo'])

    # make sure the proxy is up and listening
    print('Making sure the proxy is up and listening, please wait...')
    time.sleep(2)

    # setup proxy
    print('Setting up Chrome proxy')
    prox = Proxy()
    prox.proxy_type = ProxyType.MANUAL
    prox.http_proxy = FAST_PROXY


# setup chrome webdriver
print('Setting up Chrome WebDriver')
capabilities = webdriver.DesiredCapabilities.CHROME
prox.add_to_capabilities(capabilities)
options = webdriver.ChromeOptions()
options.add_argument('ignore-certificate-errors')
browser = webdriver.Chrome(executable_path='%s' % CHROME_WEBDRIVER_PATH, desired_capabilities=capabilities,
                           chrome_options=options)

# Navigate to zero
print('Navigating to IWA Website: %s' % APP_URL)
browser.get(APP_URL)

shop_now_button = WebDriverWait(browser, 10).until(lambda x: x.find_element(By.LINK_TEXT, "SHOP NOW"))
shop_now_button.click()

search_input = WebDriverWait(browser, 10).until(lambda x: x.find_element(By.ID, "keywords"))
search_input.click()
search_input.send_keys('alphadex')
search_input.send_keys(Keys.ENTER)

search_results = WebDriverWait(browser, 10).until(
    lambda x: x.find_element(By.CSS_SELECTOR, "a:nth-child(1) .img-thumbnail"))
search_results.click()
add_to_cart_button = WebDriverWait(browser, 10).until(lambda x: x.find_element(By.ID, "add-to-cart"))
add_to_cart_button.click()
time.sleep(2)

checkout_now_button = WebDriverWait(browser, 10).until(lambda x: x.find_element(By.ID, "checkout-now"))
checkout_now_button.click()

# close browser
print('Closing Browser')
browser.close()
browser.quit()

if not skip_fast:
    # make sure the proxy is up and listening
    print('Making sure session has been captured, please wait...')
    time.sleep(5)

    # shutdown proxy
    print('Shutting down proxy')
    # note this is the default install location
    subprocess.Popen(['%s' % FAST_EXE, '-p', FAST_PORT, '-s'])

    # temp hack to make sure FAST exe shuts down
    # input('Press enter to continue...')
