package com.northgateps.nds.beis.esb.converter;

import org.apache.camel.Converter;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsResponse;
import com.northgateps.nds.beis.backoffice.service.registerprsexemption.RegisterPRSExemptionResponse;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;


/**
 * Convert the passed in RegisterPRSExemptionResponse to the passed out RegisterPrsExemptionNdsResponse
 * 
 * @param com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsResponse
 * @return class com.northgateps.nds.beis.backoffice.service.registerprsexemption.RegisterPRSExemptionResponse
 * 
*/
@Converter
@DoNotWeaveLoggingSystem
public class ToRegisterPRSExemptionNdsResponseConverter {
    private static final NdsLogger logger = NdsLogger.getLogger(ToRegisterPRSExemptionNdsResponseConverter.class);

    @Converter    
    public static RegisterPrsExemptionNdsResponse converting(
            RegisterPRSExemptionResponse registerPRSExemptionResponse) {
        logger.info("Conversion of back office response object into NDS response object started.");
        
        RegisterPrsExemptionNdsResponse registerPrsExemptionNdsResponse = new RegisterPrsExemptionNdsResponse();
        registerPrsExemptionNdsResponse.setSuccess(registerPRSExemptionResponse.isSuccess());

        ToNdsMessagesConverter.SetNdsMessages(registerPrsExemptionNdsResponse,
                registerPRSExemptionResponse.getMessages());
        
        logger.info("Conversion of back office response object into NDS response object completed.");
        return registerPrsExemptionNdsResponse;
    }
}
