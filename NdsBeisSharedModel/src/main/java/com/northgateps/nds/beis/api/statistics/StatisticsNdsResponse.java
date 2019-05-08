package com.northgateps.nds.beis.api.statistics;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.AbstractNdsResponse;

/**
 * Response to statistics
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatisticsNdsResponse", propOrder = {})
@XmlRootElement(name = "StatisticsNdsResponse")
public class StatisticsNdsResponse extends AbstractNdsResponse {

}

