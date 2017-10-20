/**
 * Sets the default schema for all the classes exposed as XML within this package.
 */

@XmlSchema(namespace = "http://northgatepublicservices.com/nds/beis/beisregistration/2016/11/24", elementFormDefault = XmlNsForm.QUALIFIED, xmlns = {
        @XmlNs(prefix = "bereg", namespaceURI = "http://northgatepublicservices.com/nds/beis/beisregistration/2016/11/24") })

package com.northgateps.nds.beis.api.beisregistration;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
