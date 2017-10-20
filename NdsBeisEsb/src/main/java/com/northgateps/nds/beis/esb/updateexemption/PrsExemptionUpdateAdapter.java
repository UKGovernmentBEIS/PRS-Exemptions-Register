package com.northgateps.nds.beis.esb.updateexemption;

import org.apache.camel.TypeConverter;

import com.northgateps.nds.beis.api.updateexemptiondetails.UpdateExemptionDetailsNdsRequest;
import com.northgateps.nds.beis.api.updateexemptiondetails.UpdateExemptionDetailsNdsResponse;
import com.northgateps.nds.beis.backoffice.service.updateprsexemption.UpdatePRSExemptionRequest;
import com.northgateps.nds.beis.backoffice.service.updateprsexemption.UpdatePRSExemptionResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Processes the request to and from the update exemption details
 */

public class PrsExemptionUpdateAdapter extends
        NdsSoapAdapter<UpdateExemptionDetailsNdsRequest, UpdateExemptionDetailsNdsResponse, UpdatePRSExemptionRequest, UpdatePRSExemptionResponse> {

    private static final NdsLogger logger = NdsLogger.getLogger(PrsExemptionUpdateAdapter.class);

    @Override
    protected String getRequestClassName() {
        return UpdateExemptionDetailsNdsRequest.class.getName();
    }

    @Override
    protected String getResponseClassName() {
        return UpdatePRSExemptionResponse.class.getName();
    }


    @Override
    protected UpdatePRSExemptionRequest doRequestProcess(UpdateExemptionDetailsNdsRequest request,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
       
        UpdatePRSExemptionRequest updatePrsExemptionRequest;
        
        try {

            ndsExchange.setOperationName("UpdatePRSExemptionWSDL");

            TypeConverter converter = ndsExchange.getTypeConverter();
            updatePrsExemptionRequest = converter.convertTo(UpdatePRSExemptionRequest.class, request);

        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during request conversion process:  " + e.getMessage(), e);
        }

        logger.info("Successfully created the SOAP request");
        return updatePrsExemptionRequest;
    }

    @Override
    protected UpdateExemptionDetailsNdsResponse doResponseProcess(UpdatePRSExemptionResponse response,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
        
        UpdateExemptionDetailsNdsResponse updateExemptionDetailsNdsResponse;
        
        try {

            TypeConverter converter = ndsExchange.getTypeConverter();
            updateExemptionDetailsNdsResponse = converter.convertTo(UpdateExemptionDetailsNdsResponse.class, response);

        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during response conversion process:  " + e.getMessage(),
                    e);
        }

        logger.info("Successfully created the NDS response");
        
        return updateExemptionDetailsNdsResponse;
    }

}
