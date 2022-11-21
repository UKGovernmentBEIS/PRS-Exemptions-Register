<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />

<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_${springViewName}"
	var="title" />
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
					<jsp:include page="form-error.jsp" />			
					<h1 class="form-title heading-large">
						<fmt:message bundle="${FieldsBundle}"
								key="SecondaryHeading_forgottenPasswordConfirmation" />						
					</h1>
					<section class="page current notice">
						<fmt:message bundle="${FieldsBundle}"
							key="Message_Password_reset_email_sent_if_possible" />
						<c:if test="${not empty prototypeMode_LdapInfo}">
							<div class="debug info">
								<!-- In prototype mode only, display notice if user does not exist  -->
								${prototypeMode_LdapInfo}
							</div>
						</c:if>
					</section>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>
