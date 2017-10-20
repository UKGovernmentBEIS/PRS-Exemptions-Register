/**
 * Sets the default schema for all the classes exposed as XML within this package.
 */


@XmlSchema(namespace="http://northgatepublicservices.com/nds/beis/printexemptiondetails/2016/11/16",
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns={
            @XmlNs(prefix="beped", namespaceURI="http://northgatepublicservices.com/nds/beis/printexemptiondetails/2016/11/16")
})

package com.northgateps.nds.beis.api.printexemptiondetails;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;


