NdsRefAppUi CAS customisation overlay
The actual properties files are in the devOps repository.  Properties files here will be overridden.
DO NOT MAKE PROPERTY FILE CHANGES AND EXPECT THEM TO WORK ON TEST/LIVE.  Property files are provided
for local development only.

Please don't copy the properties files when copying this project (unless you need a local installation of the CAS server).
---------------------------------------

This project contains the login pages customisation for the NdsRefApp CAS implementations.

To run this locally, you need to configure it (see below) then compile it, then run it.


Configuring to run locally
--------------------------
In our CAS setup there are 3 locations where there are server specific properties.

etc/cas/config/cas.properties - this is the main one
src/main/resources/bootstrap.properties - sets the directory where cas.properties can be found
src/main/resources/services/ - this is where the service definition goes (ie description of an allowed client).  It's specified in json and includes the hostname of the client system in a couple of regex's.

Compiling it
------------
mvn clean package

(Assumes that you've already compiled the NdsPlatformCas project which is the parent Maven Overlay project for this project.)

Run it
------
Set up Apache Tomcat to run the war file produced by compiling the project eg (server.xml) : 

<Context path="/cas" docBase="C:\git\nds-env-copy\NdsEnvironment\environment\app-servers\cas-overlay-template-5\target\cas.war" reloadable="true" />

Customisation
-------------
Don't attempt to define new JSP pages or redefine pages.  Instead :

Edit the messages - src/main/resources/messages.properties
Edit the css - src/main/webapp/css/cas.css
And/or edit the html - src/main/resources/templates/fragments/

Multi-lingual support is included in the CAS project and therefore included in our overlay by default.  
To edit them, copy the bundle files to src/main/resources/ (ie the same as was done with messages.properties).