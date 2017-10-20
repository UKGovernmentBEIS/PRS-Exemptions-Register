package com.northgateps.nds.beis.api.getprsexemptionrefdata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.NdsRequest;

/**
 * Request to get exemption reference data
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetPrsExemptionRefDataNdsRequest", propOrder = {})
@XmlRootElement(name = "GetPrsExemptionRefDataNdsRequest")
public class GetPrsExemptionRefDataNdsRequest implements NdsRequest {
}
