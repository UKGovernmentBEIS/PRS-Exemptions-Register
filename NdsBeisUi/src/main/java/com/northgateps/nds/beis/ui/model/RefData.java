package com.northgateps.nds.beis.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.northgateps.nds.beis.api.ExemptionDetail;
import com.northgateps.nds.beis.ui.field.ConstrainedValuesComponent;
import com.northgateps.nds.beis.ui.field.ExemptionTypeTextComponent;
import com.northgateps.nds.beis.ui.field.PenaltyTypeTextComponent;
import com.northgateps.nds.platform.ui.field.EnumeratedValuesBinding;
import com.northgateps.nds.platform.ui.model.NdsRefData;
import com.northgateps.nds.platform.ui.model.ReferenceValue;
import com.northgateps.nds.platform.ui.tags.form.LabelValuePair;

/**
 * A container for retaining reference data in the UI, persisted by round
 * tripping to the client
 */
public class RefData extends NdsRefData implements Serializable {

    @EnumeratedValuesBinding(name = "beisCountryList", listName = "COUNTRY", loader = ConstrainedValuesComponent.class)
    public transient List<ReferenceValue> country;

    @EnumeratedValuesBinding(name = ExemptionDetail.EXEMPTION_TYPE_TEXT, listName = "exemptionTypeText", loader = ExemptionTypeTextComponent.class)
    public transient List<ExemptionTypeText> exemptionTypeText;
    
    @EnumeratedValuesBinding(name = "exemptionTypeTextDomestic", listName = "exemptionTypeTextDomestic", loader = ExemptionTypeTextComponent.class)
    public transient List<ExemptionTypeText> exemptionTypeTextDomestic;

    @EnumeratedValuesBinding(name = "penaltyTypeList", listName = "penaltyTypeText", loader = PenaltyTypeTextComponent.class)
    public transient List<PenaltyTypeText> penaltyTypeText;
    
    List<ReferenceValue> returnList = new ArrayList<ReferenceValue>();
    
    List<ReferenceValue> domesticList = new ArrayList<ReferenceValue>();
    
    List<ReferenceValue> nonDomesticList = new ArrayList<ReferenceValue>();
    
    public List<LabelValuePair> yesNoValues;
    public List<LabelValuePair> accountType;
    public List<LabelValuePair> propertyType;
    public List<LabelValuePair> userType;    
    
    public List<LabelValuePair> getUserType() {
        return userType;
    }
    
    public void setUserType(List<LabelValuePair> userType) {
        this.userType = userType;
    }


    public List<ReferenceValue> getReturnList() {
        return returnList;
    }

    
    public void setReturnList(List<ReferenceValue> returnList) {
        this.returnList = returnList;
    }

    
    public List<ReferenceValue> getDomesticList() {
        return domesticList;
    }

    
    public void setDomesticList(List<ReferenceValue> domesticList) {
        this.domesticList = domesticList;
    }

    
    public List<ReferenceValue> getNonDomesticList() {
        return nonDomesticList;
    }

    
    public void setNonDomesticList(List<ReferenceValue> nonDomesticList) {
        this.nonDomesticList = nonDomesticList;
    }

    
    public List<LabelValuePair> getYesNoValues() {
        return yesNoValues;
    }

    
    public void setYesNoValues(List<LabelValuePair> yesNoValues) {
        this.yesNoValues = yesNoValues;
    }

    
    public List<LabelValuePair> getAccountType() {
        return accountType;
    }

    
    public void setAccountType(List<LabelValuePair> accountType) {
        this.accountType = accountType;
    }

    
    public List<LabelValuePair> getPropertyType() {
        return propertyType;
    }

    
    public void setPropertyType(List<LabelValuePair> propertyType) {
        this.propertyType = propertyType;
    }

    
    public RefData() {
        yesNoValues = new ArrayList<LabelValuePair>();
        yesNoValues.add(new LabelValuePair("Label_UsedServiceBefore.Yes", "true"));
        yesNoValues.add(new LabelValuePair("Label_UsedServiceBefore.No", "false"));
        
        
        accountType = new ArrayList<LabelValuePair>();
        accountType.add(new LabelValuePair("Label_exemptionDetails.landlordDetails.accountType.ORGANISATION", "ORGANISATION"));
        accountType.add(new LabelValuePair("Label_exemptionDetails.landlordDetails.accountType.PERSON", "PERSON"));
        
        userType = new ArrayList<LabelValuePair>();
        userType.add(new LabelValuePair("Label_beisRegistrationDetails.userDetails.userType.LANDLORD", "LANDLORD"));
        userType.add(new LabelValuePair("Label_beisRegistrationDetails.userDetails.userType.AGENT", "AGENT"));
        
        propertyType = new ArrayList<LabelValuePair>();
        propertyType.add(new LabelValuePair("Label_exemptionDetails.propertyType.domestic", "PRSD"));
        propertyType.add(new LabelValuePair("Label_exemptionDetails.propertyType.nondomestic", "PRSN"));
    }
    
    public  List<PenaltyTypeText> getPenaltyTypeText() {
        return penaltyTypeText;
    }
    
    public void setPenaltyTypeText(List<PenaltyTypeText> penaltyTypeText) {
        this.penaltyTypeText = penaltyTypeText;
    }

    public List<ReferenceValue> getCountry() {
        return country;
    }

    public void setCountry(List<ReferenceValue> country) {
        this.country = country;
    }

    public List<ExemptionTypeText> getExemptionTypeText() {
        return exemptionTypeText;
    }

    public void setExemptionTypeText(List<ExemptionTypeText> exemptionTypeText) {
        this.exemptionTypeText = exemptionTypeText;
    }    
    
    public List<ExemptionTypeText> getExemptionTypeTextDomestic() {
        return exemptionTypeTextDomestic;
    }
    
    public void setExemptionTypeTextDomestic(List<ExemptionTypeText> exemptionTypeTextDomestic) {
        this.exemptionTypeTextDomestic = exemptionTypeTextDomestic;
    }
    
    public List<ReferenceValue> getDomesticExemptions() {       
        ReferenceValue domestic = new ReferenceValue();
        domestic.setCode("ALL");
        domestic.setName("All types");
        domesticList.add(domestic);
        for (ExemptionTypeText text : exemptionTypeTextDomestic) {
           if (text.getService().compareTo("PRSD") == 0) {
               domestic = new ReferenceValue(); 
                domestic.setCode(text.getCode());
                domestic.setName(text.getDescription());
                // add to the list to return
                domesticList.add(domestic);
            }
        }
        return domesticList;
    }
    
    
    public List<ReferenceValue> getNonDomesticExemptions() {        
        ReferenceValue nonDomestic = new ReferenceValue();
        nonDomestic.setCode("ALL");
        nonDomestic.setName("All types");
        nonDomesticList.add(nonDomestic);
        for (ExemptionTypeText text : exemptionTypeText) {
           if (text.getService().compareTo("PRSN") == 0) {
               nonDomestic = new ReferenceValue(); 
               nonDomestic.setCode(text.getCode());
               nonDomestic.setName(text.getDescription());
                // add to the list to return
                nonDomesticList.add(nonDomestic);
            }
        }
        return nonDomesticList;
    }
    
    
    
    public List<ReferenceValue> getPenaltyTypeText(String type) {
        List<ReferenceValue> returnList = new ArrayList<ReferenceValue>();
        ReferenceValue e = new ReferenceValue();
        e.setCode("ALL");
        e.setName("All types");
        returnList.add(e);
        for (PenaltyTypeText text : penaltyTypeText) {
           if (text.getService().compareTo(type) == 0) {
               e = new ReferenceValue(); 
                e.setCode(text.getCode());
                e.setName(text.getDescription());
                // add to the list to return
                returnList.add(e);
            }
        }
        return returnList;
    }   
    
    public List<ReferenceValue> getServiceType() {
        setServicevalues("ALL","All properties");
        setServicevalues("PRSD","Domestic");
        setServicevalues("PRSN","Non-domestic");
        return returnList;
    }
    
    private void setServicevalues(String code,String description)
    {
        ReferenceValue e = new ReferenceValue();
        e = new ReferenceValue(); 
        e.setCode(code);
        e.setName(description);
        returnList.add(e);
    }
}