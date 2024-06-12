"""
SearchPage class to interact with the search page on https://iwa.onfortify.com/products
"""

from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.common.exceptions import NoSuchElementException, TimeoutException
from selenium.webdriver.support.wait import WebDriverWait  
from selenium.webdriver.support import expected_conditions as EC 
from time import sleep

class SearchPage:
    def __init__(self, driver):
        self.driver = driver

    def open_page(self, url):
        self.driver.get(url)

    def search(self, search_term):
        search_box = self.driver.find_element(By.ID, value="keywords")
        search_box.send_keys(search_term + Keys.RETURN)

    def verify_successful_results(self):
        try:
            #search_results = WebDriverWait(self.driver, 10).until(
            #    lambda x: x.find_element(By.CSS_SELECTOR, "a:nth-child(1) .img-thumbnail")
            #)
            #element = WebDriverWait(self.driver, 10).until(  
            #    EC.presence_of_element_located((By.PARTIAL_LINK_TEXT, "products"))  
            #) 
            #product_link = self.driver.find_element(By.PARTIAL_LINK_TEXT, "products")
            #return product_link.is_displayed()
            search_results =  self.driver.find_element(By.CSS_SELECTOR, "a:nth-child(1) .img-thumbnail")
            search_results.is_displayed()
        except TimeoutException:
            assert False, "Timed out waiting for existence of search results"
        except NoSuchElementException:
            assert False, "No search results exist."  

    def view(self):
        search_results =  self.driver.find_element(By.CSS_SELECTOR, "a:nth-child(1) .img-thumbnail")
        search_results.click()
        sleep(10)
