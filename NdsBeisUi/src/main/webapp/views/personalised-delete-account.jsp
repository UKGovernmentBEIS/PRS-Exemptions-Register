﻿<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />

<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_Delete_Account"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" action="index.htm">
		<jsp:include page="preliminaries.jsp"></jsp:include>
		<form:input type="hidden" path="ndsViewState" />
			<form:input type="hidden" path="navigationalState" />
			<form:input type="hidden" path="language" />
		<main id="content" role="main">
			<jsp:include page="phase-banner.jsp"/>			
			<header>
				<nds:back/>
			</header>			
			<div class="grid-row">
				<div class="column-full">
					<jsp:include page="form-error.jsp"/>
					<h1 class="form-title heading-large" role="banner">
						<fmt:message bundle="${FieldsBundle}" key="Delete_my_account" />
					</h1>					
				</div>
			</div>
			  <section>
				  <section id="formwrap">
					  <div class="form-group">
						  <p class="body-text"><fmt:message bundle="${FieldsBundle}" key="Paragraph_DeleteAccount1" /></p>
						  <p class="body-text"><fmt:message bundle="${FieldsBundle}" key="Paragraph_DeleteAccount2" /></p>
						  <p><fmt:message bundle="${FieldsBundle}" key="Paragraph_DeleteAccount3" /></p>
					  </div>
				  </section>
				  <section class="submit">
					  <div>
						  <button type="submit" name="action" value="NEXT:DeleteUserAccount" id="button.next"
							  class="button">
						    <fmt:message bundle="${FieldsBundle}" key="Button_DeleteAccount" />
					    </button>
					  </div>
			    </section>
			  </section>
		</main>
		
		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>
