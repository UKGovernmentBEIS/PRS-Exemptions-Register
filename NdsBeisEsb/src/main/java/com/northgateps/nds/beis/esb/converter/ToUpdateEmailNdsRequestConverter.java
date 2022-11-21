package com.northgateps.nds.beis.esb.converter;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.BeisUpdateUserDetails;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsRequest;
import com.northgateps.nds.platform.api.UpdateEmailDetails;
import com.northgateps.nds.platform.api.updateemail.UpdateEmailNdsRequest;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converter class to convert an external back office object to a standard NDS object.
 * From SaveRegisteredAccountDetailsNdsRequest to UpdateEmailNdsRequest.
 */
@Converter
@DoNotWeaveLoggingSystem
public class ToUpdateEmailNdsRequestConverter {
    /**
     * @param request This is the shared UI request object for changes to a user's account.
     * @return - the the standard platform UpdateEmailNdsRequest.
     * @throws NdsBusinessException if the registered account details don't exist
     */
    @Converter
    public static UpdateEmailNdsRequest converting(SaveRegisteredAccountDetailsNdsRequest request)
            throws NdsBusinessException {
        UpdateEmailNdsRequest to = new UpdateEmailNdsRequest();
        BeisUpdateUserDetails from = null;

        if (request.getRegisteredAccountDetails() != null) {
            from = request.getRegisteredAccountDetails().getUpdateUserDetails();
        }

        if (from == null) {
            throw new NdsBusinessException("Error occurred. There are no updated user details to save.");
        }
        
        UpdateEmailDetails updateEmailDetails = new UpdateEmailDetails();
        updateEmailDetails.setUserName(from.getUsername());
        updateEmailDetails.setEmail(from.getEmail());
        updateEmailDetails.setConfirmEmail(from.getEmail());
        updateEmailDetails.setPassword(from.getPassword());
        updateEmailDetails.setTenant(request.getTenant());
        
        to.setUpdateEmailDetails(updateEmailDetails);
        to.setPublicWebAddress(request.getPublicWebAddress());
        to.setEmailTemplateName(request.getEmailTemplateName());
        return to;
    }
}
