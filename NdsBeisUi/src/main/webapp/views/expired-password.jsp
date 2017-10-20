﻿<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />

<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_changePassword"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" action="index.htm">
		<jsp:include page="preliminaries.jsp"></jsp:include>
		
		<form:input type="hidden" path="changePasswordDetails.salt" />
		<form:input type="hidden" path="changePasswordDetails.encryptedUserName" />
		<form:input type="hidden" path="changePasswordDetails.encryptedTenant" />
		
		
		<form:input type="hidden" path="ndsViewState" />
			<form:input type="hidden" path="navigationalState" />
			<form:input type="hidden" path="language" />
		<main id="content" role="main">
			<jsp:include page="phase-banner.jsp"/>			
			
			<header>
		        <h1 class="form-title heading-large">
		        	<fmt:message bundle="${FieldsBundle}" key="Heading_expired_password" />
		        </h1>
			</header>			

			<div class="grid-row">
				<div class="column-full">
					<jsp:include page="form-error.jsp"/>
				</div>
			</div>
			
			<section class="page current">
				<div id="guidance">
					<p>
						<fmt:message bundle="${FieldsBundle}" key="Paragraph_expired_password" />
					</p>
				</div>
			</section>
			
			<div class="grid-row">
				<div class="column-full">
					<div class="form-group" >
						<nds:field path="changePasswordDetails.userName" labeldecoration="required">
							<nds:label label="Label_current_username" />
							<nds:invalid />
							<nds:input placeholder="false" class="form-control" readonly="true" autocomplete="off"/>							
						</nds:field>
					</div>				
					<div class="form-group" >
						<nds:field path="changePasswordDetails.oldPassword"
							labeldecoration="required">
							<nds:label />
							<nds:invalid />
							<nds:input placeholder="false" class="form-control"
										autofocus="true" type="password" autocomplete="off"/>							
						</nds:field>
					</div>
					<div class="form-group" >
						<nds:field path="changePasswordDetails.newPassword"
							labeldecoration="required"
							hints="Hint_changePasswordDetails.newPassword">							
							<nds:label />
							<nds:hint />	
							<nds:invalid />									
							<nds:input placeholder="false"
									class="form-control" autofocus="false"
									type="password" autocomplete="off"/>
						</nds:field>
					</div>
					<div class="form-group" >
						<nds:field path="changePasswordDetails.confirmPassword"
							labeldecoration="required">							
							<nds:label />
							<nds:invalid />
							<nds:input placeholder="false" class="form-control"
									autofocus="false" type="password" autocomplete="off"/>							
						</nds:field>
					</div>
					<div class="form-group" >
						<button type="submit" name="action" id="button.next" value="NEXT"
							class="button">
							<fmt:message bundle="${FieldsBundle}"
								key="Button_CHANGE_PASSWORD" />
						</button>
					</div>
				</div>
			</div>
		</main>
		
		<jsp:include page="close.jsp"></jsp:include>
		<script
			src="${contextUi}/assets/javascripts/password-strength.js?version=${version}"></script>
	</nds:form>
</body>
</html>
