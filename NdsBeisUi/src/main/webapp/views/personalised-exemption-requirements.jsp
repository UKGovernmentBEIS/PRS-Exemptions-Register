<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_Exemption_Requirements"
	var="title" />
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
					<jsp:include page="form-error.jsp" />					
					<h1 class="form-title heading-large" role="banner">
						<fmt:message bundle="${FieldsBundle}"
							key="Heading_Exemption_Requirements" />
					</h1>					
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">					
					<div class="form-group">
						<nds:field path="exemptionDetails.exemptionType"> <!-- hack to fix an issue with caching -->
							<c:forEach items="${command.uiData.refData.exemptionTypeText}"
								var="varExemptionType">
								<c:if
									test="${command.exemptionDetails.propertyType.name() == varExemptionType.service  && command.exemptionDetails.exemptionType == varExemptionType.code}">
									${varExemptionType.pwsText}
								</c:if>
							</c:forEach>						
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
		<jsp:include page="close.jsp" />
	</nds:form>
</body>
</html>
