package com.northgateps.nds.beis.esb.registration;

import java.util.HashMap;
import com.northgateps.nds.platform.api.passwordreset.PasswordResetNdsRequest;
import com.northgateps.nds.platform.api.passwordreset.PasswordResetNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsDirectoryComponent;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.directory.DirectoryAccessConnection;
import com.northgateps.nds.platform.esb.directory.DirectoryConnectionConfig;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryException;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryMissingAttributeValueException;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryUnwillingToPerformOperationException;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.esb.exception.NdsDirectoryException;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Adapter class that updates the email address (if updateEmailRequired property in exchange is true) to the
 *  latest one which is in backoffice
 *
 */
public class UpdateEmailAddressInLdapComponent
        extends NdsDirectoryComponent<PasswordResetNdsRequest, PasswordResetNdsResponse> {

    final ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();

    @Override
    protected void doProcess(PasswordResetNdsRequest request, DirectoryAccessConnection directoryConnection,
            NdsSoapRequestAdapterExchangeProxy ndsExchange, DirectoryConnectionConfig directoryConnectionConfigImpl)
                    throws NdsApplicationException, NdsBusinessException, DirectoryException {
        String ldapAttributeEmail = configurationManager.getString("attribute.email");

       //Retrieve the updated email address from the exchange set in GetUserEmailAddressAdapter.getEmailAddress method
        String updatedEmailAddress = (String) ndsExchange.getAnExchangeProperty("userEmailAddress");

        try {
            HashMap<String, String> changes = new HashMap<>();

            if (updatedEmailAddress != null) {
                changes.put(ldapAttributeEmail, updatedEmailAddress);
            }

            String userDn = getUserDn(configurationManager, request.getPasswordResetDetails().getUsername(),
                    request.getPasswordResetDetails().getTenant());

            getUserManager().setAttributesOnEntry(directoryConnection, userDn, changes);
        } catch (DirectoryUnwillingToPerformOperationException e) {
            throw new NdsBusinessException(e.getMessage());
        } catch (DirectoryMissingAttributeValueException e) {
            throw new NdsDirectoryException("Unable to find attribute");
        } catch (DirectoryException e) {
            throw new NdsDirectoryException(e.getMessage(), e);
        }

    }

    @Override
    protected PasswordResetNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {
        PasswordResetNdsResponse response = new PasswordResetNdsResponse();

        response.setSuccess(true);

        return response;
    }

    @Override
    protected String getRequestClassName() {
        return PasswordResetNdsRequest.class.getName();
    }
}
