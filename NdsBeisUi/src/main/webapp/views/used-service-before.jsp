<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${language}" />
<!DOCTYPE html>
<html lang="en">
<fmt:message bundle="${FieldsBundle}" key="Title_UsedServiceBefore"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" method="post" action="used-service-handler">
		<jsp:include page="preliminaries.jsp"></jsp:include>
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />
		<main id="content" role="main">
			<jsp:include page="phase-banner.jsp" />
			<div class="grid-row">
				<div class="column-full">
					<jsp:include page="form-error.jsp" />
					<h1 class="form-title heading-large">
						<fmt:message bundle="${FieldsBundle}" key="Heading_UsedServiceBefore" />
					</h1>
					<p class="body-text">
						<fmt:message bundle="${FieldsBundle}" key="Paragraph_UsedServiceBefore1" />
						<a href="${usedBeforeMinStandardUrl}" id="link.navigation.minstandards" target="_blank"><fmt:message bundle="${FieldsBundle}" key="Paragraph_MinStandardsLinkText" /></a>
					</p>
					<p  class="body-text">
						<fmt:message bundle="${FieldsBundle}" key="Paragraph_UsedServiceBefore2_A" />
						<fmt:message bundle="${FieldsBundle}" key="Paragraph_ExemptionReasonsLinkText" />
						<fmt:message bundle="${FieldsBundle}" key="Paragraph_UsedServiceBefore2_B" />
						<fmt:message bundle="${FieldsBundle}" key="Paragraph_ExemptionProofLinkText" />
						<fmt:message bundle="${FieldsBundle}" key="Paragraph_UsedServiceBefore2_C" />
					</p>				
					<p class="body-text">
						<fmt:message bundle="${FieldsBundle}" key="Paragraph_UsedServiceBefore3" />
					</p>
				</div>
			</div>

			<div class="grid-row">
				<div class="column-full">
					<c:choose>
						<%--
							If a user is currently signed in, show the text describing it and a link (that looks like a button)
							that goes to the dashboard page.
						 --%>
						<c:when test="${not empty user}">
							<p class="body-text">
								<fmt:message bundle="${FieldsBundle}" key="Paragraph_UsedServiceBefore4" />
							</p>
							<div class="form-group">
								<a href="${contextUi}/personalised-dashboard?tenant=${command.tenant}" id="link-button.next" class="button next">
									<fmt:message bundle="${FieldsBundle}" key="Button_NEXT" />
								</a>
							</div>
						</c:when>

						<%-- 
							Otherwise, we show the normal part of this page, which is to ask the user if they want to log in
							or register a new account, and show the next button that either takes the user to the log-in page
							or the first page of the register-an-account flow
						--%>
						<c:otherwise>
							<div class="form-group">
								<nds:radiobuttonelement path="uiData.usedServiceBefore" items="${command.uiData.refData.yesNoValues}" />
							</div>
							<div class="form-group">
								<section class="submit">
									<div>
										<button type="submit" name="action" value="NEXT:usedservicebefore" id="button.next" class="button next">
											<fmt:message bundle="${FieldsBundle}" key="Button_NEXT" />
										</button>
									</div>
								</section>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>		
		</main>
		<jsp:include page="close.jsp"></jsp:include>		
	</nds:form>
</body>
</html>
