package com.northgateps.nds.beis.api;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.StringUtils;

import com.northgateps.nds.beis.api.validation.js.PublicEnquirySearchValidator;
import com.northgateps.nds.platform.application.api.depend.FieldDependency;
import com.northgateps.nds.platform.application.api.depend.FieldDependencyPart;
import com.northgateps.nds.platform.application.api.metadata.EnumeratedValues;
import com.northgateps.nds.platform.application.api.metadata.StringLengthFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.ViolationFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

/**
 * 
 * Model to contain the search terms for exemption search
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExemptionSearch", propOrder = { "exemptionPostcodeCriteria", "exemptionLandlordsNameCriteria", "exemptPropertyDetails" ,"town",
        "exemptionType_PRSD","exemptionType_PRSN","service","epcRequired","exemptionRefNo","languageCode" ,"panelDisplayed"})
public class ExemptionSearch extends AbstractValidatableModel {
    public final static String EXEMPTION_TYPE_TEXT = "exemptionTypeText";
    
    @StringLengthFieldMetadata(minLength =2,invalidMessage = "Validation_Field_must_be_a_valid_publicenquirysearch",validator = PublicEnquirySearchValidator.class,dependencies = {
            @FieldDependency(path = "action", values = { "FindExemptions" }, comparator = "contains") })
    private String exemptionPostcodeCriteria;
    
    @StringLengthFieldMetadata(minLength =2,invalidMessage = "Validation_Field_must_be_a_valid_publicenquirysearch",validator = PublicEnquirySearchValidator.class,dependencies = {
            @FieldDependency(path = "action", values = { "FindExemptions" }, comparator = "contains") })
    private String exemptionLandlordsNameCriteria;
    
    public String getExemptionPostcodeCriteria() {
        return exemptionPostcodeCriteria;
    }
    
    @StringLengthFieldMetadata(minLength =2,invalidMessage = "Validation_Field_must_be_a_valid_publicenquirysearch",validator = PublicEnquirySearchValidator.class,dependencies = {
            @FieldDependency(path = "action", values = { "FindExemptions" }, comparator = "contains") })
    private String exemptPropertyDetails;
    
    @StringLengthFieldMetadata(minLength =2,invalidMessage = "Validation_Field_must_be_a_valid_publicenquirysearch",validator = PublicEnquirySearchValidator.class,dependencies = {
            @FieldDependency(path = "action", values = { "FindExemptions" }, comparator = "contains") })
    private String town;
    
    
    @EnumeratedValues(name = "exemptionTypeTextDomestic")
    @ViolationFieldMetadata(invalidMessage = "Validation_Field_must_be_one_more_search_term",dependencies = {
            @FieldDependency(path = "~.exemptionPostcodeCriteria",comparator = "empty"),
            @FieldDependency(path = "~.exemptionLandlordsNameCriteria",comparator = "empty"),
            @FieldDependency(path = "~.exemptPropertyDetails",comparator = "empty"),
            @FieldDependency(path = "~.town",comparator = "empty"),
            @FieldDependency(path = "~.service", values = { "PRSD"}, comparator = "contains"),
            @FieldDependency(path = "~.exemptionType_PRSD", values = { "ALL" }, comparator = "contains"),
            @FieldDependency(type = "anyOf", parts = {
                    @FieldDependencyPart(path = "~.exemptionType_PRSN", values = { "ALL" }, comparator = "contains"),   
                    @FieldDependencyPart(path = "~.exemptionType_PRSN", values = { "ALL" }, comparator = "notContains")     
                                 }),
            @FieldDependency(path = "action", values = { "FindExemptions" }, comparator = "contains")}) 
    private String exemptionType_PRSD = "ALL";
    
    @EnumeratedValues(name = EXEMPTION_TYPE_TEXT)
    @ViolationFieldMetadata(invalidMessage = "Validation_Field_must_be_one_more_search_term",dependencies = {
            @FieldDependency(path = "~.exemptionPostcodeCriteria",comparator = "empty"),
            @FieldDependency(path = "~.exemptionLandlordsNameCriteria",comparator = "empty"),
            @FieldDependency(path = "~.exemptPropertyDetails",comparator = "empty"),
            @FieldDependency(path = "~.town",comparator = "empty"),
            @FieldDependency(path = "~.service", values = { "PRSN"}, comparator = "contains"),
            @FieldDependency(path = "~.exemptionType_PRSN", values = { "ALL" }, comparator = "contains"),
            @FieldDependency(type = "anyOf", parts = {
                    @FieldDependencyPart(path = "~.exemptionType_PRSD", values = { "ALL" }, comparator = "contains"),   
                    @FieldDependencyPart(path = "~.exemptionType_PRSD", values = { "ALL" }, comparator = "notContains")}),  
            @FieldDependency(path = "action", values = { "FindExemptions" }, comparator = "contains")}) 
    private String exemptionType_PRSN = "ALL";
    
    /*It says if the service is set to ALL and if any of the exemption type is selected i.e does not contain ALL then show violation
    that property type must be supplied*/
    @ViolationFieldMetadata(invalidMessage = "Validation_Field_must_be_supplied_property_type",dependencies = {
            @FieldDependency(path = "~.service", values = { "ALL" }, comparator = "contains"),
            @FieldDependency(type = "anyOf", parts = {
                        @FieldDependencyPart(path = "~.exemptionType_PRSD", values = { "ALL" }, comparator = "notContains"),   
                        @FieldDependencyPart(path = "~.exemptionType_PRSN", values = { "ALL" }, comparator = "notContains")     
                                     }),
            @FieldDependency(path = "action", values = { "FindExemptions" }, comparator = "contains")}) 
    private String service = "ALL";
    
    private boolean epcRequired;
    
    private String exemptionRefNo;
    
    private String languageCode;
    
    private boolean panelDisplayed;

    
    
    public boolean isPanelDisplayed() {
        return panelDisplayed;
    }


    
    public void setPanelDisplayed(boolean panelDisplayed) {
        this.panelDisplayed = panelDisplayed;
    }


    public String getLanguageCode() {
        return languageCode;
    }


    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }



    public String getExemptionRefNo() {
        return exemptionRefNo;
    }


    public void setExemptionRefNo(String exemptionRefNo) {
        this.exemptionRefNo = exemptionRefNo;
    }


    public boolean isEpcRequired() {
        return epcRequired;
    }

    
    public void setEpcRequired(boolean epcRequired) {
        this.epcRequired = epcRequired;
    }

    public void setExemptionPostcodeCriteria(String exemptionPostcodeCriteria) {
        this.exemptionPostcodeCriteria = exemptionPostcodeCriteria;
    }
    
    public String getExemptionLandlordsNameCriteria() {
        return exemptionLandlordsNameCriteria;
    }

    
    public void setExemptionLandlordsNameCriteria(String exemptionLandlordsNameCriteria) {
        this.exemptionLandlordsNameCriteria = exemptionLandlordsNameCriteria;
    }
    
    public String getExemptionType_PRSD() {
        return exemptionType_PRSD;
    }

    public void setExemptionType_PRSD(String exemptionType_PRSD) {
        this.exemptionType_PRSD = exemptionType_PRSD;
    }

    public String getExemptionType_PRSN() {
        return exemptionType_PRSN;
    }

    public void setExemptionType_PRSN(String exemptionType_PRSN) {
        this.exemptionType_PRSN = exemptionType_PRSN;
    }

    public static String getExemptionTypeText() {
        return EXEMPTION_TYPE_TEXT;
    }
    
    public String getExemptPropertyDetails() {
        return exemptPropertyDetails;
    }

    
    public void setExemptPropertyDetails(String exemptPropertyDetails) {
        this.exemptPropertyDetails = exemptPropertyDetails;
    }
    
    public String getTown() {
        return town;
    }

    
    public void setTown(String town) {
        this.town = town;
    }
    

    public String getService() {
        return service;
    }

    
    public void setService(String service) {
        this.service = service;
    }
    
   
   //if any advance search is done then return true so that advance search fields are displayed
    public boolean getExemptionAdvancedSearchDone() {
        if (!StringUtils.isEmpty(exemptionLandlordsNameCriteria) || !StringUtils.isEmpty(exemptPropertyDetails)
                || !StringUtils.isEmpty(town)
                || (!StringUtils.equals(service, null) && !StringUtils.equals(service, "ALL")
                        || (!StringUtils.equals(exemptionType_PRSD, null)
                                && !StringUtils.equals(exemptionType_PRSD, "ALL"))
                        || (!StringUtils.equals(exemptionType_PRSN, null)
                                && !StringUtils.equals(exemptionType_PRSN, "ALL")))) {
            return true;
        }

        return false;

    }

}
