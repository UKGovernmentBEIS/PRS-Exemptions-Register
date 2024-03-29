<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />

<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_reportForgottenPassword"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>	
	<nds:form id="pageForm" action="index.htm">
		<jsp:include page="preliminaries.jsp"></jsp:include>
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />
			
		<main id="content" role="main">
			<jsp:include page="phase-banner.jsp"/>
			<header>
				<!-- This needs to link back to the sign on page which will be CAS so will be a link not a back button which
				     uses the navigation framework -->
				<a href="${contextUi}/personalised-dashboard?tenant=${command.tenant}" class="link-back back" role="back" id="Back.link">
					<fmt:message bundle="${FieldsBundle}" key="Button_BACK" />
				</a>								
			</header>		
			<div class="grid-row">
				<div class="column-full">
					<jsp:include page="form-error.jsp"/>
		
					<h1 class="form-title heading-large">
						<fmt:message bundle="${FieldsBundle}" key="Heading_reportForgottenPassword" />						
					</h1>
					<p>
						<fmt:message bundle="${FieldsBundle}" key="SubHeading_reportForgottenPassword" />
					</p>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">
					<div class="form-group">
						<nds:field path="passwordResetDetails.username">
							<nds:label />
							<nds:invalid />
							<nds:input class="form-control" />							
						</nds:field>
					</div>
					<section class="submit">
						<div>
							<button type="submit" name="action" id="button.next" value="NEXT" class="button next">
								<fmt:message bundle="${FieldsBundle}" key="Button_make_password_reset_request" />
							</button>
						</div>
					</section>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>
