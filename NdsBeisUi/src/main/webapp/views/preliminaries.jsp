
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="headingForSite">
	<fmt:message bundle="${FieldsBundle}" key="Heading_preliminariesRegister" />
</c:set>
<c:set var="headingLinkGoToGovTitle">
	<fmt:message bundle="${FieldsBundle}" key="Title_preliminariesLinkGoToGov" />
</c:set>

<div id="skiplink-container">
	<div>
		<a href="#content" id="link.skip" class="skiplink"> <fmt:message
				bundle="${FieldsBundle}" key="Title_preliminariesLinkSkipToMainContent" />
		</a>
	</div>
</div>
<div id="global-cookie-message">
	<div class="outer-block">
		<div class="inner-block">
			<p>
				<fmt:message bundle="${FieldsBundle}"
					key="Paragraph_preliminariesCookieHeader" />
				<a href="cookies" id="link.find.out.cookies" target="_blank"> <fmt:message
						bundle="${FieldsBundle}" key="Title_preliminariesLinkCookie" />
				</a>
			</p>
		</div>
	</div>
</div>

<header role="banner" class="with-proposition" id="global-header">
	<div class="header-wrapper">
		<div class="header-global">
			<div class="header-logo">
				<a href="${govUkUrl}" title="${headingLinkGoToGovTitle}"
					id="logo" class="content"> <img
					src="${contextUi}/assets/images/gov.uk_logotype_crown.png" width="35"
					height="31" alt="" /> <fmt:message bundle="${FieldsBundle}"
						key="Heading_preliminariesBrandGovUK" />
				</a>
			</div>
		</div>
		<div class="header-proposition">
			<jsp:include page="hello-user.jsp">
				<jsp:param name="title" value="${headingForSite}" />
				<jsp:param name="moreops" value="${param.moreops}" />
			</jsp:include>
		</div>
	</div>
</header>
<!--end header-->
<div id="journey"
	data-journey="beis-apply-${springViewName}:stage:start"></div>
<div id="global-header-bar"></div>