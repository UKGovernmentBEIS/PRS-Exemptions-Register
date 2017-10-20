package com.northgateps.nds.beis.esb.registration;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.northgateps.nds.platform.api.NdsRequest;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsDirectoryAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.directory.DirectoryConnection;
import com.northgateps.nds.platform.esb.directory.DirectoryConnectionConfig;
import com.northgateps.nds.platform.esb.directory.DirectoryServices;
import com.northgateps.nds.platform.esb.directory.ServiceAccessDetails;
import com.northgateps.nds.platform.esb.directory.UserServices;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryException;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.esb.exception.NdsDirectoryException;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;


/**
 * Abstract class to retrieve the account id from LDAP, derive from this class and implement the three simple methods
 */
public abstract class RetrieveAccountIdLdapAdapter<TRequest extends NdsRequest, TResponse extends NdsResponse>
        extends NdsDirectoryAdapter<TRequest, TResponse> {
    
    private static final NdsLogger logger = NdsLogger.getLogger(RetrieveAccountIdLdapAdapter.class);

    public static String EMAIL_ADDRESS = "email";

    @Override
    protected void doRequestProcess(TRequest request, DirectoryConnection directoryConnection,
            NdsSoapRequestAdapterExchangeProxy ndsExchange, DirectoryConnectionConfig directoryConnectionConfigImpl)
                    throws NdsApplicationException, NdsBusinessException {
        logger.info(" Started setting account id in request ");

        ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();

        // Get the service name
        String serviceName = configurationManager.getString("service.foundationLayerPartyService.name");

        String userDn = getUserDn(configurationManager, getUsername(request), getTenant(request));

        try {

            ServiceAccessDetails serviceAccessDetails = UserServices.serviceLookup(directoryConnection, userDn,
                    serviceName, true, true, false);

            // Check the user id as we have a temporary issue where the uid is
            // set automatically. This however has hypens which will not be in a
            // partyref
            if (serviceAccessDetails.getUserId() == null || serviceAccessDetails.getUserId().isEmpty()
                    || serviceAccessDetails.getUserId().contains("-")) {
                throw new NdsBusinessException(
                        "Error occurred. The user is only partially registered and does not have a valid account id");
            }

            // get the account id, set it in the request and set the request in the exchange
            setAccountId(request, serviceAccessDetails.getUserId());

            Map<String, String> userAttributesMap = DirectoryServices.retrieveEntryAttributes(directoryConnection,
                    userDn, "mail");

            if (userAttributesMap != null && StringUtils.isNotEmpty(userAttributesMap.get("mail"))) {
                ndsExchange.setAnExchangeProperty(EMAIL_ADDRESS, userAttributesMap.get("mail"));
            }

            ndsExchange.setRequestMessageBody(request);
            logger.info("Sucessfully set account id in request and email in exchange");

        } catch (DirectoryException e) {
            /*if (e.getCause() instanceof LdapInvalidAttributeValueException) {
                throw new NdsDirectoryException(e.getCause().getMessage());
            } else {*/
                throw new NdsDirectoryException(e.getMessage(), e);
            /*}*/
        }
    }

    protected abstract String getUsername(TRequest request);

    protected abstract String getTenant(TRequest request);

    protected abstract void setAccountId(TRequest request, String accountId);
}
