/**
 * Sets the default schema for all the classes exposed as XML within this package.
 */


@XmlSchema(namespace="http://northgatepublicservices.com/nds/beis/saveregisteredaccountdetails/2016/09/11",
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns={
            @XmlNs(prefix="besreg", namespaceURI="http://northgatepublicservices.com/nds/beis/saveregisteredaccountdetails/2016/09/11")
})

package com.northgateps.nds.beis.api.saveregisteredaccountdetails;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
