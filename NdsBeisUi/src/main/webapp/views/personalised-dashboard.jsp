<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>


<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_Dashboard" var="title" />
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>

<body>
	<nds:form id="pageForm" method="post" action="index.htm">
		<jsp:include page="preliminaries.jsp" >
			<jsp:param name="moreops" value="true" />
		</jsp:include>		
		<form:input type="hidden" path="ndsViewState" />
		<form:input type="hidden" path="navigationalState" />
		<form:input type="hidden" path="language" />
		<main id="content" role="main"> <jsp:include
			page="phase-banner.jsp" />
		<div class="grid-row">
			<div class="column-full">
				<jsp:include page="form-error.jsp"/>
				<section id="forminfowrap">
					<h2 class="form-title heading-large" role="banner">
						<fmt:message bundle="${FieldsBundle}" key="Heading_Dashboard" />
					</h2>
				</section>
				<p>
					<fmt:message bundle="${FieldsBundle}"
						key="Paragraph_DashboardSummary" />
				</p>
				<div class="panel panel-border-wide">
				  <p>
				    <fmt:message bundle="${FieldsBundle}"
						key="Paragraph_DashboardHint" />
				  </p>
				</div>
			</div>
		</div>
		<div class="grid-row">
			<div class="column-full">
				<section class="dashpanel cols2">
					<h2 class="heading-2">
						<fmt:message
								bundle="${FieldsBundle}" key="Link_CurrentExemptions" />
					</h2>
					<p id="current-exemption-count"
						class="single-stat-headline impact-number">${command.dashboardDetails.currentExemptionCount}</p>
				</section>
				<section class="dashpanel cols2">
					<h2 class="heading-2">
						<fmt:message
								bundle="${FieldsBundle}" key="Link_ExpiredExemptions" />
					</h2>
					<p id="expired-exemption-count"
						class="single-stat-headline impact-number">${command.dashboardDetails.expiredExemptionCount}</p>
				</section>


				<section>
					<details id="exemption-details" open>
						<summary id="exemption-details-link">
							<span class="summary"> <fmt:message
									bundle="${FieldsBundle}" key="Link_DashboardViewExemptions" />
							</span>
						</summary>
						<div class="panel panel-border-narrow">
							<section id="exemptions-content">
								<div id="exemption-tabs" class="js-tabs nav-tabs">
									<ul class="tabs-nav" role="tablist">
										<li class="active"><a id="tab-current-exemptions"
											href="#current-exemptions" role="tab" tabindex="0"><fmt:message
													bundle="${FieldsBundle}" key="Link_CurrentExemptions" /></a></li>
										<li><a id="tab-expired-exemptions"
											href="#expired-exemptions" role="tab" tabindex="0"><fmt:message
													bundle="${FieldsBundle}" key="Link_ExpiredExemptions" /></a></li>
									</ul>
								</div>
								<div
									class="js-tab-content tab-content tabs-body application-notice info-notice"
									role="note">
									<div id="current-exemptions"
										class="js-tabs-pane tab-pane tabs-pane tabs-panel-selected"
										role="tabpanel">
										<c:if
											test="${command.dashboardDetails.currentExemptionCount == 0}">
											<p>
												<fmt:message bundle="${FieldsBundle}"
													key="Paragraph_NoCurrentExemptions" />
											</p>
										</c:if>
										<c:forEach
											items="${command.dashboardDetails.currentExemptions}"
											varStatus="res">
											
												<table class="exemption-table dgstandard">
													<thead>
														<tr>
															<th colspan="3">${command.dashboardDetails.currentExemptions[res.index].address}
															</th>
														</tr>
													</thead>
													<tr>
														<td class="dgdata"><fmt:message
																bundle="${FieldsBundle}" key="RowLabel_StartDate" /></td>
														<td class="dgdata" colspan="2"><time
																datetime="${command.dashboardDetails.currentExemptions[res.index].startDate}">
																<spring:eval
																	expression="T(com.northgateps.nds.beis.ui.util.NdsDateTimeUtils).getDatePrintFormatter().format(command.dashboardDetails.currentExemptions[res.index].startDate)" />
															</time></td>
													</tr>
													<tr>
														<td class="dgdata"><fmt:message
																bundle="${FieldsBundle}" key="RowLabel_EndingDate" /></td>
														<td class="dgdata"><time
																datetime="${command.dashboardDetails.currentExemptions[res.index].endDate}">
																<spring:eval
																	expression="T(com.northgateps.nds.beis.ui.util.NdsDateTimeUtils).getDatePrintFormatter().format(command.dashboardDetails.currentExemptions[res.index].endDate)" />
															</time>															
														</td>
														<td class="right no-wrap">
															<button type="submit" name="action"
																value="ChosenExemption:${command.dashboardDetails.currentExemptions[res.index].referenceId}"
																id="button.endExemption" role="link" >														
																	<fmt:message bundle="${FieldsBundle}"
																		key="Link_EndExemption" />
															</button>
														</td>
													</tr>
													<tr>
														<td class="dgdata"><fmt:message
																bundle="${FieldsBundle}" key="RowLabel_Type" /></td>
														<td class="dgdata" colspan="2">${command.dashboardDetails.currentExemptions[res.index].description}</td>
													</tr>
												</table>
												
											
										</c:forEach>
									</div>
									<div id="expired-exemptions"
										class="js-tabs-pane tab-pane tabs-pane" role="tabpanel"
										style="display: none">
										<c:if
											test="${command.dashboardDetails.expiredExemptionCount == 0}">
											<p>
												<fmt:message bundle="${FieldsBundle}"
													key="Paragraph_NoExpiredExemptions" />
											</p>
										</c:if>
										<c:forEach
											items="${command.dashboardDetails.expiredExemptions}"
											varStatus="res">
											<table class="exemption-table dgstandard">
												<thead>
													<tr>
														<th class="dgdata" colspan="2">
															${command.dashboardDetails.expiredExemptions[res.index].address}
														</th>
													</tr>
												</thead>
												<tr>
													<td class="dgdata"><fmt:message
															bundle="${FieldsBundle}" key="RowLabel_StartDate" /></td>
													<td class="dgdata"><time
															datetime="${command.dashboardDetails.expiredExemptions[res.index].startDate}">
															<spring:eval
																expression="T(com.northgateps.nds.beis.ui.util.NdsDateTimeUtils).getDatePrintFormatter().format(command.dashboardDetails.expiredExemptions[res.index].startDate)" />
														</time></td>
												</tr>
												<tr>
													<td class="dgdata"><fmt:message
															bundle="${FieldsBundle}" key="RowLabel_EndDate" /></td>
													<td class="dgdata"><time
															datetime="${command.dashboardDetails.expiredExemptions[res.index].endDate}">
															<spring:eval
																expression="T(com.northgateps.nds.beis.ui.util.NdsDateTimeUtils).getDatePrintFormatter().format(command.dashboardDetails.expiredExemptions[res.index].endDate)" />
														</time></td>
												</tr>
												<tr>
													<td class="dgdata"><p>
															<fmt:message bundle="${FieldsBundle}" key="RowLabel_Type" />
														</p></td>
													<td class="dgdata"><p>${command.dashboardDetails.expiredExemptions[res.index].description}</p></td>
												</tr>
											</table>
											<p></p>
										</c:forEach>
									</div>

								</div>
							</section>
						</div>
					</details>
				</section>
			</div>
		</div>
		<div class="grid-row divider">
			<div class="column-full">
				<section class="page current">
					<div class="content-block">
						<section class="submit">						
							<div id="get-started" class="get-started group">
								<button type="submit" name="action" value="NEXT" id="button.next"
									class="button next">
									<fmt:message bundle="${FieldsBundle}" key="Button_Register" />
								</button>
							</div>	
						</section>
						<section class="more">
							<div>
								<p>
								<a href="${moreAboutExemptionsUrl}" id="exemptions-more" target="_blank">
											<fmt:message bundle="${FieldsBundle}" key="Link_MoreExemptions" /></a>
								</p>
							</div>
						</section>
					</div>
				</section>
			</div>
		</div>
		</main>
		<jsp:include page="close.jsp"></jsp:include>
		<script
			src="${contextUi}/assets/javascripts/views/dashboard${minify}.js?version=${version}"></script>
	</nds:form>
</body>
</html>
