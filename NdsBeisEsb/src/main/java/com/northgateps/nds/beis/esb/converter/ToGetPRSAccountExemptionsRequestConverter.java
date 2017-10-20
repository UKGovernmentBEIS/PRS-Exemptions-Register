package com.northgateps.nds.beis.esb.converter;

import java.math.BigInteger;

import org.apache.camel.Converter;
import org.jsoup.helper.StringUtil;

import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsRequest;
import com.northgateps.nds.beis.backoffice.service.getprsaccountexemptions.GetPRSAccountExemptionsRequest;
import com.northgateps.nds.platform.esb.converter.StringToBigIntegerConverter;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * converter for BEIS foundation layer account exemptions request which can convert
 * from different request types.
 */
@Converter
@DoNotWeaveLoggingSystem
public final class ToGetPRSAccountExemptionsRequestConverter {

	private static final NdsLogger logger = NdsLogger.getLogger(ToGetPRSAccountExemptionsRequestConverter.class);
	
	/**
	 * @param request
	 *            - This is the NDS request that needs transforming for BEIS
	 *            foundation layer to understand.
	 * @return - the provided details converted to a
	 *         GetPRSAccountExemptionsRequest object ready to send to the
	 *         external service.
	 */
	@Converter
	public static GetPRSAccountExemptionsRequest converting(GetPRSAccountExemptionsNdsRequest request) {

		logger.info("Starting Conversion For getting PRS account exmeption request");

		//do the conversion
		GetPRSAccountExemptionsRequest backOfficeRequest = new GetPRSAccountExemptionsRequest();	
		if(request.getUserType() == UserType.LANDLORD){
		    backOfficeRequest.setLandlordPartyRef( new BigInteger(request.getAccountId()));
		}else{
		    backOfficeRequest.setAgentPartyRef( new BigInteger(request.getAccountId()));
		}		     
		
		if(!StringUtil.isBlank(request.getExemptionRefNo()) ){
            backOfficeRequest.setExemptionRefNo(StringToBigIntegerConverter.convert(request.getExemptionRefNo())); 
        }
		logger.info("Completed Conversion For getting PRS account exmeption request");
        
		return backOfficeRequest;
	}
}
