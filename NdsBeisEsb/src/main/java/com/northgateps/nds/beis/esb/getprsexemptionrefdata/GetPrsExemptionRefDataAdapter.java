package com.northgateps.nds.beis.esb.getprsexemptionrefdata;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.camel.TypeConverter;

import com.northgateps.nds.beis.api.ExemptionTypeDetails;
import com.northgateps.nds.beis.api.getprsexemptionrefdata.GetPrsExemptionRefDataNdsRequest;
import com.northgateps.nds.beis.api.getprsexemptionrefdata.GetPrsExemptionRefDataNdsResponse;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataRequest;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Processes the getting of exemption reference data between native (REST) objects and
 * back office.
 *
 */
public class GetPrsExemptionRefDataAdapter extends
        NdsSoapAdapter<GetPrsExemptionRefDataNdsRequest, GetPrsExemptionRefDataNdsResponse, GetPRSExemptionReferenceDataRequest, GetPRSExemptionReferenceDataResponse> {

    private static final NdsLogger logger = NdsLogger.getLogger(GetPrsExemptionRefDataAdapter.class);

    /**
     * converts NDS request to BEIS request
     */
    @Override
    protected GetPRSExemptionReferenceDataRequest doRequestProcess(GetPrsExemptionRefDataNdsRequest request,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
        ndsExchange.setOperationName("GetPRSExemptionReferenceDataWSDL");
        GetPRSExemptionReferenceDataRequest getPRSExemptionReferenceDataRequest = new GetPRSExemptionReferenceDataRequest();
        try {
            TypeConverter converter = ndsExchange.getTypeConverter();
            getPRSExemptionReferenceDataRequest = converter.convertTo(GetPRSExemptionReferenceDataRequest.class,
                    request);
        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during request conversion process:  " + e.getMessage(), e);
        }
        logger.info("Successfully created the BEIS request");
        return getPRSExemptionReferenceDataRequest;
    }

    /**
     * converts beis response to NDS response
     */
    @Override
    protected GetPrsExemptionRefDataNdsResponse doResponseProcess(GetPRSExemptionReferenceDataResponse externalResponse,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
        GetPrsExemptionRefDataNdsResponse getPrsExemptionRefDataNdsResponse = new GetPrsExemptionRefDataNdsResponse();
        try {
            TypeConverter converter = ndsExchange.getTypeConverter();
            getPrsExemptionRefDataNdsResponse = converter.convertTo(GetPrsExemptionRefDataNdsResponse.class,
                    externalResponse);
            List<ExemptionTypeDetails> exemptionTypeDetailsList = getPrsExemptionRefDataNdsResponse.getExemptionTypeTextList();
            Collections.sort(exemptionTypeDetailsList, new Comparator<ExemptionTypeDetails>() {

                @Override
                public int compare(ExemptionTypeDetails exemptionDetails1, ExemptionTypeDetails exemptionDetails2) {
                    return Integer.valueOf(exemptionDetails1.getSequence()).compareTo(Integer.valueOf(exemptionDetails2.getSequence()));
                }
            });
        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during response conversion process:  " + e.getMessage(),
                    e);
        }
        logger.info("Successfully created the NDS response");
        return getPrsExemptionRefDataNdsResponse;
    }

    @Override
    protected String getResponseClassName() {
        return GetPRSExemptionReferenceDataResponse.class.getName();
    }

    @Override
    protected String getRequestClassName() {
        return GetPrsExemptionRefDataNdsRequest.class.getName();
    }
}
