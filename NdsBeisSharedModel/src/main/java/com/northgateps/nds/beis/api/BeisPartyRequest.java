package com.northgateps.nds.beis.api;

import com.northgateps.nds.platform.api.NdsRequest;
/**
 * 
 * This interface class has been created so that we can use common adapter code
 *
 */
public interface BeisPartyRequest extends NdsRequest{
    
    public String getTenant();

    public void setTenant(String tenant);
    
    public String getUsername();

    public void setUsername(String username);    
}

