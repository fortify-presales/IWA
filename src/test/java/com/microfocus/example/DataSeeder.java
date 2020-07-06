package com.microfocus.example;

import com.microfocus.example.entity.Authority;
import com.microfocus.example.entity.AuthorityType;
import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.User;
import com.microfocus.example.utils.EncryptedPasswordUtils;

/**
 * A simple data seeder for domain objects.
 *
 * @author Kevin A. Lee
 */
public class DataSeeder {

    // Test Users:
    // User 1 - stored in database on startup
    public static final Integer TEST_USER1_ID = 99999;
    public static final String TEST_USER1_USERNAME = "test1";
    public static final String TEST_USER1_NAME = "Test User 1";
    public static final String TEST_USER1_PASSWORD = "0n3L0ngPa55w0rd";
    public static final String TEST_USER1_EMAIL = "test1@localhost";
    public static final String TEST_USER1_MOBILE = "0123456789";
    // User 2 - created in tests
    public static final String TEST_USER2_USERNAME = "test2";
    public static final String TEST_USER2_NAME = "Test User 2";
    public static final String TEST_USER2_PASSWORD = "0n3L0ngPa55w0rd";
    public static final String TEST_USER2_EMAIL = "test2@localhost";
    public static final String TEST_USER2_MOBILE = "0123456789";

    // Test Products:
    // Product 1 - stored in database on startup
    public static final Integer TEST_PRODUCT1_ID = 99999;
    public static final String TEST_PRODUCT1_CODE = "TEST-A000-00001";
    public static final String TEST_PRODUCT1_NAME = "Test Product 1";
    public static final String TEST_PRODUCT1_SUMMARY = "Product Summary.";
    public static final String TEST_PRODUCT1_DESCRIPTION = "This is a brief description of the product for testing.";
    public static final float TEST_PRODUCT1_TRADE_PRICE = new Float(10.0);
    public static final float TEST_PRODUCT1_RETAIL_PRICE = new Float(20.0);
    public static final int TEST_PRODUCT1_DELIVERY_TIME = 5;
    public static final int TEST_PRODUCT1_AVERAGE_RATING = 5;
    // Product 2 - created in tests
    public static final String TEST_PRODUCT2_CODE = "TEST-A000-00002";
    public static final String TEST_PRODUCT2_NAME = "Test Product 2";
    public static final String TEST_PRODUCT2_SUMMARY = "Product Summary";
    public static final String TEST_PRODUCT2_DESCRIPTION = "This is a brief description of the product for testing.";
    public static final float TEST_PRODUCT2_TRADE_PRICE = new Float(10.0);
    public static final float TEST_PRODUCT2_RETAIL_PRICE = new Float(20.0);
    public static final int TEST_PRODUCT2_DELIVERY_TIME = 5;
    public static final int TEST_PRODUCT2_AVERAGE_RATING = 5;

    // Test Messages:
    // Message 1 - stored in database on startup
    public static final Integer TEST_MESSAGE1_ID = 99999;
    public static final Integer TEST_MESSAGE1_USERID = 99999;
    public static final String TEST_MESSAGE1_TEXT = "This is an example message";

    public static User generateUser() {
        User user = new User();
        user.setUsername(TEST_USER2_USERNAME);
        user.setPassword(EncryptedPasswordUtils.encryptPassword(TEST_USER2_PASSWORD));
        user.setName(TEST_USER2_NAME);
        user.setEmail(TEST_USER2_EMAIL);
        user.setMobile(TEST_USER2_MOBILE);
        // no roles
        return user;
    }

    public static Product generateProduct() {
        Product product = new Product();
        product.setCode(TEST_PRODUCT2_CODE);
        product.setName(TEST_PRODUCT2_NAME);
        product.setSummary(TEST_PRODUCT2_SUMMARY);
        product.setDescription(TEST_PRODUCT2_DESCRIPTION);
        product.setTradePrice(TEST_PRODUCT2_TRADE_PRICE);
        product.setRetailPrice(TEST_PRODUCT2_RETAIL_PRICE);
        product.setDeliveryTime(TEST_PRODUCT2_DELIVERY_TIME);
        product.setAverageRating(TEST_PRODUCT2_AVERAGE_RATING);
        product.setAvailable(true);
        return product;
    }

    public static Authority generateAdminRole() {
        Authority role = new Authority();
        role.setName(AuthorityType.ROLE_ADMIN);
        return role;
    }

    public static Authority generateUserRole() {
        Authority role = new Authority();
        role.setName(AuthorityType.ROLE_USER);
        return role;
    }

    public static Authority generateCustomerRole() {
        Authority role = new Authority();
        role.setName(AuthorityType.ROLE_CUSTOMER);
        return role;
    }

    public static Authority generateSupervisorRole() {
        Authority role = new Authority();
        role.setName(AuthorityType.ROLE_SUPERVISOR);
        return role;
    }

    public static Authority generateGuestRole() {
        Authority role = new Authority();
        role.setName(AuthorityType.ROLE_GUEST);
        return role;
    }

    public static Authority generateApiRole() {
        Authority role = new Authority();
        role.setName(AuthorityType.ROLE_API);
        return role;
    }

}
