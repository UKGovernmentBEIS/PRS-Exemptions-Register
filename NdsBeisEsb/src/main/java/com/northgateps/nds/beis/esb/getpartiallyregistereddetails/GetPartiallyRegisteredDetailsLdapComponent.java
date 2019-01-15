package com.northgateps.nds.beis.esb.getpartiallyregistereddetails;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.northgateps.nds.beis.api.AccountType;
import com.northgateps.nds.beis.api.BeisAccountDetails;
import com.northgateps.nds.beis.api.BeisAddressDetail;
import com.northgateps.nds.beis.api.BeisUpdateUserDetails;
import com.northgateps.nds.beis.api.OrganisationNameDetail;
import com.northgateps.nds.beis.api.PersonNameDetail;
import com.northgateps.nds.beis.api.UpdateBeisRegistrationDetails;
import com.northgateps.nds.beis.api.getpartiallyregistereddetails.GetPartiallyRegisteredDetailsNdsRequest;
import com.northgateps.nds.beis.api.getpartiallyregistereddetails.GetPartiallyRegisteredDetailsNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsDirectoryComponent;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.directory.DirectoryAccessConnection;
import com.northgateps.nds.platform.esb.directory.DirectoryConnectionConfig;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryException;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryMissingAttributeValueException;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.esb.exception.NdsDirectoryException;

/**
 * Adapter class that processes the retrieving of partially registered user details between native (REST) objects and LDAP objects
 */
public class GetPartiallyRegisteredDetailsLdapComponent 
    extends NdsDirectoryComponent<GetPartiallyRegisteredDetailsNdsRequest, GetPartiallyRegisteredDetailsNdsResponse> {
    
    @Override
    protected void doProcess(GetPartiallyRegisteredDetailsNdsRequest request,
            DirectoryAccessConnection directoryConnection, NdsSoapRequestAdapterExchangeProxy ndsExchange,
            DirectoryConnectionConfig directoryConnectionConfigImpl)
                    throws NdsApplicationException, NdsBusinessException, DirectoryException {
        
        UpdateBeisRegistrationDetails beisRegistrationDetails = new UpdateBeisRegistrationDetails();

        try {
            String userDn = getUserDn(configurationManager, request.getUsername(), request.getTenant());

            Map<String, String> attributes = getUserManager().userAttributes(directoryConnection, userDn);

            beisRegistrationDetails.setAccountDetails(GetAccountDetailsFromLdapAttributes(attributes));
            beisRegistrationDetails.setUpdateUserDetails(GetUpdateUserDetailsFromLdapAttributes(attributes));
            beisRegistrationDetails.setContactAddress(GetAddressDetailsFromLdapAttributes(attributes));

            GetPartiallyRegisteredDetailsNdsResponse response = new GetPartiallyRegisteredDetailsNdsResponse();

            response.setPartiallyRegisteredDetails(beisRegistrationDetails);

            ndsExchange.setResponseMessageBody(response);

        } catch (DirectoryMissingAttributeValueException e) {
            throw new NdsDirectoryException("Unable to find attribute");
        } catch (DirectoryException e) {
            throw new NdsDirectoryException(e.getMessage(), e);
        }                
    }
    
    /**
     * This function will read the account detail attributes from ldap and set the appropriate 
     * details in the account taking into consideration organisations and persons. 
     * @param attributes this is a map of name value pairs representing LDAP attributes
     * @return account details held in LDAP
     */
    private BeisAccountDetails GetAccountDetailsFromLdapAttributes(Map<String, String> attributes){
        
        BeisAccountDetails accountDetails = new BeisAccountDetails();
        
        String ldapAttributeForename = configurationManager.getString("attribute.forename");        
        String ldapAttributeSurname = configurationManager.getString("attribute.surname");
        String ldapAttributeHomePhone = configurationManager.getString("attribute.homePhone");       
        String ldapAttributeOrgname = configurationManager.getString("attribute.organisationName");               
        
        if(!StringUtils.isEmpty(attributes.get(ldapAttributeOrgname))){
            
            OrganisationNameDetail organisationNameDetail = new OrganisationNameDetail();
            organisationNameDetail.setOrgName(attributes.get(ldapAttributeOrgname));
            accountDetails.setOrganisationNameDetail(organisationNameDetail);
            accountDetails.setAccountType(AccountType.ORGANISATION);
            
        }else if(!StringUtils.isEmpty(attributes.get(ldapAttributeForename))){
            
            PersonNameDetail personNameDetail = new PersonNameDetail();
            personNameDetail.setFirstname(attributes.get(ldapAttributeForename));       
            personNameDetail.setSurname(attributes.get(ldapAttributeSurname));                       
            accountDetails.setPersonNameDetail(personNameDetail);  
            accountDetails.setAccountType(AccountType.PERSON);
            
        }

        accountDetails.setTelNumber(attributes.get(ldapAttributeHomePhone));   
        
        return accountDetails;
        
    }
    
    /**
     * This function will read the user detail attributes from ldap and set them in the update user details
     * @param attributes this is a map of name value pairs representing LDAP attributes
     * @return user details held in LDAP
     */
    private BeisUpdateUserDetails GetUpdateUserDetailsFromLdapAttributes(Map<String, String> attributes){
        
        BeisUpdateUserDetails updateUserDetails = new BeisUpdateUserDetails();
        
        String ldapAttributeEmail = configurationManager.getString("attribute.email");
        String ldapAttributeUid = configurationManager.getString("attribute.userid");
        
        updateUserDetails.setEmail(attributes.get(ldapAttributeEmail));
        updateUserDetails.setUsername(attributes.get(ldapAttributeUid));

        return updateUserDetails;
    }
    
    /**
     * This function will read the address attributes from ldap and set them in the address
     * class for the response.
     * @param attributes this is a map of name value pairs representing LDAP attributes
     * @return address details held in LDAP
     */
    private BeisAddressDetail GetAddressDetailsFromLdapAttributes(Map<String, String> attributes){
        
        String ldapAttributeStreetLineOne = configurationManager.getString("attribute.street");
        String ldapAttributeStreetLineTwo = configurationManager.getString("attribute.street2");
        String ldapAttributeTown = configurationManager.getString("attribute.town");
        String ldapAttributeCounty = configurationManager.getString("attribute.county");
        String ldapAttributePostcode = configurationManager.getString("attribute.postalCode");
        String ldapAttributeUprn = configurationManager.getString("attribute.uprn"); 
        String ldapAttributeCountry = configurationManager.getString("attribute.country");
        
        BeisAddressDetail addressDetails = new BeisAddressDetail();
        
        addressDetails.setLine(new ArrayList<String>());
        addressDetails.getLine().add(attributes.get(ldapAttributeStreetLineOne));
        addressDetails.getLine().add(attributes.get(ldapAttributeStreetLineTwo));
        addressDetails.setTown(attributes.get(ldapAttributeTown));
        addressDetails.setCounty(attributes.get(ldapAttributeCounty));
        addressDetails.setPostcode(attributes.get(ldapAttributePostcode));
        addressDetails.setCountry(attributes.get(ldapAttributeCountry));           
        addressDetails.setUprn(attributes.get(ldapAttributeUprn));
        
        return addressDetails;
        
    }

    @Override
    protected GetPartiallyRegisteredDetailsNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {
        
        GetPartiallyRegisteredDetailsNdsResponse response = null;

        try{

            response = (GetPartiallyRegisteredDetailsNdsResponse) ndsExchange.getRequestMessageBody(GetPartiallyRegisteredDetailsNdsResponse.class);
            response.setSuccess(true);

        }catch (ClassNotFoundException e) {
            throw new NdsApplicationException(e.getMessage(), e);
        }
        
        return response;
    }

    @Override
    protected String getRequestClassName() {        
        return GetPartiallyRegisteredDetailsNdsRequest.class.getName();
    }
}
