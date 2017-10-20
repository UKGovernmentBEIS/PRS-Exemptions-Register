package com.northgateps.nds.beis.esb.adapter;

import org.apache.camel.TypeConverter;

import com.northgateps.nds.beis.api.PRSPenaltySearchNdsRequest;
import com.northgateps.nds.beis.api.PRSPenaltySearchNdsResponse;
import com.northgateps.nds.beis.backoffice.service.prspenaltysearch.PRSPenaltySearchRequest;
import com.northgateps.nds.beis.backoffice.service.prspenaltysearch.PRSPenaltySearchResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Processes the request to and from the get Penalty search result
 */
public class PRSPenaltySearchAdapter extends
        NdsSoapAdapter<PRSPenaltySearchNdsRequest, PRSPenaltySearchNdsResponse, PRSPenaltySearchRequest, PRSPenaltySearchResponse> {

    private static final NdsLogger logger = NdsLogger.getLogger(PRSPenaltySearchAdapter.class);

    @Override
    protected String getRequestClassName() {
        return PRSPenaltySearchNdsRequest.class.getName();
    }

    @Override
    protected String getResponseClassName() {
        return PRSPenaltySearchResponse.class.getName();
    }

    @Override
    protected PRSPenaltySearchRequest doRequestProcess(PRSPenaltySearchNdsRequest request,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
        PRSPenaltySearchRequest prsPenaltySearchRequest = new PRSPenaltySearchRequest();

        try {

            ndsExchange.setOperationName("PRSPenaltySearchWSDL");

            TypeConverter converter = ndsExchange.getTypeConverter();
            prsPenaltySearchRequest = converter.convertTo(PRSPenaltySearchRequest.class, request);

        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during request conversion process:  " + e.getMessage(), e);
        }

        logger.info("Successfully created the SOAP request");

        return prsPenaltySearchRequest;
    }

    @Override
    protected PRSPenaltySearchNdsResponse doResponseProcess(PRSPenaltySearchResponse response,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {

        PRSPenaltySearchNdsResponse prsPenaltySearchNdsResponse = new PRSPenaltySearchNdsResponse();

        try {

            TypeConverter converter = ndsExchange.getTypeConverter();
            prsPenaltySearchNdsResponse = converter.convertTo(PRSPenaltySearchNdsResponse.class, response);

        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during response conversion process:  " + e.getMessage(),
                    e);
        }

        logger.info("Successfully created the NDS response");

        return prsPenaltySearchNdsResponse;
    }

}
