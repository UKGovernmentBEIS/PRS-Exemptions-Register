package com.northgateps.nds.beis.esb.util;
import java.util.List;
import java.util.regex.Pattern;

import org.owasp.html.ElementPolicy;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import com.google.common.base.Predicate;

/**copyright from the ebay example.
 * // Copyright (c) 2011, Mike Samuel
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
// Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
// Neither the name of the OWASP nor the names of its contributors may
// be used to endorse or promote products derived from this software
// without specific prior written permission.
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
// BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
// ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
 *  
 * This class used to scan html contain recieved from Back office
 * Its uses OWASP_Java_HTML_Sanitizer
 * https://www.owasp.org/index.php/OWASP_Java_HTML_Sanitizer_Project  
 * Currently it uses ebay publish policy as reference,url as below
 * https://github.com/OWASP/java-html-sanitizer/blob/master/src/main/java/org/owasp/html/examples/EbayPolicyExample.java
 *
 */
public class BeisHtmlSanitizer {

 // Some common regular expression definitions.

    private static final Pattern HTML_ID = Pattern.compile(
        "[a-zA-Z0-9\\:\\-_\\.]+");
    // force non-empty with a '+' at the end instead of '*'
    private static final Pattern HTML_TITLE = Pattern.compile(
        "[\\p{L}\\p{N}\\s\\-_',:\\[\\]!\\./\\\\\\(\\)&]*");
    private static final Pattern HTML_CLASS = Pattern.compile(
        "[a-zA-Z0-9\\s,\\-_]+");

    private static final Pattern ONSITE_URL = Pattern.compile(
        "(?:[\\p{L}\\p{N}\\\\\\.\\#@\\$%\\+&;\\-_~,\\?=/!]+|\\#(\\w)+)");
    private static final Pattern OFFSITE_URL = Pattern.compile(
        "\\s*(?:(?:ht|f)tps?://|mailto:)[\\p{L}\\p{N}]"
        + "[\\p{L}\\p{N}\\p{Zs}\\.\\#@\\$%\\+&;:\\-_~,\\?=/!\\(\\)]*+\\s*");

    private static final Predicate<String> ONSITE_OR_OFFSITE_URL
        = matchesEither(ONSITE_URL, OFFSITE_URL);

    /**
     * A policy that can be used to produce policies that sanitize to HTML sinks
     * via {@link PolicyFactory#apply}.
     */
    public static final PolicyFactory POLICY_DEFINITION = new HtmlPolicyBuilder()
            .allowAttributes("id").matching(HTML_ID).globally()
            .allowAttributes("class").matching(HTML_CLASS).globally()
            .allowAttributes("lang").matching(Pattern.compile("[a-zA-Z]{2,20}"))
                .globally()
            .allowAttributes("title").matching(HTML_TITLE).globally()
            .allowAttributes("href").matching(ONSITE_OR_OFFSITE_URL)
                .onElements("a")
            .allowStandardUrlProtocols()
            .allowElements(
                    "a", "label","h1", "h2", "h3", "h4", "h5", "h6",
                    "p", "i", "b", "u", "strong", "em", "small", "big", 
                    "samp", "sub", "sup",
                    "hr", "br", "col", "font", "span", "div", 
                     "ol", "li", "dd", "dt", "dl", "tbody", "thead", "tfoot",
                    "table", "td", "th", "tr", "colgroup", "legend")
            //Applying GDS style for ul tag
            .allowElements(
                    new ElementPolicy() {
                   @Override
                    public String apply(String elementName, List<String> attrs) {
                        attrs.add("class");
                        attrs.add("list list-bullet");
                        return elementName;
                    }
                    }, "ul")
            .toFactory();

  
    private static Predicate<String> matchesEither(final Pattern a, final Pattern b) 
    {
            return new Predicate<String>() {
            @Override
            public boolean apply(String s) {
              return a.matcher(s).matches()|| b.matcher(s).matches();
            }
          };
    }
    
    public static String sanitizeHtml(String untrustedHTML)
    {
        String safeHTML = POLICY_DEFINITION.sanitize(untrustedHTML);
        return safeHTML;
    }
    
}
