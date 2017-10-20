package com.northgateps.nds.beis.esb.getreferencevalues;

import java.util.ArrayList;
import java.util.List;

import com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesNdsRequest;
import com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesNdsResponse;
import com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesResponseDetails;
import com.northgateps.nds.beis.api.getreferencevalues.ReferenceValuesDetails;
import com.northgateps.nds.beis.backoffice.service.getreferencevalues.GetReferenceValuesResponse;
import com.northgateps.nds.beis.backoffice.service.getreferencevalues.GetReferenceValuesResponse.ReferenceValues;
import com.northgateps.nds.beis.backoffice.service.getreferencevalues.GetReferenceValuesResponse.ReferenceValues.ReferenceValue;

/**
 * Test Data in an Nds request to be used in tests conversion between NDS type object
 * (GetReferenceValuesNdsRequest) and external SOAP WS facing type object
 * (GetReferenceValues)
 */

public class CreateGetReferenceValuesTestData {
    
    public static GetReferenceValuesNdsRequest createGetReferenceValuesNdsRequest() {
        GetReferenceValuesNdsRequest getReferenceValuesNdsRequest = new GetReferenceValuesNdsRequest();
        getReferenceValuesNdsRequest.setReferenceValuesDetails(createReferenceValuesDetailsForNdsRequest("ADDRESS ROLES", "NBEU", "SYS"));
        return getReferenceValuesNdsRequest;
    }
    
    public static GetReferenceValuesResponse createGetReferenceValuesResponse() {
        GetReferenceValuesResponse getReferenceValuesResponse = new GetReferenceValuesResponse();
        getReferenceValuesResponse.setReferenceValues(createReferenceValues());
        getReferenceValuesResponse.setSuccess(true);
        return getReferenceValuesResponse;
    }
    
    private static ReferenceValues createReferenceValues() {
        ReferenceValues referenceValues = new ReferenceValues();
        referenceValues.getReferenceValue().add(createReferenceValue("ADDRESS ROLES", "NBEU", "SYS", "AOR", "Assessor Organisation"));
        referenceValues.getReferenceValue().add(createReferenceValue("ADDRESS ROLES", "NBEU", "SYS", "PHYSICAL", "Physical Location"));
        referenceValues.getReferenceValue().add(createReferenceValue("ADDRESS ROLES", "NBEU", "SYS", "PROV", "Providers"));
        return referenceValues;
    }
    
    private static ReferenceValue createReferenceValue(String domainCode, String workplaceCode, String serviceCode, String code, String name) {
        GetReferenceValuesResponse.ReferenceValues.ReferenceValue referenceValue = new GetReferenceValuesResponse.ReferenceValues.ReferenceValue();
        referenceValue.setDomainCode(domainCode);
        referenceValue.setWorkplaceCode(workplaceCode);
        referenceValue.setServiceCode(serviceCode);
        referenceValue.setCode(code);
        referenceValue.setName(name);
        return referenceValue;
    }
    
    public static GetReferenceValuesNdsResponse createGetReferenceValuesNdsResponse() {
        GetReferenceValuesNdsResponse getReferenceValuesNdsResponse = new GetReferenceValuesNdsResponse();
        List<ReferenceValuesDetails> referenceValuesDetails = new ArrayList<ReferenceValuesDetails>();
        GetReferenceValuesResponseDetails getReferenceValuesResponseDetails = new GetReferenceValuesResponseDetails();
        referenceValuesDetails.add(createReferenceValuesDetails("ADDRESS ROLES", "NBEU", "SYS", "AOR", "Assessor Organisation"));
        referenceValuesDetails.add(createReferenceValuesDetails("ADDRESS ROLES", "NBEU", "SYS", "PHYSICAL", "Physical Location"));
        referenceValuesDetails.add(createReferenceValuesDetails("ADDRESS ROLES", "NBEU", "SYS", "PROV", "Providers"));
        getReferenceValuesResponseDetails.setReferenceValuesDetails(referenceValuesDetails);
        getReferenceValuesNdsResponse.setGetReferenceValuesResponseDetails(getReferenceValuesResponseDetails);
        getReferenceValuesNdsResponse.setSuccess(true);
        return getReferenceValuesNdsResponse;
    }
    
    private static ReferenceValuesDetails createReferenceValuesDetails(String domainCode, String workplaceCode, String serviceCode, String code, String name) {
        ReferenceValuesDetails referenceValuesDetails = new ReferenceValuesDetails();
        referenceValuesDetails.setDomainCode(domainCode);
        referenceValuesDetails.setWorkplaceCode(workplaceCode);
        referenceValuesDetails.setServiceCode(serviceCode);
        referenceValuesDetails.setCode(code);
        referenceValuesDetails.setName(name);
        return referenceValuesDetails;
    }
    
    private static ReferenceValuesDetails createReferenceValuesDetailsForNdsRequest(String domainCode, String workplaceCode, String serviceCode) {
        ReferenceValuesDetails referenceValuesDetails = new ReferenceValuesDetails();
        referenceValuesDetails.setDomainCode(domainCode);
        referenceValuesDetails.setWorkplaceCode(workplaceCode);
        referenceValuesDetails.setServiceCode(serviceCode);
        return referenceValuesDetails;
    }
    
    public static GetReferenceValuesResponse createMultipleGetReferenceValuesResponse() {
        GetReferenceValuesResponse getReferenceValuesResponse = new GetReferenceValuesResponse();
        getReferenceValuesResponse.setReferenceValues(createReferenceValues());
        getReferenceValuesResponse.setSuccess(true);
        return getReferenceValuesResponse;

    }
    
    public static GetReferenceValuesNdsResponse createMultipleGetReferenceValuesNdsResponse() {
        GetReferenceValuesNdsResponse getReferenceValuesNdsResponse = new GetReferenceValuesNdsResponse();
        List<ReferenceValuesDetails> referenceValuesDetails = new ArrayList<ReferenceValuesDetails>();
        GetReferenceValuesResponseDetails getReferenceValuesResponseDetails = new GetReferenceValuesResponseDetails();
        referenceValuesDetails.add(createReferenceValuesDetails("ADDRESS ROLES", "NBEU", "SYS", "AOR", "Assessor Organisation"));
        referenceValuesDetails.add(createReferenceValuesDetails("ADDRESS ROLES", "NBEU", "SYS", "PHYSICAL", "Physical Location"));
        referenceValuesDetails.add(createReferenceValuesDetails("ADDRESS ROLES", "NBEU", "SYS", "PROV", "Providers"));

        getReferenceValuesResponseDetails.setReferenceValuesDetails(referenceValuesDetails);
        getReferenceValuesNdsResponse.setGetReferenceValuesResponseDetails(getReferenceValuesResponseDetails);
        getReferenceValuesNdsResponse.setSuccess(true);

        return getReferenceValuesNdsResponse;
    }
    
    public static GetReferenceValuesNdsRequest createNoCriteriaGetReferenceValuesNdsRequest() {
        GetReferenceValuesNdsRequest getReferenceValuesNdsRequest = new GetReferenceValuesNdsRequest();
        getReferenceValuesNdsRequest.setReferenceValuesDetails(createReferenceValuesDetails(null,null,null,null,null));
        return getReferenceValuesNdsRequest;
    }
    
    public static GetReferenceValuesNdsRequest createWithCriteriaGetReferenceValuesNdsRequest() {
        GetReferenceValuesNdsRequest getReferenceValuesNdsRequest = new GetReferenceValuesNdsRequest();
        getReferenceValuesNdsRequest.setReferenceValuesDetails(createReferenceValuesDetails("", "NBEU", "SYS", "", ""));
        return getReferenceValuesNdsRequest;
    }
}
