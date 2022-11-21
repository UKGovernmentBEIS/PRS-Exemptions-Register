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
	key="Title_reportForgottenUsernameConfirmation" var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" method="post">
		<jsp:include page="preliminaries.jsp"></jsp:include>
		<main id="content" role="main"> 
			<jsp:include page="phase-banner.jsp" /> 
			<form:input type="hidden" path="ndsViewState" /> 
			<form:input type="hidden" path="navigationalState" /> 
			<form:input type="hidden" path="language" />
			<div class="grid-row">
				<div class="column-full">
					<jsp:include page="form-error.jsp" />
					<section id="forminfowrap">
						<h1 class="form-title heading-large">
							<fmt:message bundle="${FieldsBundle}" key="Legend_reportForgottenUsernameConfirmation" />
						</h1>
					</section>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp" />
	</nds:form>
</body>
</html>
