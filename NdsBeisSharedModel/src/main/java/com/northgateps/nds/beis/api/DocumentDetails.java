package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.NdsErrorResponse;
/**
 * 
 * Class contains information about document
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentDetails", propOrder = { "documentCode", "documentDescription", "documentName",
        "documentFileType", "documentReference", "binaryData" })
@XmlRootElement(name = "DocumentDetails")
public class DocumentDetails extends NdsErrorResponse {

    private String documentCode;

    private Object documentDescription;
    
    private String documentName;

    private String documentFileType;

    private String documentReference;

    private byte[] binaryData;

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public Object getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(Object documentDescription) {
        this.documentDescription = documentDescription;
    }

    public String getDocumentFileType() {
        return documentFileType;
    }

    public void setDocumentFileType(String documentFileType) {
        this.documentFileType = documentFileType;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(String documentReference) {
        this.documentReference = documentReference;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

}
