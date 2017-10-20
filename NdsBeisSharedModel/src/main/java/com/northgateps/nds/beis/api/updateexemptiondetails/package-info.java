/**
 * Sets the default schema for all the classes exposed as XML within this package.
 */

@XmlSchema(namespace="http://northgatepublicservices.com/nds/beis/updateexemptiondetails/2017/01/11",
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns={
            @XmlNs(prefix="beued", namespaceURI="http://northgatepublicservices.com/nds/beis/updateexemptiondetails/2017/01/11")
})

package com.northgateps.nds.beis.api.updateexemptiondetails;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;


