print_r($_SERVER);
<p>
<a href="https://lt-ac38446.global.internal/NdsRefAppUi/personalised/#firstname">Personalised area</a>
<p>
<table>
<?php
	foreach ($_SERVER as $key => $value) {
		if (strpos($key, "Shib-") !== false) {
			$style = "color:blue";
		} else {
			$style = "";
		}
		print "<TR><TD style='$style'>$key</TD><TD>$value</TD></TR>";
	}
?>
</table>