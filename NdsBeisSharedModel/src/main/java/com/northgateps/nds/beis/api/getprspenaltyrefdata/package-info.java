/**
 * Also sets the default schema for all the classes exposed as XML within this
 * package.
 */

@XmlSchema(namespace = "https://docs.google.com/spreadsheets/d/1DZLmJQJlrWnPDQaGrd1dEd1qNwmccsYB2M5DERctFVk", elementFormDefault = XmlNsForm.QUALIFIED, xmlns = {
		@XmlNs(prefix = "beerd", namespaceURI = "http://northatepublicservices.com/nds/beis/getprspenaltyrefdata/2017/06/06") })

package com.northgateps.nds.beis.api.getprspenaltyrefdata;

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlNs;
