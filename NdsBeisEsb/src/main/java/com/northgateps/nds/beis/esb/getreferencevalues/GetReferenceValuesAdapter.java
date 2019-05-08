package com.northgateps.nds.beis.esb.getreferencevalues;

import java.util.ArrayList;

import org.apache.camel.TypeConverter;
import org.apache.commons.lang3.StringUtils;
import com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesNdsRequest;
import com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesNdsResponse;
import com.northgateps.nds.beis.api.getreferencevalues.ReferenceValuesDetails;
import com.northgateps.nds.beis.backoffice.service.getreferencevalues.GetReferenceValues;
import com.northgateps.nds.beis.backoffice.service.getreferencevalues.GetReferenceValuesResponse;
import com.northgateps.nds.platform.esb.adapter.NdsFilterSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxyImpl;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Processes the request to and from the Get Reference Values service
 */
public class GetReferenceValuesAdapter extends NdsFilterSoapAdapter<GetReferenceValuesNdsRequest, GetReferenceValuesNdsResponse, GetReferenceValues, GetReferenceValuesResponse> 
		{

    private static final NdsLogger logger = NdsLogger.getLogger(GetReferenceValuesAdapter.class);

    @Override
    protected GetReferenceValues doRequestProcess(GetReferenceValuesNdsRequest request,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {

        GetReferenceValues getReferenceValues = new GetReferenceValues();

        try {
            ndsExchange.setOperationName("GetReferenceValuesWSDL");
            TypeConverter converter = ndsExchange.getTypeConverter();
            getReferenceValues = converter.convertTo(GetReferenceValues.class, request);
        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during request conversion process:  " + e.getMessage(), e);
        }

        logger.info("Successfully created the SOAP request");
        return getReferenceValues;
    }

    @Override
    protected GetReferenceValuesNdsResponse doResponseProcess(GetReferenceValuesResponse response,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {

        GetReferenceValuesNdsResponse getReferenceValuesNdsResponse = new GetReferenceValuesNdsResponse();

        try {

            TypeConverter converter = ndsExchange.getTypeConverter();
            getReferenceValuesNdsResponse = converter.convertTo(GetReferenceValuesNdsResponse.class, response);

        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during response conversion process:  " + e.getMessage(),
                    e);
        }

        logger.info("Successfully created the NDS response");

        return getReferenceValuesNdsResponse;
    }

    /**
     * Called from the base-class to de-serialize the incoming request.
     * 
     * See this:
     * http://stackoverflow.com/questions/3403909/get-generic-type-of-class
     * -at-runtime
     */
    @Override
    protected String getRequestClassName() {
        return GetReferenceValuesNdsRequest.class.getName();
    }

    @Override
    protected String getResponseClassName() {
        return GetReferenceValuesResponse.class.getName();
    }

    @Override
    protected String getNdsResponseClassName() {
        return GetReferenceValuesNdsResponse.class.getName();
    }

    @Override
    protected GetReferenceValuesNdsRequest doFilterRequestProcess(GetReferenceValuesNdsRequest ndsRequest,
            NdsSoapRequestAdapterExchangeProxyImpl ndsExchange) {
        // request criteria are cleared because we always want all of the records from the WS - criteria will be
        // applied on response instead
        ndsRequest.getReferenceValuesDetails().setDomainCode(null);
        ndsRequest.getReferenceValuesDetails().setCode(null);
        ndsRequest.getReferenceValuesDetails().setServiceCode(null);
        ndsRequest.getReferenceValuesDetails().setWorkplaceCode(null);
        return ndsRequest;
    }

    @Override
    protected GetReferenceValuesNdsResponse doFilterResponseProcess(GetReferenceValuesNdsResponse ndsResponse,
            GetReferenceValuesNdsRequest ndsRequest, NdsSoapRequestAdapterExchangeProxyImpl ndsExchange) {
        ArrayList<ReferenceValuesDetails> referenceValuesDetailsFiltered = new ArrayList<ReferenceValuesDetails>();
        for (ReferenceValuesDetails referenceValuesDetail : ndsResponse.getGetReferenceValuesResponseDetails().getReferenceValuesDetails()) {
            boolean result = matchesRequestFilterCriteria(ndsRequest, referenceValuesDetail);
            if (result) {
                referenceValuesDetailsFiltered.add(referenceValuesDetail);
            }
        }
        ndsResponse.getGetReferenceValuesResponseDetails().setReferenceValuesDetails(referenceValuesDetailsFiltered);
        return ndsResponse;
    }

    /**
     * This method is used to match filter criteria received in request
     * 
     * @param ndsRequest
     * @param referenceValuesDetails
     * @return true/false
     */
    private boolean matchesRequestFilterCriteria(GetReferenceValuesNdsRequest ndsRequest,
            ReferenceValuesDetails referenceValuesDetails) {
       if (StringUtils.isNotEmpty(ndsRequest.getReferenceValuesDetails().getDomainCode())) {
            return ndsRequest.getReferenceValuesDetails().getDomainCode().equals(
                    referenceValuesDetails.getDomainCode());
        }

        if (StringUtils.isNotEmpty(ndsRequest.getReferenceValuesDetails().getServiceCode())) {
            return ndsRequest.getReferenceValuesDetails().getServiceCode().equals(
                    referenceValuesDetails.getServiceCode());
        }

        if (StringUtils.isNotEmpty(ndsRequest.getReferenceValuesDetails().getWorkplaceCode())) {
            return ndsRequest.getReferenceValuesDetails().getWorkplaceCode().equals(
                    referenceValuesDetails.getWorkplaceCode());
        }

        if (StringUtils.isNotEmpty(ndsRequest.getReferenceValuesDetails().getCode())) {
            return ndsRequest.getReferenceValuesDetails().getCode().equals(referenceValuesDetails.getCode());
        }
        
        return true;
    }
    
}
