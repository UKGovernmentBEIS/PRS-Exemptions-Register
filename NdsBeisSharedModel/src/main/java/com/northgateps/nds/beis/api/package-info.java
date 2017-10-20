/**
 * Adds binding for the marshalling to and from XML of Java Time
 * objects Sets the default schema for all the classes exposed as XML within
 * this package.
 * 
 */
@XmlJavaTypeAdapters({
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(type = java.time.ZonedDateTime.class, value = ZonedDateTimeAdapter.class), })

@XmlSchema(namespace = "http://northgatepublicservices.com/nds/beis/model/2016/11/16", elementFormDefault = XmlNsForm.QUALIFIED, xmlns = {
		@XmlNs(prefix = "beis", namespaceURI = "http://northgatepublicservices.com/nds/beis/model/2016/11/16") })

package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;


import com.northgateps.nds.platform.api.marshall.ZonedDateTimeAdapter;
