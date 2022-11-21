<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_ErrorPrintExemptionSummary"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" method="post">
		<jsp:include page="preliminaries.jsp"/>
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />
		<main id="content" role="main"> 
			<jsp:include page="phase-banner.jsp" />

			<h1 class="form-title heading-large">
				<span> <fmt:message bundle="${FieldsBundle}"
									key="SecondaryHeading_ErrorPrintExemptionSummary" />
				</span>
			</h1>

			<section class="page current notice">
				<fmt:message bundle="${FieldsBundle}"
							 key="Message_ErrorPrintExemptionSummary" />
			</section>
			<div>
				<a href="personalised-dashboard" id="Back.link" class="loginButton button">
					<fmt:message bundle="${FieldsBundle}"
								 key="Button_ReturnToExemptionRegistrations" />
				</a>
			</div>
		</main>
		<jsp:include page="close.jsp"/>
	</nds:form>
</body>
</html>
