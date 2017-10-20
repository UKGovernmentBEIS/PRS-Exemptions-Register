<html>
	<head>
	
		<!-- modified from http://stackoverflow.com/questions/24662684/pure-css-static-starfield -->
		<style>
			#space, .stars {
			  overflow: hidden;
			  position: absolute;
			  top: 0;
			  bottom: 0;
			  left: 0;
			  right: 0;
			  z-index:-1;
			}
			
			.stars {
			  background-image: 
			    radial-gradient(1px 1px at 20px 30px, #eee, rgba(0,0,0,0)),
			    radial-gradient(1px 1px at 40px 70px, #fff, rgba(0,0,0,0)),
			    radial-gradient(1px 1px at 50px 160px, #ddd, rgba(0,0,0,0)),
			    radial-gradient(2px 2px at 90px 40px, #fff, rgba(0,0,0,0)),
			    radial-gradient(1px 1px at 130px 80px, #fff, rgba(0,0,0,0)),
			    radial-gradient(1px 1px at 160px 120px, #ddd, rgba(0,0,0,0));
			  background-repeat: repeat;
			  background-size: 200px 200px;
			}
			
			body {
			  	background: linear-gradient(#190033, black);
			  	color: white;
			  	font-size: 110%;
			}
		</style>
	</head>
	<body>
		<h1>
			You have accessed the "secure/" directory
		</h1>
		<br>
			README.txt:
		</br>
		<br>
			<?php include("README.txt"); ?>
		</br>


		<div id="space">
		   <div class="stars"></div>
		</div>
	</body>
</html>