JSON CAS Service Config
-----------------------
This directory contains the service configuration json files for connecting to CAS.

IMPORTANT NOTE TO DEVELOPERS
----------------------------
Files in this directory will be removed and replaced by devops when deploying to servers.
ie changes developers make to these files only affect development and there's no need to 
inform devops.

However, if you add/remove a new attribute type to json files, you must tell devops how
to update the server versions of these files. 

SECURITY
--------
If two app contexts exist for different systems from security point of view, 
the json config files must be stored in different directories to prevent cross ui
context login issues.  You may need to develop that ability.

EXAMPLE FILE
------------
{
	"serviceId" : "http://localhost/NdsPublicEngagementUi/",
	"loginUrl" : "http://localhost/NdsPublicEngagementCas/login",
	"logoutUrl" : "http://localhost/NdsPublicEngagementCas/logout",
	"logBackInLocation" : "personalised/home-page",
	"name" : "PublicEngagement",
	"description" : "PE CAS Authentication",
	"casToUiEncryption.key" : "asdjkvopisduifbjernfgksndfjhdbuuycvopbuicoit,.em.,mbioicvuoiu",
	"casToUiEncryption.iterations" : 5000,
	"casToUiEncryption.keyLength" : 128
}

EXPLAINATION OF EXAMPLE
-----------------------
"serviceId" :
	Must include the "http" protocol part AND the context part eg "NdsPublicEngagementUi"
	and must end with a "/", ie there must be 4 "/" in total.  (For the matching process, 
	everything in the requested URL after the 4th "/" is discarded.)
"loginUrl" :
	Location of the CAS server for redirecting the user's browser for login.
"logoutUrl" :
	Location of the _CAS server_ for redirecting the user's browser for SLO (Single Log Out).
	Do not change this to send the user to anywhere else (as that will allow login without password if they're
	quick enough, autotest will fail).  Instead see the CAS service json file and look for logoutRedirectUrl.
"logBackInLocation" :
	Path relative to "serviceId" to allow the user to log back in (ie rather than going to the start page
	and clicking the login link)
"name" :
	A human readable name for the service definition.
"description" :
	A human readable description for the service definition.
"casToUiEncryption" :
	Allows encryption/decryption of additional information sent to or received from the CAS server.
