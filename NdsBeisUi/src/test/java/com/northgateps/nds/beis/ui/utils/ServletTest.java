package com.northgateps.nds.beis.ui.utils;

import org.junit.Test;

import com.northgateps.nds.platform.ui.utils.PlatformServletTest;

/**
 * Check the servlet xml file for programming errors.
 * 
 * @see PlatformServletTest for more details.
 */
public class ServletTest extends PlatformServletTest {

    @Test
    public void test() throws Exception {
        findAndTestServletFiles("src/main/webapp/WEB-INF/web-servlet.xml");
    }

    @Override
    protected boolean inKnownExceptions(String key) {
        if ("FindExemptions".equals(key) || "ChosenExemption:".equals(key) || "FindPenalties".equals(key)
                || "ChosenPenalty:".equals(key) || "SearchGdarGdip".equals(key) || "SelectedExemption:".equals(key)) {
            return true;
        }

        return super.inKnownExceptions(key);
    }

}
