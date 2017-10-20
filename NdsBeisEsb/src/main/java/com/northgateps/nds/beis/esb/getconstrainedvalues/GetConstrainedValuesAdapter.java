package com.northgateps.nds.beis.esb.getconstrainedvalues;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.northgateps.nds.beis.api.getconstrainedvalues.GetConstrainedValuesNdsRequest;
import com.northgateps.nds.beis.api.getconstrainedvalues.GetConstrainedValuesNdsResponse;
import com.northgateps.nds.platform.api.ConstrainedValue;
import com.northgateps.nds.platform.esb.adapter.NdsCsvAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxyImpl;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Processes the getting of constrained values between native (REST) objects and
 * CSV file.
 *
 */
// TODO CR:PE-176 - coupling here between the csv file and the response object
// is too tight. What happens if
// we want to get the list from a database or external service instead? There
// should be one
// class (adapter) to load thecsv into the constrainedValueList and another to
// gather up the
// children codes under their parent and package it into the REST response.
// Then, if we
// want to load the lists from a database or external service, we only need to
// change the former class.
// CR Action agreed with Code Reviewer: Can leave as is for now, eventually
// de-couple it. Perhaps by renaming the NdsCsvAdapter to NdsConstraintsAdapter.
// Abstracting the looping code from here, to loop TRecords in
// NdsConstraintsAdapter which produces generic
// array list of constraints to be used in this class here
public class GetConstrainedValuesAdapter extends
		NdsCsvAdapter<CsvConstrainedValueRecord, GetConstrainedValuesNdsRequest, GetConstrainedValuesNdsResponse> {

	@SuppressWarnings("unused")
	private static final NdsLogger logger = NdsLogger.getLogger(GetConstrainedValuesAdapter.class);

	@Override
	protected GetConstrainedValuesNdsResponse doResponseProcess(
			ArrayList<CsvConstrainedValueRecord> arrayListOfCsvRecords,
			NdsSoapRequestAdapterExchangeProxyImpl originalNdsExchange) {

		// outer String key is the listname - inner String key is the code
		TreeMap<String, TreeMap<String, ConstrainedValue>> constrainedValueTreeMap = new TreeMap<String, TreeMap<String, ConstrainedValue>>();

		for (CsvConstrainedValueRecord csvConstraintValueRecord : arrayListOfCsvRecords) {

			ConstrainedValue constrainedValue = new ConstrainedValue();

			constrainedValue.setCode(csvConstraintValueRecord.getCode());
			constrainedValue.setListName(csvConstraintValueRecord.getListName());
			constrainedValue.setMeaning(csvConstraintValueRecord.getMeaning());
			constrainedValue.setParentCode(csvConstraintValueRecord.getParentCode());
			constrainedValue.setParentListName(csvConstraintValueRecord.getParentListName());
			constrainedValue.setSortOrder(Integer.parseInt(csvConstraintValueRecord.getSortOrder()));

			// retrieve the listname
			TreeMap<String, ConstrainedValue> constrainedValueList = constrainedValueTreeMap
					.get(constrainedValue.getListName());

			// if listname not yet exists - create it
			if (constrainedValueList == null) {
				constrainedValueList = new TreeMap<String, ConstrainedValue>();
				constrainedValueTreeMap.put(constrainedValue.getListName(), constrainedValueList);
			}

			// add the cv to the listname
			constrainedValueList.put(constrainedValue.getCode(), constrainedValue);

		}

		// Converting inner TreeMap of cv's to an Array to send over JAXB
		TreeMap<String, ConstrainedValue[]> constrainedValueListMap = new TreeMap<String, ConstrainedValue[]>();

		for (Entry<String, TreeMap<String, ConstrainedValue>> entry : constrainedValueTreeMap.entrySet()) {
			constrainedValueListMap.put(entry.getKey(), entry.getValue().values().toArray(new ConstrainedValue[0]));
		}

		GetConstrainedValuesNdsResponse response = new GetConstrainedValuesNdsResponse();
		response.setConstrainedValues(constrainedValueListMap);
		response.setSuccess(true);
		return response;
	}

	@Override
	protected GetConstrainedValuesNdsResponse doFilterResponseProcess(GetConstrainedValuesNdsResponse ndsResponse,
			GetConstrainedValuesNdsRequest ndsRequest, NdsSoapRequestAdapterExchangeProxyImpl ndsExchange) {

		// keep the records that match the request criteria
		if (!ndsRequest.getRequestedListNames().isEmpty()) {

			TreeMap<String, ConstrainedValue[]> filteredList = new TreeMap<String, ConstrainedValue[]>();

			Iterator<Entry<String, ConstrainedValue[]>> iter = ndsResponse.getConstrainedValues().entrySet().iterator();
			Entry<String, ConstrainedValue[]> entry = null;

			while (iter.hasNext()) {
				entry = iter.next();
				if (ndsRequest.getRequestedListNames().contains(entry.getKey())) {
					filteredList.put(entry.getKey(), entry.getValue());
				}
			}
			ndsResponse.setConstrainedValues(filteredList);
		}

		return ndsResponse;
	}

	@Override
	protected String getOriginalCsvClassName() {
		return CsvConstrainedValueRecord.class.getName();
	}

	@Override
	protected String getNewResponseClassName() {
		return GetConstrainedValuesNdsResponse.class.getName();
	}

	@Override
	protected String getRequestClassName() {
		return GetConstrainedValuesNdsRequest.class.getName();
	}

	@Override
	protected String getSuffix(GetConstrainedValuesNdsRequest ndsRequest) {
		return "";
	}

}
