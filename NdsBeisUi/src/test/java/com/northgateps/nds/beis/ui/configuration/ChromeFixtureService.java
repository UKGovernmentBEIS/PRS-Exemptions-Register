package com.northgateps.nds.beis.ui.configuration;

import java.util.TreeMap;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import net.thucydides.core.fixtureservices.FixtureException;
import net.thucydides.core.fixtureservices.FixtureService;

/**
 * This rather awkward class injects the ChromeOption experimental option "useAutomationExtension=false" into the 
 * configuration of the Serenity managed WebDriver for Chrome, to stop Chrome from blocking on a dialog box saying:
 * 
 * Failed to load extension from {filename}. Loading of unpacked extensions is disabled by the administrator.
 * 
 * The extension allows a few features like window resizing but they are not needed for our tests.
 * 
 * This class is wired into Serenity via a file called "META-INF/services/net.thucydides.core.fixtureservices.FixtureService"
 * on the classpath. Its source is therefore in the src/test/resources folder.
 */
public class ChromeFixtureService implements FixtureService {

    @Override
    public void setup() throws FixtureException {
    }

    @Override
    public void shutdown() throws FixtureException {
    }

    @Override
    public void addCapabilitiesTo(DesiredCapabilities capabilities) {
        Object optionsCap = capabilities.getCapability(ChromeOptions.CAPABILITY);
        @SuppressWarnings("unchecked")
        TreeMap<String, Object> options = (TreeMap<String, Object>)optionsCap;
        options.put("useAutomationExtension", false);
    }

}
