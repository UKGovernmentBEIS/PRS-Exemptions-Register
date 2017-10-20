package com.northgateps.nds.beis.api;

import java.util.List;

public interface ExemptionType {

    String getMinDocuments();

    void setMinDocuments(String minDocuments);

    String getMaxDocuments();

    void setMaxDocuments(String maxDocuments);

    String getDocumentsRequired();

    void setDocumentsRequired(String documentsRequired);

    String getDocumentsPwsLabel();

    void setDocumentsPwsLabel(String documentsPwsLabel);

    String getDocumentsPwsText();

    void setDocumentsPwsText(String documentsPwsText);

    public String getService();

    public void setService(String service);

    String getDescription();

    void setDescription(String description);

    public String getPwsDescription();

    public void setPwsDescription(String pwsDescription);

    public String getCode();

    public void setCode(String code);

    public String getPwsText();

    public void setPwsText(String pwsText);

    public String getStartDateRequired();

    public void setStartDateRequired(String startDateRequired);

    public String getStartDatePwsLabel();

    public void setStartDatePwsLabel(String startDatePwsLabel);

    public String getStartDatePwsText();

    public void setStartDatePwsText(String startDatePwsText);
    
    public String getSequence();

    public void setSequence(String sequence);

    public String getTextPwsLabel();

    public void setTextPwsLabel(String textPwsLabel);

    public String getTextPwsText();

    public void setTextPwsText(String textPwsText);

    public String getDurationUnit();

    public void setDurationUnit(String durationUnit);

    public String getDuration();

    public void setDuration(String duration);

    String getFrvPwsLabel();

    void setFrvPwsLabel(String frvPwsLabel);

    String getFrvPwsText();

    void setFrvPwsText(String frvPwsText);

    String getFrvDomain();

    void setFrvDomain(String frvDomain);

    String getTextRequired();

    void setTextRequired(String textRequired);

    String getFrvRequired();

    void setFrvRequired(String frvRequired);
    
    List<ExemptionTypeLov> getExemptionTypeLovList();

    void setExemptionTypeLovList(List<ExemptionTypeLov> exemptionTypeLovList);
}
