/**
 * Also sets the default schema for all the classes exposed as XML within this
 * package.
 */

@XmlSchema(namespace = "http://northatepublicservices.com/nds/beis/getprsexemptionrefdata/2017/01/09", elementFormDefault = XmlNsForm.QUALIFIED, xmlns = {
		@XmlNs(prefix = "beerd", namespaceURI = "http://northatepublicservices.com/nds/beis/getprsexemptionrefdata/2017/01/09") })

package com.northgateps.nds.beis.api.getprsexemptionrefdata;

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlNs;
