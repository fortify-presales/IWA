package com.microfocus.example;

import com.microfocus.example.entity.*;
import com.microfocus.example.utils.EncryptedPasswordUtils;

import java.util.UUID;

/**
 * A simple data seeder for domain objects.
 *
 * @author Kevin A. Lee
 */
public class DataSeeder {

    // Test Users:
    // User 1 - stored in database on startup
    public static final UUID TEST_USER1_ID = UUID.fromString("bd5b9e2f-ac55-4e34-a76d-599b7e5b3308");
    public static final String TEST_USER1_USERNAME = "test1";
    public static final String TEST_USER1_FIRST_NAME = "Test";
    public static final String TEST_USER1_LAST_NAME = "User 1";
    public static final String TEST_USER1_PASSWORD = "0n3L0ngPa55w0rd";
    public static final String TEST_USER1_EMAIL = "test1@localhost";
    public static final String TEST_USER1_PHONE = "0123456789";
    // User 2 - created in tests
    public static final String TEST_USER2_USERNAME = "test2";
    public static final String TEST_USER2_FIRST_NAME = "Test";
    public static final String TEST_USER2_LAST_NAME = "User 2";
    public static final String TEST_USER2_PASSWORD = "0n3L0ngPa55w0rd";
    public static final String TEST_USER2_EMAIL = "test2@localhost";
    public static final String TEST_USER2_PHONE = "0123456789";

    // Test Products:
    // Product 1 - stored in database on startup
    public static final UUID TEST_PRODUCT1_ID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    public static final String TEST_PRODUCT1_CODE = "TEST-A000-00001";
    public static final String TEST_PRODUCT1_NAME = "Test Product 1";
    public static final String TEST_PRODUCT1_SUMMARY = "Product Summary.";
    public static final String TEST_PRODUCT1_DESCRIPTION = "This is a brief description of the product for testing.";
    public static final float TEST_PRODUCT1_PRICE = new Float(10.0);
    public static final Boolean TEST_PRODUCT1_ON_SALE = false;
    public static final int TEST_PRODUCT1_TIME_TO_STOCK = 5;
    public static final int TEST_PRODUCT1_RATING = 5;
    // Product 2 - created in tests
    public static final String TEST_PRODUCT2_CODE = "TEST-A000-00002";
    public static final String TEST_PRODUCT2_NAME = "Test Product 2";
    public static final String TEST_PRODUCT2_SUMMARY = "Product Summary";
    public static final String TEST_PRODUCT2_DESCRIPTION = "This is a brief description of the product for testing.";
    public static final float TEST_PRODUCT2_PRICE = new Float(10.0);
    public static final Boolean TEST_PRODUCT2_ON_SALE = true;
    public static final float TEST_PRODUCT2_SALE_PRICE = new Float(5.0);
    public static final int TEST_PRODUCT2_TIME_TO_STOCK = 5;
    public static final int TEST_PRODUCT2_RATING = 5;

    // Test Messages:
    // Message 1 - stored in database on startup
    public static final UUID TEST_MESSAGE1_ID = UUID.fromString("d67dbf99-e775-4d1b-87f7-0b86c739a5ba");
    public static final UUID TEST_MESSAGE1_USERID = UUID.fromString("bd5b9e2f-ac55-4e34-a76d-599b7e5b3308");;
    public static final String TEST_MESSAGE1_TEXT = "This is an example message";
    // Message2 - created in tests
    public static final UUID TEST_MESSAGE2_USERID = UUID.fromString("bd5b9e2f-ac55-4e34-a76d-599b7e5b3308");;
    public static final String TEST_MESSAGE2_TEXT = "This is another example message";

    // Test Order:
    // Order 1 - stored in database on startup
    public static final UUID TEST_ORDER1_ID = UUID.fromString("c97f4c8b-4c14-4d0c-9354-69fd7ee324da");
    public static final UUID TEST_ORDER1_USERID = UUID.fromString("bd5b9e2f-ac55-4e34-a76d-599b7e5b3308");
    public static final String TEST_ORDER1_ORDER_NUM = "TEST-O000-0001";

    // Test Review:
    // Review 1 - stored in database on startup
    public static final UUID TEST_REVIEW1_ID = UUID.fromString("8ab3eb7c-42f6-11ec-81d3-0242ac130003");
    public static final UUID TEST_REVIEW1_PRODUCTID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    public static final UUID TEST_REVIEW1_USERID = UUID.fromString("bd5b9e2f-ac55-4e34-a76d-599b7e5b3308");
    public static final String TEST_REVIEW1_COMMENT = "This is an example review of Test Product 1. It is very good";

    public static User generateUser() {
        User user = new User();
        user.setUsername(TEST_USER2_USERNAME);
        user.setPassword(EncryptedPasswordUtils.encryptPassword(TEST_USER2_PASSWORD));
        user.setFirstName(TEST_USER2_FIRST_NAME);
        user.setLastName(TEST_USER2_LAST_NAME);
        user.setEmail(TEST_USER2_EMAIL);
        user.setPhone(TEST_USER2_PHONE);
        // no roles
        return user;
    }

    public static Message generateMessage() {
        Message message = new Message();
        message.setText(TEST_MESSAGE2_TEXT);
        message.setRead(false);
        return message;
    }

    public static Product generateProduct() {
        Product product = new Product();
        product.setCode(TEST_PRODUCT2_CODE);
        product.setName(TEST_PRODUCT2_NAME);
        product.setSummary(TEST_PRODUCT2_SUMMARY);
        product.setDescription(TEST_PRODUCT2_DESCRIPTION);
        product.setPrice(TEST_PRODUCT2_PRICE);
        product.setOnSale(TEST_PRODUCT2_ON_SALE);
        product.setSalePrice(TEST_PRODUCT2_SALE_PRICE);
        product.setInStock(true);
        product.setTimeToStock(TEST_PRODUCT2_TIME_TO_STOCK);
        product.setRating(TEST_PRODUCT2_RATING);
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
