<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="nds" uri="http://www.northgateps.com/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<fmt:setLocale value="${command.language}" />
<!DOCTYPE html>
<html lang="${command.language}">
<fmt:message bundle="${FieldsBundle}" key="Title_Accessibility" var="title" />
<jsp:include page="head.jsp">
  <jsp:param name="title" value="${title}" />
  <jsp:param name="isTitleGdarGdipSuffix" value="true" />
</jsp:include>
<body>
  <nds:form id="pageForm" method="post">
    <jsp:include page="green-deal-preliminaries.jsp"></jsp:include>
    <form:input type="hidden" path="ndsViewState" />
    <form:input type="hidden" path="navigationalState" />
    <form:input type="hidden" path="language" />
    <main id="content" role="main">
      <jsp:include page="phase-banner.jsp" />
      <div class="grid-row">
        <div class="column-full">
          <jsp:include page="form-error.jsp" />
          <h1 class="form-title heading-large">
            <fmt:message bundle="${FieldsBundle}" key="Heading_GdarGdipAccessibility" />
          </h1>
        </div>
      </div>
      <div class="grid-row">
        <div class="column-two-thirds">
          <div class="form-group">
<p class="body-text">This website is run by <a
  href="https://www.gov.uk/government/organisations/department-for-energy-security-and-net-zero"
  id="link.dbeis" target="_blank">Department for Energy Security and Net Zero - GOV.UK (www.gov.uk)
  <fmt:message bundle="${FieldsBundle}" key="Link_NewWindowWarning" /></a> (DESNZ).</p>
<p class="body-text">We want as many people as possible to be able to use this website. For example, that means you
  should be able to:</p>
<ul class="list list-bullet">
  <li>change colours, contrast levels and fonts</li>
  <li>zoom in up to 300% without the text spilling off the screen</li>
  <li>navigate most of the website using just a keyboard</li>
  <li>navigate most of the website using speech recognition software</li>
  <li>listen to most of the website using a screen reader</li>
</ul>
<p class="body-text">We’ve also made the website text as simple as possible to understand.</p>
<p class="body-text"><a href="https://mcmw.abilitynet.org.uk/" id="link.abilitynet" target="_blank">AbilityNet
    <fmt:message bundle="${FieldsBundle}" key="Link_NewWindowWarning" /></a> has advice on making your device easier to
  use if you have a disability.</p>

<h2 class="heading-medium">Technical information about this website’s accessibility</h2>
<p class="body-text">DESNZ is committed to making its website accessible, in accordance with the Public Sector Bodies
  (Websites and Mobile Applications) (No. 2) Accessibility Regulations 2018.</p>
<p class="body-text">This website is fully compliant with the <a href="https://www.w3.org/TR/WCAG21/" id="link.wcag"
    target="_blank">Web Content Accessibility Guidelines version 2.1
    <fmt:message bundle="${FieldsBundle}" key="Link_NewWindowWarning" /></a> AA standard.</p>
<p class="body-text">This statement was prepared in October 2022. It was last updated in October 2022.</p>

<h2 class="heading-medium">Reporting accessibility problems with this website</h2>
<p class="body-text">We’re always looking to improve the accessibility of this website. If you find any problems not
  listed on this page or think we’re not meeting accessibility requirements, contact: </p>
<ul class="list list-bullet">
  <li><a href="mailto:GDRegister@beis.gov.uk" id="link.mailto.gdr1" target="_blank">GDRegister@beis.gov.uk</a></li>
</ul>

<h2 class="heading-medium">Enforcement procedure</h2>
<p class="body-text">The Equality and Human Rights Commission (EHRC) is responsible for enforcing the Public Sector
  Bodies (Websites and Mobile Applications) (No. 2) Accessibility Regulations 2018 (the ‘accessibility regulations’). If
  you’re not happy with how we respond to your complaint, <a href="https://www.equalityadvisoryservice.com/" id="link.equalityadvisory"
    target="_blank">contact the Equality Advisory and Support Service (EASS)
    <fmt:message bundle="${FieldsBundle}" key="Link_NewWindowWarning" /></a>.</p>

<h2 class="heading-medium">How we tested this website</h2>
<p class="body-text">This website was last tested in October 2023. The test was carried out by accessibility
  specialists in the NEC testing team.</p>
<p class="body-text">The team tested the most used customer journeys through the website and a sample of the different
  types of page.</p>

<h2 class="heading-medium">What to do if you cannot access parts of this website</h2>
<p class="body-text">If you need information on this website in a different format like accessible PDF, large print,
  easy read, audio recording or braille:</p>
<ul class="list list-bullet">
  <li>email <a href="mailto:GDRegister@beis.gov.uk" id="link.mailto.gdr2" target="_blank">GDRegister@beis.gov.uk</a></li>
  <li>call the digital helpline on 0800 098 7950</li>
</ul>

<p class="body-text">We’ll consider your request and get back to you in 10 days.</p>
          </div>
        </div>
      </div>
    </main>
    <jsp:include page="green-deal-close.jsp"></jsp:include>
  </nds:form>
</body>

</html>