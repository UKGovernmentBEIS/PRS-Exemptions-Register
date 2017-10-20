package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.StringLengthFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;
/**
 * Class contain information about agent
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AgentNameDetails", propOrder = { "agentName"})
public class AgentNameDetails extends AbstractValidatableModel {
    
    @RequiredFieldMetadata
    @StringLengthFieldMetadata(maxLength = 300)
    private String agentName;

    public String getAgentName() {
        return agentName;
    }
    
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

}
