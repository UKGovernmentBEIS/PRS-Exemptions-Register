/**
 * Also sets the default schema for all the classes exposed as XML within this
 * package.
 */

@XmlSchema(namespace = "http://northatepublicservices.com/nds/beis/getreferencevalues/2017/02/02", elementFormDefault = XmlNsForm.QUALIFIED, xmlns = {
		@XmlNs(prefix = "begrv", namespaceURI = "http://northatepublicservices.com/nds/beis/getreferencevalues/2017/02/02") })

package com.northgateps.nds.beis.api.getreferencevalues;

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlNs;
