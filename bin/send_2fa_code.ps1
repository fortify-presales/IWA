$EmailTo = "user2@localhost.com"
$EmailFrom = "do-not-reply@iwa.onfortify.com"
$Subject = "[IWA Pharmacy Direct] Your One Time Passcode" 
$Body = "233866" 
$SMTPServer = "localhost" 
$SMTPMessage = New-Object System.Net.Mail.MailMessage($EmailFrom,$EmailTo,$Subject,$Body)
$SMTPClient = New-Object Net.Mail.SmtpClient($SmtpServer, 25) 
$SMTPClient.EnableSsl = $false 
$SMTPClient.Credentials = New-Object System.Net.NetworkCredential("iwa@localhost.com", "password"); 
$SMTPClient.Send($SMTPMessage)