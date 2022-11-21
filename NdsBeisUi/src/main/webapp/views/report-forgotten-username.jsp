<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />

<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}"
	key="Title_reportForgottenUsername" var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>	
	<nds:form id="pageForm" method="post" action="index.htm">
		<jsp:include page="preliminaries.jsp"></jsp:include>
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />
		<main id="content" role="main"> 
			<jsp:include page="phase-banner.jsp" /> 
			<header>
				<!-- This needs to link back to the sign on page which will be CAS so will be a link not a back
	                             button which uses the navigation framework -->
				<a href="${contextUi}/personalised-dashboard?tenant=${command.tenant}"
					class="link-back back" role="back" id="back"> <fmt:message
						bundle="${FieldsBundle}" key="Button_BACK" />
				</a>
			</header>
			<div class="grid-row">
				<div class="column-full">
					<jsp:include page="form-error.jsp"/>
					<section id="forminfowrap">
						<h1 class="form-title heading-large">
							<fmt:message bundle="${FieldsBundle}" key="Heading_reportForgottenUsername" />
						</h1>
					</section>
					<p>
						<fmt:message bundle="${FieldsBundle}"
								key="Legend_reportForgottenUsername" />
					</p>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">				
					<div class="form-group">
						<nds:field path="forgottenUsernameDetails.emailAddress"
							labeldecoration="required">
							<nds:label />
							<nds:invalid />
							<nds:input class="form-control" />							
						</nds:field>
					</div>
					
					<section class="submit">
						<div class="form-group">
							<button type="submit" name="action" value="NEXT" id="button.next"
								class="button next">
								<fmt:message bundle="${FieldsBundle}" key="Button_NEXT" />
							</button>
						</div>
					</section>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp" />
	</nds:form>
</body>
</html>
