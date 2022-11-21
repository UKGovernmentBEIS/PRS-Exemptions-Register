package com.northgateps.nds.beis.esb.adapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.Exchange;
import org.apache.camel.util.xml.StringSource;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import com.northgateps.nds.beis.api.printexemptiondetails.PrintExemptionDetailsNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxyImpl;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Additional logic required for generating the Exemption Details PDF output.
 * This handles (re)formatting of embedded HTML in the source data.
 */
public class PrintExemptionDetailsAdapter {

    private static final NdsLogger logger = NdsLogger.getLogger(PrintExemptionDetailsAdapter.class);
      
    /**
     * Replace escaped HTML markup in the request message with equivalent XSL:FO markup.
     *
     * Text sourced from the back-office is intended for display on a web page and may contain HTML markup,
     * without this, the mark-up will appear as text in the output.
     *
     * The exact formatting transforms can be found in replace-html-with-fo.xslt.
     *
     * @param exchange - found exchange to be modified
     * @throws NdsApplicationException if error is found
     */
    public void unescapeHtml(Exchange exchange) throws NdsApplicationException, ClassNotFoundException, UnsupportedEncodingException {
    	NdsSoapRequestAdapterExchangeProxyImpl ndsExchange = new NdsSoapRequestAdapterExchangeProxyImpl(exchange);
    	
        try {
            Document body = (Document) ndsExchange.getRequestMessageBody(Document.class);
            NodeList evaluate = findInlineTextNodes(body);

            // Template is based on documentation at http://www.ibm.com/developerworks/xml/library/x-xslfo2app/
            try (InputStream templateIs = getClass().getClassLoader().getResourceAsStream("replace-html-with-fo.xslt")) {
                StreamSource templateSource = new StreamSource(templateIs);
                Transformer transformer = TransformerFactory.newInstance().newTransformer(templateSource);
                transformHtmlToXslFo(body, evaluate, transformer);

                if (logger.isDebugEnabled()) {
                    DOMImplementationLS domImplementation = (DOMImplementationLS) body.getImplementation();
                    LSSerializer lsSerializer = domImplementation.createLSSerializer();
                    logger.debug(lsSerializer.writeToString(body));
                }
            }
        } catch (ClassNotFoundException | XPathExpressionException | TransformerException | SAXException | IOException e) {
            throw new NdsApplicationException("Request failed due to : " + e.getMessage(), e);
        }
        
        // fix unicode characters
        hackPoundSign(exchange);
    }

    private void transformHtmlToXslFo(Document body, NodeList evaluate, Transformer transformer) throws IOException, TransformerException, SAXException {
        // There are no markers to identify text that contains HTML that needs to be re-formatted, so look at all
        for (int i = 0; i < evaluate.getLength(); i++) {
            Node n = evaluate.item(i);
            
            String formattedText = cleanUpNodeText(n);
            
            // We only want to transform elements that actually contain tags
            if (formattedText.matches("(?s).*(<(\\w+)[^>]*>.*</\\2>|<(\\w+)[^>]*//*>).*")) {
                transformHtmlToXslFo(body, transformer, n, formattedText);
            }
            else
            {	
            	// This line need to replace escape character in xml to formatted text when pdf file generated. 
            	// For e.g . if xml text contains &#39; character ,formatText will replace with 
            	// single quotient (') character when pdf file generated.
            	n.setTextContent(formattedText);
            }
        }
    }

    private void transformHtmlToXslFo(Document body, Transformer transformer, Node n, String formattedText)
            throws IOException, TransformerException, SAXException {
        // enforce a single top-level element as required for XML, so that we can transform it.
        formattedText = "<root>" + formattedText + "</root>";
        StringSource pwsTextSource = new StringSource(formattedText);
        
        try (StringWriter writer = new StringWriter()) {
            Result pwsTextResult = new StreamResult(writer);
            transformer.transform(pwsTextSource, pwsTextResult);

            String formattedPwsText = writer.toString();
            replaceNodeContentWithText(body, n, formattedPwsText);

        }
    }

    private String cleanUpNodeText(Node n) {
        String escapedPwsText = n.getTextContent();

        // Unescape HTML tags that are encoded in the source XML, e.g. '&lt;' to '<'
        String unescapedPwsText = StringEscapeUtils.unescapeHtml4(escapedPwsText);

        // Use a relaxed parser to clean up poorly structured HTML, then convert it to XHTML so we can treat it as XML.
        return Jsoup.clean(unescapedPwsText, "", Whitelist.relaxed(),
                new org.jsoup.nodes.Document.OutputSettings().syntax(
                        org.jsoup.nodes.Document.OutputSettings.Syntax.xml).prettyPrint(false));
    }

    /** 
     * No matter what we do there seems to be a conversion to some character encoding other than utf-8
     * when we call <to uri="fop:application/pdf"/>, this causes pound signs to provoke an exception saying 
     * "Invalid byte 1 of 1-byte UTF-8 sequence" ie not utf-8.
     * Since we specifically need pound signs, replace them here (and not in the cleanUpNodeText method, that
     * either fails too with same message or actually prints &#163; depending on where you put the replacement).
     */
    public void hackPoundSign(Exchange exchange) throws ClassNotFoundException, UnsupportedEncodingException {
    	NdsSoapRequestAdapterExchangeProxyImpl ndsExchange = new NdsSoapRequestAdapterExchangeProxyImpl(exchange);
    	String body = (String) ndsExchange.getRequestMessageBody(String.class);
    	
    	String poundString = "&#163;";
    	body = body.replaceAll("£", poundString);
    	body = body.replaceAll(":pound;", poundString);
    	    	
    	logger.debug("Pounds replaced, body is now " + body);
    	ndsExchange.setResponseMessageBody(body);
    }
    
    private void replaceNodeContentWithText(Document body, Node n, String formattedPwsText)
            throws IOException, SAXException {
       
        // Convert the re-formatted text into a document
        try (ByteArrayInputStream bais = new ByteArrayInputStream(formattedPwsText.getBytes(StandardCharsets.UTF_8))) {
            Document pwsTextDoc = getXMLDocumentBuilder().parse(bais);
            // Import the new document into the source XML as a new node.
            Node replacementNode = body.importNode(pwsTextDoc.getDocumentElement(), true);

            // Replace the "fo:inline" element with the reformatted version.
            n.getParentNode().replaceChild(replacementNode, n);
        }
    }
    
    /**
     * Create default XML Document Builder using DocumentBuilderFactory    
     */
    private DocumentBuilder getXMLDocumentBuilder() {
        DocumentBuilder dBuilder = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.error("Unable to create default XML Document builder, generating exemption details PDFs will not work", e);
        }
        return dBuilder;       
    }

    private NodeList findInlineTextNodes(Document body) throws XPathExpressionException {
        XPathFactory xpFactory = XPathFactory.newInstance();
        XPath pwsTextPath = xpFactory.newXPath();
        // Text fields are contained in "fo:inline" elements, so we're interested in the text content of those
        XPathExpression expr = pwsTextPath.compile("//*[local-name()='inline']/text()");
        return (NodeList) expr.evaluate(body.getDocumentElement(), XPathConstants.NODESET);
    }
    
    /**
     * Set up the response details for the produced file, including content type, name and content.
     *
     * @param exchange the Camel message exchange to set the response on.
     * @throws NdsApplicationException if {@link ByteArrayOutputStream} doesn't exist.
     */
    public void processResponse(Exchange exchange) throws NdsApplicationException {
        NdsSoapRequestAdapterExchangeProxyImpl ndsExchange = new NdsSoapRequestAdapterExchangeProxyImpl(exchange);

        PrintExemptionDetailsNdsResponse response = new PrintExemptionDetailsNdsResponse();

        try {
            ByteArrayOutputStream baos = (ByteArrayOutputStream) ndsExchange.getRequestMessageBody(ByteArrayOutputStream.class);

            response.setContentType("application/pdf");
            response.setFileName("exemption-details.pdf");
            response.setSource(baos.toByteArray());
            response.setFileSize(response.getSource().length);
            response.setSuccess(true);
        } catch (ClassNotFoundException e) {
            logger.error("Request failed due to : " + e.getMessage(), e);
            throw new NdsApplicationException("Request failed due to : " + e.getMessage());
        }
        ndsExchange.setResponseMessageBody(response);
    }

}
