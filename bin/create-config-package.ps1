#
# Example script to create scancentral package containg IaC/Docker/config files etc
#

& scancentral package -bt none -exclude ".fortify" -exclude ".idea" -exclude ".pytest_cache" -exclude ".gitignore" `
    -exclude "iwa.iml" -exclude ".env" -exclude "pom.xml" -exclude fod -exclude data -exclude media -exclude samples `
    -exclude src -exclude target -o FoDConfigUpload.zip