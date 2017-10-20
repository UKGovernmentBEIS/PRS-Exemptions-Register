/**
 * Also sets the default schema for all the classes exposed as XML within this
 * package.
 */

@XmlSchema(namespace = "http://northatepublicservices.com/nds/beis/registerexemption/2016/12/15", elementFormDefault = XmlNsForm.QUALIFIED, xmlns = {
		@XmlNs(prefix = "berex", namespaceURI = "http://northatepublicservices.com/nds/beis/registerexemption/2016/12/15") })

package com.northgateps.nds.beis.api.registerprsexemption;

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlNs;
