<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_Cookies" var="title" />
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
					<jsp:include page="form-error.jsp"/>			
					<h1 class="form-title heading-large" role="banner">
						<fmt:message bundle="${FieldsBundle}" key="Heading_Cookies" />
					</h1>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-two-thirds">			
					<div class="form-group">						
						 <fmt:message bundle="${FieldsBundle}" key="Paragraph_GreenDealCookies" var="include"/>
                    <jsp:include page="${include}" flush="true" />
					</div>				
				</div>				
			</div>		
		</main>
		<jsp:include page="green-deal-close.jsp"></jsp:include>
	</nds:form>
</body>
</html>