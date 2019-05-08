package com.northgateps.nds.beis.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.northgateps.nds.beis.api.validation.js.EPCUploadFieldValidator;
import com.northgateps.nds.beis.api.validation.js.ExemptionUploadFieldValidator;
import com.northgateps.nds.beis.api.validation.js.StartDateValidator;
import com.northgateps.nds.platform.api.Upload;
import com.northgateps.nds.platform.application.api.depend.FieldDependency;
import com.northgateps.nds.platform.application.api.metadata.EnumeratedValues;
import com.northgateps.nds.platform.application.api.metadata.GenericFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.PastDateFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.StringLengthFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;
import com.northgateps.nds.platform.application.api.format.js.PreserveFieldFormatter;

/**
 * Class to contain exemption information
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExemptionDetail", propOrder = { "referenceId", "propertyType", "exemptionType", "epc",
        "epcEvidenceFiles", "startDate", "exemptionReason", "exemptionReasonAdditionalText", "exemptionText",
        "exemptionTextFile", "exemptionStartDate","landlordDetails", "exemptionConfirmationText", "exemptionConfirmationIndicator" })
public class ExemptionDetail extends AbstractValidatableModel {

    public final static String EXEMPTION_TYPE_TEXT = "exemptionTypeText";
  
    public final static String EXEMPTION_TYPE_LOV = "exemptionTyleLov";

    private String referenceId;
    @RequiredFieldMetadata
    private PropertyType propertyType;

    @RequiredFieldMetadata
    @EnumeratedValues(name = EXEMPTION_TYPE_TEXT)
    private String exemptionType;

    @RequiredFieldMetadata
    @PastDateFieldMetadata(offset = "0")
    @GenericFieldMetadata(validator = StartDateValidator.class, dependencies = {
            @FieldDependency(path = "~.exemptionStartDate", comparator = "notEmpty") })
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime exemptionStartDate;

    @RequiredFieldMetadata
    private EnergyPerformanceCertificate epc;

    @GenericFieldMetadata(validator = ExemptionUploadFieldValidator.class, dependencies = {
            @FieldDependency(path = "uiData.selectedExemptionTypeText.minDocuments", comparator = "compareField") })
    private Upload epcEvidenceFiles = new Upload();

    private Date startDate;

    @RequiredFieldMetadata(invalidMessage = "Validation_Field_ExemptionReason_must_be_specified")
    @EnumeratedValues(name = EXEMPTION_TYPE_TEXT)
    @StringLengthFieldMetadata(maxLength = 4000)
    private String exemptionReason;

    @StringLengthFieldMetadata(maxLength = 4000, formatter = PreserveFieldFormatter.class)    
    private String exemptionReasonAdditionalText;

    @StringLengthFieldMetadata(maxLength = 4000, formatter = PreserveFieldFormatter.class)   
    private String exemptionText;

    @GenericFieldMetadata(validator = EPCUploadFieldValidator.class, dependencies = {
            @FieldDependency(path = "~.exemptionText", comparator = "empty") }, invalidMessage = "Validation_Field_must_be_a_LoadedEntered")
    private Upload exemptionTextFile = new Upload();
    
    private LandlordDetails landlordDetails;  
    
    @StringLengthFieldMetadata(maxLength = 4000, formatter = PreserveFieldFormatter.class)   
    private String exemptionConfirmationText;
    
    @RequiredFieldMetadata(invalidMessage = "Validation_Confirm_Field_should_be_selected")
    private String exemptionConfirmationIndicator;

  
    /**
     * Clears the data capture state fields, to reset the inputs after e.g. the data has been submitted
     */
    public void reset() {
        this.referenceId = null;
        this.propertyType = null;
        this.exemptionType = null;
        this.exemptionStartDate = null;
        this.epc = null;
        this.epcEvidenceFiles = new Upload();
        this.startDate = null;
        this.exemptionReason = null;
        this.exemptionReasonAdditionalText = null;
        this.exemptionText = null;
        this.exemptionConfirmationText = null;
        this.exemptionConfirmationIndicator = null;
        exemptionTextFile = new Upload();        
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public String getExemptionType() {
        return exemptionType;
    }

    public void setExemptionType(String exemptionType) {
        this.exemptionType = exemptionType;
    }

    public ZonedDateTime getExemptionStartDate() {
        return exemptionStartDate;
    }

    public void setExemptionStartDate(ZonedDateTime exemptionStartDate) {
        this.exemptionStartDate = exemptionStartDate;
    }

    public EnergyPerformanceCertificate getEpc() {
        return epc;
    }

    public void setEpc(EnergyPerformanceCertificate epc) {
        this.epc = epc;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getExemptionReason() {
        return exemptionReason;
    }

    public void setExemptionReason(String exemptionReason) {
        this.exemptionReason = exemptionReason;
    }

    public String getExemptionReasonAdditionalText() {
        return exemptionReasonAdditionalText;
    }

    public void setExemptionReasonAdditionalText(String exemptionReasonAdditionalText) {
        this.exemptionReasonAdditionalText = exemptionReasonAdditionalText;
    }

    public String getExemptionText() {
        return exemptionText;
    }

    public void setExemptionText(String exemptionText) {
        this.exemptionText = exemptionText;
    }

    public Upload getExemptionTextFile() {
        return exemptionTextFile;
    }

    public void setExemptionTextFile(Upload exemptionTextFile) {
        this.exemptionTextFile = exemptionTextFile;
    }

    public Upload getEpcEvidenceFiles() {
        return epcEvidenceFiles;
    }

    public void setEpcEvidenceFiles(Upload epcEvidenceFiles) {
        this.epcEvidenceFiles = epcEvidenceFiles;
    }
    
    public LandlordDetails getLandlordDetails() {
        return landlordDetails;
    }
    
    public void setLandlordDetails(LandlordDetails landlordDetails) {
        this.landlordDetails = landlordDetails;
    }

	public String getExemptionConfirmationText() {
		return exemptionConfirmationText;
	}

	public void setExemptionConfirmationText(String exemptionConfirmationText) {
		this.exemptionConfirmationText = exemptionConfirmationText;
	}

	public String getExemptionConfirmationIndicator() {
		return exemptionConfirmationIndicator;
	}

	public void setExemptionConfirmationIndicator(String exemptionConfirmationIndicator) {
		this.exemptionConfirmationIndicator = exemptionConfirmationIndicator;
	}

}
