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
<fmt:message bundle="${FieldsBundle}" key="Title_Exemption_Text"
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
					<h1>
						<label class="form-title heading-large" for="exemptionDetails.exemptionText">
							<c:out value="${command.uiData.selectedExemptionTypeText.textPwsLabel}" escapeXml="false"></c:out>
						</label>
					</h1>
				</section>
			</div>
		</div>
		<div class="grid-row">
			<div class="column-full">
				<div class="text">
					<p>
						<c:out
							value="${command.uiData.selectedExemptionTypeText.textPwsText}"
							escapeXml="false"></c:out>
					</p>
				</div>
				<div class="form-group">
						<nds:field path="exemptionDetails.exemptionText">
							<nds:invalid />
							<nds:textarea class="form-control" rows="10" cols="40"
								data-maxlength="4000" />							
						</nds:field>
					</div>

				<c:choose>
					<c:when
						test="${not empty command.exemptionDetails.epc.files.resources}">
						<c:set var="showaddbutton"
							value="${fn:length(command.exemptionDetails.exemptionTextFile.resources) == 0 }" />
					</c:when>
					<c:otherwise>
						<c:set var="showaddbutton" value="true" />
					</c:otherwise>
				</c:choose>
				
				<div class="form-group">
					<jsp:include page="personalised-exemption-upload-component.jsp">
						<jsp:param name="path"
							value="exemptionDetails.exemptionTextFile" />
						<jsp:param name="showLabel" value="true" />
						<jsp:param name="supportedtypes"
							value="${uploadProperties.limitationsMap['resource'].getSupportedTypeNames(FieldsBundle,command)}" />
						<jsp:param name="supportedmaxsize"
							value="${uploadProperties.limitationsMap['resource'].getMaxResourceSize(command) }" />
						<jsp:param name="supportedmaxsizemb"
							value="${uploadProperties.limitationsMap['resource'].getMaxResourceSizeMB(command) }" />
						<jsp:param name="showaddbutton" value="${showaddbutton}" />
						<jsp:param name="uipath" value="uiData.multipartExemptionTextFile" />
						<jsp:param name="uidescription" value="uiData.epcFileDescription" />
						<jsp:param name="actionvalue" value="AddResource" />
					</jsp:include>
				</div>

                <div id="divTemp">
					<input type="hidden" name="hiddenUploadField" id="hiddenUploadField"/>
			    </div>
				<section class="next">
					<c:if
						test="${not empty command.exemptionDetails.exemptionTextFile.resources}">
						<div class="form-group">
							<h3 class="heading-medium"> <fmt:message
									bundle="${FieldsBundle}"
									key="Label_exemptionDetails.uploadtext" />
							</h3>
							<table id="uploadedFiles">
								<tr>
									<th><fmt:message bundle="${FieldsBundle}"
											key="UploadColumn_filename" /></th>
									<th><fmt:message bundle="${FieldsBundle}"
											key="UploadColumn_description" /></th>
									<th><fmt:message bundle="${FieldsBundle}"
											key="UploadColumn_remove" /></th>
								</tr>
								<c:forEach
									items="${command.exemptionDetails.exemptionTextFile.resources}"
									varStatus="res">
									<tr>
										<td class="filename"><a id="${res.index}"
											href="${contextUi}/download-document/${command.exemptionDetails.exemptionTextFile.resources[res.index].fileId}"
											target="_blank">
												${command.exemptionDetails.exemptionTextFile.resources[res.index].fileName}
										</a></td>
										<td><nds:input
												path="exemptionDetails.exemptionTextFile.resources[${res.index}].description" class="form-control" /></td>
										<td class="removefile">
											<button type="submit" name="action"
												id="DeleteResource:${command.exemptionDetails.exemptionTextFile.resources[res.index].fileId}"
												value="DeleteResource:${command.exemptionDetails.exemptionTextFile.resources[res.index].fileId}"
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
		<script
			src="${contextUi}/assets/javascripts/views/file-upload-shared${minify}.js?version=${version}"></script>

	</nds:form>
</body>
</html>
