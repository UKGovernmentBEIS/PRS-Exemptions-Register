package com.northgateps.nds.beis.api.viewdocument;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.DocumentDetails;
import com.northgateps.nds.platform.api.AbstractNdsResponse;

/**
 * Response to view document.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ViewPdfNdsResponse", propOrder = {"document", "superseded"})
@XmlRootElement(name = "ViewPdfNdsResponse")
public class ViewPdfNdsResponse extends AbstractNdsResponse {    
 
    private DocumentDetails document;

    private Boolean superseded = false;
    
    public DocumentDetails getDocument() {
        return document;
    }

    
    public void setDocument(DocumentDetails document) {
        this.document = document;
    }


	public Boolean getSuperseded() {
		return superseded;
	}


	public void setSuperseded(Boolean superseded) {
		this.superseded = superseded;
	}
    

}
