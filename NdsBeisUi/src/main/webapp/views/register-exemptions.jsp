<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_Register_Exemptions"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" action='index.htm'>
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
					<h1 class="form-title heading-large">
						<fmt:message bundle="${FieldsBundle}"
							key="Heading_Register_Exemptions" />
					</h1>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">
					<section id="formwrap">
						<div class="form-group">
							<table class="dgstandard">
								<thead>
									<tr>
										<th colspan="2" class="dgdata"><c:out value="${command.uiData.selectedExemptionData.address}"></c:out></th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="dgdata"><fmt:message bundle="${FieldsBundle}"
												key="ColumnLabel_exemptionRegisteredOn" />												
										<td class="dgdata">
											<time datetime="${command.uiData.selectedExemptionData.registeredDate}">
												<spring:eval
													expression="T(com.northgateps.nds.beis.ui.util.NdsDateTimeUtils).getDatePrintFormatter().format(command.uiData.selectedExemptionData.registeredDate)" />
											</time>
										</td>
									</tr>
									<c:if test="${command.uiData.selectedExemptionData.landLordName.trim().length()>0}">
										<tr>
											<td class="dgdata"><fmt:message bundle="${FieldsBundle}"
													key="ColumnLabel_Landlordname" />
											<td class="dgdata"><c:out value="${command.uiData.selectedExemptionData.landLordName}"/> </td>		
										</tr>
									</c:if>
									<tr>
										<td class="dgdata"><fmt:message bundle="${FieldsBundle}"
												key="ColumnLabel_exemptionType" />
										<td class="dgdata"><c:out value="${command.uiData.selectedExemptionData.pwsDescription}" escapeXml="false"></c:out> </td>		
									</tr>
								</tbody>
							</table>
						</div>
						<c:if test="${command.uiData.selectedExemptionData.epcExists == true}">
						<p><a id="epcfile-download-link" href="${contextUi}/download-epc-file/${command.uiData.selectedExemptionData.exemptionRefNo}">Download EPC</a></p>
						<p><a id="report.epc.content" href="mailto:${feedbackEmail}">Report this content</a></p>
						</c:if>
					</section>
		
					<section class="submit">
					
						<button type="submit" name="action" value="NEXT"
							id="button.next" class="button next">
							<fmt:message bundle="${FieldsBundle}" key="Button_NewSearch" />
						</button>
						
					</section>
				</div>
			</div>
		</main>

		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>

</body>
</html>