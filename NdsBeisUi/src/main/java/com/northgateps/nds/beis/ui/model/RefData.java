package com.northgateps.nds.beis.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.northgateps.nds.beis.api.ExemptionDetail;
import com.northgateps.nds.beis.ui.field.ConstrainedValuesComponent;
import com.northgateps.nds.beis.ui.field.ExemptionTypeTextComponent;
import com.northgateps.nds.platform.ui.field.EnumeratedValuesBinding;
import com.northgateps.nds.platform.ui.model.NdsRefData;
import com.northgateps.nds.platform.ui.model.ReferenceValue;
import com.northgateps.nds.beis.ui.field.PenaltyTypeTextComponent;

/**
 * A container for retaining reference data in the UI, persisted by round
 * tripping to the client
 */
public class RefData extends NdsRefData implements Serializable {

    @EnumeratedValuesBinding(name = "beisCountryList", listName = "COUNTRY", loader = ConstrainedValuesComponent.class)
    public transient List<ReferenceValue> country;

    @EnumeratedValuesBinding(name = ExemptionDetail.EXEMPTION_TYPE_TEXT, listName = "exemptionTypeText", loader = ExemptionTypeTextComponent.class)
    public transient List<ExemptionTypeText> exemptionTypeText;
    
    @EnumeratedValuesBinding(name = "penaltyTypeList", listName = "penaltyTypeText", loader = PenaltyTypeTextComponent.class)
    public transient List<PenaltyTypeText> penaltyTypeText;
    
    List<ReferenceValue> returnList = new ArrayList<ReferenceValue>();
    
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
    
    public List<ReferenceValue> getExemptionTypeText(String type) {
        List<ReferenceValue> returnList = new ArrayList<ReferenceValue>();
        ReferenceValue e = new ReferenceValue();
        e.setCode("ALL");
        e.setName("All types");
        returnList.add(e);
        for (ExemptionTypeText text : exemptionTypeText) {
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