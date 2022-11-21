package com.northgateps.nds.beis.esb.process;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.northgateps.nds.beis.api.ExemptionDetail;
import com.northgateps.nds.beis.api.ExemptionDetailEsbModel;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsRequest;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsResponse;
import com.northgateps.nds.platform.application.api.validation.ValidationContext;
import com.northgateps.nds.platform.application.api.validation.ValidationDomain;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.esb.camel.ValidateCamelRouteBean;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/*
 * Validation class to ensure the RegisterPrsExemption request is valid
 */
public class ValidateRegisterPrsExemptionNdsRequest extends ValidateCamelRouteBean {

	private static final NdsLogger logger = NdsLogger.getLogger(ValidateRetrieveRegisteredUserDetails.class);

	/**
	 * Validates a RegisterPrsExemption request
	 * 
	 * @param request consists of data to be validated
	 * @return request - after validation
	 * @throws NdsApplicationException if the following expected data are missing: username, tenant and register
	 * 	 prs exemption details
	 * @throws NdsBusinessException if there's a violation found
	 */
	public RegisterPrsExemptionNdsRequest validate(final RegisterPrsExemptionNdsRequest request)
			throws NdsApplicationException, NdsBusinessException {

		List<ValidationViolation> violations = new ArrayList<ValidationViolation>();
		logger.info("Validating RegisterPrsExemption request");
		if (request.getUserName() == null) {
			throw new NdsApplicationException("Expected username but it was null");
		}
		if (request.getTenant() == null) {
			throw new NdsApplicationException("Expected tenant but it was null");
		}
		if (request.getRegisterPrsExemptionDetails() == null) {
			throw new NdsApplicationException("Expected PRS Exemption details but it was null");
		}

		// Code for validating Scottish and Northern Ireland postcodes while
		// registering an exemption
		ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();
		final String rejectedPostcodeList = configurationManager.getString("exemption.excludedPostcodes");

		Pattern pattern = Pattern.compile(rejectedPostcodeList);
		Matcher m = pattern.matcher(request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpc()
				.getPropertyAddress().getPostcode());
		if (m.find())
			violations.add(new ValidationViolation("postcode",
					"This address has a postcode that is not in England or Wales. You can only register an exemption for a property in England or Wales"));

		request.getRegisterPrsExemptionDetails().getExemptionDetails().validate("ExemptionDetails", violations,
				new ValidationContext<ExemptionDetailEsbModel>(new ExemptionDetailEsbModel() {

					@Override
					public ExemptionDetail getExemptionDetail() {
						return request.getRegisterPrsExemptionDetails().getExemptionDetails();
					}
				}, ValidationDomain.COMPLETE, scriptsLoader, getModelAnnotationsProvider(), null));

		if (violations.size() > 0) {
			throw new NdsBusinessException(RegisterPrsExemptionNdsResponse.class, violations);
		}

		return request;
	}

}
