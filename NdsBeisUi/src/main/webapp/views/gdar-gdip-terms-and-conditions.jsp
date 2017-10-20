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
<fmt:message bundle="${FieldsBundle}" key="Title_TermsAndConditions"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" method="post">
		<jsp:include page="green-deal-preliminaries.jsp"></jsp:include>
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />
		<main id="content" role="main">
			<jsp:include page="phase-banner.jsp" />
			<div class="grid-row">
				<div class="column-full">
					<jsp:include page="form-error.jsp" />
					<div class="form-group">						
						 <fmt:message bundle="${FieldsBundle}" key="Paragraph_GreenDealTermsAndConditions" var="include"/>
                    <jsp:include page="${include}" flush="true" />
					</div>
				</div>
			</div>			
		</main>
		<jsp:include page="green-deal-close.jsp"></jsp:include>		
	</nds:form>
</body>
</html>
