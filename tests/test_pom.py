import pytest
from tests.pages.login_page import LoginPage
from tests.pages.search_page import SearchPage

from dotenv import load_dotenv
import sys, os

directory_path = os.getcwd()
env_file = str(directory_path) + os.sep + '.env'

load_dotenv(env_file)
APP_URL = os.getenv('APP_URL')
if not APP_URL:
    APP_URL = 'https://iwa.onfortify.com'
print('APP_URL: %s' %APP_URL)

@pytest.mark.login
def test_login_functionality(chrome_browser):
    """
    Test the login functionality of the IWA website
    """
    url = '%s/login' % APP_URL
    login_page = LoginPage(chrome_browser)
    profile_title = "IWA Pharmacy Direct - User Profile"

    # Open Page
    login_page.open_page(url)

    # Enter Username and Password
    login_page.enter_username("user1@localhost.com")
    login_page.enter_password("password")

    # Click Login
    login_page.click_login()

    assert profile_title in chrome_browser.title

    # Verify Successful Login by checking the presence of a logout button
    #assert login_page.verify_successful_login()


@pytest.mark.search
def test_search_functionality(chrome_browser):
    """
    Test the search functionality of the IWA website
    """
    url = '%s/products' % APP_URL
    search_term = "alphadex"
    search_page = SearchPage(chrome_browser)

    # Open Page
    search_page.open_page(url)

    # Search for the term
    search_page.search(search_term)

    # Verify Successful Search by checking the presence of results
    #assert search_page.verify_successful_results()

    # Assert that the title contains the search term.
    #assert search_term in chrome_browser.title

    search_page.view()
