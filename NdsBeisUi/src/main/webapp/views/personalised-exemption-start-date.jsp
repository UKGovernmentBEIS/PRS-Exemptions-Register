<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}"
	key="Title_Exemption_Start_Date" var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" method="post">
		<jsp:include page="preliminaries.jsp" />
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
					<section id="forminfowrap">
						<h1 class="form-title heading-large" role="banner">
							${command.uiData.selectedExemptionTypeText.startDatePwsLabel}</h1>
					</section>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">
					<div class="form-group">	
										
						<nds:datetime path="exemptionDetails.exemptionStartDate"
							labeldecoration="none"
							label="Label_exemptionDetails.exemptionStartDate"
							hideTimeField="true" 
							invalidAboveInput="true"/>
						
					</div>			
					<div class="form-group">
						<section class="submit">
							<div>
								<button type="submit" name="action" value="NEXT" id="button.next"
									class="button next">
									<fmt:message bundle="${FieldsBundle}" key="Button_NEXT" />
								</button>
							</div>
						</section>
					</div>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
		<script src="${contextUi}/assets/javascripts/application.js?version=${version}"></script>
	</nds:form>
</body>
</html>
