package com.northgateps.nds.beis.esb.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.RegisteredExemptionDetail;
import com.northgateps.nds.beis.api.dashboard.DashboardDetails;
import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsResponse;
import com.northgateps.nds.beis.backoffice.service.getprsaccountexemptions.GetPRSAccountExemptionsResponse;
import com.northgateps.nds.beis.backoffice.service.getprsaccountexemptions.GetPRSAccountExemptionsResponse.Exemptions.Exemption;
import com.northgateps.nds.platform.esb.converter.XMLGregorianCalendarToZonedDateTimeConverter;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converter class to convert an external back office object to a standard NDS object.
 * From GetPRSAccountExemptionsResponse to GetPRSAccountExemptionsNdsResponse.
 */
@Converter
@DoNotWeaveLoggingSystem
public final class ToGetPRSAccountExemptionsNdsResponseConverter {
    
	private static final NdsLogger logger = NdsLogger.getLogger(ToGetPRSAccountExemptionsNdsResponseConverter.class);

	/**
	 * Conversion process
	 * @param response - the back office response to be converted to a standard NDS object
	 * @return - the dashboard nds response of class GetPRSAccountExemptionsNdsResponse
	 */
	@Converter
	public static GetPRSAccountExemptionsNdsResponse converting(GetPRSAccountExemptionsResponse response) {

		logger.info("Starting Conversion For getting GetPRSAccountExemptionsNdsResponse");

		//setup the response ready for data
		GetPRSAccountExemptionsNdsResponse dashboardResponse = new GetPRSAccountExemptionsNdsResponse();
		dashboardResponse.setDashboardDetails(new DashboardDetails());		
		List<RegisteredExemptionDetail> registeredExemptions = new ArrayList<RegisteredExemptionDetail>();		
		dashboardResponse.getDashboardDetails().setExemptionDetailList(registeredExemptions);
		
		//convert the exemptions
		if( response.getExemptions() != null ){
			List<Exemption> exemptions = response.getExemptions().getExemption();
			
			for( Exemption exemption : exemptions){
				
				RegisteredExemptionDetail registeredExemption = new RegisteredExemptionDetail();
                
				registeredExemption.setLandlord(exemption.getLandlord());
				registeredExemption.setExemptionReference(exemption.getExemptionReference());
                registeredExemption.setReferenceId(exemption.getExemptionRefNo() == null ? null :exemption.getExemptionRefNo().toString());
                registeredExemption.setDescription(exemption.getPWSDescription());
                registeredExemption.setStartDate(XMLGregorianCalendarToZonedDateTimeConverter.convert(exemption.getRegisteredDate()));
                registeredExemption.setEndDate(XMLGregorianCalendarToZonedDateTimeConverter.convert(exemption.getEndDate()));
                registeredExemption.setAddress(exemption.getAddress());				
                registeredExemptions.add(registeredExemption);
			}
		}
				
		return dashboardResponse;
	}
	
	
}