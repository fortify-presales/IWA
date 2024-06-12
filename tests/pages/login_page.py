"""
Login Page Class for https://iwa.onfortify.com/login
"""

from selenium.webdriver.common.by import By
from selenium.common.exceptions import TimeoutException, NoSuchElementException
from selenium.webdriver.support.wait import WebDriverWait  
from selenium.webdriver.support import expected_conditions as EC 
from time import sleep

class LoginPage:
    def __init__(self, driver):
        self.driver = driver

    def open_page(self, url):
        self.driver.get(url)

    def enter_username(self, email):
        self.driver.find_element(By.ID, "email").send_keys(
            email
        )  # Email element ID

    def enter_password(self, password):
        self.driver.find_element(By.ID, "password").send_keys(
            password
        )  # Password element ID

    def click_login(self):
        self.driver.find_element(By.ID, "login-submit").click()  # Submit button ID

    def verify_successful_login(self):
        try:
            #logout_button = WebDriverWait(self.driver, 20).until(lambda x: x.find_element(By.PARTIAL_LINK_TEXT, "Logout"))
            #element = WebDriverWait(self.driver, 10).until(  
            #    EC.presence_of_element_located((By.XPATH, '//a[contains(@href,"logout")]'))  
            #)  
            logout_button = self.driver.find_element(By.PARTIAL_LINK_TEXT, "Logout")
            #ogout_button = self.driver.find_element_by_partial('//a[contains(@href,"logout")]')
            return logout_button.is_displayed()
        except TimeoutException:
            assert False, "Timed out waiting for existence of Logout button"
        except NoSuchElementException:
            assert False, "Logout button does not exist."
