<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />

<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_accountActivation"
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
					<section id="forminfowrap">
						<h1 class="form-title heading-large">
							<fmt:message bundle="${FieldsBundle}" key="Paragraph_activationRegistrationDetails.activateRegistration" />
						</h1>
					</section>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">
					<section class="page current">
						<nds:field path="activateRegistrationDetails.activationCode">
							<nds:label label="Legend_activateRegistrationDetails"></nds:label>
							<nds:invalid />
							<nds:input placeholder="false" class="form-control"/>							
						</nds:field>
					</section>
					<section class="submit">
						<div>
							<div>
								<button type="submit" name="action" id="button.next" value="NEXT"
									class="button next" formnovalidate>
									<fmt:message bundle="${FieldsBundle}" key="Button_ACTIVATION" />
								</button>
							</div>
						</div>
					</section>
				</div>
			</div>
		</main>

		<jsp:include page="close.jsp"></jsp:include>
		
	</nds:form>
</body>
</html>
