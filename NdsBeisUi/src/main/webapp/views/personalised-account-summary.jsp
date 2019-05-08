<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${language}" />
<!DOCTYPE html>
<html lang="${language}">
<fmt:message bundle="${FieldsBundle}" key="Title_MyAcountDetails"
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
		</header>
		<div class="grid-row">
          <div class="column-full">
				<jsp:include page="form-error.jsp"/>
				<section id="forminfowrap">
					<h2 class="form-title heading-large" role="banner">
						<fmt:message bundle="${FieldsBundle}"
							key="Heading_Title_MyAcountDetails" />
					</h2>
				</section>
			</div>
		</div>
		<div class="grid-row">
			<div class="column-full">
				<section id="formwrap">
					<div class="form-group">
					 <table class="dgstandard">
						
						<c:if test="${command.beisRegistrationDetails.userDetails.userType == 'AGENT'}">
						
						       <tr>
									<td class="dgdata"><fmt:message bundle="${FieldsBundle}"
											key="ColumnLabel_myAccountDetails.beisRegistrationDetails.accountDetails.agentNameDetails.agentName" />
									</td>
									<td class="dgdata">${command.beisRegistrationDetails.accountDetails.agentNameDetails.agentName}</td>
									<td class="link-right">
										<button type="submit" name="action"
											value="NEXT:EditPersonalDetails"
											id="button.changeAgentDetails" role="link">
											<fmt:message bundle="${FieldsBundle}"
												key="Link_ChangeAgentDetails" />
										</button>
									</td>
								</tr>
						</c:if>

							<c:if
								test="${command.beisRegistrationDetails.accountDetails.accountType == 'PERSON'}">
								<tr>
									<td class="dgdata"><fmt:message bundle="${FieldsBundle}"
											key="ColumnLabel_myAccountDetails.beisRegistrationDetails.personNameDetail.firstName" />
									</td>
									<td class="dgdata">${command.beisRegistrationDetails.accountDetails.personNameDetail.firstname}</td>
									<td class="link-right">
										<button type="submit" name="action"
											value="NEXT:EditPersonalDetails"
											id="button.changePersonalDetails" role="link">
											<fmt:message bundle="${FieldsBundle}"
												key="Link_ChangePersonalDetails" />
										</button>
									</td>
								</tr>
								<tr>
									<td class="dgdata"><fmt:message bundle="${FieldsBundle}"
											key="ColumnLabel_myAccountDetails.beisRegistrationDetails.personNameDetail.lastName" />
									</td>
									<td class="dgdata">${command.beisRegistrationDetails.accountDetails.personNameDetail.surname}</td>
									<td />
								</tr>
							</c:if>
							<c:if
								test="${command.beisRegistrationDetails.accountDetails.accountType == 'ORGANISATION'}">
								<tr>
									<td class="dgdata"><fmt:message bundle="${FieldsBundle}"
											key="ColumnLabel_myAccountDetails.beisRegistrationDetails.organisationNameDetail.orgname" />
									</td>
									<td class="dgdata">${command.beisRegistrationDetails.accountDetails.organisationNameDetail.orgName}</td>
									<td class="link-right">

										<button type="submit" name="action"
											value="NEXT:EditPersonalDetails"
											id="button.changeOrganisationDetails" role="link">
											<fmt:message bundle="${FieldsBundle}"
												key="Link_ChangeOrganisationDetails" />
										</button>
									</td>
								</tr>
							</c:if>

							<tr>
								<td class="dgdata"><fmt:message bundle="${FieldsBundle}"
										key="ColumnLabel_myAccountDetails.beisRegistrationDetails.telNumber" />
								</td>
								<td class="dgdata">${command.beisRegistrationDetails.accountDetails.telNumber}</td>
								<td />
							</tr>

							<tr>
								<td class="dgdata top"><fmt:message
										bundle="${FieldsBundle}"
										key="ColumnLabel_myAccountDetails.beisRegistrationDetails.contactAddress" />
								</td>
								
								<td class="dgdata" id="addressLines">
								   <span>${command.beisRegistrationDetails.contactAddress.line[0]}</span>
								   <c:if test="${not empty command.beisRegistrationDetails.contactAddress.line[1]}">
								   <br />${command.beisRegistrationDetails.contactAddress.line[1]}
								   </c:if>
								   <br />
								   <span>${command.beisRegistrationDetails.contactAddress.town}</span>
								   <c:if test="${not empty command.beisRegistrationDetails.contactAddress.county}">
                                   <br>${command.beisRegistrationDetails.contactAddress.county}
								   </c:if>
								   <c:if test="${not empty command.beisRegistrationDetails.contactAddress.postcode}">
                                   <br>${command.beisRegistrationDetails.contactAddress.postcode}
								   </c:if>
								   <br />
								   <span>${command.beisRegistrationDetails.contactAddress.country}</span>
								</td>
								
								<td class="link-right">
									<button type="submit" name="action" value="NEXT:ChangeAddress"
										id="button.changeAddress" role="link">
										<fmt:message bundle="${FieldsBundle}" key="Link_ChangeAddress" />
									</button>
								</td>
							</tr>
							<tr>
								<td class="dgdata"><fmt:message bundle="${FieldsBundle}"
										key="ColumnLabel_myAccountDetails.beisRegistrationDetails.emailAddress" />
								</td>
								<td class="dgdata word-break">${command.beisRegistrationDetails.userDetails.email}</td>
								<td class="link-right">
									<button type="submit" name="action" value="NEXT:ChangeEmail"
										id="button.changeEmailID" role="link">
										<fmt:message bundle="${FieldsBundle}" key="Link_ChangeEmail" />
									</button>
								</td>
							</tr>
						</table>
					</div>
				</section>

				<div class="actions-group form-group">
					<button type="submit" name="action" value="NEXT:ChangePassword"
						id="button.changePassword" role="link">
						<fmt:message bundle="${FieldsBundle}" key="Link_ChangePassword" />
					</button>
					<div>
						<button type="submit" name="action" value="NEXT:DeleteMyAccount"
							id="button.deleteMyAccount" role="link">
							<fmt:message bundle="${FieldsBundle}" key="Link_Delete_my_account" />
						</button>
					</div>
				</div>


				<section class="submit">
					<div class="actions-group">
						<button type="submit" name="action" value="NEXT:Home"
							id="button.next" class="button next">
							<fmt:message bundle="${FieldsBundle}" key="Button_ViewMyExemptions" />
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