<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />

<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_SecurityDetails"
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

		<main id="content" role="main"> <jsp:include
			page="phase-banner.jsp" /> <header>
			<nds:back />
		</header>
		<div class="grid-row">
			<div class="column-full">
				<jsp:include page="form-error.jsp" />
				<h1 class="form-title heading-large">
					<fmt:message bundle="${FieldsBundle}" key="Heading_SecurityDetails" />
				</h1>
			</div>
		</div>
		<div class="grid-row divider">
			<div class="column-full">
				<section id="formwrap">
					<div class="form-group">
						<nds:field path="beisRegistrationDetails.userDetails.username"
							labeldecoration="required"
							hints="Hint_beisRegistrationDetails.userDetails.username">
							<nds:label />
							<nds:hint />
							<nds:invalid />
							<nds:input class="form-control" placeholder="false" />
						</nds:field>
					</div>

					<div class="form-group">
						<nds:field path="beisRegistrationDetails.userDetails.password"
							labeldecoration="required"
							hints="Hint_beisRegistrationDetails.userDetails.password">
							<nds:label />
							<nds:hint />
							<nds:invalid />
							<nds:input class="form-control" placeholder="false"	type="password" autocomplete="off"/>
						</nds:field>
					</div>
					<div class="form-group">
						<nds:field
							path="beisRegistrationDetails.userDetails.confirmPassword"
							labeldecoration="required">
							<nds:label />
							<nds:invalid />
							<nds:input class="form-control"	placeholder="false" type="password" autocomplete="off"/>
						</nds:field>
					</div>
					<div class="form-group">
						<nds:field path="uiData.isAgreeRegistrationTermsConditions"
							labeldecoration="required" class="form-checkbox">
							<nds:invalid />
							<fieldset>
							    <div class="multiple-choice">
							        <!--  Can't use the NDS CheckBoxTag - see comment on the CheckBoxTag  -->
								    <input id="uiData.isAgreeRegistrationTermsConditions" name="uiData.isAgreeRegistrationTermsConditions" autolabel="false" type="checkbox" value="agreeTerms">
									<label class="block-label selection-button-checkbox"
									for="uiData.isAgreeRegistrationTermsConditions">								 									
										<fmt:message bundle="${FieldsBundle}" key="Label_uiData.isAgreeRegistrationTermsConditions" /> 
										<a	href="terms-and-conditions" id="term-condition-link" target="_blank"> 
											<fmt:message bundle="${FieldsBundle}" key="Label_termsconditions_text" />
										</a>
									</label>
									<input type="hidden" name="_uiData.isAgreeRegistrationTermsConditions" value="on">
								</div>
							</fieldset>
						</nds:field>
					</div>
					<div class="form-group">
						<section class="submit">
							<div class="form-group">
								<button type="submit" name="action" value="NEXT"
									id="button.next" class="button next" formnovalidate>
									<fmt:message bundle="${FieldsBundle}" key="Label_CreateAccount" />
								</button>
							</div>
						</section>
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
