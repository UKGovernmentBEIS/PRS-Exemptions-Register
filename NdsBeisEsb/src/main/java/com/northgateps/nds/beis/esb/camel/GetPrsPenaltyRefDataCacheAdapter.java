package com.northgateps.nds.beis.esb.camel;

import com.northgateps.nds.beis.backoffice.service.getprspenaltyreferencedata.GetPRSPenaltyReferenceDataResponse;
import com.northgateps.nds.platform.esb.camel.NdsCacheHandler;

/**
 * Cache adapter class for getPrsPenaltyRefData route
 */
public class GetPrsPenaltyRefDataCacheAdapter extends NdsCacheHandler {

    public GetPrsPenaltyRefDataCacheAdapter() {
        super("GetPRSPenaltyReferenceDataWSDL", "prsPenaltyRefData", "cxf:bean:getPrsPenaltyRefDataEndPoint",
                   "com.northgateps.nds.beis.api.getprspenaltyrefdata.GetPrsPenaltyRefDataNdsResponse", "defaultCache");
       
    }

    @Override
    protected boolean useBackupCache() {
        return false;
    }

    @Override
    protected boolean callBackOffice() {
        return true;
    }

    @Override
    protected Class<GetPRSPenaltyReferenceDataResponse> getBackOfficeClassName() {
        return GetPRSPenaltyReferenceDataResponse.class;
    }

    @Override
    protected boolean isCallToSubRouteRequired() {
        return false;
    }

}
