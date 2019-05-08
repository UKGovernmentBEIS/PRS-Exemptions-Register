<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}"
	key="Title_End_Exemption" var="title" />
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
							<fmt:message bundle="${FieldsBundle}" key="Heading_End_Exemption" />
					    </h1>
					</section>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">
					<div class="form-group">
						<p class="body-text">							
							<fmt:message bundle="${FieldsBundle}" key="Message_End_Exemption" />						
						</p>						
						<h4 id="address" class="heading-small">${command.uiData.registeredExemptionDetail.address}</h4>
						<p class="body-text">
							${command.uiData.registeredExemptionDetail.description}
						</p>						
						<div class="panel panel-border-wide">
							<p>							
								<fmt:message bundle="${FieldsBundle}" key="Message_Exemption_Ended" />
							</p>			
						</div>
					</div>
					<div class="form-group">
						<section class="submit">
							<div>
								 <button type="submit" name="action" value="SelectedExemption:${command.uiData.registeredExemptionDetail.referenceId}" id="button.next"
									class="button next">
									<fmt:message bundle="${FieldsBundle}" key="Button_End_Exemption" />
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
