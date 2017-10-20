package com.northgateps.nds.beis.esb.adapter;

import org.apache.camel.TypeConverter;

import com.northgateps.nds.beis.api.PRSExemptionSearchNdsRequest;
import com.northgateps.nds.beis.api.PRSExemptionSearchNdsResponse;
import com.northgateps.nds.beis.backoffice.service.prsexemptionsearch.PRSExemptionSearchRequest;
import com.northgateps.nds.beis.backoffice.service.prsexemptionsearch.PRSExemptionSearchResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Processes the request to and from the get exemption search result
 */
public class PRSExemptionSearchAdapter extends
        NdsSoapAdapter<PRSExemptionSearchNdsRequest, PRSExemptionSearchNdsResponse, PRSExemptionSearchRequest, PRSExemptionSearchResponse> {

    private static final NdsLogger logger = NdsLogger.getLogger(PRSExemptionSearchAdapter.class);

    @Override
    protected String getRequestClassName() {
        return PRSExemptionSearchNdsRequest.class.getName();
    }

    @Override
    protected String getResponseClassName() {
        return PRSExemptionSearchResponse.class.getName();
    }

    @Override
    protected PRSExemptionSearchRequest doRequestProcess(PRSExemptionSearchNdsRequest request,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
        PRSExemptionSearchRequest prsExemptionSearchRequest = new PRSExemptionSearchRequest();

        try {

            ndsExchange.setOperationName("PRSExemptionSearchWSDL");

            TypeConverter converter = ndsExchange.getTypeConverter();
            prsExemptionSearchRequest = converter.convertTo(PRSExemptionSearchRequest.class, request);

        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during request conversion process:  " + e.getMessage(), e);
        }

        logger.info("Successfully created the SOAP request");

        return prsExemptionSearchRequest;
    }

    @Override
    protected PRSExemptionSearchNdsResponse doResponseProcess(PRSExemptionSearchResponse response,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {

        PRSExemptionSearchNdsResponse prsExemptionSearchNdsResponse = new PRSExemptionSearchNdsResponse();

        try {
            
            TypeConverter converter = ndsExchange.getTypeConverter();
            prsExemptionSearchNdsResponse = converter.convertTo(PRSExemptionSearchNdsResponse.class, response);

        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during response conversion process:  " + e.getMessage(),
                    e);
        }

        logger.info("Successfully created the NDS response");

        return prsExemptionSearchNdsResponse;
    }

}
