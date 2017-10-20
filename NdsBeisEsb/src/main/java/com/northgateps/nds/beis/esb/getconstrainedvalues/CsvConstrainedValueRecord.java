package com.northgateps.nds.beis.esb.getconstrainedvalues;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

/**
 * A template class to hold Constrained values read from a CSV file
 *
 */
@CsvRecord(separator = ",", skipFirstLine = true)
public class CsvConstrainedValueRecord {

	@DataField(pos = 1)
	private String parentCode;

	@DataField(pos = 2)
	private String code;

	@DataField(pos = 3)
	private String meaning;

	@DataField(pos = 4)
	private String sortOrder;

	@DataField(pos = 5)
	private String listName;

	@DataField(pos = 6)
	private String parentListName;

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getParentListName() {
		return parentListName;
	}

	public void setParentListName(String parentListName) {
		this.parentListName = parentListName;
	}

}