<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_failover_landing" var="title" />
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
			<jsp:include page="phase-banner.jsp"/>
			<div class="grid-row">
				<div class="column-full">
					<jsp:include page="form-error.jsp"/>				
					<h1 class="form-title heading-large">
						<fmt:message bundle="${FieldsBundle}" key="Heading_failover_landing" />
					</h1>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">
					<section id="formwrap">
						<div class="form-group">						
							<a href="used-service-before" id="login"><fmt:message bundle="${FieldsBundle}" key="Paragraph_login" /></a>
							<br />
							<a href="register-search-penalties" id="register-search-penalties-link"><fmt:message bundle="${FieldsBundle}" key="Heading_register_search_penalties" /></a>
							<br />
							<a href="register-search-exemptions" id="register-search-exemptions-link"><fmt:message bundle="${FieldsBundle}" key="Heading_Register_Search_Exemptions" /></a>
							<br />
							<a href="register-search-gdar-gdip" id="register-search-gdar-gdip-link"><fmt:message bundle="${FieldsBundle}" key="Heading_RegisterSearchGdarGdip" /></a>		
						</div>
					</section>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>
