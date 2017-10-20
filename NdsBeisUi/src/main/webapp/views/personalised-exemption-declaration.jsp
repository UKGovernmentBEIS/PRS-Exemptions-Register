<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${language}" />
<!DOCTYPE html>
<html lang="${language}">
<fmt:message bundle="${FieldsBundle}" key="Title_ExemptionDeclaration"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" method="post">
		<jsp:include page="preliminaries.jsp"></jsp:include>
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
				<h1 class="form-title heading-large" role="banner">
					<fmt:message bundle="${FieldsBundle}"
						key="Heading_ExemptionDeclaration" />
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
									<th colspan="2"><fmt:message bundle="${FieldsBundle}"
											key="Heading_Title_ExemptionDeclaration" /></th>
								</tr>
							</thead>
							<tbody>
								<c:if
									test="${(not empty command.exemptionDetails.landlordDetails)}">
									<c:if
										test="${(not empty command.exemptionDetails.landlordDetails.personNameDetail)}">
										<tr>
											<td class="top"><fmt:message bundle="${FieldsBundle}"
													key="ColumnLabel_exemptionDetails.landlordName" /></td>
											<td class="dgdata">${command.exemptionDetails.landlordDetails.personNameDetail.firstname} ${command.exemptionDetails.landlordDetails.personNameDetail.surname}</td>
										</tr>
									</c:if>
									<c:if
										test="${(not empty command.exemptionDetails.landlordDetails.organisationNameDetail)}">
										<tr>
											<td class="top"><fmt:message bundle="${FieldsBundle}"
													key="ColumnLabel_exemptionDetails.landlordName" /></td>
											<td class="dgdata">${command.exemptionDetails.landlordDetails.organisationNameDetail.orgName}</td>
										</tr>
									</c:if>									
									<tr>
										<td class="top"><fmt:message bundle="${FieldsBundle}"
												key="ColumnLabel_exemptionDetails.landlordEmail" /></td>
										<td class="dgdata">${command.exemptionDetails.landlordDetails.emailAddress}
										</td>
									</tr>
									<tr>
										<td class="top"><fmt:message bundle="${FieldsBundle}"
												key="ColumnLabel_exemptionDetails.landlordPhoneNumber" /></td>
										<td class="dgdata">${command.exemptionDetails.landlordDetails.phoneNumber}
										</td>
									</tr>
								</c:if>
								<tr>
									<td class="top"><fmt:message bundle="${FieldsBundle}"
											key="ColumnLabel_myAccountDetails.beisRegistrationDetails.contactAddress" />
									</td>
									<td class="dgdata" id="addressLines">
										${command.exemptionDetails.epc.propertyAddress.line[0]}, <c:if
											test="${(not empty command.exemptionDetails.epc.propertyAddress.line[1])}">
											 ${command.exemptionDetails.epc.propertyAddress.line[1]},
			    						 </c:if> ${command.exemptionDetails.epc.propertyAddress.town}, <c:if
											test="${(not empty command.exemptionDetails.epc.propertyAddress.county)}">
											 ${command.exemptionDetails.epc.propertyAddress.county},
		    							</c:if> ${command.exemptionDetails.epc.propertyAddress.postcode}
									</td>

								</tr>
								<tr>
									<td class="top"><fmt:message bundle="${FieldsBundle}"
											key="ColumnLabel_exemptionDetails.exemptionType" /></td>
									<td class="dgdata">${command.uiData.selectedExemptionTypeText.pwsDescription}

									</td>
								</tr>

								<tr>
									<td class="top"><fmt:message bundle="${FieldsBundle}"
											key="ColumnLabel_Epc_Uploaded" /></td>


									<c:forEach
										items="${command.exemptionDetails.epc.files.resources}"
										varStatus="res">
										<c:if test="${res.index > 0}">
											<c:out value="${'<tr>'}" escapeXml="false" />
										</c:if>

										<td class="top"><fmt:message bundle="${FieldsBundle}"
												key="UploadColumn_filename" />:
											${command.exemptionDetails.epc.files.resources[res.index].fileName}
											<br>
											${command.exemptionDetails.epc.files.resources[res.index].description}
										</td>
										<c:if
											test="${res.index + 1 > command.exemptionDetails.epc.files.resources.size()}">
											<c:out value="${'</tr>'}" escapeXml="false" />
										</c:if>

									</c:forEach>
								</tr>
							</tbody>

							<thead>
								<tr>
									<th colspan="2"><fmt:message bundle="${FieldsBundle}"
											key="ColumnLabel_exemptionDetails.evidenceSubmitted" /></th>
								</tr>
							</thead>
							<tbody>
								<c:if
									test="${command.uiData.selectedExemptionTypeText.documentsRequired=='true'}">

									<tr>
										<td class="top"
											rowspan="${command.exemptionDetails.epcEvidenceFiles.resources.size()}">${command.uiData.selectedExemptionTypeText.documentsPwsLabel}</td>
										<c:if
											test="${command.exemptionDetails.epcEvidenceFiles.resources.size()=='0'}">
											<c:out value="${'<td></td>'}" escapeXml="false" />
										</c:if>
										<c:forEach
											items="${command.exemptionDetails.epcEvidenceFiles.resources}"
											varStatus="res">
											<c:if test="${res.index > 0}">
												<c:out value="${'<tr>'}" escapeXml="false" />
											</c:if>

											<td class="top"><fmt:message bundle="${FieldsBundle}"
													key="UploadColumn_filename" />:
												${command.exemptionDetails.epcEvidenceFiles.resources[res.index].fileName}
												<br>
												${command.exemptionDetails.epcEvidenceFiles.resources[res.index].description}
											</td>
											<c:if
												test="${res.index + 1 > command.exemptionDetails.epcEvidenceFiles.resources.size()}">
												<c:out value="${'</tr>'}" escapeXml="false" />
											</c:if>

										</c:forEach>
									</tr>
								</c:if>
								<c:if
									test="${command.uiData.selectedExemptionTypeText.textRequired=='true'}">

									<tr>
										<!--  calculate the row span -->
										<c:set var="rowCount"
											value="${command.exemptionDetails.exemptionTextFile.resources.size()}" />
										<c:if test="${rowCount == 0}">
											<c:set var="rowCount" value="1" />
										</c:if>
										<td class="top"
											rowspan="${rowCount + (fn:length(command.exemptionDetails.exemptionText) gt 0 ? 1 : 0)}">
											${command.uiData.selectedExemptionTypeText.textPwsLabel}</td>

										<c:forEach
											items="${command.exemptionDetails.exemptionTextFile.resources}"
											varStatus="res">
											<c:if test="${res.index > 0}">
												<c:out value="${'<tr>'}" escapeXml="false" />
											</c:if>
											<td><fmt:message bundle="${FieldsBundle}"
													key="UploadColumn_filename" />:
												${command.exemptionDetails.exemptionTextFile.resources[res.index].fileName}
												<br>
												${command.exemptionDetails.exemptionTextFile.resources[res.index].description}
											</td>
											<c:if
												test="${res.index + 1 > command.exemptionDetails.exemptionTextFile.resources.size()}">
												<c:out value="${'</tr>'}" escapeXml="false" />
											</c:if>
										</c:forEach>
									</tr>
									<c:if
										test="${fn:length(command.exemptionDetails.exemptionText) gt 0}">
										<tr>
											<td><span class="pre-wrap">${command.exemptionDetails.exemptionText}</span></td>
										</tr>
									</c:if>
								</c:if>
								<c:if
									test="${command.uiData.selectedExemptionTypeText.startDateRequired=='true' }">
									<tr>
										<td class="top">${command.uiData.selectedExemptionTypeText.startDatePwsLabel}
										</td>
										<td class="dgdata"><time
												datetime="${command.exemptionDetails.exemptionStartDate}">
												<spring:eval
													expression="T(com.northgateps.nds.beis.ui.util.NdsDateTimeUtils).getDatePrintFormatter().format(command.exemptionDetails.exemptionStartDate)" />
											</time></td>
									</tr>
								</c:if>
								<c:if
									test="${command.uiData.selectedExemptionTypeText.frvRequired=='true' }">
									<tr>
										<td class="top"><c:out
												value="${command.uiData.selectedExemptionTypeText.frvPwsLabel}"></c:out>
										</td>
										<td class="dgdata"><c:out
												value="${command.uiData.selectedExemptionTypeLovText}"
												escapeXml="false" /><br> <span class="pre-wrap">${command.exemptionDetails.exemptionReasonAdditionalText}</span>
										</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
				</section>
				<section class="page current">
					<div class="form-group">
						<div class="text">
							<div class="notice">
								<i class="icon icon-important"> <span
									class="visually-hidden">Warning</span>
								</i> <strong class="form-title heading-large" role="banner">
									<fmt:message bundle="${FieldsBundle}" key="Heading_Declaration" />
								</strong>
							</div>
						</div>
						<div class="panel panel-border-wide">
  						
							<ul id="requirementList" class="list list-bullet">
								<li><fmt:message bundle="${FieldsBundle}"
										key="Label_confirmation_information" /></li>
								<c:choose>
									<c:when test="${command.exemptionDetails.propertyType.name() == 'PRSD'}">
      									<li><fmt:message bundle="${FieldsBundle}"
										key="Label_confirmation_legal_PRSD" /></li>
   								    </c:when>
									<c:otherwise>
										<li><fmt:message bundle="${FieldsBundle}"
										key="Label_confirmation_legal_PRSN" /></li>
									</c:otherwise>
								</c:choose>								
							</ul>						
						</div>
						<nds:field path="uiData.isAgreed" labeldecoration="required"
							class="form-checkbox">
							<nds:invalid />
							<fieldset>
								<label class="block-label selection-button-checkbox"
									for="uiData.isAgreed"> <nds:checkbox value="Agree"
										autolabel="false" /> <fmt:message bundle="${FieldsBundle}"
										key="Label_uiData.isAgreed" />
								</label>
							</fieldset>
						</nds:field>
						<p>
							<fmt:message bundle="${FieldsBundle}"
								key="Paragraph_privacy_policy_start" />
							<a href="privacy-policy" id="privacy-policy-link" target="_blank">
								<fmt:message bundle="${FieldsBundle}"
									key="Paragraph_privacy_policy_link" />
							</a>
							<fmt:message bundle="${FieldsBundle}"
								key="Paragraph_privacy_policy_end" />
						</p>
					</div>
				</section>

				<section class="submit">
					<div>
						<button type="submit" name="action" value="NEXT" id="button.next"
							class="button next">
							<fmt:message bundle="${FieldsBundle}"
								key="Button_RegisterExemption" />
						</button>
					</div>
				</section>
			</div>
		</div>
		</main>

		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>