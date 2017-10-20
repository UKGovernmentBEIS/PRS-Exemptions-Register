<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}"
	key="Title_Exemption_List_Of_Values" var="title" />
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
								${command.uiData.selectedExemptionTypeText.frvPwsLabel}</h1>
					</section>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">
					<p class="form-group">
						${command.uiData.selectedExemptionTypeText.frvPwsText}
					</p>					
					<div class="form-group">
						<nds:field path="exemptionDetails.exemptionReason">
							<nds:invalid />							
							<fieldset>							
								<c:forEach items="${command.uiData.selectedExemptionTypeText.exemptionTypeLovList}"
									var="varExemptionTypeLov">
									<nds:radiobutton label="${varExemptionTypeLov.text}"
											value="${varExemptionTypeLov.code}" />
								</c:forEach>								
							</fieldset>
						</nds:field>
					</div>
			
					<div class="form-group">
						<nds:field
							path="exemptionDetails.exemptionReasonAdditionalText">
							<nds:label />
							<nds:invalid />
							<nds:textarea class="form-control" rows="10" cols="40" data-maxlength="4000" />							
						</nds:field>
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
	</nds:form>
</body>
</html>
