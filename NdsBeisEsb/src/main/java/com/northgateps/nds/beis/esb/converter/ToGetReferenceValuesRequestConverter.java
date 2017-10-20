package com.northgateps.nds.beis.esb.converter;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesNdsRequest;
import com.northgateps.nds.beis.api.getreferencevalues.ReferenceValuesDetails;
import com.northgateps.nds.beis.backoffice.service.getreferencevalues.GetReferenceValues;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converts between a NDS object and an external back office object
 *
 * 
 */
@Converter
@DoNotWeaveLoggingSystem
public class ToGetReferenceValuesRequestConverter {

    /**
     * Convert the passed in GetReferenceValuesNdsRequest to the passed out GetReferenceValues
     * 
     * @param class GetReferenceValuesNdsRequest
     * @return GetReferenceValues
     */
    @Converter
    public GetReferenceValues converting(GetReferenceValuesNdsRequest getReferenceValuesNdsRequest)
            throws DatatypeConfigurationException {
        /* Class being converted to */
        ReferenceValuesDetails referenceValuesDetails = getReferenceValuesNdsRequest.getReferenceValuesDetails();
        GetReferenceValues getReferenceValues = new GetReferenceValues();

        if (referenceValuesDetails != null) {
            
            getReferenceValues.setServiceCode(referenceValuesDetails.getServiceCode());
            getReferenceValues.setDomainCode(referenceValuesDetails.getDomainCode());
            getReferenceValues.setWorkplaceCode(referenceValuesDetails.getWorkplaceCode());
            getReferenceValues.setCode(referenceValuesDetails.getCode());
            
        }
        return getReferenceValues;
    }
}
