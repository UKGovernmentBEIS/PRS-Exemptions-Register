package com.northgateps.nds.beis.esb;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.AbstractApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import com.northgateps.nds.platform.esb.camel.NdsFileSystemXmlApplicationContext;
import com.northgateps.nds.platform.esb.security.MockAuthentication;
import com.northgateps.nds.platform.logger.NdsLogger;

/** Common utilities for route tests */
public class RouteTestUtils {
    
    private static final NdsLogger logger = NdsLogger.getLogger(RouteTestUtils.class);
    
    public static AbstractApplicationContext createApplicationContext(final String... routeInclusionList) {
        

        // grant test role based security access
        MockAuthentication.setMockAuthentication("ROLE_BEIS_UI");
        
        // create context
        return new NdsFileSystemXmlApplicationContext(new String[] {
                "src/main/webapp/WEB-INF/applicationContext-security.xml",
                "src/main/webapp/WEB-INF/beis-camel-context.xml" 
                 }) {

            @Override
            protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader) {
                super.initBeanDefinitionReader(reader);
                
                if (routeInclusionList.length > 0) {
                    Set<String> routeInclusionSet = new HashSet<String>(Arrays.asList(routeInclusionList));
                    reader.setDocumentLoader(new DocumentLoader() {
                        
                        DocumentLoader baseLoader = new DefaultDocumentLoader();
    
                        @Override
                        public Document loadDocument(InputSource inputSource, EntityResolver entityResolver,
                                ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {
                            Document doc = baseLoader.loadDocument(inputSource, entityResolver, errorHandler, validationMode, namespaceAware);
                            NodeList routes = doc.getElementsByTagNameNS("http://camel.apache.org/schema/spring", "route");
                            for (int i = routes.getLength() - 1 ; i >= 0 ; i--) {
                                Element route = (Element) routes.item(i);
                                if (routeInclusionSet.contains(route.getAttribute("id"))) {
                                    logger.debug("route permitted: " + route.getAttribute("id"));  
                                } else {
                                    route.getParentNode().removeChild(route);
                                }
                            }
                            
                            return doc;
                        }
                    });
                }
            }
        };
    }

}
