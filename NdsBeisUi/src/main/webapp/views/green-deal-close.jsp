
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<footer role="contentinfo" id="footer" class="group js-footer">
  <div class="footer-wrapper">
    <div class="footer-meta">
      <div class="footer-meta-inner">
        <fmt:message bundle="${FieldsBundle}" key="Link_Footer_NewWindowWarning" var="newWindowWarningText" />
        <h2 class="visuallyhidden">Support links</h2>
        <ul>
          <!-- Show version number -->
          <c:if test="${not empty version}">
            <li>${version}</li>
          </c:if>
          <li>
            <a href="gdar-gdip-cookies" id="link.navigation.cookies" target="_blank" class="with-tooltip">
              Cookies <span class="tooltip-text" role="tooltip">${newWindowWarningText}</span>
            </a>
          </li>
          <li>
          	<a id="link.navigation.phase.feedback" href="mailto:${greenDealFeedbackEmail}" class="with-tooltip">
              Feedback <span class="tooltip-text" role="tooltip">${newWindowWarningText}</span>
            </a>
          </li>
          <li>
            <a href="${contextUi}/gdar-gdip-accessibility" id="link.navigation.accessibility" target="_blank" class="with-tooltip">
              Accessibility <span class="tooltip-text" role="tooltip">${newWindowWarningText}</span>
            </a>
          </li>
          <li>
            Built by the 
            <a href="https://www.gov.uk/government/organisations/department-for-business-energy-and-industrial-strategy" id="link.gds" target="_blank" class="with-tooltip">
              Department of Business, Energy and Industrial Strategy <span class="tooltip-text" role="tooltip">${newWindowWarningText}</span>
            </a>
          </li>
        </ul>
        
        <div class="open-government-licence">
          <p class="logo">
            <a rel="license" href="https://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/" id="link.logo.open.government.license" target="_blank" class="with-tooltip">
              Open Government Licence <span class="tooltip-text" role="tooltip">${newWindowWarningText}</span>
            </a>
          </p>
          <p>
            All content is available under the 
            <a rel="license" href="https://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/" id="link.open.government.license" target="_blank" class="with-tooltip">
              Open Government Licence v3.0 <span class="tooltip-text" role="tooltip">${newWindowWarningText}</span>
            </a>
            , except where otherwise stated
          </p>
        </div>
        <div class="version">
          
	       <!-- Show version number -->
	       <c:if test="${not empty version}">
	       		${version}
	       </c:if>
	      
        </div>
      </div>
      
    </div>
  </div>
</footer>

<c:choose>
    <c:when test="${not empty minify}">
        <script src="${contextUi}/assets/javascripts/platform-close-pkg${minify}.js?version=${version}" async="true"></script>
    </c:when>
    <c:otherwise>
        <script src="${contextUi}/assets/javascripts/metadata.platform.js?version=${version}"></script>  
       	<script src="${contextUi}/assets/javascripts/metadata.beis${minify}.js?version=${version}"></script>
        <script src="${contextUi}/assets/javascripts/validation.platform.js?version=${version}"></script>
        <script src="${contextUi}/assets/javascripts/spin.platform.js?version=${version}" async="true"></script>        
        <script src="${contextUi}/assets/javascripts/complex-dropdowns.platform.js?version=${version}" async="true"></script>
        <script src="${contextUi}/assets/javascripts/simple-dropdowns.platform.js?version=${version}" async="true"></script>
        <script src="${contextUi}/assets/javascripts/advice-disclosure.platform.js?version=${version}" async="true"></script>
        <script src="${contextUi}/assets/javascripts/geographic-address.platform.js?version=${version}" async="true"></script>
        <script src="${contextUi}/assets/javascripts/textarea-counter.platform.js?version=${version}" async="true"></script>
        <script src="${contextUi}/assets/javascripts/numberinput.platform.js?version=${version}" async="true"></script>
        <script src="${contextUi}/assets/javascripts/pagination.platform.js?version=${version}" async="true"></script>
        <script src="${contextUi}/assets/javascripts/group-disclosure.platform.js?version=${version}" async="true"></script>
        <script src="${contextUi}/assets/javascripts/govuk-template.platform.js?version=${version}" async="true"></script>
        <script src="${contextUi}/assets/javascripts/govuk-stageprompt.platform.js?version=${version}" async="true"></script>
        <script src="${contextUi}/assets/javascripts/js-loaded.platform.js?version=${version}" async="true"></script>                           
   </c:otherwise>   
</c:choose>

<script src="${contextUi}/assets/javascripts/nds-google-analytics.platform${minify}.js?version=${version}" async="true" data-ga-trackId="${gaTrackId}" id="gasrc"></script>
<!-- govuk_frontend_toolkit js -->
<script src="${contextUi}/assets/javascripts/vendor/polyfills/bind${minify}.js?version=${version}"></script>
<script src="${contextUi}/assets/javascripts/govuk/selection-buttons${minify}.js?version=${version}"></script>
<script src="${contextUi}/assets/javascripts/govuk/shim-links-with-button-role${minify}.js?version=${version}"></script>
<script src="${contextUi}/assets/javascripts/govuk/show-hide-content${minify}.js?version=${version}"></script>

<!-- govuk_elements js -->
<script src="${contextUi}/assets/javascripts/govuk/details.polyfill${minify}.js?version=${version}"></script>
<script src="${contextUi}/assets/javascripts/application${minify}.js?version=${version}"></script>

