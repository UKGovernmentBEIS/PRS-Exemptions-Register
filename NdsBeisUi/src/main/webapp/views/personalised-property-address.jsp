<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_PropertyAddress"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" method="post">
		<jsp:include page="preliminaries.jsp"></jsp:include>
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />
		<main id="content" role="main"> 
		<jsp:include page="phase-banner.jsp" /> 
			<header>
				<nds:back />
			</header>
			<div class="grid-row">
				<div class="column-full">
					<jsp:include page="form-error.jsp"/>
					<c:if test="${validmessage.length() > 0}">
						<section class="page current custommessage">
							<div id="custommessage">${validmessage}</div>
						</section>
					</c:if>
	
					<h2 class="form-title heading-large" role="banner">
						<fmt:message bundle="${FieldsBundle}" key="Heading_PropertyAddress" />
					</h2>
					<p class="form-hint">
						<fmt:message bundle="${FieldsBundle}"
							key="Hint_please_enter_address_of_property_exemption" />
					</p>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">
					<section id="formwrap">
						<div id="current-address-detail" class="form-group">
							<nds:field path="exemptionDetails.epc.propertyAddress">
								<nds:invalid/>							
								<nds:address path="exemptionDetails.epc.propertyAddress" invalidAboveInput="true" />								
							</nds:field>							
						</div>
						<div class="form-group">
							<section class="submit">
								<div>
									<button type="submit" name="action" value="NEXT" id="button.next"
										class="button next" formnovalidate>
										<fmt:message bundle="${FieldsBundle}" key="Button_NEXT" />
									</button>
								</div>
							</section>
						</div>
					</section>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>