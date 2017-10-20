<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}"
	key="Title_Register_Search_Penalties" var="title" />
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

				<h1 class="form-title heading-large" role="banner">
					<fmt:message bundle="${FieldsBundle}"
						key="Heading_register_search_penalties" />
				</h1>
				<p>
					<fmt:message bundle="${FieldsBundle}"
						key="Paragraph_penalties_notice_text" />
				</p>
				<p>
					<fmt:message bundle="${FieldsBundle}"
						key="Paragraph_penalties_notice_text1" />
				</p>

				<details id="penalty-notice">
					<summary id="what-is-penalty-notice">
						<span class="summary"> <fmt:message
								bundle="${FieldsBundle}" key="Link_what_is_penalty_notice" />
						</span>
					</summary>
					<div class="panel panel-border-narrow">
						<p>
							<fmt:message bundle="${FieldsBundle}"
								key="Paragraph_what_is_penalty_notice" />
						</p>

						<p>
							<fmt:message bundle="${FieldsBundle}"
								key="Paragraph_what_is_penalty_notice1" />
							<a href="${moreAboutExemptionsUrl}" id="gov.uk.url"
								target="_blank"><fmt:message bundle="${FieldsBundle}"
									key="Link_find_more_about_penalty_notices" /></a>
						</p>
					</div>
				</details>

				<div class="form-group">
					<nds:field path="uiData.penaltySearch.penaltyPostcodesCriteria"
						labeldecoration="required">
						<nds:label />
						<nds:invalid />
						<nds:input class="form-control form-control-1-8" placeholder="false"
							autofocus="true" />
					</nds:field>
				</div>
                <nds:field path="uiData.penaltySearch.panelDisplayed">
					<nds:input type="hidden" id = "panelDisplayed"/>
				</nds:field>
				<div class="form-group">

					<details id="other-ways"
						${command.uiData.penaltySearch.panelDisplayed == true || command.uiData.penaltySearch.getPenaltyAdvancedSearchDone() ? 'open' : ''}>
						<summary id="other-ways-to-search-penalties">
							<span class="summary"> <fmt:message
									bundle="${FieldsBundle}"
									key="Link_other_ways_to_search_for_penalties" />
							</span>
						</summary>
						<div class="panel panel-border-narrow searchdetail">
							<p>
								<fmt:message bundle="${FieldsBundle}"
									key="Paragraph_combine_search_criteria_for_detailed_search_penalties" />
							</p>

							<div class="form-group">
								<nds:field
									path="uiData.penaltySearch.penaltyLandlordsNameCriteria"
									labeldecoration="required" hints="Hint_search_by_landordname">
									<nds:label />
									<nds:hint />
									<nds:invalid />
									<nds:input class="form-control" placeholder="false" />
								</nds:field>
							</div>

							<div class="form-group">
								<nds:field path="uiData.penaltySearch.rentalPropertyDetails"
									labeldecoration="required" hints="Hint_rental_property_details">
									<nds:label />
									<nds:hint />
									<nds:invalid />
									<nds:input class="form-control" placeholder="false" />
								</nds:field>
							</div>

							<div class="form-group">
								<nds:field path="uiData.penaltySearch.town"
									labeldecoration="required">
									<nds:label />
									<nds:hint />
									<nds:invalid />
									<nds:input class="form-control" placeholder="false" />
								</nds:field>
							</div>

                            <div class="form-group" id="property-drop-down">
								<nds:selectelement
									path="uiData.penaltySearch.propertyType"
									id="select-box"
									items="${command.uiData.refData.getServiceType()}"
									labeldecoration="required" />
							</div>

							<div class="form-group" id="penalty-drop-down-prsd">
								<nds:selectelement path="uiData.penaltySearch.penaltyType_PRSD"
									id="select-box-prsd"
									items="${command.uiData.refData.getPenaltyTypeText('PRSD')}"
									labeldecoration="required" />
							</div>

							<div class="form-group" id="penalty-drop-down-prsn">
								<nds:selectelement path="uiData.penaltySearch.penaltyType_PRSN"
									id="select-box-prsn"
									items="${command.uiData.refData.getPenaltyTypeText('PRSN')}"
									labeldecoration="required" />

							</div>


						</div>
					</details>
				</div>

			</div>
		</div>
		<div class="grid-row">
			<div class="column-full">

				<div class="form-group">
					<section class="submit">
						<button type="submit" name="action" value="FindPenalties"
							id="button.next" class="button next" formnovalidate>
							<fmt:message bundle="${FieldsBundle}"
								key="Button_FindPenaltyNotices" />
						</button>
					</section>
				</div>
				<div class="form-group">
					<c:if test="${command.uiData.getPenaltySearchResult !=null}">
						<div id="local_authority_list">
							<c:choose>
								<c:when
									test="${command.uiData.getPenaltySearchResult.penalties.size() == 0}">
									<p id="customlede">
										<fmt:message bundle="${FieldsBundle}"
											key="Prompt_getPenaltySearchResult.noMatch_found" />
									</p>
								</c:when>
								<c:otherwise>
								   <table class="dgstandard" id="penaltiesList">
										<c:forEach var="penalty"
											items="${command.uiData.getPenaltySearchResult.penalties}">
											<tr>
												<td><c:choose>
														<c:when test="${penalty.address == null}">
															<button class="button-link" role="link" type="submit"
																name="action" id="button.authority.addressNotPublished"
																value="ChosenPenalty:${penalty.penaltyRefNo}"
																formnovalidate>
																<fmt:message bundle="${FieldsBundle}"
																	key="Prompt_getPenaltySearchResult.addressNotPublished" />
															</button>
														</c:when>
														<c:otherwise>
															<button class="button-link" role="link" type="submit"
																name="action" id="button.authority.address"
																value="ChosenPenalty:${penalty.penaltyRefNo}"
																formnovalidate>
																<c:out value="${penalty.address}"></c:out>
															</button>
														</c:otherwise>
													</c:choose></td>
												<td><c:if test="${(not empty penalty.landLordName)}">
														<button class="button-link" role="link" type="submit"
															name="action" id="button.authority.landlordname"
															value="ChosenPenalty:${penalty.penaltyRefNo}">
															<c:out value="${penalty.landLordName}"></c:out>
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
						<a href="${contextUi}/${app.IndexPageUrl}" id="finish" class="button next">
							<fmt:message bundle="${FieldsBundle}" key="Button_FINISH" />
						</a>
					</div>
				</section>
			</div>
		</div>
		</main>

		<jsp:include page="close.jsp"></jsp:include>
		<script
			src="${contextUi}/assets/javascripts/views/register-search-penalties${minify}.js?version=${version}"></script>
	</nds:form>
</body>
</html>