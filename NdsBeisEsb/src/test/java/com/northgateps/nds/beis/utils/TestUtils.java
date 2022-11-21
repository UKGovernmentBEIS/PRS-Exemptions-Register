package com.northgateps.nds.beis.utils;

import com.northgateps.nds.platform.application.api.utils.PlatformTokenFactory;

/**
 * Test utilities for unit tests
 *
 */
public class TestUtils {

    /**
     * This method generates random string
     *
     * @return randomly-generated string
     */
    public static String generateRandomString() {

        return PlatformTokenFactory.generateInternalUniqueReferenceWithPrefix("INTEGRATIONTESTS_");

    }

}
