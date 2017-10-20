<!DOCTYPE html>
<html>
        <head>
	        <meta charset="utf-8">
	        <title>Mock Shibboleth Security</title>
	        <link rel="stylesheet" type="text/css" href="/mockidp/main.css">
	        <meta id="springViewName" name="dcterms.identifier" content="login-form" />
        </head>
        <body>

    <div class="wrapper">
      <div class="container">
        <header>
          <img src="/mockidp/nps-logo.jpg" alt="Northgate Public Services">
        </header>

        <div class="content">
          <div class="column one">
            <form action="login.php" method="post">

			  Login with Mock Shibboleth Security
			  
		      <section>
				<p class="form-element form-error"><?php print($error); ?></p>
		      </section>
	
              <div class="form-element-wrapper">
                <label for="username">Username</label>
                <input class="form-element form-field" id="username" name="<?php print($mockUsernameIndex); ?>" type="text">
              </div>

              <div class="form-element-wrapper">
                <label for="password">Password</label>
                <input class="form-element form-field" id="password" name="j_password" type="password" value="Not actually used in mock security">
              </div>
              
              <div class="form-element-wrapper">
                <label for="site">Site</label>
                <input class="form-element form-field" id="site" name="<?php print($mockSiteIndex); ?>" type="text" value="<?php print($site);?>">
              </div>

              <div class="form-element-wrapper">
                <button class="form-element form-button" type="submit" name="_eventId_proceed">Login</button>
              </div>
              
			<!--
              <div>
					Additional cookies/headers to add in get string format <input name="<?php print($mockAdditionalHeaders); ?>" />
              </div>
              -->
            </form>
          </div>

          <div class="column two">
            <ul class="list list-help">
              <li class="list-help-item">
              		<a href="<?php print($relativeLink); ?>passwordresetrequest" id="forgottenPassword">
              			<span class="item-marker">&rsaquo;</span>Forgotten your password?</a>
              </li>
              <li class="list-help-item">
              		<a href="<?php print($relativeLink); ?>register"><span class="item-marker">&rsaquo;</span>Register</a>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <footer>
        <div class="container container-footer">
             <p class="footer-text">Mock Shibboleth Security</p>
             <p><?php
             		print "your cookies :";
					print_r($_COOKIE);
				?>
			 </p>
        </div>
      </footer>
    </div>

    </body>
</html>
        