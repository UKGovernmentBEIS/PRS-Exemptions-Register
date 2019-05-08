package com.northgateps.nds.beis.esb.camel;

import org.apache.camel.Exchange;

import com.northgateps.nds.platform.esb.camel.NdsCacheHandler;

/**
 * Cache adapter class for getConstrainedValues route
 */
public class GetConstrainedValuesCacheAdapter  extends NdsCacheHandler {

    public GetConstrainedValuesCacheAdapter() {
        super(null, "constrainedValues", "singleFileReader:{{beis.csvConstraintsFilePath}}", null, 
              "defaultCache");
        
    }

    @Override
    protected boolean useBackupCache() {
        return false;
    }

    @Override
    protected Class<?> getBackOfficeClassName() {
        return null;
    }

    @Override
    protected boolean callBackOffice() {
        return false;
    }
    
    @Override
    protected boolean validateResponseCorrectForCaching(Object response, Exchange exchange) {
        return !(response == null);
    }

    @Override
    protected boolean isCallToSubRouteRequired() {
        return false;
    }

}
