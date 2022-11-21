<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:message bundle="${FieldsBundle}" key="File_SizeLimit"
	var="oversizemessage" />
<nds:field path="${param.path}">
	<fieldset class="file" data-maxsize="${param.supportedmaxsize}"
		data-maxsize-invalid-msg="${oversizemessage}">
		<c:if test="${param.showLabel}">
			<nds:label />
		</c:if>
		<span class="form-hint"> <fmt:message bundle="${FieldsBundle}"
				key="Hint_FileUpload">
				<fmt:param value="${param.supportedtypes}" />
				<fmt:param value="${param.supportedmaxsizemb}" />
			</fmt:message>
		</span>

		<nds:invalid />
		<c:if test="${param.showaddbutton=='true'}">
				<table >
					<tr class="js-hidden">
						<th>&nbsp;</th>
						<th class="js-hidden"><fmt:message bundle="${FieldsBundle}"
								key="UploadColumn_description" /></th>
						<th class="width-fixed">&nbsp;</th>
						<th>&nbsp;</th>
					</tr>
					<tr>
						<td class="hideline" ><input type="file"
							name="${param.uipath}" id="${param.path}" class="resource" /></td>
						<td class="hideline js-hidden" ><nds:input
								path="${param.uidescription}" class="form-control js-hidden" /></td>
						<td class="hideline js-hidden width-fixed">
							<button type="submit" name="action" value="${param.actionvalue}"
								id="button.uploadresource" class="button js-hidden" field-path="${param.path}">
								<fmt:message bundle="${FieldsBundle}" key="Button_addFile" />
							</button> 
						</td>
						<td class="hideline">
						<span class="uploadspinner"></span>
						</td>
					</tr>

				</table>
			
		</c:if>

	</fieldset>
</nds:field>