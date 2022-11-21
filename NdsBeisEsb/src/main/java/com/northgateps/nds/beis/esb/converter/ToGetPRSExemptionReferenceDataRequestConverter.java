package com.northgateps.nds.beis.esb.converter;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.getprsexemptionrefdata.GetPrsExemptionRefDataNdsRequest;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataRequest;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converter class to convert a standard NDS object to an external back office object.
 * From GetPrsExemptionRefDataNdsRequest to GetPRSExemptionReferenceDataRequest.
 */
@Converter
@DoNotWeaveLoggingSystem
public class ToGetPRSExemptionReferenceDataRequestConverter {

    /**
     * Convert the passed in RegisterPrsExemptionNdsRequest to the passed out GetPRSExemptionReferenceDataRequest
     * 
     * @param getPrsExemptionRefDataNdsRequest request to be converted to GetPRSExemptionReferenceDataRequest
     * @return class GetPRSExemptionReferenceDataRequest
     * @throws DatatypeConfigurationException if error occurs during conversion
     */
    @Converter
    public GetPRSExemptionReferenceDataRequest converting(
            GetPrsExemptionRefDataNdsRequest getPrsExemptionRefDataNdsRequest) throws DatatypeConfigurationException {
        /* Class being converted to */
        GetPRSExemptionReferenceDataRequest getPRSExemptionReferenceDataRequest = new GetPRSExemptionReferenceDataRequest();
        return getPRSExemptionReferenceDataRequest;
    }
}
