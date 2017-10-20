<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${applicationPhase != 'LIVE'}">
	<div class="phase-banner-beta">
		<p>
			<strong class="phase-tag">${applicationPhase}</strong> <span>
				<fmt:message bundle="${FieldsBundle}"
					key="FeedBackMessage_${applicationPhase}" /> <c:if
					test="${applicationPhase != 'TEST'}">
					<a id="link.navigation.phase.feedback"
						href="mailto:${feedbackEmail}"> <fmt:message
								bundle="${FieldsBundle}" key="FeedBackMessage_feedback" />
					</a> <fmt:message bundle="${FieldsBundle}" key="FeedBackMessage_END" />
				</c:if>
			</span>
		</p>
	</div>
</c:if>