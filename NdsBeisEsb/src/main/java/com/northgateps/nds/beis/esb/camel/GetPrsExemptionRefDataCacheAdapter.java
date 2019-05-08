package com.northgateps.nds.beis.esb.camel;

import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse;
import com.northgateps.nds.platform.esb.camel.NdsCacheHandler;

/**
 * Cache adapter class for getPrsExemptionRefData route
 */
public class GetPrsExemptionRefDataCacheAdapter extends NdsCacheHandler {

    public GetPrsExemptionRefDataCacheAdapter() {
        super("GetPRSExemptionReferenceDataWSDL", "prsExemptionRefData", "cxf:bean:getPrsExemptionRefDataEndPoint", 
                "com.northgateps.nds.beis.api.getprsexemptionrefdata.GetPrsExemptionRefDataNdsResponse", "defaultCache");
        
    }

    @Override
    protected boolean useBackupCache() {
        return false;
    }

    @Override
    protected Class<GetPRSExemptionReferenceDataResponse> getBackOfficeClassName() {
        return GetPRSExemptionReferenceDataResponse.class;
    }

    @Override
    protected boolean callBackOffice() {
        return true;
    }

    @Override
    protected boolean isCallToSubRouteRequired() {
        return false;
    }

}
