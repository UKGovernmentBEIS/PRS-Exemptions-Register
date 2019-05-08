package com.northgateps.nds.beis.esb.camel;

import com.northgateps.nds.beis.backoffice.service.getreferencevalues.GetReferenceValuesResponse;
import com.northgateps.nds.platform.esb.camel.NdsCacheHandler;

public class GetReferenceDataCacheAdapter extends NdsCacheHandler {

    public GetReferenceDataCacheAdapter() {
        super("GetReferenceValuesWSDL", "getReferenceValues", "cxf:bean:beisGetReferenceValuesService", 
              "com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesNdsResponse", "defaultCache");
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
    protected Class<GetReferenceValuesResponse> getBackOfficeClassName() {
        return GetReferenceValuesResponse.class;
    }

    @Override
    protected boolean isCallToSubRouteRequired() {
        return false;
    }

}
