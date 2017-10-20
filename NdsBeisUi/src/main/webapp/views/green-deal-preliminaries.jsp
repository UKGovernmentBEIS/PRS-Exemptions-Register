
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link href="${contextUi}/assets/stylesheets/green-deal.css?version=${version}"
	media="screen" rel="stylesheet" />

<div id="skiplink-container">
	<div>
		<a href="#content" id="link.skip" class="skiplink">Skip to main
			content</a>
	</div>
</div>
<div id="global-cookie-message">
	<div class="outer-block">
		<div class="inner-block">
			<p>
				Green Deal uses cookies to make the site simpler. <a href="gdar-gdip-cookies"
					id="link.find.out.cookies">Find out more about cookies</a>
			</p>
		</div>
	</div>
</div>

<header role="banner" id="global-header">
	<div class="header-wrapper">
		<div class="header-global">
			<div class="header-logo">
				<a href="register-search-gdar-gdip"
					title="Go to the Green Deal homepage" id="logo" class="content">
					Find a Green Deal Advice Report or Improvement Package </a>
			</div>
			<img src="${contextUi}/assets/images/green_deal_small.png" class="gd-logo"
				width="58" height="72" alt="" />
		</div>
	</div>
</header>
<!--end header-->
<div id="journey"
	data-journey="beis-apply-${springViewName}:stage:start"></div>
<div id="global-header-bar"></div>