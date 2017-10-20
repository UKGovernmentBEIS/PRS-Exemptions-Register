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
 * Model to contain the search terms for penalty search
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExemptionSearch", propOrder = { "penaltyPostcodesCriteria", "penaltyLandlordsNameCriteria", "rentalPropertyDetails" ,"town",
        "propertyType","penaltyType_PRSD","penaltyType_PRSN","languageCode","panelDisplayed"})
public class PenaltySearch extends AbstractValidatableModel{
    
    @StringLengthFieldMetadata(minLength =2,invalidMessage = "Validation_Field_must_be_a_valid_publicenquirysearch",validator = PublicEnquirySearchValidator.class,dependencies = {
            @FieldDependency(path = "action", values = { "FindPenalties" }, comparator = "contains") })
    private String penaltyPostcodesCriteria;
    
    @StringLengthFieldMetadata(minLength =2,invalidMessage = "Validation_Field_must_be_a_valid_publicenquirysearch",validator = PublicEnquirySearchValidator.class,dependencies = {
            @FieldDependency(path = "action", values = { "FindPenalties" }, comparator = "contains") })
    private String penaltyLandlordsNameCriteria;
    
    @StringLengthFieldMetadata(minLength =2,invalidMessage = "Validation_Field_must_be_a_valid_publicenquirysearch",validator = PublicEnquirySearchValidator.class,dependencies = {
            @FieldDependency(path = "action", values = { "FindPenalties" }, comparator = "contains") })
    private String rentalPropertyDetails;
    
    @StringLengthFieldMetadata(minLength =2,invalidMessage = "Validation_Field_must_be_a_valid_publicenquirysearch",validator = PublicEnquirySearchValidator.class,dependencies = {
            @FieldDependency(path = "action", values = { "FindPenalties" }, comparator = "contains") })
    private String town;
    
    @ViolationFieldMetadata(invalidMessage = "Validation_Field_must_be_supplied_property_type",dependencies = {
            @FieldDependency(path = "~.propertyType", values = { "ALL" }, comparator = "contains"),
            @FieldDependency(type = "anyOf", parts = {
                        @FieldDependencyPart(path = "~.penaltyType_PRSD", values = { "ALL" }, comparator = "notContains"),   
                        @FieldDependencyPart(path = "~.penaltyType_PRSN", values = { "ALL" }, comparator = "notContains")     
                                     }),
            @FieldDependency(path = "action", values = { "FindPenalties" }, comparator = "contains") 
            }) 
    private String propertyType = "ALL";
    
    @EnumeratedValues(name = "penaltyTypeList")
    @ViolationFieldMetadata(invalidMessage = "Validation_Field_must_be_one_more_search_term_penalty",dependencies = {
            @FieldDependency(path = "~.penaltyPostcodesCriteria",comparator = "empty"),
            @FieldDependency(path = "~.penaltyLandlordsNameCriteria",comparator = "empty"),
            @FieldDependency(path = "~.rentalPropertyDetails",comparator = "empty"),
            @FieldDependency(path = "~.town",comparator = "empty"),
            @FieldDependency(path = "~.propertyType", values = { "PRSD"}, comparator = "contains"),
            @FieldDependency(path = "~.penaltyType_PRSD", values = { "ALL" }, comparator = "contains"),
            @FieldDependency(type = "anyOf", parts = {
                    @FieldDependencyPart(path = "~.penaltyType_PRSN", values = { "ALL" }, comparator = "contains"),   
                    @FieldDependencyPart(path = "~.penaltyType_PRSN", values = { "ALL" }, comparator = "notContains")     
                                 }),
            @FieldDependency(path = "action", values = { "FindPenalties" }, comparator = "contains")}) 
    private String penaltyType_PRSD = "ALL";
    
    @EnumeratedValues(name = "penaltyTypeList")
    @ViolationFieldMetadata(invalidMessage = "Validation_Field_must_be_one_more_search_term_penalty",dependencies = {
            @FieldDependency(path = "~.penaltyPostcodesCriteria",comparator = "empty"),
            @FieldDependency(path = "~.penaltyLandlordsNameCriteria",comparator = "empty"),
            @FieldDependency(path = "~.rentalPropertyDetails",comparator = "empty"),
            @FieldDependency(path = "~.town",comparator = "empty"),
            @FieldDependency(path = "~.propertyType", values = { "PRSN"}, comparator = "contains"),
            @FieldDependency(path = "~.penaltyType_PRSN", values = { "ALL" }, comparator = "contains"),
            @FieldDependency(type = "anyOf", parts = {
                    @FieldDependencyPart(path = "~.penaltyType_PRSD", values = { "ALL" }, comparator = "contains"),   
                    @FieldDependencyPart(path = "~.penaltyType_PRSD", values = { "ALL" }, comparator = "notContains")}),  
            @FieldDependency(path = "action", values = { "FindPenalties" }, comparator = "contains")}) 
    private String penaltyType_PRSN = "ALL";
    
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


    public String getPenaltyPostcodesCriteria() {
        return penaltyPostcodesCriteria;
    }

    
    public void setPenaltyPostcodesCriteria(String penaltyPostcodesCriteria) {
        this.penaltyPostcodesCriteria = penaltyPostcodesCriteria;
    }

    
    public String getPenaltyLandlordsNameCriteria() {
        return penaltyLandlordsNameCriteria;
    }

    
    public void setPenaltyLandlordsNameCriteria(String penaltyLandlordsNameCriteria) {
        this.penaltyLandlordsNameCriteria = penaltyLandlordsNameCriteria;
    }

    
    public String getRentalPropertyDetails() {
        return rentalPropertyDetails;
    }

    
    public void setRentalPropertyDetails(String rentalPropertyDetails) {
        this.rentalPropertyDetails = rentalPropertyDetails;
    }

    
    public String getTown() {
        return town;
    }

    
    public void setTown(String town) {
        this.town = town;
    }

    
    public String getPropertyType() {
        return propertyType;
    }

    
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    
    public String getPenaltyType_PRSD() {
        return penaltyType_PRSD;
    }

    
    public void setPenaltyType_PRSD(String penaltyType_PRSD) {
        this.penaltyType_PRSD = penaltyType_PRSD;
    }

    
    public String getPenaltyType_PRSN() {
        return penaltyType_PRSN;
    }

    
    public void setPenaltyType_PRSN(String penaltyType_PRSN) {
        this.penaltyType_PRSN = penaltyType_PRSN;
    }
    
  //if any advance search is done then return true so that advance search fields are displayed
    public boolean getPenaltyAdvancedSearchDone() {
        if (!StringUtils.isEmpty(penaltyLandlordsNameCriteria) || !StringUtils.isEmpty(rentalPropertyDetails)
                || !StringUtils.isEmpty(town)
                || (!StringUtils.equals(propertyType, null) && !StringUtils.equals(propertyType, "ALL")
                        || (!StringUtils.equals(penaltyType_PRSD, null)
                                && !StringUtils.equals(penaltyType_PRSD, "ALL"))
                        || (!StringUtils.equals(penaltyType_PRSN, null)
                                && !StringUtils.equals(penaltyType_PRSN, "ALL")))) {
            return true;
        }

        return false;

    }
 }
