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
	<nds:form id="pageForm" action="index.htm">
		<jsp:include page="preliminaries.jsp"></jsp:include>
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />
		<main id="content" role="main">
			<jsp:include page="phase-banner.jsp"/>
			<div class="grid-row">
				<div class="column-full">				
					<jsp:include page="form-error.jsp"/>
					<h1 class="form-title heading-large">
						<fmt:message bundle="${FieldsBundle}" key="Heading_ActivatePasswordReset" />
					</h1>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">		
				  <div class="${empty command.activatePasswordResetDetails.username ? 'form-group' : 'form-group hide'}">
					<nds:input type="hidden"
							path="activatePasswordResetDetails.readonlyUsername" />
						<nds:field path="activatePasswordResetDetails.username"
							label="Label_passwordResetDetails.username">
							<nds:label />
							<nds:invalid />
							<nds:input placeholder="false"
								readonly="${command.activatePasswordResetDetails.readonlyUsername}" class="form-control" />
						</nds:field>
						</div>
				  <div class="${empty command.activatePasswordResetDetails.activationCode  ? 'form-group' : 'form-group hide'}">
						<nds:input type="hidden"
							path="activatePasswordResetDetails.readonlyActivationCode" />
						<nds:field path="activatePasswordResetDetails.activationCode">
							<nds:label />
							<nds:invalid />
							<nds:input placeholder="false" class="form-control"
								readonly="${command.activatePasswordResetDetails.readonlyActivationCode}" />
						</nds:field>
		           </div>
					<div class="form-group" >		
						<nds:field path="activatePasswordResetDetails.password" hints="Hint_changePasswordDetails.newPassword">
							<nds:label />
							<nds:hint />
							<nds:invalid />
							<nds:input placeholder="false" type="password"
								class="form-control" forceempty="true" autocomplete="off" />				
							
						</nds:field>
					</div>
					<div class="form-group" >
						<nds:field path="activatePasswordResetDetails.confirmPassword">
							<nds:label />
							<nds:invalid />
							<nds:input placeholder="false" type="password" forceempty="true"
								class="form-control" autocomplete="off"/>							
						</nds:field>
					</div>
					<section class="submit">
						<div>
							<div>
								<button type="submit" name="action" id="button.next" value="NEXT"
									class="button next">
									<fmt:message bundle="${FieldsBundle}" key="Button_Set_Password" />
								</button>
							</div>
						</div>
					</section>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
		<script
			src="${contextUi}/assets/javascripts/password-strength.js?version=${version}"></script>
	</nds:form>
</body>
</html>
