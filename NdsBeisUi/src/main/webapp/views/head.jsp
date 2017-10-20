<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<head data-nds-version="${version}">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <!-- There is meta data we can add to the page to stop searches finding the site.  -->
    <meta name="robots" content="noindex, nofollow">
    <title>${param.title}</title>
    <meta id="springViewName" name="dcterms.identifier" content="${springViewName}" />
    <meta id="entryPointName" name="entryPointName" content="${entryPointName}" />
    <meta id="request-method" name="request-method" content="${pageContext.request.method}" />
    <meta id="skipRefreshPagePreservation" name="skipRefreshPagePreservation" content="${skipRefreshPagePreservation}" />
	   
    <c:choose>
        <c:when test="${not empty minify}">
            <script src="${contextUi}/assets/javascripts/beis-head-pkg${minify}.js?version=${version}"></script>
			<script src="${contextUi}/assets/javascripts/platform-shared-head-pkg${minify}.js?version=${version}"></script>			            
        </c:when>
        <c:otherwise>
            <script src="${contextUi}/assets/javascripts/jquery-init.js?version=${version}"></script>
            <script src="${contextUi}/assets/javascripts/vendor/history.js?version=${version}"></script>
            <script src="${contextUi}/assets/javascripts/head.platform.js?version=${version}"></script>
        </c:otherwise>
    </c:choose>	
    
    <!--  Ensure this is loaded before the gov-uk css as the nds-platform-ui css overrides some of the correct styles -->
    <c:choose>
        <c:when test="${not empty minify}">
            <link href="${contextUi}/assets/stylesheets/beis-pkg${minify}.css?version=${version}" media="screen" rel="stylesheet" />
        </c:when>
        <c:otherwise>
            <link href="${contextUi}/assets/stylesheets/nds-platform-ui.css?version=${version}" media="screen" rel="stylesheet" />
            <link href="${contextUi}/assets/stylesheets/beis.css?version=${version}" media="screen" rel="stylesheet" />
        </c:otherwise>
    </c:choose>
    
    <!--[if gt IE 8]><!-->
    <link href="${contextUi}/assets/stylesheets/sass_css/govuk-template${minify}.css?version=${version}" media="screen" rel="stylesheet" />
    <!--<![endif]-->
    <!--[if IE 6]><link href="${contextUi}/assets/stylesheets/sass_css/govuk-template-ie6${minify}.css?version=${version}" media="screen" rel="stylesheet" /><![endif]-->
    <!--[if IE 7]><link href="${contextUi}/assets/stylesheets/sass_css/govuk-template-ie7${minify}.css?version=${version}" media="screen" rel="stylesheet" /><![endif]-->
    <!--[if IE 8]><link href="${contextUi}/assets/stylesheets/sass_css/govuk-template-ie8${minify}.css?version=${version}" media="screen" rel="stylesheet" /><![endif]-->
    <link href="${contextUi}/assets/stylesheets/sass_css/govuk-template-print${minify}.css?version=${version}" media="print" rel="stylesheet" />
    <!--[if IE 8]>
    <script>
      (function(){if(window.opera){return;}
       setTimeout(function(){var a=document,g,b={families:(g=
       ["nta"]),urls:["${contextUi}/assets/stylesheets/fonts-ie8${minify}.css?version=${version}"]},
       c="${contextUi}/assets/javascripts/vendor/goog/webfont-debug${minify}.js?version=${version}",d="script",
       e=a.createElement(d),f=a.getElementsByTagName(d)[0],h=g.length;WebFontConfig
       ={custom:b},e.src=c,f.parentNode.insertBefore(e,f);for(;h=h-1;a.documentElement
       .className+=' wf-'+g[h].replace(/\s/g,'').toLowerCase()+'-n4-loading');},0)
      })()
    </script>
    <![endif]-->
    <!--[if gte IE 9]><!-->
    <link href="${contextUi}/assets/stylesheets/fonts${minify}.css?version=${version}" media="all" rel="stylesheet" />
    <!--<![endif]-->
    <!--[if lt IE 9]>
      <script src="${contextUi}/assets/javascripts/ie${minify}.js?version=${version}" ></script>
    <![endif]-->
       
    
    <!--[if gt IE 8]><!--><link href="${contextUi}/assets/stylesheets/sass_css/govuk-elements${minify}.css?version=${version}" media="screen" rel="stylesheet" /><!--<![endif]-->
    <!--[if IE 6]><link href="${contextUi}/assets/stylesheets/sass_css/govuk-elements-ie6${minify}.css?version=${version}" media="screen" rel="stylesheet" /><![endif]-->    
    <!--[if IE 7]><link href="${contextUi}/assets/stylesheets/sass_css/govuk-elements-ie7${minify}.css?version=${version}" media="screen" rel="stylesheet" /><![endif]-->
    <!--[if IE 8]><link href="${contextUi}/assets/stylesheets/sass_css/govuk-elements-ie8${minify}.css?version=${version}" media="screen" rel="stylesheet" /><![endif]-->
    
    <link href="${contextUi}/assets/stylesheets/jquery-ui${minify}.css?version=${version}" media="screen" rel="stylesheet" />
    <link href="${contextUi}/assets/stylesheets/print${minify}.css?version=${version}" media="print" rel="stylesheet" />
    <script src="${contextUi}/assets/javascripts/jquery-ui-init.js?version=${version}"></script>
    
    
    <link rel="shortcut icon" href="${contextUi}/assets/images/favicon.ico?version=${version}" type="image/x-icon" />
    <!-- Size for iPad and iPad mini (high resolution) -->
    <link rel="apple-touch-icon-precomposed" sizes="152x152" href="${contextUi}/assets/images/apple-touch-icon-152x152.png?version=${version}" />
    <!-- Size for iPhone and iPod touch (high resolution) -->
    <link rel="apple-touch-icon-precomposed" sizes="120x120" href="${contextUi}/assets/images/apple-touch-icon-120x120.png?version=${version}" />
    <!-- Size for iPad 2 and iPad mini (standard resolution) -->
    <link rel="apple-touch-icon-precomposed" sizes="76x76" href="${contextUi}/assets/images/apple-touch-icon-76x76.png?version=${version}" />
    <!-- Default non-defined size, also used for Android 2.1+ devices -->
    <link rel="apple-touch-icon-precomposed" href="${contextUi}/assets/images/apple-touch-icon-60x60.png?version=${version}" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="schema.dcterms" href="http://purl.org/dc/terms/">
    <c:catch>
      <fmt:message bundle="${FieldsBundle}" key="please-check-the-form" var="i18nPleaseCheckTheForm" />
      <meta id="i18n-please-check-the-form" name="i18n-please-check-the-form" content="${i18nPleaseCheckTheForm}" />
    </c:catch>
    <meta property="og:image" content="${contextUi}/assets/images/opengraph-image.png?version=${version}" />
</head>
