<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<c:choose>
	<c:when test="${command.beisRegistrationDetails.userDetails.userType == 'AGENT'}">
		<fmt:message bundle="${FieldsBundle}"
			key="Title_AccountAddress_${command.beisRegistrationDetails.userDetails.userType}"
			var="title" />
	</c:when>
	<c:otherwise>
		<fmt:message bundle="${FieldsBundle}"
			key="Title_AccountAddress_${command.beisRegistrationDetails.accountDetails.accountType}"
			var="title" />
	</c:otherwise>
</c:choose>
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
							<h2><fmt:message bundle="${FieldsBundle}" key="Label_CustomMessage" /></h2>
							<div id="custommessage">
								<div>
									<ul>
										<li>${validmessage}</li>
									</ul>
								</div>
							</div>
						</section>
	
					</c:if>
	
					<h1 class="form-title heading-large">
						<c:choose>
							<c:when test="${command.beisRegistrationDetails.userDetails.userType == 'AGENT'}">
								<fmt:message bundle="${FieldsBundle}"
									key="Heading_AccountAddress_${command.beisRegistrationDetails.userDetails.userType}" />
							</c:when>
							<c:otherwise>
								<fmt:message bundle="${FieldsBundle}"
									key="Heading_AccountAddress_${command.beisRegistrationDetails.accountDetails.accountType}" />
							</c:otherwise>
						</c:choose>						
					</h1>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">
					<section id="formwrap">
						<div id="current-address-detail" class="form-group">
						<p class="form-hint">
						<fmt:message bundle="${FieldsBundle}"
							key="Hint_address_to_write_you" />
					    </p>
							<nds:field path="beisRegistrationDetails.contactAddress">
							    <nds:invalid/>
								<nds:address path="beisRegistrationDetails.contactAddress"
									showCountryList="true" invalidAboveInput="true" />
							</nds:field>
							<%--Ensure countries are loaded by having a field mapped to this --%>
							<nds:field path="beisRegistrationDetails.contactAddress.country">
							</nds:field>
						</div>
						<section class="submit">
							<div>
								<button type="submit" name="action" value="NEXT" id="button.next"
									class="button next" formnovalidate>
									<c:choose>
										<c:when
											test="${command.uiData.registrationStatus != 'FOUND_PARTIALLY_REGISTERED'}">
											<fmt:message bundle="${FieldsBundle}" key="Button_NEXT" />											
										</c:when>
										<c:otherwise>
											<fmt:message bundle="${FieldsBundle}" key="Button_SAVE" />										
										</c:otherwise>
									</c:choose>																				
								</button>
							</div>
						</section>
					</section>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>