# Welcome to the 2021 version of CAS

## Startup

### Local development

It's gradle and Spring Boot so start with ./gradlew -Dcas.standalone.configurationDirectory=/c/git/beis/NdsBeisCas/etc/cas/config -Dspring.devtools.restart.enabled=false bootRun

### Tomcat equivalent locally

See example_server.xml which is my <tomcat home>/conf/server.xml file.

cd <tomcat home>/bin
export JAVA_OPTS="-Dcas.standalone.configurationDirectory=c:\git\beis\NdsBeisCas\etc\cas\config\ -Dspring.devtools.restart.enabled=false"
startup.bat

### Notes

Add -DCAS_UPDATE_CHECK_ENABLED=true if you want to check for newer versions.

Add -Dspring.devtools.add-properties=false if you don't want, whatever that does (add this for production).

The "-Dspring.devtools.restart.enabled=false" part stops Spring devtools from being included which it probably won't be by default but included here as it seems hard to get rid of on dev so paranoid it might be needed later too.

## MongoDb

For nds_db serviceTicketsCollection and proxyTicketsCollection and ticketGrantingTicketsCollection and proxyGrantingTicketsCollection and transientSessionTicketsCollection:
Either drop the json text indexes (or drop the whole collections) before upgrading.  The new code has different index options which manefest as an error on startup.

## Configuration changes for servers

Make sure the following dev-only code is not present :

```
spring.thymeleaf.check-template=true
spring.thymeleaf.cache=false
```

Set the random generated keys to something unique:

```
cas.tgc.crypto.encryption.key=CJ1erqaEluhyq50STU2Ke0386RgnKdvfqwNott1hdIY
cas.tgc.crypto.signing.key=PXVKvySop69sMeBJr_riLH1vWdm8rVEG3jSa4hm0MJbwAxdcs966X4e5uCxQCJmG8KIbt78aXEWE-dPsiKMeVw
cas.webflow.crypto.signing.key=eKN4OACHnEpOKRlTfx0-vND7Gb_fMVUqefwqyIVwRavfKGROe_XroB8WXesAbnAvbAOVUG7sZMwsannSd-_CTw
cas.authn.pm.reset.crypto.encryption.key=cBgJK0DlC9tumK0i0SglsTUrzfvxSYF4Poln5dG3-xw
cas.authn.pm.reset.crypto.signing.key=ZVWchxNh40xX4fIw1wkIhV30X6NfVCiyG7aM_4mlw2IYQK6SjN7LEE74Vvl2Vli8NOWPXGBtvllkImRQ_cGNHg
# this one needs to be base64 encoded and exactly 16 characters long (decoded) per cas.webflow.crypto.encryption.key-size default
cas.webflow.crypto.encryption.key=a3NsZGtsamxraGlsdXl5aQ==
```

Update properties per system as before (eg filesystem locations, database connection details etc).