package com.northgateps.nds.beis.esb.dashboard;

import org.apache.camel.TypeConverter;
import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsRequest;
import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsResponse;
import com.northgateps.nds.beis.backoffice.service.getprsaccountexemptions.GetPRSAccountExemptionsRequest;
import com.northgateps.nds.beis.backoffice.service.getprsaccountexemptions.GetPRSAccountExemptionsResponse;
import com.northgateps.nds.beis.esb.registration.RetrieveRegisteredDetailsSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;
/**
 * Class to call backoffice to retrieve account exemptions
 */
public class GetPrsAccountExemptionsAdapter extends
		NdsSoapAdapter<GetPRSAccountExemptionsNdsRequest, GetPRSAccountExemptionsNdsResponse, GetPRSAccountExemptionsRequest, GetPRSAccountExemptionsResponse> {

	protected static final NdsLogger logger = NdsLogger.getLogger(GetPrsAccountExemptionsAdapter.class);
	
	/**
	 * Called from the base-class to de-serialize the incoming request.
	 * 
	 * @See http://stackoverflow.com/questions/3403909/get-generic-type-of-class
	 *      -at-runtime
	 */
	@Override
	protected String getRequestClassName() {
		return GetPRSAccountExemptionsNdsRequest.class.getName();
	}

	@Override
	protected GetPRSAccountExemptionsRequest doRequestProcess(GetPRSAccountExemptionsNdsRequest request,
			NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {

		GetPRSAccountExemptionsRequest backOfficeRequest = null;

		try {
			ndsExchange.setOperationName("GetPRSAccountExemptionsWSDL");

			request.setUserType((UserType) ndsExchange.getAnExchangeProperty(RetrieveRegisteredDetailsSoapAdapter.USER_TYPE));   

            // Create the type converter
			TypeConverter converter = ndsExchange.getTypeConverter();
			backOfficeRequest = converter.convertTo(GetPRSAccountExemptionsRequest.class, request);
		} catch (Exception e) {
			throw new NdsApplicationException("Error occured during request conversion process:  " + e.getMessage(), e);
		}

		logger.info("Successfully created the SOAP request");		

		return backOfficeRequest;
	}

	@Override
	protected GetPRSAccountExemptionsNdsResponse doResponseProcess(GetPRSAccountExemptionsResponse externalResponse,
			NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
		
		GetPRSAccountExemptionsNdsResponse response = new GetPRSAccountExemptionsNdsResponse();
		
		try {
			
			// Create the type converter
			TypeConverter converter = ndsExchange.getTypeConverter();
			response = converter.convertTo(GetPRSAccountExemptionsNdsResponse.class, externalResponse);
			response.setUserType((UserType) ndsExchange.getAnExchangeProperty(RetrieveRegisteredDetailsSoapAdapter.USER_TYPE));
		} catch (Exception e) {
			throw new NdsApplicationException("Error occured during response conversion process:  " + e.getMessage(), e);
		}

		logger.info("Successfully converted the SOAP response");
		
		response.setSuccess(true);
		
		return response;
	}

	@Override
	protected String getResponseClassName() {
		return GetPRSAccountExemptionsResponse.class.getName();
	}

}
