<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${language}" />
<!DOCTYPE html>
<html lang="${language}">
<fmt:message bundle="${FieldsBundle}" key="Title_ExemptionConfirmation"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
<nds:form id="pageForm" method="post" action="generate-exemption-details">
		<jsp:include page="preliminaries.jsp"></jsp:include>
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />
		<main id="content" role="main">
			<jsp:include page="phase-banner.jsp" />
			<div class="grid-row">
				<div class="column-two-thirds">
					<jsp:include page="form-error.jsp"/>
					<div class="govuk-box-highlight">
						<h2 class="bold-large" role="banner">
							<fmt:message bundle="${FieldsBundle}"
								key="Heading_ExemptionConfirmation" />
						</h2>
						<p>	
							<fmt:message bundle="${FieldsBundle}"
								key="SubHeading_ExemptionConfirmation" />
							
							<br />
							<strong class="heading-medium">
								<c:out value="${command.exemptionDetails.referenceId}"></c:out>
							</strong>
						</p>
					</div>
					<c:if test="${fault==null || empty fault}">
					<p>
						<fmt:message bundle="${FieldsBundle}"
								key="Label_confirmation_email" />
					</p>
					</c:if>
				
					<h2 class="heading-medium">
						<fmt:message bundle="${FieldsBundle}" key="Label_happen_next" />
					</h2>
					<c:choose>
					<c:when
						test="${command.exemptionDetails.propertyType.name() == 'PRSD'}">
						<p>
							<fmt:message bundle="${FieldsBundle}"
								key="Label_happen_next_text_domestic" />
						</p>
						<p>
							<fmt:message bundle="${FieldsBundle}"
								key="Label_happen_next_further_text_domestic" />
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<fmt:message bundle="${FieldsBundle}"
								key="Label_happen_next_text_non_domestic" />
						</p>
						<p>
							<fmt:message bundle="${FieldsBundle}"
								key="Label_happen_next_further_text_non_domestic" />
						</p>
					</c:otherwise>
				</c:choose>

					<section class="submit">
						<div class="form-group">
							<a href="${contextUi}/personalised-dashboard?tenant=${command.tenant}" id="finish" class="button next">
								<fmt:message bundle="${FieldsBundle}" key="Button_ViewMyExemptions" />
							</a>
						</div>
					</section>
					<div>
						<button type="submit" name="action"
								value="PrintExemptionDetailsSummary" id="button.printExemptionDetails"
								role="link">
							<fmt:message bundle="${FieldsBundle}" key="Link_PrintEvidenceSummary" />
						</button>
					</div>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>
