# Vulnerabilities

## A01:2021-Broken Access Control
### /user/files/download/unverified - file parameter (below steps to validate this vulnerability)
	1. Go to Home Page.
    2. Login as "user1@localhost.com", password as "password".
	3. Click "Download Files" menu from the user home page.
	4. Enter "c:\\windows\\system.ini" and "Submit".
	5. Browser will prompt to keep or discard the "system.ini" file which is being downloaded
	6. Let us try another method to re-run the same exploit.
	7. Open new browser tab.
	8. Enter "http://localhost:8888//user/files/download/unverified?file=../../../../../../windows/system.ini".
	9. Browser will prompt to keep or discard the "system.ini" file which is being downloaded.
	10. Any "*secret and sensitive files*" can be downloaded using this exploit.
---
## A02:2021-Cryptographic Failures

---
## A03:2021-Injection: Cross site scripting (Reflected)
### /products/xss - search textbox ( below Steps to validate this vulnerability)
	1. Go to Home Page.
	3. In the search textbox, enter the following:

        ###<i><script>alert("hi")</script></i>

	4. Click Search.
	5. Script in the search field gets executed (reflected) and exposes the vulnerability.
---
## A03:2021-Injection: SQL Injection
### /Products - search textbox ( below Steps to validate this vulnerability)
	1. Go to Home Page.
    2. Login as "user1@localhost.com", password as "password".
	3. Hover on the username on the right top of the screen next to Search glass icon.
	4. Note that the drop-down menu contains Home, API Explorer and Logout.
	5. Click on the SHOP menu.
	6. List of available products will be displayed.
	7. In the search textbox, enter the following:

        ###'; INSERT INTO user_authorities (authority_id, user_id) VALUES ('05970e74-c82b-4e21-b100-f8184d6e3454', '32e7db01-86bc-4687-9ecb-d79b265ac14f') --

	8. Click Search button.
    9. A message such as ### Searching for: ###'; INSERT INTO user_authorities (authority_id, user_id) VALUES ('05970e74-c82b-4e21-b100-f8184d6e3454', '32e7db01-86bc-4687-9ecb-d79b265ac14f') -- will be shown.
    10. Hover on the username on the top right of the screen and select Logout from the pop-up menu.
    11. Login again with the credentials given above.
    12. Now, the username dropdown on the right top of the screen will additionally show: "Site Administration" "Database Console" menu options.
    13. user1 has become an admin and can do anything - including adding products, canceling orders, removing users etc.  
---
## A04:2021-Insecure Design

---
## A05:2021-Security Misconfiguration

---
## A06:2021-Vulnerable and Outdated Components

---
## A07:2021-Identification and Authentication Failures

---
## A08:2021-Software and Data Integrity Failures

---
## A09:2021-Security Logging and Monitoring Failures: Log Forging
### /user/log - val parameter (below steps to validate this vulnerability)
	1. Go to Home Page.
    2. Login as "user1@localhost.com", password as "password".
	3. Click "Log Entry" menu from the user home page.
	4. Enter "17" as the first line
	5. Enter the following as the second line:

        2022-08-09 02:46:04.136  INFO 2654 --- [nio-8080-exec-3] c.m.e.w.c.UserController                 : Order payment reversed successfully for user1"

	6. Click "Submit".
	7. In the web application console (cmd window), "Order payment reversed successfully for user1" would is logged on a separate line as a valid log entry.  
---
## A10:2021-Server-Side Request Forgery
