/**
 * Also sets the default schema for all the classes exposed as XML within this
 * package.
 */

@XmlSchema(namespace = "http://northatepublicservices.com/nds/beis/getpartiallyregistereddetails/2017/01/16", elementFormDefault = XmlNsForm.QUALIFIED, xmlns = {
        @XmlNs(prefix = "begprd", namespaceURI = "http://northatepublicservices.com/nds/beis/getpartiallyregistereddetails/2017/01/16") })

package com.northgateps.nds.beis.api.getpartiallyregistereddetails;

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlNs;