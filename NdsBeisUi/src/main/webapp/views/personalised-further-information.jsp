<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_FurtherInformation"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form method="post" action="index.htm"
		enctype="multipart/form-data">
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
						key="Heading_FurtherInformation" />
				</h1>
				</section>
			</div>
		</div>
		<div class="grid-row">
			<div class="column-full">
				<div class="text">
					<p>
						<c:out
							value="${command.uiData.selectedExemptionTypeText.confirmationPagetitle}"
							escapeXml="false"></c:out>
					</p>
				</div>
				<div class="form-group">
						<nds:field path="exemptionDetails.exemptionConfirmationText">
							<nds:invalid />
							<nds:textarea class="form-control" rows="10" cols="40"
								data-maxlength="4000" />							
						</nds:field>
			    </div>
			    <c:if test="${command.uiData.selectedExemptionTypeText.confirmationcheckbox == 'Y'}">
				    <section class="page current">
						<nds:field path="exemptionDetails.exemptionConfirmationIndicator" labeldecoration="required"
							class="form-checkbox">
							<nds:invalid />
							<fieldset>
							    <div class="multiple-choice">
							        <!--  Can't use the NDS CheckBoxTag - see comment on the CheckBoxTag  -->								    
								    <input id="exemptionDetails.exemptionConfirmationIndicator" name="exemptionDetails.exemptionConfirmationIndicator" autolabel="false" type="checkbox" value="Y">
									<label class="block-label selection-button-checkbox"
										for="exemptionDetails.exemptionConfirmationIndicator">									 
										 <c:out	value="${command.uiData.selectedExemptionTypeText.confirmationwording}" escapeXml="false"></c:out>
									</label>
									<input type="hidden" name="_exemptionDetails.exemptionConfirmationIndicator" value="on">
								</div>
							</fieldset>
						</nds:field>
					</section>
				</c:if>
				<section class="submit">
						<div>
							<button type="submit" name="action" value="NEXT" id="button.next"
								class="button next" formnovalidate>
								<fmt:message bundle="${FieldsBundle}" key="Button_NEXT" />
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
