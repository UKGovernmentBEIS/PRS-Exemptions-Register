package com.northgateps.nds.beis.esb.converter;


import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.updateexemptiondetails.UpdateExemptionDetailsNdsResponse;
import com.northgateps.nds.beis.backoffice.service.updateprsexemption.UpdatePRSExemptionResponse;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;


/**
 * Converter class to convert an external back office object to a standard NDS object.
 * From UpdatePRSExemptionResponse to UpdateExemptionDetailsNdsResponse.
 */
@Converter
@DoNotWeaveLoggingSystem

public class ToUpdatePrsExemptionNdsResponseConverter {

    @Converter
    public static UpdateExemptionDetailsNdsResponse converting(            
            UpdatePRSExemptionResponse updatePrsExemptionResponse) {
        
        UpdateExemptionDetailsNdsResponse response = new UpdateExemptionDetailsNdsResponse();
        response.setSuccess(updatePrsExemptionResponse.isSuccess());

        ToNdsMessagesConverter.SetNdsMessages(response,
                updatePrsExemptionResponse.getMessages());
        
        return response;
    }
}
