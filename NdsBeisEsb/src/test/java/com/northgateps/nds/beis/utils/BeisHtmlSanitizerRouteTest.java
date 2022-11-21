package com.northgateps.nds.beis.utils;

import static org.junit.Assert.assertEquals;

import javax.script.ScriptException;

import org.junit.Test;

import com.northgateps.nds.beis.esb.util.BeisHtmlSanitizer;
/**
 * 
 * Test to verify BeisHtmlSanitizer class
 *
 */
public class BeisHtmlSanitizerRouteTest {
    
    /**
     * Test some happy cases when white list html tag is provided as input html. 
     *
     * @throws ScriptException if an error occurs
     */
    @Test
    public void testListIsDecoratedWithClassAttribute() throws ScriptException {
        String untrustedHTML="<ul><li>Test1</li><li>Test2</li></ul>";
        String expectHTML="<ul class=\"list list-bullet\"><li>Test1</li><li>Test2</li></ul>";
        String safeHTML = BeisHtmlSanitizer.sanitizeHtml(untrustedHTML);
        assertEquals("Checking if output decorated with gds class attribute for trusted html input", safeHTML, expectHTML);
    }

    /**
     * Test some failure cases when untrusted html tag is provided as input html.
     *
     * @throws ScriptException if an error occurs
     */
    @Test
    public void testUntrustedHtmlIsStrippedOut() throws ScriptException {
        String untrustedHTML="<times><time>Test1</time><times>";
        String expectHTML="Test1";
        String safeHTML = BeisHtmlSanitizer.sanitizeHtml(untrustedHTML);
        assertEquals("Checking output for untrusted html input", safeHTML, expectHTML);
    }

    
}
