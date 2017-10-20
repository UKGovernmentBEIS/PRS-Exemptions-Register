﻿<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />

<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_ChangeEmailAddress"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm">
		<jsp:include page="preliminaries.jsp"></jsp:include>
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
						<h2 class="form-title heading-large" role="banner">
							<fmt:message bundle="${FieldsBundle}"
								key="Heading_ChangeEmailAddress" />
						</h2>
					</section>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">
					<p>
						<fmt:message bundle="${FieldsBundle}"
							key="Hint_please_enter-password_info" />
					</p>
	
					<section class="page current">
						<nds:field path="updateEmailDetails.email">
							<nds:label /> 
							<nds:invalid />
							<nds:input placeholder="false" class="form-control" autofocus="false" />						
						</nds:field>
					</section>
	
					<section class="page current">
						<nds:field path="updateEmailDetails.confirmEmail">
							<nds:label />
							<nds:invalid />
							<nds:input class="form-control" placeholder="false"	autofocus="false" />
						</nds:field>
					</section>
	
					<section class="page current">
						<nds:field path="updateEmailDetails.password">
							<nds:label />
							<nds:invalid /> 
							<nds:input placeholder="false"	class="form-control" autofocus="false" type="password" autocomplete="off" />						
						</nds:field>
					</section>
	
					<section class="submit">
						<div>
							<div>
								<button type="submit" name="action" id="button.next" value="NEXT"
									class="button">
									<fmt:message bundle="${FieldsBundle}" key="Button_Save_Changes" />
								</button>
							</div>
						</div>
					</section>
				</div>
			</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>
