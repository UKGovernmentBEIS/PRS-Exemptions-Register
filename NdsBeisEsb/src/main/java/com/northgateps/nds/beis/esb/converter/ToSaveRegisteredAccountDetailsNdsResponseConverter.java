package com.northgateps.nds.beis.esb.converter;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsResponse;
import com.northgateps.nds.beis.backoffice.service.maintainpartydetails.MaintainPartyDetailsResponse;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Convert the passed in service responses into a {@link SaveRegisteredAccountDetailsNdsResponse}
 * 
 */
@Converter
@DoNotWeaveLoggingSystem
public class ToSaveRegisteredAccountDetailsNdsResponseConverter {

    private static final NdsLogger logger = NdsLogger.getLogger(
            ToSaveRegisteredAccountDetailsNdsResponseConverter.class);

    @Converter
    public static SaveRegisteredAccountDetailsNdsResponse converting(
            MaintainPartyDetailsResponse maintainPartyDetailsResponse) {

        logger.info(
                "Conversion of back office MaintainPartyDetailsResponse object into NDS SaveRegisteredAccountDetailsNdsResponse response object started.");

        SaveRegisteredAccountDetailsNdsResponse saveRegisteredAccountDetailsNdsResponse = new SaveRegisteredAccountDetailsNdsResponse();

        saveRegisteredAccountDetailsNdsResponse.setSuccess(maintainPartyDetailsResponse.isSuccess());

        ToNdsMessagesConverter.SetNdsMessages(saveRegisteredAccountDetailsNdsResponse,
                maintainPartyDetailsResponse.getMessages());

        return saveRegisteredAccountDetailsNdsResponse;
    }
}
