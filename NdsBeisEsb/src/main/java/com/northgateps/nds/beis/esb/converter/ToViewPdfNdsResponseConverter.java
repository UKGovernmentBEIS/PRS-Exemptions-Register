package com.northgateps.nds.beis.esb.converter;

import java.util.List;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.DocumentDetails;
import com.northgateps.nds.beis.api.viewdocument.ViewPdfNdsResponse;
import com.northgateps.nds.beis.backoffice.service.core.MessageStructure;
import com.northgateps.nds.beis.backoffice.service.core.MessagesStructure;
import com.northgateps.nds.beis.backoffice.service.viewdocumentgdipgdar.ViewPdfResponse;
import com.northgateps.nds.beis.backoffice.service.viewdocumentgdipgdar.ViewPdfResponse.Document;
import com.northgateps.nds.platform.api.NdsMessages;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converter class to convert an external back office object to a standard NDS object.
 * From ViewPdfResponse to ViewPdfNdsResponse.
 */
@Converter
@DoNotWeaveLoggingSystem
public class ToViewPdfNdsResponseConverter {

    private static final NdsLogger logger = NdsLogger.getLogger(ToViewPdfNdsResponseConverter.class);

    @Converter
    public static ViewPdfNdsResponse converting(ViewPdfResponse viewPdfResponse) {

        logger.info("Starting Conversion For ViewPdfResponse");

        ViewPdfNdsResponse viewPdfNdsResponse = new ViewPdfNdsResponse();

        /* Class being converted to */
        DocumentDetails documentDetails = new DocumentDetails();

        if (viewPdfResponse != null) {
            if (viewPdfResponse.isSuccess()) {
                viewPdfNdsResponse.setSuccess(true);
                if (viewPdfResponse.getDocument() != null) {
                    Document document = viewPdfResponse.getDocument();
                    documentDetails.setDocumentCode(document.getDocumentCode());
                    documentDetails.setDocumentDescription(document.getDocumentDescription());
                    documentDetails.setDocumentName(document.getDocumentName());
                    documentDetails.setDocumentFileType(document.getDocumentFileType());
                    documentDetails.setDocumentReference(document.getDocumentReference());
                    if (viewPdfResponse.getDocument().getBinaryData() != null) {
                        documentDetails.setBinaryData(document.getBinaryData());
                    }
                    viewPdfNdsResponse.setDocument(documentDetails);
                }
            } else {

                Boolean success = null;

                MessagesStructure messagesStructure = viewPdfResponse.getMessages();
                if (messagesStructure != null) {
                    List<MessageStructure> messages = messagesStructure.getMessage();
                    if (messages != null) {

                        for (MessageStructure messageStructure : messages) {

                            if ("BEI-30255".equals(messageStructure.getCode())) {

                                // superseded
                                viewPdfNdsResponse.setSuperseded(true);

                                if (success == null) {
                                    success = true;
                                }
                            } else if ("BEI-30245".equals(messageStructure.getCode())) {

                                // no results found
                                if (success == null) {
                                    success = true;
                                }
                            } else {
                                // genuine error
                                NdsMessages ndsMessages = new NdsMessages();
                                ndsMessages.setExceptionCaught(
                                        (messageStructure.getText() != null) ? messageStructure.getText() : "");
                                ndsMessages.setErrorNumber(
                                        (messageStructure.getCode() != null) ? messageStructure.getCode() : "");
                                ndsMessages.setErrorType((messageStructure.getSeverity() != null)
                                        ? messageStructure.getSeverity().toString() : "");
                                viewPdfNdsResponse.setNdsMessages(ndsMessages);

                                success = false;
                            }

                        }
                    }

                }

                viewPdfNdsResponse.setSuccess(success);
            }
        }
        logger.info("Finished Conversion For ViewPdfResponse");
        return viewPdfNdsResponse;
    }
}
