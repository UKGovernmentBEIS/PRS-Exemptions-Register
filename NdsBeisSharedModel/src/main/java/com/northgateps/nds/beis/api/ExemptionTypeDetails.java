package com.northgateps.nds.beis.api;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

/**
 * Class to contain all the information for an exemption type
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExemptionTypeDetails", propOrder = { "code", "service","description", "pwsDescription", "pwsText",
        "startDateRequired", "startDatePwsLabel", "startDatePwsText", "sequence", "textPwsLabel", "textPwsText",
        "durationUnit", "duration", "minDocuments", "maxDocuments", "documentsRequired", "documentsPwsLabel",
        "documentsPwsText", "frvPwsLabel", "frvRequired", "frvPwsText", "frvDomain", "textRequired","ExemptionTypeLovList" })
public class ExemptionTypeDetails extends AbstractValidatableModel
        implements ExemptionType, Comparable<ExemptionTypeDetails> {

    private String code;
    private String service;
    private String description;
    private String pwsDescription;
    private String pwsText;
    private String startDateRequired;
    private String startDatePwsLabel;
    private String startDatePwsText;
    private String sequence;
    private String textPwsLabel;
    private String textPwsText;
    private String durationUnit;
    private String duration;
    private String minDocuments;
    private String maxDocuments;
    private String documentsRequired;
    private String documentsPwsLabel;
    private String documentsPwsText;
    private String frvPwsLabel;
    private String frvPwsText;
    private String frvDomain;
    private String frvRequired;
    private String textRequired;
    private List<ExemptionTypeLov> ExemptionTypeLovList;

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
    public String getService() {
        return service;
    }

    @Override
    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
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
    public String getSequence() {
        return sequence;
    }

    @Override
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @Override
    public String getTextPwsLabel() {
        return textPwsLabel;
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
    public String getFrvDomain() {
        return frvDomain;
    }

    @Override
    public void setFrvDomain(String frvDomain) {
        this.frvDomain = frvDomain;
    }

    @Override
    public int compareTo(ExemptionTypeDetails o) {
        return sequence.compareTo(o.getSequence());
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
    public String getFrvRequired() {
        return frvRequired;
    }

    @Override
    public void setFrvRequired(String frvRequired) {
        this.frvRequired = frvRequired;
    }

    @Override
    public List<ExemptionTypeLov> getExemptionTypeLovList() {
        return ExemptionTypeLovList;
    }

    @Override
    public void setExemptionTypeLovList(List<ExemptionTypeLov> exemptionTypeLovList) {
        ExemptionTypeLovList = exemptionTypeLovList;
    }
    
   

}
