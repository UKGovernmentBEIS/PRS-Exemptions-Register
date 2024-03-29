<%@ page
	import="com.northgateps.nds.platform.ui.exception.ExceptionMessageResolver"%>
<%@ page isErrorPage="true"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>

<!-- static version of head.jsp -->
<head data-nds-version="${version}">
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<title>${param.title}</title>
	<meta id="springViewName" name="dcterms.identifier" content="${springViewName}" />
	
	<!-- IDs looked for by the google_gtag script -->
	<meta id="gaId" name="gaId" content="${gaTrackId}" />
	<script src="${contextUi}/assets/javascripts/google_gtag.js?version=${version}"></script>
	
	<base href="${pageContext.request.contextPath}/">
	
	<c:choose>
		<c:when test="${not empty minify}">
			<script src="assets/javascripts/beis-head-pkg${minify}.js?version=${version}"></script>
			<script src="assets/javascripts/platform-shared-head-pkg${minify}.js?version=${version}"></script>			
		</c:when>
		<c:otherwise>
			<script src="assets/javascripts/jquery-init.js?version=${version}"></script>
			<script src="assets/javascripts/vendor/history.js?version=${version}"></script>
			<script src="assets/javascripts/head.platform.js?version=${version}"></script>
		</c:otherwise>
	</c:choose>
	<script src="assets/javascripts/jquery-ui-init.js?version=${version}"></script>
	<!--[if gt IE 8]><!-->
	<link
		href="assets/stylesheets/sass_css/govuk-template${minify}.css?version=${version}"
		media="screen" rel="stylesheet" />
	<!--<![endif]-->
	<!--[if IE 6]><link href="assets/stylesheets/sass_css/govuk-template-ie6${minify}.css?version=${version}" media="screen" rel="stylesheet" /><![endif]-->
	<!--[if IE 7]><link href="assets/stylesheets/sass_css/govuk-template-ie7${minify}.css?version=${version}" media="screen" rel="stylesheet" /><![endif]-->
	<!--[if IE 8]><link href="assets/stylesheets/sass_css/govuk-template-ie8${minify}.css?version=${version}" media="screen" rel="stylesheet" /><![endif]-->
	<link
		href="assets/stylesheets/sass_css/govuk-template-print${minify}.css?version=${version}"
		media="print" rel="stylesheet" />
	<!--[if IE 8]>
	    <script>
	      (function(){if(window.opera){return;}
	       setTimeout(function(){var a=document,g,b={families:(g=
	       ["nta"]),urls:["assets/stylesheets/fonts-ie8${minify}.css?version=${version}"]},
	       c="assets/javascripts/vendor/goog/webfont-debug${minify}.js?version=${version}",d="script",
	       e=a.createElement(d),f=a.getElementsByTagName(d)[0],h=g.length;WebFontConfig
	       ={custom:b},e.src=c,f.parentNode.insertBefore(e,f);for(;h=h-1;a.documentElement
	       .className+=' wf-'+g[h].replace(/\s/g,'').toLowerCase()+'-n4-loading');},0)
	      })()
	    </script>
	    <![endif]-->
	<!--[if gte IE 9]><!-->
	<link href="assets/stylesheets/fonts${minify}.css?version=${version}"
		media="all" rel="stylesheet" />
	<!--<![endif]-->
	<!--[if lt IE 9]>
	      <script src="assets/javascripts/ie${minify}.js?version=${version}" ></script>
	    <![endif]-->
	
	
	<!--[if gt IE 8]><!-->
	<link href="assets/stylesheets/sass_css/govuk-elements${minify}.css?version=${version}" media="screen" rel="stylesheet" />
	<!--<![endif]-->
	<!--[if IE 6]><link href="assets/stylesheets/sass_css/govuk-elements-ie6${minify}.css?version=${version}" media="screen" rel="stylesheet" /><![endif]-->
	<!--[if IE 7]><link href="assets/stylesheets/sass_css/govuk-elements-ie7${minify}.css?version=${version}" media="screen" rel="stylesheet" /><![endif]-->
	<!--[if IE 8]><link href="assets/stylesheets/sass_css/govuk-elements-ie8${minify}.css?version=${version}" media="screen" rel="stylesheet" /><![endif]-->
	
	<link href="assets/stylesheets/jquery-ui${minify}.css?version=${version}" media="screen" rel="stylesheet" />
	<link href="assets/stylesheets/print${minify}.css?version=${version}" media="print" rel="stylesheet" />
	
	<c:choose>
		<c:when test="${not empty minify}">
			<link href="assets/stylesheets/beis-pkg${minify}.css?version=${version}" media="screen" rel="stylesheet" />
		</c:when>
		<c:otherwise>
			<link href="assets/stylesheets/nds-platform-ui.css?version=${version}" media="screen" rel="stylesheet" />
			<link href="assets/stylesheets/beis.css?version=${version}" media="screen" rel="stylesheet" />
		</c:otherwise>
	</c:choose>
	
	<link rel="shortcut icon" href="assets/images/favicon.ico?version=${version}" type="image/x-icon" />
	<!-- Size for iPad and iPad mini (high resolution) -->
	<link rel="apple-touch-icon-precomposed" sizes="152x152" href="assets/images/apple-touch-icon-152x152.png?version=${version}" />
	<!-- Size for iPhone and iPod touch (high resolution) -->
	<link rel="apple-touch-icon-precomposed" sizes="120x120" href="assets/images/apple-touch-icon-120x120.png?version=${version}" />
	<!-- Size for iPad 2 and iPad mini (standard resolution) -->
	<link rel="apple-touch-icon-precomposed" sizes="76x76" href="assets/images/apple-touch-icon-76x76.png?version=${version}" />
	<!-- Default non-defined size, also used for Android 2.1+ devices -->
	<link rel="apple-touch-icon-precomposed" href="assets/images/apple-touch-icon-60x60.png?version=${version}" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<link rel="schema.dcterms" href="http://purl.org/dc/terms/">
	
	<c:catch>
		<fmt:message bundle="${FieldsBundle}" key="please-check-the-form" var="i18nPleaseCheckTheForm" />
		<meta id="i18n-please-check-the-form" name="i18n-please-check-the-form" content="${i18nPleaseCheckTheForm}" />
	</c:catch>
	<meta property="og:image" content="assets/images/opengraph-image.png?version=${version}" />
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.js"></script>
	<script src="assets/javascripts/jsinit${minify}.js?version=${version}"></script>
</head>

<body>

	<!-- static version of preliminaries.jsp -->
<div id="journey"  data-journey="beis-apply-${springViewName}:stage:start"></div>
	<header role="banner" id="global-header" class="with-proposition">
		<div class="header-wrapper">
			<div class="header-global">
				<div class="header-logo">
					<span class="default-branding"><fmt:message
							bundle="${FieldsBundle}" key="Heading_DefaultBranding" /></span>
				</div>
			</div>
			<div class="header-proposition" id="proposition-name">
				<h2 class="heading-medium"></h2>
			</div>
		</div>
	</header>

	<!-- start of error page content -->

	<div id="content" role="main">

		<!--  static phase banner -->
		<c:if test="${applicationPhase != 'LIVE'}">
			<div class="phase-banner-beta">
				<p>
					<strong class="phase-tag">${applicationPhase}</strong> <span><fmt:message
							bundle="${FieldsBundle}"
							key="FeedBackMessage_${applicationPhase}" /></span>
				</p>
			</div>
		</c:if>

		<div id="wrapper">
			<p class="quiet large">There was a problem and the action could
				not be completed. Please wait for a short time and then try again.</p>
			<div class="post-body">
				<div>
					Error event reference :
					<%= ExceptionMessageResolver.resolve(exception) %></div>
			</div>
			<p>&nbsp;</p>
			<a id="link.back.home" href="">Back</a>
		</div>
	</div>

	<!-- start of static version of close.jsp -->
	<footer role="contentinfo" class="group js-footer">
		<div class="footer-wrapper">
			<div class="footer-meta">
				<div class="footer-meta-inner">
					<!-- Show version number -->
					<c:if test="${not empty version}">${version} </c:if>
				</div>
			</div>
		</div>
	</footer>
	
	<div id="global-app-error" class="app-error hidden"></div>

</body>
</html>
