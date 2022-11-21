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
<fmt:message bundle="${FieldsBundle}"
	key="Title_Register_Search_Exemptions" var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" action='index.htm'>
		<jsp:include page="preliminaries.jsp"></jsp:include>
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />
		<main id="content" role="main"> <jsp:include
			page="phase-banner.jsp" /> <header> </header>
		<div class="grid-row">
			<div class="column-full">
				<jsp:include page="form-error.jsp" />
				<h1 class="form-title heading-large">
					<fmt:message bundle="${FieldsBundle}"
						key="Heading_Register_Search_Exemptions" />
				</h1>
				<p class="body-text">
					<fmt:message bundle="${FieldsBundle}"
						key="Paragraph_uiData.searchExemptionText" />
					<a href="${registerSearchExemptionsUrl}"
						id="min.standard.energy.efficiency" target="_blank"><fmt:message
							bundle="${FieldsBundle}"
							key="Paragraph_MinStandrdEnergyEfficiency" /></a>
				</p>
				<p class="body-text">
					<fmt:message bundle="${FieldsBundle}"
						key="Paragraph_exemptionSearchText1" />
					<br />
					<fmt:message bundle="${FieldsBundle}"
						key="Paragraph_exemptionSearchText2" />
				</p>

			</div>
		</div>
		<div class="grid-row">
			<div class="column-full">
				<div class="form-group">
				    <nds:field path="uiData.exemptionSearch.exemptionPostcodeCriteria"
						labeldecoration="required">
						<nds:label />
						<nds:invalid />
						<nds:input class="form-control form-control-1-8" placeholder="false" />
					</nds:field>
				</div>
				<nds:field path="uiData.exemptionSearch.panelDisplayed">
					<nds:input type="hidden" id = "panelDisplayed"/>
					</nds:field>
				<div class="form-group">
				<details id="other-ways"
						${command.uiData.exemptionSearch.panelDisplayed == true || command.uiData.exemptionSearch.getExemptionAdvancedSearchDone()? 'open' : ''}>
						<summary id="other-ways-to-search-exemptions">
							<span class="summary"> <fmt:message
									bundle="${FieldsBundle}"
									key="Link_other_ways_to_search_for_exemptions" />
							</span>
						</summary>
						<div class="panel panel-border-narrow searchdetail">
							<p>
								<fmt:message bundle="${FieldsBundle}"
									key="Paragraph_combine_search_criteria_for_detailed_search_exemptions" />
							</p>

							<div class="form-group">
								<nds:field
									path="uiData.exemptionSearch.exemptionLandlordsNameCriteria"
									labeldecoration="required" hints="Hint_search_by_landordname">
									<nds:label />
									<nds:hint />
									<nds:invalid />
									<nds:input class="form-control" placeholder="false" />
								</nds:field>
							</div>

							<div class="form-group">
								<nds:field path="uiData.exemptionSearch.exemptPropertyDetails"
									labeldecoration="required" hints="Hint_exempt_property_details">
									<nds:label />
									<nds:hint />
									<nds:invalid />
									<nds:input class="form-control" placeholder="false" />
								</nds:field>
							</div>

							<div class="form-group">
								<nds:field path="uiData.exemptionSearch.town"
									labeldecoration="required">
									<nds:label />
									<nds:hint />
									<nds:invalid />
									<nds:input class="form-control" placeholder="false" />
								</nds:field>
							</div>

							<div class="form-group" id="property-drop-down">
								<nds:selectelement
									path="uiData.exemptionSearch.service"
									id="select-box"
									items="${command.uiData.refData.getServiceType()}"
									labeldecoration="required" />
							</div>
                            

							<div class="form-group" id="exemption-drop-down-prsd">
								<nds:selectelement
									path="uiData.exemptionSearch.exemptionType_PRSD"
									id="select-box-prsd"
									items="${command.uiData.refData.getDomesticExemptions()}"
									labeldecoration="required" />
							</div>

							<div class="form-group" id="exemption-drop-down-prsn">
								<nds:selectelement
									path="uiData.exemptionSearch.exemptionType_PRSN"
									id="select-box-prsn"
									items="${command.uiData.refData.getNonDomesticExemptions()}"
									labeldecoration="required" />
							</div>							
						
						</div>
					</details>
					
				</div>
				<div class="form-group">
					<section class="submit">
						<button type="submit" name="action" value="FindExemptions"
							id="button.next" class="button next">
							<fmt:message bundle="${FieldsBundle}" key="Button_FindExemptions" />
						</button>
					</section>
				</div>
			    <div class="form-group">
					<c:if test="${command.uiData.getExemptionSearchResult !=null}">
						<div id="local_authority_list">
							<c:choose>
								<c:when
									test="${command.uiData.getExemptionSearchResult.exemptions.size() == 0}">
									<p id="customlede">
										<fmt:message bundle="${FieldsBundle}"
											key="Prompt_getExemptionSearchResult.noMatch" />
									</p>
								</c:when>
								<c:otherwise>
                                   <table class="dgstandard" id="exemptionsList">
										<c:forEach var="exemption"
											items="${command.uiData.getExemptionSearchResult.exemptions}">
											<tr>
												<td class="dgdata">
													<button class="button-link" role="link" type="submit"
														name="action" id="button.authority.address"
														value="ChosenExemption:${exemption.exemptionRefNo}">
														<c:choose>
															<c:when test="${exemption.address == null}">
																<fmt:message bundle="${FieldsBundle}"
																	key="Prompt_getExemptionSearchResult.addressNotSupplied" />
															</c:when>
															<c:otherwise>
																<c:out value="${exemption.address}"></c:out>
															</c:otherwise>
														</c:choose>
													</button>
												</td>
												<td><c:if test="${(not empty exemption.landLordName)}">
														<button class="button-link" role="link" type="submit"
															name="action" id="button.authority.landlordname"
															value="ChosenExemption:${exemption.exemptionRefNo}">
															<c:out value="${exemption.landLordName}"></c:out>
														</button>
													</c:if></td>
											</tr>
										</c:forEach>
									</table>

								</c:otherwise>
							</c:choose>
						</div>
					</c:if>
				</div>
				<section class="submit">
					<div class="form-group">
						<a href="${registerSearchExemptionsFinishUrl}" id="finish" class="button next">
							<fmt:message bundle="${FieldsBundle}" key="Button_FINISH" />
						</a>
					</div>
				</section>
			</div>
		</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
		<script
			src="${contextUi}/assets/javascripts/views/register-search-exemptions${minify}.js?version=${version}"></script>
	</nds:form>
</body>
</html>