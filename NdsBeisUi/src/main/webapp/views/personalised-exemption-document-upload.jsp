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
<fmt:message bundle="${FieldsBundle}" key="Title_Exemption_Document_Upload"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>

	<nds:form id="pageForm" method="post" action='index.htm'
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
				<jsp:include page="form-error.jsp"/>
				
				<section id="forminfowrap">
					<h2 class="form-title heading-large" role="banner">
						<c:out
							value="${command.uiData.selectedExemptionTypeText.documentsPwsLabel}"
							escapeXml="false"></c:out>
					</h2>
				</section>
			</div>
		</div>
		<div class="grid-row">
			<div class="column-full">
				<div class="text">
					<p>
						<c:out
							value="${command.uiData.selectedExemptionTypeText.documentsPwsText}"
							escapeXml="false"></c:out>
					</p>
				</div>
				<c:choose>
					<c:when
						test="${not empty command.exemptionDetails.epc.files.resources}">
						<c:set var="showaddbutton"
							value="${fn:length(command.exemptionDetails.epcEvidenceFiles.resources) < command.uiData.selectedExemptionTypeText.maxDocuments }" />
					</c:when>
					<c:otherwise>
						<c:set var="showaddbutton" value="true" />
					</c:otherwise>
				</c:choose>
				
				<div class="form-group">
					<jsp:include page="personalised-exemption-upload-component.jsp">
						<jsp:param name="path"
							value="exemptionDetails.epcEvidenceFiles" />
						<jsp:param name="showLabel" value="false" />
						<jsp:param name="supportedtypes"
							value="${uploadProperties.limitationsMap['resource'].getSupportedTypeNames(FieldsBundle,command)}" />
						<jsp:param name="uipath" value="uiData.multipartExemptionFile" />
						<jsp:param name="actionvalue" value="AddResource" />
						<jsp:param name="supportedmaxsize"
							value="${uploadProperties.limitationsMap['resource'].getMaxResourceSize(command) }" />
						<jsp:param name="supportedmaxsizemb"
							value="${uploadProperties.limitationsMap['resource'].getMaxResourceSizeMB(command) }" />
						<jsp:param name="showaddbutton" value="${showaddbutton}" />
						<jsp:param name="uidescription" value="uiData.epcFileDescription" />
					</jsp:include>
				</div>
				

				<section class="next">
					<c:if
						test="${not empty command.exemptionDetails.epcEvidenceFiles.resources}">
						<div class="form-group">
							<h3 class="heading-medium"> <fmt:message
									bundle="${FieldsBundle}"
									key="Label_exemptionDetails.uploadtext" />
							</h3>
							<table>
								<tr>
									<th><fmt:message bundle="${FieldsBundle}"
											key="UploadColumn_filename" /></th>
									<th><fmt:message bundle="${FieldsBundle}"
											key="UploadColumn_description" /></th>
									<th><fmt:message bundle="${FieldsBundle}"
											key="UploadColumn_remove" /></th>
								</tr>
								<c:forEach
									items="${command.exemptionDetails.epcEvidenceFiles.resources}"
									varStatus="res">
									<tr>
										<td class="filename"><a id="${res.index}"
											href="${contextUi}/download-document/${command.exemptionDetails.epcEvidenceFiles.resources[res.index].fileId}.do"
											target="_blank">
												${command.exemptionDetails.epcEvidenceFiles.resources[res.index].fileName}
										</a></td>
										<td><nds:input
												path="exemptionDetails.epcEvidenceFiles.resources[${res.index}].description" class="form-control" /></td>
										<td class="removefile">
											<button type="submit" name="action"
												id="DeleteResource:${command.exemptionDetails.epcEvidenceFiles.resources[res.index].fileId}"
												value="DeleteResource:${command.exemptionDetails.epcEvidenceFiles.resources[res.index].fileId}"
												role="link">
												<fmt:message bundle="${FieldsBundle}"
													key="Button_removeFile" />
											</button> <span class="uploadspinner"></span>
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</c:if>
					<section class="submit">
						<div>
							<button type="submit" name="action" value="NEXT" id="button.next"
								class="button next">
								<fmt:message bundle="${FieldsBundle}" key="Button_NEXT" />
							</button>
						</div>
					</section>
				</section>
			</div>
		</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
		<script
			src="${contextUi}/assets/javascripts/views/file-upload-shared${minify}.js?version=${version}"></script>

	</nds:form>
</body>
</html>
