package com.northgateps.nds.beis.api.registerprsexemption;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.NdsErrorResponse;
/**
 * Response to hold exemption registration response
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisterPrsExemptionNdsResponse", propOrder = { "" })
@XmlRootElement(name = "RegisterPrsExemptionNdsResponse")
public class RegisterPrsExemptionNdsResponse extends NdsErrorResponse {

}
