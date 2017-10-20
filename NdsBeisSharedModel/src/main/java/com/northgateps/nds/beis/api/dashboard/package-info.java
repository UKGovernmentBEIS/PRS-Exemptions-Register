/**
 * Also sets the default schema for all the classes exposed as XML within this package.
 */

@XmlSchema(namespace="http://northatepublicservices.com/nds/beis/dashboard/2016/02/22",
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns={
            @XmlNs(prefix="bdash", namespaceURI="http://northatepublicservices.com/nds/beis/dashboard/2016/02/22")
})

package com.northgateps.nds.beis.api.dashboard;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlNs;

