package com.northgateps.nds.beis.esb.dashboard;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
/**
 * Helper class to be used to process xml
 */
public class DashboardUtils {

    /**
     * Takes xml as string and convert that string to a Document
     * 
     * @param xmlStr xml data stored as string, to be converted
     * @return Document xml document
     * @throws NdsApplicationException if error is found
     */
    public static Document convertStringToDocument(String xmlStr) throws NdsApplicationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            throw new NdsApplicationException(e.getCause());
        }
    }
}
