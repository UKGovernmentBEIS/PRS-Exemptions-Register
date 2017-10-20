<?php 

/** 
 * Mock Shibboleth
 * Allows you to set session headers just like Shibboleth would in order to 
 * simulate being logged in by Shibboleth SP without having to actually 
 * have any of it installed.
 *
 * 
 * Ie this is totally insecure.  
 * 
 * Do not deploy.  
 * 
 * This is purely for dev convenience only!
 */
	include "constants.inc";
	
	// check for invalid username (literally)
	$error = "";
	
	if ($_POST[$mockUsernameIndex] == "invalid") {
		$error = "The password you entered was incorrect.";
	} else if (isset($_POST[$mockUsernameIndex])) {

		// successful login - set the mock shibby cookie
		setcookie($mockShibbyCookie, $_POST[$mockUsernameIndex], time()+1800, "/");  // expires in half an hour
		setcookie($mockUsernameIndex, $_POST[$mockUsernameIndex], time()+1800, "/");  // expires in half an hour
		
		// redirect to the chosen site
		$site = "/" . $_POST[$mockSiteIndex];
		print("Logging in to $site");
		header("Location: " . $site);
	} 

	// show login form

	// site part of the request eg "NdsBbisUi" in http://localhost/NdsBbisUi/
	// (note that "path" is set as a variable in the RewriteRule directive)
	if ((isset($_REQUEST["path"]) && (trim($_REQUEST["path"]) != ""))) {
		$site = $_REQUEST["path"];
	} else {
	
		// set a default
		$site = "NdsBbisUi/";
	}

	// analyse url to find where to base relative links
	$numSlashes = substr_count($_SERVER["SCRIPT_URL"], "/") - 2;
	$relativeLink = "";
	
	for ($i = 0; $i < $numSlashes; $i++) {
		$relativeLink .= "../";
	}

	print "<!--Mock Shibboleth-->";
	include("login_form.inc");

?>