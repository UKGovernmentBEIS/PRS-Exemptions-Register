package com.northgateps.nds.beis.esb.converter;

import java.util.List;

import com.northgateps.nds.beis.backoffice.service.core.MessageStructure;
import com.northgateps.nds.beis.backoffice.service.core.MessagesStructure;
import com.northgateps.nds.platform.api.AbstractNdsResponse;
import com.northgateps.nds.platform.api.NdsMessages;

/**
 * 
 * This class is a helper class to convert the messages structure from
 *
 */
public class ToNdsMessagesConverter {

    /**
     * This function will copy a com.northgateps.nds.beis.backoffice.service.core.MessagesStructure
     * to NdsMessages on a response object that inherits from the AbstractNdsResponse class.
     * 
     * @param ndsResponse - A class that inherits AbstractNdsResponse base class
     * @param messagesStructure this must be the CORE messages structure
     */
    public static void SetNdsMessages(AbstractNdsResponse ndsResponse, MessagesStructure messagesStructure) {

        if (messagesStructure != null) {

            List<MessageStructure> messages = messagesStructure.getMessage();

            if (messages != null) {

                for (MessageStructure messageStructure : messages) {

                    NdsMessages ndsMessages = new NdsMessages();

                    ndsMessages.setExceptionCaught(
                            (messageStructure.getText() != null) ? messageStructure.getText() : "");

                    ndsMessages.setErrorNumber((messageStructure.getCode() != null) ? messageStructure.getCode() : "");

                    ndsMessages.setErrorType(
                            (messageStructure.getSeverity() != null) ? messageStructure.getSeverity().toString() : "");

                    ndsResponse.setNdsMessages(ndsMessages);

                }
            }

        }

    }
}
