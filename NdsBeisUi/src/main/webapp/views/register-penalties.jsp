<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_Register_Penalties"
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
					<h1 class="form-title heading-large" role="banner">
						<fmt:message bundle="${FieldsBundle}"
							key="Heading_Register_Penalties" />
					</h1>
				</div>
			</div>
			<div class="grid-row">
				<div class="column-full">	

					<section id="formwrap">
						<div class="form-group">
							<table>
								<thead>
									<tr>
									<c:if test="${command.uiData.selectedPenaltyData.address !=null}">
										<th colspan="2">
											<c:out value="${command.uiData.selectedPenaltyData.address}"/>
										</th>
										</c:if>
									</tr>
								</thead>
								<tbody>
									
								<c:if test="${command.uiData.selectedPenaltyData.landLordName!=null}">
									<tr>
										<td><fmt:message bundle="${FieldsBundle}"
												key="ColumnLabel_Organisation" />
										<td>
										<c:out value="${command.uiData.selectedPenaltyData.landLordName}"></c:out>
										</td>		
									</tr>
									</c:if>
									<c:if test="${command.uiData.selectedPenaltyData.pwsDescription !=null}">
									<tr>
										<td><fmt:message bundle="${FieldsBundle}"
												key="ColumnLabel_DetailsOfBreach" />
										<td><c:out value="${command.uiData.selectedPenaltyData.pwsDescription}"/> </td>		
									</tr>
									</c:if>
									<c:if test="${command.uiData.selectedPenaltyData.amount !=null}">
									<tr>
										<td><fmt:message bundle="${FieldsBundle}"
												key="ColumnLabel_AmountOfFinancialPenalty" />
										<td><fmt:message bundle="${FieldsBundle}"
												key="Currency_Pounds" /><c:out value="${command.uiData.selectedPenaltyData.amount}"></c:out> </td>		
									</tr>
									</c:if>
								</tbody>
							</table>
						</div>
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