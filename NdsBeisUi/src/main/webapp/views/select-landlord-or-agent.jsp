<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_Select_Landlord_Or_Agent"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" method="post" action='index.htm'>
		<jsp:include page="preliminaries.jsp"></jsp:include>
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />		
		<main id="content" role="main"> 
		<jsp:include page="phase-banner.jsp" />
			<header>								
					<nds:back/>				
			</header>
			<div class="grid-row">
				<div class="column-full">
				<jsp:include page="form-error.jsp"/>
                <c:if test="${command.uiData.registrationStatus == 'FOUND_PARTIALLY_REGISTERED'}">
					<div class="panel panel-border-wide" id="VerifyDetailsPrompt">
						<p>
							<fmt:message bundle="${FieldsBundle}"
								key="Prompt_Select_Landlord_Type_Verify_Details" />
						</p>
						<p>
							<fmt:message bundle="${FieldsBundle}"
								key="Prompt_Select_Landlord_Type_Verify_Details_Part_Two" />								
						</p>
					</div>
				</c:if>
				<h1 class="form-title heading-large" role="banner">
					<fmt:message bundle="${FieldsBundle}"
						key="Heading_Select_Landlord_Or_Agent" />
				</h1>
				<div>
					<p class="body-text">
						<fmt:message bundle="${FieldsBundle}"
								key="Paragraph_SelectLandlordOrAgentDetails1" />
					</p>			
					
				</div>
			</div>
		</div>
		<div class="grid-row divider">
			<div class="column-full">				
				<div class="form-group"> 
		       		<nds:radiobuttonelement path="beisRegistrationDetails.userDetails.userType"
						items="${command.uiData.refData.userType}"/>
		       	</div>
				<div class="panel panel-border-narrow" id="userDetails">
						<p>
							<fmt:message bundle="${FieldsBundle}"
								key="Paragraph_SelectLandlordOrAgentDetails2" />
						</p>
				</div>
				<div class="form-group">
					<section class="submit">
						<div class="form-group">
							<button type="submit" name="action" value="NEXT" id="button.next"
								class="button next" formnovalidate>
								<fmt:message bundle="${FieldsBundle}" key="Button_NEXT" />
							</button>
						</div>
					</section>
				</div>
			</div>
		</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
		<script
			src="${contextUi}/assets/javascripts/views/select-landlord-or-agent${minify}.js?version=${version}"></script>
	</nds:form>
</body>
</html>