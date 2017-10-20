package com.northgateps.nds.beis.ui.controller.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.FieldError;

import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.controller.NdsAction;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;

/**
 * Handler to reject Scottish and Northern Ireland postcodes while registering an exemption
 */
public class ExemptionPostcodeValidationEventHandler extends AbstractViewEventHandler {
	
	@Override
	public void onAfterValidation(NdsFormModel allModel, final ControllerState<?> controllerState) {
		final BeisAllModel model = (BeisAllModel) allModel;
		
		if (model != null &&
			model.getExemptionDetails() != null &&
			model.getExemptionDetails().getEpc() != null &&
			model.getExemptionDetails().getEpc().getPropertyAddress() != null ) {
		  
		    model.getExemptionDetails().getEpc().getPropertyAddress().setCountry("GB");
		    final String rejectedPostcodeList = ConfigurationFactory.getConfiguration().getString("exemption.excludedPostcodes");		
			Pattern pattern = Pattern.compile(rejectedPostcodeList);				
			Matcher m = pattern.matcher(model.getExemptionDetails().getEpc().getPropertyAddress().getPostcode());
	
			if (m.find()) {
	
				FieldError error = new FieldError("exemptionDetails.epc.propertyAddress",
						"exemptionDetails.epc.propertyAddress.postcode", model.getExemptionDetails().getEpc().getPropertyAddress().getPostcode(), false, null, new Object[0],
						"Validation_Field_must_not_be_outside_england_wales");
				controllerState.getBindingResult().addError(error);
				model.setAction(NdsAction.NONE);
	
				return;
			}
		}
	}
}
