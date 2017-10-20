<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- 
	User greeting and options when logged in.
 -->

<div class="content">
	<c:if test="${not empty user}">
		<a href="#proposition-links" class="js-header-toggle menu"
			id="togglemenu">Menu</a>
	</c:if>
	<nav id="proposition-menu">
		<span id="proposition-name">${param.title}</span>
		<c:if test="${not empty user}">
			<ul id="proposition-links">
				<c:if test="${param.moreops}">
					<li>
						<button type="submit" name="action" value="NEXT:MyAccountDetails"
							id="button.myAccountDetails" role="link">
							<fmt:message bundle="${FieldsBundle}" key="Link_MyAccountDetails" />
						</button>
					</li>
				</c:if>
				<li>
					<a href="${contextUi}/${logout_url}" id="logout"> <fmt:message
							bundle="${FieldsBundle}" key="MenuLogout" />
					</a>
				</li>
			</ul>
		</c:if>
	</nav>

</div>


