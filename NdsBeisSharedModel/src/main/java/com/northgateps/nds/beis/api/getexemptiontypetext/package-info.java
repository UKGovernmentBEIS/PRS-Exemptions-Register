/**
 * Also sets the default schema for all the classes exposed as XML within this
 * package.
 */

@XmlSchema(namespace = "http://northatepublicservices.com/nds/beis/getexemptiontypetest/2016/09/12", elementFormDefault = XmlNsForm.QUALIFIED, xmlns = {
		@XmlNs(prefix = "bgett", namespaceURI = "http://northatepublicservices.com/nds/beis/getexemptiontypetest/2016/09/12") })

package com.northgateps.nds.beis.api.getexemptiontypetext;

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlNs;
