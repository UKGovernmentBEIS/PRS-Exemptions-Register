<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${command.language}" />

<!DOCTYPE html>
<html lang="${command.language}">
<c:choose>
	<c:when
		test="${command.beisRegistrationDetails.userDetails.userType == 'AGENT'}">
		<fmt:message bundle="${FieldsBundle}"
			key="Title_ChangeAccountDetails_${command.beisRegistrationDetails.userDetails.userType}"
			var="title" />
	</c:when>
	<c:otherwise>
		<fmt:message bundle="${FieldsBundle}"
			key="Title_ChangeAccountDetails_${command.beisRegistrationDetails.accountDetails.accountType}"
			var="title" />
	</c:otherwise>
</c:choose>
<jsp:include page="head.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>
<body>
	<nds:form id="pageForm" method="post">
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
				<jsp:include page="form-error.jsp" />
				<section id="forminfowrap">
					<h1 class="form-title heading-large">
						<c:choose>
							<c:when
								test="${command.beisRegistrationDetails.userDetails.userType == 'AGENT'}">
								<fmt:message bundle="${FieldsBundle}"
									key="Heading_ChangeAccountDetails_${command.beisRegistrationDetails.userDetails.userType}" />
							</c:when>
							<c:otherwise>
								<fmt:message bundle="${FieldsBundle}"
									key="Heading_ChangeAccountDetails_${command.beisRegistrationDetails.accountDetails.accountType}" />
							</c:otherwise>
						</c:choose>
					</h1>
				</section>
			</div>
		</div>
		<div class="grid-row">
			<div class="column-full">
				<section id="formwrap">
					<c:choose>
						<c:when
							test="${command.beisRegistrationDetails.userDetails.userType == 'AGENT'}">
							<div class="form-group">
								<nds:field
									path="beisRegistrationDetails.accountDetails.agentNameDetails.agentName"
									labeldecoration="required">
									<nds:label></nds:label>
									<nds:invalid />
									<nds:input class="form-control" placeholder="false" />
								</nds:field>
							</div>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when
									test="${command.beisRegistrationDetails.accountDetails.accountType == 'PERSON'}">
									<div class="form-group">
										<nds:field
											path="beisRegistrationDetails.accountDetails.personNameDetail.firstname"
											labeldecoration="required">
											<nds:label></nds:label>
											<nds:invalid />
											<nds:input class="form-control" placeholder="false" />
										</nds:field>
									</div>
									<div class="form-group">
										<nds:field
											path="beisRegistrationDetails.accountDetails.personNameDetail.surname"
											labeldecoration="required">
											<nds:label />
											<nds:invalid />
											<nds:input class="form-control" placeholder="false" />
										</nds:field>
									</div>
								</c:when>
								<c:otherwise>
									<div class="form-group">
										<nds:field
											path="beisRegistrationDetails.accountDetails.organisationNameDetail.orgName"
											labeldecoration="none">
											<nds:label />
											<nds:invalid />
											<nds:input class="form-control" placeholder="false" />
										</nds:field>
									</div>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
					<div class="form-group">
						<nds:field path="beisRegistrationDetails.accountDetails.telNumber">
							<nds:label />
							<nds:invalid />
							<nds:input class="form-control" placeholder="false" />
						</nds:field>
					</div>
					<div class="form-group">
						<section class="submit">
							<div class="form-group">
								<c:choose>
									<c:when
										test="${command.beisRegistrationDetails.accountDetails.accountType == 'PERSON'}">
										<button type="submit" name="action" value="NEXT"
											id="button.next.person" class="button next" formnovalidate>
											<fmt:message bundle="${FieldsBundle}"
												key="Button_Save_Changes" />
										</button>
									</c:when>
									<c:otherwise>
										<button type="submit" name="action" value="NEXT"
											id="button.next.org" class="button next" formnovalidate>
											<fmt:message bundle="${FieldsBundle}" key="Button_Org" />
										</button>
									</c:otherwise>
								</c:choose>
							</div>
						</section>
					</div>
				</section>
			</div>
		</div>
		</main>

		<jsp:include page="close.jsp"></jsp:include>
	</nds:form>
</body>
</html>
