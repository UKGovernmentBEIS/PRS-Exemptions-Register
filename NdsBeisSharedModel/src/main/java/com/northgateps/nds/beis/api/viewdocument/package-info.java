/**
 * Sets the default schema for all the classes exposed as XML within this package.
 */


@XmlSchema(namespace="http://northgatepublicservices.com/nds/beis/viewdocument/2017/01/19",
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns={
            @XmlNs(prefix="bevd", namespaceURI="http://northgatepublicservices.com/nds/beis/viewdocument/2017/01/19")
})

package com.northgateps.nds.beis.api.viewdocument;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;


