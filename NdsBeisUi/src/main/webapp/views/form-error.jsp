<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="page current fault">
	<c:if test="${fault.length() > 0}">
		<div class="error-summary" role="group" tabindex="-1">${fault}</div>
	</c:if>
</div>