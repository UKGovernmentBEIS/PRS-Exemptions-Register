package com.northgateps.nds.beis.esb.dashboard;

import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsRequest;
import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsResponse;
import com.northgateps.nds.beis.esb.registration.RetrieveAccountIdLdapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Class to be used to fetch party id from Ldap, derives from the base class
 * that does all the work
 * 
 */
public class GetPrsAccountExemptionsLookupIdAdapter extends RetrieveAccountIdLdapAdapter<GetPRSAccountExemptionsNdsRequest, GetPRSAccountExemptionsNdsResponse> {

	@SuppressWarnings("unused")
	private static final NdsLogger logger = NdsLogger.getLogger(GetPrsAccountExemptionsLookupIdAdapter.class);

	@Override
	protected String getUsername(GetPRSAccountExemptionsNdsRequest request) {
		return request.getUsername();
	}

	@Override
	protected String getTenant(GetPRSAccountExemptionsNdsRequest request) {
		return request.getTenant();
	}

	@Override
	protected void setAccountId(GetPRSAccountExemptionsNdsRequest request, String accountId) {
		request.setAccountId(accountId);
	}

	@Override
	protected GetPRSAccountExemptionsNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
			throws NdsApplicationException {
	    
		GetPRSAccountExemptionsNdsResponse response = new GetPRSAccountExemptionsNdsResponse();

		response.setSuccess(true);

		return response;

	}

	/**
	 * Called from the base-class to de-serialize the incoming request.
	 * 
	 * See this:
	 * http://stackoverflow.com/questions/3403909/get-generic-type-of-class
	 * -at-runtime
	 */
	@Override
	protected String getRequestClassName() {
		return GetPRSAccountExemptionsNdsRequest.class.getName();
	}

}