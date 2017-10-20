<?php

	/**
	 * Logout.
 	 */

	include "constants.inc";

	unset($_COOKIE[$mockShibbyCookie]);
	setcookie($mockShibbyCookie, "", time()-3600, "/");
	
	print ("Cookie : ");
	print_r($_COOKIE);
?>
<p>
<a href="/mockidp/login.php">Log in to NDS dev</a>
</p>