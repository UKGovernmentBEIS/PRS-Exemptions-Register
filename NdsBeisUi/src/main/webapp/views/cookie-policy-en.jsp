<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%-- This page contains hard coded English text. However, it is included into a container page via
     the resource bundle such that it is only included in the English language rendering of that
     container page. The Welsh (say) rendering of the container page would include a different
     file, with hard coded Welsh text. This avoids the resource bundle having to containing a 
     large number of text fragments to build the page. --%>
     
<p>This DESNZ website puts small files onto your computer.  These are known as cookies. </p>
<p>Cookies are used to:</p>
<ul class="list list-bullet">
    <li>remember settings and information that you've entered so that you don't have to keep entering them again</li>
    <li>measure how you use the website so that we can make sure it meets your needs</li>
</ul>
<p>Cookies are used to make the website work better for you.They aren't used to identify you personally.
To learn more about cookies and how to manage them, visit <a id="aboutcookies" href="http://www.aboutcookies.org/" target="_blank">AboutCookies.org <fmt:message bundle="${FieldsBundle}" key="Link_NewWindowWarning" /></a></p>
<h2 class="heading-medium">How we use cookies</h2>
<h3 class="heading-small">Keeping you signed in</h3>
<table>
    <tr>
        <th>Name</th>
        <th>Purpose</th>
        <th>Expires</th>
    </tr>
    <tr>
        <td>SESSION</td>
        <td>A number that tracks your current connection to keep you signed in</td>
        <td>When you close your browser</td>
    </tr>
    <tr>
        <td>TGC</td>
        <td>A number that tracks your current connection to keep you signed in</td>
        <td>When you close your browser</td>
    </tr>
</table>
<h3 class="heading-small">Measuring website usage (Google Analytics)</h3>
<p>We use Google Analytics software to collect information about how you use this DESNZ website. We do this to make sure the site is meeting the needs of its users and to understand how we could do it better.</p>
<p>Google Analytics stores information about:</p>
<ul class="list list-bullet">
    <li>the pages you visit and how long you spend on each page</li>
    <li>how you got to the site</li>
    <li>what you click on while you are visiting the site</li>
</ul>
<p>We don't collect or store your personal information (for example your name or address) so this information can't be used to identify who you are.</p>
<p>The following cookies are set by Google Analytics:</p>
<table>
    <tr>
        <th>Name</th>
        <th>Purpose</th>
        <th>Expires</th>
    </tr>
    <tr>
        <td>_ga</td>
        <td>A randomly generated number that determines the number of unique visitors to the website</td>
        <td>2 years</td>
    </tr>
    <tr>
        <td>_utma</td>
        <td>Determines the number of unique visitors to the site</td>
        <td>2 years</td>
    </tr>
    <tr>
        <td>_utmb</td>
        <td>This works with _utmc to calculate the average length of time you spend on our site</td>
        <td>30 minutes</td>
    </tr>
    <tr>
        <td>_utmc</td>
        <td>This works with _utmb to calculate when you close your browser</td>
        <td>When you close your browser</td>
    </tr>
    <tr>
        <td>_utmz</td>
        <td>This provides information about how you reached the site (for example, from another website or a search engine)</td>
        <td>6 months</td>
    </tr>
</table>
<p>You can <a id="gaoptout" href="https://tools.google.com/dlpage/gaoptout" target="_blank">opt out of Google Analytics cookies <fmt:message bundle="${FieldsBundle}" key="Link_NewWindowWarning" /></a> on the Google website.</p>
<h3 class="heading-small">Our introductory message</h3>
<p>When you first visit this DESNZ website you may see a pop-up welcome message. We'll store a cookie so that your computer knows you've seen the message and knows not to show it again.</p>
<table>
    <tr>
        <th>Name</th>
        <th>Purpose</th>
        <th>Expires</th>
    </tr>
    <tr>
        <td>seen_cookie_message</td>
        <td>Saves a message to let us know that you have seen our cookie message</td>
        <td>28 days</td>
    </tr>
</table>