package com.northgateps.nds.beis.esb.converter;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.getprsexemptionrefdata.GetPrsExemptionRefDataNdsRequest;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataRequest;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converts between a NDS object and an external back office object
 *
 * 
 */
@Converter
@DoNotWeaveLoggingSystem
public class ToGetPRSExemptionReferenceDataRequestConverter {

    /**
     * Convert the passed in RegisterPrsExemptionNdsRequest to the passed out RegisterPRSExemptionRequest
     * 
     * @param class RegisterPrsExemptionNdsRequest
     * @return RegisterPRSExemptionRequest
     */
    @Converter
    public GetPRSExemptionReferenceDataRequest converting(
            GetPrsExemptionRefDataNdsRequest getPrsExemptionRefDataNdsRequest) throws DatatypeConfigurationException {
        /* Class being converted to */
        GetPRSExemptionReferenceDataRequest getPRSExemptionReferenceDataRequest = new GetPRSExemptionReferenceDataRequest();
        return getPRSExemptionReferenceDataRequest;
    }
}
