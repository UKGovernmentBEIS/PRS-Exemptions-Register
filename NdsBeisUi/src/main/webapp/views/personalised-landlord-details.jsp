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
			key="Title_LandlordDetail_${command.exemptionDetails.landlordDetails.accountType}" var="title"  />

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
		<jsp:include page="phase-banner.jsp" /> 
		<header>
			<nds:back />
		</header>
		<div class="grid-row">
			<div class="column-full">
				<jsp:include page="form-error.jsp"/>				
					<h1 class="form-title heading-large" role="banner">
						<fmt:message bundle="${FieldsBundle}"
								key="Heading_LandlordDetail" />				
					</h1>				
			</div>
		</div>
		<div class="grid-row">
			<div class="column-full">
				<section id="formwrap">
					<c:choose>
						<c:when
							test="${command.exemptionDetails.landlordDetails.accountType == 'PERSON'}">
							<div class="form-group">
								<nds:field
									path="exemptionDetails.landlordDetails.personNameDetail.firstname"
									labeldecoration="required">
									<nds:label />
									<nds:invalid />
									<nds:input class="form-control" placeholder="false"
										autofocus="true" />
								</nds:field>
							</div>
							<div class="form-group">
								<nds:field
									path="exemptionDetails.landlordDetails.personNameDetail.surname"
									labeldecoration="required">
									<nds:label />
									<nds:invalid />
									<nds:input class="form-control" placeholder="false"
										autofocus="false" />
								</nds:field>
							</div>
						</c:when>
						<c:otherwise>
							<div class="form-group">
								<nds:field
									path="exemptionDetails.landlordDetails.organisationNameDetail.orgName"
									labeldecoration="required">
									<nds:label />
									<nds:invalid />
									<nds:input class="form-control" placeholder="false"
										autofocus="true" />
								</nds:field>
							</div>
						</c:otherwise>
					</c:choose>

					<div class="form-group">
						<nds:field path="exemptionDetails.landlordDetails.emailAddress"
							labeldecoration="required">
							<nds:label />
							<nds:invalid />
							<nds:input class="form-control" placeholder="false"
								autofocus="false" />
						</nds:field>
					</div>
					<div class="form-group">
						<nds:field path="exemptionDetails.landlordDetails.confirmEmail"
							labeldecoration="required">
							<nds:label />
							<nds:invalid />
							<nds:input class="form-control" placeholder="false"
								autofocus="false" />
						</nds:field>
					</div>
					<div class="form-group">
						<nds:field path="exemptionDetails.landlordDetails.phoneNumber">
							<nds:label />
							<nds:invalid />
							<nds:input class="form-control" placeholder="false"
								autofocus="false" />
						</nds:field>
					</div>
					<div class="form-group">
						<section class="submit">
							<div class="form-group">
								<button type="submit" name="action" value="NEXT"
									id="button.next" class="button next" formnovalidate>
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
		<script src="${contextUi}/assets/javascripts/app${minify}.js?version=${version}"></script>
		<script
			src="${contextUi}/assets/javascripts/password-strength.js?version=${version}"></script>
	</nds:form>
</body>
</html>
