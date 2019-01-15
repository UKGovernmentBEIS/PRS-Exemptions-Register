package com.northgateps.nds.beis.esb.getprspenaltyrefdata;

import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;

import com.northgateps.nds.beis.api.getprspenaltyrefdata.GetPrsPenaltyRefDataNdsRequest;
import com.northgateps.nds.beis.api.getprspenaltyrefdata.GetPrsPenaltyRefDataNdsResponse;
import com.northgateps.nds.beis.backoffice.service.getprspenaltyreferencedata.GetPRSPenaltyReferenceDataRequest;
import com.northgateps.nds.beis.backoffice.service.getprspenaltyreferencedata.GetPRSPenaltyReferenceDataResponse;
import com.northgateps.nds.platform.esb.adapter.BackupCacheableAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxyImpl;
import com.northgateps.nds.platform.esb.camel.CacheKeyGenerator;
import com.northgateps.nds.platform.esb.camel.NdsCacheHandlerParser;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Processes the getting of penalty reference data between native (REST) objects and
 * back office.
 *
 */
public class GetPrsPenaltyRefDataAdapter extends
		BackupCacheableAdapter<GetPrsPenaltyRefDataNdsRequest, GetPrsPenaltyRefDataNdsResponse, GetPRSPenaltyReferenceDataRequest, GetPRSPenaltyReferenceDataResponse>
		implements CacheKeyGenerator {
    
    private static final NdsLogger logger = NdsLogger.getLogger(GetPrsPenaltyRefDataAdapter.class);

    @Override
    protected GetPRSPenaltyReferenceDataRequest doRequestProcess(GetPrsPenaltyRefDataNdsRequest request,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
        
        ndsExchange.setOperationName("GetPRSPenaltyReferenceDataWSDL");
        GetPRSPenaltyReferenceDataRequest getPRSPenaltyReferenceDataRequest = new GetPRSPenaltyReferenceDataRequest();
        
        logger.info("Successfully created the BEIS request");
        return getPRSPenaltyReferenceDataRequest;
        
    }

    /**
     * converts beis response to NDS response
     */
    @Override
    protected GetPrsPenaltyRefDataNdsResponse doResponseProcess(GetPRSPenaltyReferenceDataResponse externalResponse,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
        GetPrsPenaltyRefDataNdsResponse getPrsPenaltyRefDataNdsResponse = new GetPrsPenaltyRefDataNdsResponse();
        try {
            TypeConverter converter = ndsExchange.getTypeConverter();
            getPrsPenaltyRefDataNdsResponse = converter.convertTo(GetPrsPenaltyRefDataNdsResponse.class,
                    externalResponse);
            
            
        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during response conversion process:  " + e.getMessage(),
                    e);
        }
        logger.info("Successfully created the NDS response");
        return getPrsPenaltyRefDataNdsResponse;
    }

    @Override
    protected String getRequestClassName() {
        return GetPrsPenaltyRefDataNdsRequest.class.getName();
    }

    @Override
    protected String getResponseClassName() {
        return GetPRSPenaltyReferenceDataResponse.class.getName();
    }

    @Override
    public void getCacheKey(Exchange exchange) {
        exchange.getIn().setHeader(NdsCacheHandlerParser.CACHE_KEY_INDEX, this.getClass().getName());        
    }

    // N/A
	@Override
	protected GetPrsPenaltyRefDataNdsResponse doFilterResponseProcess(GetPrsPenaltyRefDataNdsResponse ndsResponse,
			GetPrsPenaltyRefDataNdsRequest ndsRequest, NdsSoapRequestAdapterExchangeProxyImpl ndsExchange) {
		// TODO Auto-generated method stub
		return null;
	}

	// N/A
	@Override
	protected GetPrsPenaltyRefDataNdsRequest doFilterRequestProcess(GetPrsPenaltyRefDataNdsRequest ndsRequest,
			NdsSoapRequestAdapterExchangeProxyImpl ndsExchange) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getNdsResponseClassName() {
		return GetPrsPenaltyRefDataNdsResponse.class.getName();
	}
}
