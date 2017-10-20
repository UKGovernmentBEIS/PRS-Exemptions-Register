package com.northgateps.nds.beis.api.dashboard;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


import com.northgateps.nds.beis.api.RegisteredExemptionDetail;

/**
 * Registered exemption details to be displayed on the dashboard
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DashboardDetails", propOrder = { "exemptionDetailList" })
public class DashboardDetails implements Serializable {
	private List<RegisteredExemptionDetail> exemptionDetailList;

	public List<RegisteredExemptionDetail> getExemptionDetailList() {
		return exemptionDetailList;
	}

	public void setExemptionDetailList(List<RegisteredExemptionDetail> exemptionDetailList) {
		this.exemptionDetailList = exemptionDetailList;
	}
	
	enum ExemptionType{
		Current,
		Expired
	}
	
	private ArrayList<RegisteredExemptionDetail> getExemptions(ExemptionType exemptionType){
		
		ArrayList<RegisteredExemptionDetail> exemptions = new ArrayList<RegisteredExemptionDetail>();
		
		if( exemptionDetailList != null){
			for( int i=0; i < exemptionDetailList.size(); i++ ){
				
				switch( exemptionType){
					case Current:
				
						if( exemptionDetailList.get(i).getEndDate() == null ||
								exemptionDetailList.get(i).getEndDate().compareTo(ZonedDateTime.now()) > 0 ){
							exemptions.add(exemptionDetailList.get(i));
						}
						break;
						
					case Expired:
						
						if( exemptionDetailList.get(i).getEndDate() != null &&
								exemptionDetailList.get(i).getEndDate().compareTo(ZonedDateTime.now()) <= 0 ){
							exemptions.add(exemptionDetailList.get(i));
						}
						
						break;
				}
			}
		}
		
		return exemptions;
	}
	
	public ArrayList<RegisteredExemptionDetail> getCurrentExemptions(){
		return getExemptions(ExemptionType.Current);
	}

	public ArrayList<RegisteredExemptionDetail> getExpiredExemptions(){
		return getExemptions(ExemptionType.Expired);
	}
	
	public int getCurrentExemptionCount(){
		return getCurrentExemptions().size();
	}
	
	public int getExpiredExemptionCount(){
		return getExpiredExemptions().size();
	}
}
