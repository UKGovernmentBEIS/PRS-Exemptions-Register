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
	key="Title_AccountActivationConfirmation" var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	
	<nds:form id="pageForm">
		<jsp:include page="preliminaries.jsp"></jsp:include>		
		<main id="content" role="main">
			<jsp:include page="phase-banner.jsp"/>
			<form:input type="hidden" path="ndsViewState" />
			<form:input type="hidden" path="navigationalState" />
			<form:input type="hidden" path="language" />		
			
			<div class="grid-row">
				<div class="column-full">
					<jsp:include page="form-error.jsp"/>
					<h1 class="form-title heading-large" role="banner">
						<fmt:message bundle="${FieldsBundle}"
							key="Paragraph_activationRegistrationDetails.confirmActivation" />
					</h1>
					<p class="body-text" id="confirmSuccessLoginNotice">
						<fmt:message bundle="${FieldsBundle}"
							key="Paragraph_activationRegistrationDetails.confirmSuccessLogin" />
					</p>
					<section class="submit">
						<div>
							<a
								href="${contextUi}/personalised-dashboard?tenant=${command.tenant}"
								class="loginButton button" id="loginButton"> <fmt:message
									bundle="${FieldsBundle}" key="Common_login" />
							</a>
						</div>
					</section>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>
