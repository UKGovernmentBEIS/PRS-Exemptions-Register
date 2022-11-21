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
					<h1 class="form-title heading-large">
						<fmt:message bundle="${FieldsBundle}"
							key="Heading_Select_Property_Type" />
					</h1>
				</section>
			</div>
		</div>
	
		<div class="grid-row">
			<div class="column-full">				
				<div class="form-group"> 
		       		<nds:radiobuttonelement path="exemptionDetails.propertyType"
						items="${command.uiData.refData.propertyType}" cssClass="propertyType"/>
		       	</div>				
				<div>
					<details role="group">
						<summary role="button" aria-controls="select_property_name_div"
							aria-expanded="false" id="select_property_type_link">
							<span class="summary"><fmt:message
									bundle="${FieldsBundle}" key="link_property_type" /></span>
						</summary>
						<section id="description_about_property">
							<div class="panel panel-border-narrow"
								id="select_property_name_div" >
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
						</section>
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
