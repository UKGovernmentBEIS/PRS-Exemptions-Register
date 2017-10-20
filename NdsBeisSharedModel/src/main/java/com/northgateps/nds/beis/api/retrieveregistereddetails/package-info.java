/**
 * Sets the default schema for all the classes exposed as XML within this package.
 */


@XmlSchema(namespace="http://northgatepublicservices.com/nds/beis/retrieveregistereddetails/2016/11/03",
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns={
            @XmlNs(prefix="brrud", namespaceURI="http://northgatepublicservices.com/nds/beis/retrieveregistereddetails/2016/11/03")
})

package com.northgateps.nds.beis.api.retrieveregistereddetails;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlNs;


