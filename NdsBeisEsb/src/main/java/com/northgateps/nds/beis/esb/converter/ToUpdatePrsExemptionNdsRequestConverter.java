package com.northgateps.nds.beis.esb.converter;

import java.math.BigInteger;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.updateexemptiondetails.UpdateExemptionDetailsNdsRequest;
import com.northgateps.nds.beis.backoffice.service.updateprsexemption.UpdatePRSExemptionRequest;

import com.northgateps.nds.platform.esb.converter.StringToBigIntegerConverter;
import com.northgateps.nds.platform.esb.converter.ZonedDateTimeToXMLGregorianCalendarConverter;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converter class to convert a standard NDS object to an external back office object.
 * From UpdateExemptionDetailsNdsRequest to UpdatePRSExemptionRequest.
 */
@Converter
@DoNotWeaveLoggingSystem

public final class ToUpdatePrsExemptionNdsRequestConverter {

    private static final NdsLogger logger = NdsLogger.getLogger(ToUpdatePrsExemptionNdsRequestConverter.class);

    /**
     * Convert the passed in updateExemptionDetailsNdsRequest to the passed out UpdatePRSExemptionRequest
     *
     * @param updateExemptionDetailsNdsRequest request to be converted to UpdatePRSExemptionRequest
     * @return class com.northgateps.nds.beis.backoffice.service.updateprsexemption.UpdatePRSExemptionRequest
     *
     * @throws NdsApplicationException if error is found
     * @throws DatatypeConfigurationException if error is found
     */
    @Converter
    public static UpdatePRSExemptionRequest converting(
            UpdateExemptionDetailsNdsRequest updateExemptionDetailsNdsRequest)
            throws NdsApplicationException, DatatypeConfigurationException {

        logger.info("Starting Conversion For RetrievingRegistrationDetails Request");

        UpdatePRSExemptionRequest request = new UpdatePRSExemptionRequest();
        if(updateExemptionDetailsNdsRequest.getUpdateExemptionDetails() !=null){
            request.setExemptionRefNo(
                    StringToBigIntegerConverter.convert(updateExemptionDetailsNdsRequest.getUpdateExemptionDetails().getReferenceId()));        
            request.setEndDate(
                    ZonedDateTimeToXMLGregorianCalendarConverter.convert(updateExemptionDetailsNdsRequest.getUpdateExemptionDetails().getEndDate()));
        }       
        request.setPartyRef(new BigInteger(updateExemptionDetailsNdsRequest.getAccountId()));        

        logger.info("Finished Conversion For RetrievingRegistrationDetails Request");

        return request;
    }

}
