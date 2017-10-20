package com.northgateps.nds.cas.web.flow;

/**
 * Override the NdsPlatformCas version to hard code the tenant to BEIS.
 * If this project becomes multi-tenanted, this will probably need to be deleted.
 */
public class ExtractTenantAction {
    
    /** 
     * Extracts and returns the tenant assuming it's been added to the service definition. 
     * @throws InvalidLoginLocationException if the tenant data cannot be found. 
     */
    public String extractTenant(Object parameters) {
        return "BEIS";
    }
}