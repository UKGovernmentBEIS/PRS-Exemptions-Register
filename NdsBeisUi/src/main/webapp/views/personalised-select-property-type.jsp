<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_Select_Property_Type"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" method="post">
		<jsp:include page="preliminaries.jsp" />
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />
		<main id="content" role="main"> <jsp:include
			page="phase-banner.jsp" /> <header>
			<nds:back />
		</header>
		<div class="grid-row">
			<div class="column-full">
				<jsp:include page="form-error.jsp" />
				<section id="forminfowrap">
					<h1 class="form-title heading-large" role="banner">
						<fmt:message bundle="${FieldsBundle}"
							key="Heading_Select_Property_Type" />
					</h1>
				</section>
			</div>
		</div>
	
		<div class="grid-row">
			<div class="column-full">
				<div class="form-group">
					<nds:field path="exemptionDetails.propertyType">
						<fieldset class="inline radio">
							<nds:invalid />
							<nds:radiobutton
								label="Label_exemptionDetails.propertyType.domestic"
								value="PRSD" class="propertyType" />
							<nds:radiobutton
								label="Label_exemptionDetails.propertyType.nondomestic"
								value="PRSN" class="propertyType" />
						</fieldset>
					</nds:field>
				</div>
				
				<div>
					<details role="group">
						<summary role="button" aria-controls="select_property_name_div"
							aria-expanded="false" id="select_property_type_link">
							<span class="summary"><fmt:message
									bundle="${FieldsBundle}" key="link_property_type" /></span>
						</summary>
						<div class="panel panel-border-narrow"
							id="select_property_name_div" aria-hidden="true">
							<p id="more_about_property_type">
								<fmt:message bundle="${FieldsBundle}"
									key="paragraph_more_about_property_type" />
							</p>
							<p id="more_about_property_type1">
								<fmt:message bundle="${FieldsBundle}"
									key="paragraph_more_about_property_type1" />
							</p>
							<p id="more_about_property_type2">
								<fmt:message bundle="${FieldsBundle}"
									key="paragraph_more_about_property_type2" />
							</p>
							<p id="more_about_property_type3">
								<fmt:message bundle="${FieldsBundle}"
									key="paragraph_more_about_property_type3" />
							</p>
						</div>
					</details>
				</div>
				<div class="form-group">
					<section class="submit">
						<div>

							<button type="submit" name="action" value="NEXT" id="button.next"
								class="button next">
								<fmt:message bundle="${FieldsBundle}" key="Button_NEXT" />
							</button>
						</div>
					</section>
				</div>
			</div>
		</div>

		</main>
		<jsp:include page="close.jsp"></jsp:include>

	</nds:form>
</body>
</html>
