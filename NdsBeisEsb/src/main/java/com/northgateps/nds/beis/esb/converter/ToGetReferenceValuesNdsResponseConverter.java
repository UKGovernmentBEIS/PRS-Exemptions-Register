package com.northgateps.nds.beis.esb.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesNdsResponse;
import com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesResponseDetails;
import com.northgateps.nds.beis.api.getreferencevalues.ReferenceValuesDetails;
import com.northgateps.nds.beis.backoffice.service.getreferencevalues.GetReferenceValuesResponse;
import com.northgateps.nds.beis.backoffice.service.getreferencevalues.GetReferenceValuesResponse.ReferenceValues.ReferenceValue;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Convert the passed in GetReferenceValuesResponse to the passed out GetReferenceValuesNdsResponse
 * 
 * @param com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesNdsResponse
 * @return class com.northgateps.nds.beis.backoffice.service.getreferencevalues.GetReferenceValuesResponse
 * 
 */
@Converter
@DoNotWeaveLoggingSystem
public class ToGetReferenceValuesNdsResponseConverter {

    private static final NdsLogger logger = NdsLogger.getLogger(ToGetReferenceValuesNdsResponseConverter.class);

    @Converter
    public static GetReferenceValuesNdsResponse converting(GetReferenceValuesResponse getReferenceValuesResponse) {
        logger.info("Conversion of back office response object into NDS response object started.");

        GetReferenceValuesNdsResponse getReferenceValuesNdsResponse = new GetReferenceValuesNdsResponse();
        getReferenceValuesNdsResponse.setSuccess(getReferenceValuesResponse.isSuccess());

        ToNdsMessagesConverter.SetNdsMessages(getReferenceValuesNdsResponse,getReferenceValuesResponse.getMessages());
        if (getReferenceValuesResponse.getReferenceValues() != null) {
            List<ReferenceValue> referenceValueList = getReferenceValuesResponse.getReferenceValues().getReferenceValue();
            if (referenceValueList != null) {
                GetReferenceValuesResponseDetails getReferenceValuesResponseDetails = new GetReferenceValuesResponseDetails();
                List<ReferenceValuesDetails> referenceValuesDetailsList = new ArrayList<ReferenceValuesDetails>();
                for (ReferenceValue referenceValue : referenceValueList) {

                    ReferenceValuesDetails referenceValuesDetails = new ReferenceValuesDetails();
                    referenceValuesDetails.setCode(referenceValue.getCode());
                    referenceValuesDetails.setDomainCode(referenceValue.getDomainCode());
                    referenceValuesDetails.setWorkplaceCode(referenceValue.getWorkplaceCode());
                    referenceValuesDetails.setServiceCode(referenceValue.getServiceCode());
                    //Sequence value is set to 0 if it is null or negative for sorting
                    referenceValuesDetails.setSequence(
                            (referenceValue.getSequence() != null && referenceValue.getSequence().intValue() > 0)
                                    ? referenceValue.getSequence().intValue() : 0); 
                    referenceValuesDetails.setName(referenceValue.getName());
                    referenceValuesDetailsList.add(referenceValuesDetails);
                }
                getReferenceValuesResponseDetails.setReferenceValuesDetails(referenceValuesDetailsList);
                getReferenceValuesNdsResponse.setGetReferenceValuesResponseDetails(getReferenceValuesResponseDetails);

            }
        }
        logger.info("Conversion of back office response object into NDS response object completed.");
        return getReferenceValuesNdsResponse;
    }
}
