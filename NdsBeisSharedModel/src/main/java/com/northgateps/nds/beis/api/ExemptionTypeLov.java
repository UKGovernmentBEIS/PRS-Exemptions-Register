package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

/**
 * A single value from a list of exemption type lis of values.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExemptionTypeLov", propOrder = { "domain", "code", "service", "description", "text","sequence" })
public class ExemptionTypeLov extends AbstractValidatableModel {

    private String domain;
    private String service;
    private String code;
    private String description;
    private String text;
    private String sequence;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    
    public String getSequence() {
        return sequence;
    }

    
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

}
