<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import = "java.sql.*"%>
<%@ page import = "java.util.*" %>
<%@ page import= "java.io.*" %>

<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_RegisterSearchGdarGdip"
	var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
	<jsp:param name="noTitleSuffix" value="true" />
</jsp:include>

<body>	
	<jsp:include page="green-deal-preliminaries.jsp" />
	<nds:form id="pageForm" method="post" action="index.htm">
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />
		<main id="content" role="main"> 
		<c:if test="${applicationPhase != 'BETA'}">
		<jsp:include page="phase-banner.jsp" />		
		</c:if>	
			<div class="grid-row">
				<div class="column-full">
					<jsp:include page="form-error.jsp" />		
					<section id="forminfowrap">
						<h1 class="form-title heading-large">
							<fmt:message bundle="${FieldsBundle}" key="Heading_RegisterSearchGdarGdip" />
						</h1>
					</section>			
				</div>
			</div>

		<div class="grid-row">
				<div class="column-full">
					<div class="form-group">				
						<nds:field path="uiData.gdarGdipSearch.searchTerm" labeldecoration="none" hints="Hint_uiData.gdarGdipSearch.searchTerm">													
							<div>
								<p class="body-text">
									<fmt:message bundle="${FieldsBundle}"
										key="Paragraph_RegisterSearchGdarGdip" />
								</p>
								<nds:hint />						
								<span> 
									<nds:input class="form-control" placeholder="false"/>										
								</span>
							</div>
							<nds:invalid />
						</nds:field>
					</div>				
					<div class="form-group">					
						<div id="get-started" class="get-started group">
							<button type="submit" name="action" value="SearchGdarGdip" id="button.search"
								class="button next">
								<fmt:message bundle="${FieldsBundle}" key="Button_Search" />
							</button>
						</div>
					</div>
				</div>
			</div>			
			<div class="grid-row">
				<div class="column-two-thirds">	
					<c:choose>									
						<c:when test="${command.uiData.gdarGdipSearch.status == 'FILE_AVAILABLE'}">
							<div class="form-group" id="download-file-content">
								<table>
									<thead>
										<tr>
											<th><fmt:message bundle="${FieldsBundle}" key="Paragraph_RegisterSearchGdarGdip_DownloadPrompt"/></th>
										</tr>							
									</thead>
									<tbody>
										<tr>
											<td><a id="file-download-link" href="${contextUi}/download-gdip-gdar/${command.uiData.gdarGdipSearch.searchTerm}">${command.uiData.gdarGdipSearch.fileName}</a></td>
										</tr>
									</tbody>
								</table>
							</div>
							<div>
								<span>
									<fmt:message bundle="${FieldsBundle}" key="Paragraph_RegisterSearchGdarGdip_TermsPrefix"/>
									<a href="gdar-gdip-terms-and-conditions" id="term-condition-link" target="_blank"> 
										<fmt:message bundle="${FieldsBundle}" key="Label_termsconditions_text" />
									</a>
								</span>
							</div>
						</c:when>
						<c:otherwise>
							<div class="form-group" id="search-message-contents">
								<c:choose>
									<c:when test="${command.uiData.gdarGdipSearch.status == 'NO_RESULTS'}">							
										<p><fmt:message bundle="${FieldsBundle}" key="Paragraph_RegisterSearchGdarGdip_NoMatches"/></p>
									</c:when>
									<c:when test="${command.uiData.gdarGdipSearch.status == 'SUPERSEDED'}">
										<p><fmt:message bundle="${FieldsBundle}" key="Paragraph_RegisterSearchGdarGdip_Superseded"/></p>
										<p><fmt:message bundle="${FieldsBundle}" key="Paragraph_RegisterSearchGdarGdip_Superseded2"/></p>									
									</c:when>
								</c:choose>
							</div>
						</c:otherwise>						
					</c:choose>
				</div>
			</div>		
		</main>
		<jsp:include page="green-deal-close.jsp"></jsp:include>

	</nds:form>
</body>
</html>