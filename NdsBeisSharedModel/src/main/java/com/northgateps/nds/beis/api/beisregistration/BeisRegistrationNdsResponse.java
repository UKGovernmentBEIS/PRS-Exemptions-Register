package com.northgateps.nds.beis.api.beisregistration;

import javax.xml.bind.annotation.XmlAccessType;
/**
 * The NDS response to indicate success/failure of new user registered 
 */
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.AbstractNdsResponse;

/**
 * Response to registering a new user
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BeisRegistrationNdsResponse", propOrder = {})
@XmlRootElement(name = "BeisRegistrationNdsResponse")
public class BeisRegistrationNdsResponse extends AbstractNdsResponse {

}