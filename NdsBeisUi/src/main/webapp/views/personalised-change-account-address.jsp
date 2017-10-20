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
<fmt:message bundle="${FieldsBundle}" key="Title_changeAccountAddress"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" action="index.htm">
		<jsp:include page="preliminaries.jsp"></jsp:include>
		<main id="content" role="main"> <jsp:include
			page="phase-banner.jsp" /> <form:input type="hidden"
			path="ndsViewState" /> <form:input type="hidden"
			path="navigationalState" /> <form:input type="hidden"
			path="language" /> <header>
			<nds:back />
		</header>

		<div class="grid-row">
			<div class="column-full">
				<jsp:include page="form-error.jsp"/>
				<h1 class="form-title heading-large" role="banner">
					<fmt:message bundle="${FieldsBundle}"
						key="Heading_changeAccountAddress" />
				</h1>
			</div>
		</div>

		<div class="grid-row">
			<div class="column-full">
				<section id="formwrap">
	                <div id="current-address-detail" class="form-group">
		                <nds:field path="modifiedContactAddress">
		                	<nds:invalid/>				
							<nds:address path="modifiedContactAddress" showCountryList="true" invalidAboveInput="true"/>
						</nds:field>
						<nds:field path="modifiedContactAddress.country"></nds:field>
					</div>
					<section class="submit">
						<div class="actions-group">
							<button type="submit" name="action" value="NEXT" id="button.next"
								class="button next" formnovalidate>
								<fmt:message bundle="${FieldsBundle}"
									key="Button_Save_Changes" />
							</button>
						</div>
					</section>
				</section>
			</div>
		</div>
		</main>

		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>
