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
<fmt:message bundle="${FieldsBundle}"
	key="Title_EndExemptionConfirmation" var="title" />
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
			<div class="grid-row">
				<div class="colum-full">
					<jsp:include page="form-error.jsp"/>
				</div>
				<div class="column-two-thirds">
					<div class="govuk-box-highlight">
						<h1 class="bold-large">
							<fmt:message bundle="${FieldsBundle}" key="Heading_EndExemptionConfirmation" />
						</h1>
						<p>
							<fmt:message bundle="${FieldsBundle}"
								key="SubHeading_EndExemptionConfirmation" />
							<br/>												
							<strong id="endDate" class="heading-medium"> 
								<c:set var="endDate" value="<%=new java.util.Date()%>" />
								<fmt:formatDate pattern="dd MMMM yyyy" value="${endDate}" />
							</strong>
						</p>
					</div>
				</div>
			</div>	
			<div class="grid-row">
				<div class="column-full">
					<section class="submit">
						<div class="form-group">
							<button type="submit" name="action" value="NEXT" id="button.next"
								class="button next">
								<fmt:message bundle="${FieldsBundle}"
									key="Button_ViewMyExemptions" />
							</button>
						</div>
					</section>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>
