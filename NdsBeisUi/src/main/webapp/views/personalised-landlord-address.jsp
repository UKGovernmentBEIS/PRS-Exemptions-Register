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
			key="Title_LandlordAddress_${command.exemptionDetails.landlordDetails.accountType}" var="title"  />

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
	
					<h2 class="form-title heading-large" role="banner">
						<fmt:message bundle="${FieldsBundle}"
									key="Heading_LandlordAddress"/>											
					</h2>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">
					<section id="formwrap">
						<div id="current-address-detail" class="form-group">
							<nds:field path="exemptionDetails.landlordDetails.landlordAddress">
								<nds:invalid/>
								<nds:address path="exemptionDetails.landlordDetails.landlordAddress"
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
											<fmt:message bundle="${FieldsBundle}" key="Button_NEXT" />																												
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