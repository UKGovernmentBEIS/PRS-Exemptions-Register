<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_Select_Landlord_Type"
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
		<main id="content" role="main"> <jsp:include
			page="phase-banner.jsp" />
			<header>
				<c:if test="${command.uiData.registrationStatus != 'FOUND_PARTIALLY_REGISTERED'}">					
					<nds:back/>
				</c:if>
			</header>
			<div class="grid-row">
				<div class="column-full">
				<jsp:include page="form-error.jsp"/>
				<h1 class="form-title heading-large">
					<fmt:message bundle="${FieldsBundle}"
						key="Heading_Select_Landlord_Type" />
				</h1>
				<details id="exemption-details">
					<summary id="exemption-details-link">
						<span class="summary"> <fmt:message
								bundle="${FieldsBundle}" key="Link_SelectLandlordTypeDetails" />
						</span>
					</summary>
					<div class="panel panel-border-narrow">
						<p>
							<fmt:message bundle="${FieldsBundle}"
								key="Paragraph_SelectLandlordTypeDetails" />
						</p>
					</div>
				</details>
			</div>
		</div>

		<div class="grid-row divider">
			<div class="column-full">				
				<div class="form-group"> 
		       		<nds:radiobuttonelement path="beisRegistrationDetails.accountDetails.accountType"
						items="${command.uiData.refData.accountType}"/>
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
	</nds:form>
</body>
</html>