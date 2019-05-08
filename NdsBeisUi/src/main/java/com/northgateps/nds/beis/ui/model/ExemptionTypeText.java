package com.northgateps.nds.beis.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.northgateps.nds.beis.api.ExemptionType;
import com.northgateps.nds.beis.api.ExemptionTypeLov;
import com.northgateps.nds.platform.ui.model.ReferenceValue;

/**
 * A single value from a list of exemption type text values.
 */
public class ExemptionTypeText extends ReferenceValue implements ExemptionType, Serializable {

    private String description;
    private String current;
    private String service;
    private String sequence;
    private String pwsDescription;
    private String pwsText;
    private String durationUnit;
    private String duration;
    private String minDocuments;
    private String maxDocuments;
    private String documentsRequired;
    private String documentsPwsLabel;
    private String documentsPwsText;
    private String textRequired;
    private String textPwsLabel;
    private String textPwsText;
    private String startDateRequired;
    private String startDatePwsLabel;
    private String startDatePwsText;
    private String frvRequired;
    private String frvPwsLabel;
    private String frvPwsText;
    private String frvDomain;
    private List<ExemptionTypeLov> exemptionTypeLovList;
    private String confirmationPagetitle;
    private String confirmationcheckbox;
    private String confirmationwording;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    @Override
    public String getService() {
        return service;
    }

    @Override
    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String getSequence() {
        return sequence;
    }

    @Override
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @Override
    public String getPwsDescription() {
        return pwsDescription;
    }

    @Override
    public void setPwsDescription(String pwsDescription) {
        this.pwsDescription = pwsDescription;
    }

    @Override
    public String getPwsText() {
        return pwsText;
    }

    @Override
    public void setPwsText(String pwsText) {
        this.pwsText = pwsText;
    }

    @Override
    public String getDurationUnit() {
        return durationUnit;
    }

    @Override
    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    @Override
    public String getDuration() {
        return duration;
    }

    @Override
    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String getMinDocuments() {
        return minDocuments;
    }

    @Override
    public void setMinDocuments(String minDocuments) {
        this.minDocuments = minDocuments;
    }

    @Override
    public String getMaxDocuments() {
        return maxDocuments;
    }

    @Override
    public void setMaxDocuments(String maxDocuments) {
        this.maxDocuments = maxDocuments;
    }

    @Override
    public String getDocumentsRequired() {
        return documentsRequired;
    }

    @Override
    public void setDocumentsRequired(String documentsRequired) {
        this.documentsRequired = documentsRequired;
    }

    @Override
    public String getDocumentsPwsLabel() {
        return documentsPwsLabel;
    }

    @Override
    public void setDocumentsPwsLabel(String documentsPwsLabel) {
        this.documentsPwsLabel = documentsPwsLabel;
    }

    @Override
    public String getDocumentsPwsText() {
        return documentsPwsText;
    }

    @Override
    public void setDocumentsPwsText(String documentsPwsText) {
        this.documentsPwsText = documentsPwsText;
    }

    @Override
    public String getTextRequired() {
        return textRequired;
    }

    @Override
    public void setTextRequired(String textRequired) {
        this.textRequired = textRequired;
    }

    @Override
    public String getTextPwsLabel() {
        return textPwsLabel;
    }

    @Override
    public String getFrvDomain() {
        return frvDomain;
    }

    @Override
    public void setFrvDomain(String frvDomain) {
        this.frvDomain = frvDomain;
    }

    @Override
    public void setTextPwsLabel(String textPwsLabel) {
        this.textPwsLabel = textPwsLabel;
    }

    @Override
    public String getTextPwsText() {
        return textPwsText;
    }

    @Override
    public void setTextPwsText(String textPwsText) {
        this.textPwsText = textPwsText;
    }

    @Override
    public String getStartDateRequired() {
        return startDateRequired;
    }

    @Override
    public void setStartDateRequired(String startDateRequired) {
        this.startDateRequired = startDateRequired;
    }

    @Override
    public String getStartDatePwsLabel() {
        return startDatePwsLabel;
    }

    @Override
    public void setStartDatePwsLabel(String startDatePwsLabel) {
        this.startDatePwsLabel = startDatePwsLabel;
    }

    @Override
    public String getStartDatePwsText() {
        return startDatePwsText;
    }

    @Override
    public void setStartDatePwsText(String startDatePwsText) {
        this.startDatePwsText = startDatePwsText;
    }

    @Override
    public String getFrvRequired() {
        return frvRequired;
    }

    @Override
    public void setFrvRequired(String frvRequired) {
        this.frvRequired = frvRequired;
    }

    @Override
    public String getFrvPwsLabel() {
        return frvPwsLabel;
    }

    @Override
    public void setFrvPwsLabel(String frvPwsLabel) {
        this.frvPwsLabel = frvPwsLabel;
    }

    @Override
    public String getFrvPwsText() {
        return frvPwsText;
    }

    @Override
    public void setFrvPwsText(String frvPwsText) {
        this.frvPwsText = frvPwsText;
    }

    @Override
    public List<ExemptionTypeLov> getExemptionTypeLovList() {
        if(exemptionTypeLovList==null){
            this.exemptionTypeLovList =  new ArrayList<ExemptionTypeLov>();
        } 
        return this.exemptionTypeLovList;
        
    }

    @Override
    public void setExemptionTypeLovList(List<ExemptionTypeLov> exemptionTypeLovList) {
        this.exemptionTypeLovList = exemptionTypeLovList;
    }

	public String getConfirmationPagetitle() {
		return confirmationPagetitle;
	}

	public void setConfirmationPagetitle(String confirmationPagetitle) {
		this.confirmationPagetitle = confirmationPagetitle;
	}

	public String getConfirmationcheckbox() {
		return confirmationcheckbox;
	}

	public void setConfirmationcheckbox(String confirmationcheckbox) {
		this.confirmationcheckbox = confirmationcheckbox;
	}

	public String getConfirmationwording() {
		return confirmationwording;
	}

	public void setConfirmationwording(String confirmationwording) {
		this.confirmationwording = confirmationwording;
	}

 

}
