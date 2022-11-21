package com.northgateps.nds.beis.esb.converter;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesNdsRequest;
import com.northgateps.nds.beis.api.getreferencevalues.ReferenceValuesDetails;
import com.northgateps.nds.beis.backoffice.service.getreferencevalues.GetReferenceValues;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converter class to convert a standard NDS object to an external back office object.
 * From GetReferenceValuesNdsRequest to GetReferenceValues.
 */
@Converter
@DoNotWeaveLoggingSystem
public class ToGetReferenceValuesRequestConverter {

    /**
     * Convert the passed in GetReferenceValuesNdsRequest to the passed out GetReferenceValues
     * 
     * @param getReferenceValuesNdsRequest request to be converted to GetReferenceValues
     * @return class GetReferenceValues
     * @throws DatatypeConfigurationException if an error occurs during the conversion
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
