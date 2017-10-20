package com.northgateps.nds.beis.api.viewdocument;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.NdsRequest;

/**
 * Request to view document
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ViewPdfNdsRequest", propOrder = { "referenceNumber", "includeDocument" })
@XmlRootElement(name = "ViewPdfNdsRequest")
public class ViewPdfNdsRequest implements NdsRequest {

    private String referenceNumber;

    private boolean includeDocument;

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public boolean isIncludeDocument() {
        return includeDocument;
    }

    public void setIncludeDocument(boolean includeDocument) {
        this.includeDocument = includeDocument;
    }
}
