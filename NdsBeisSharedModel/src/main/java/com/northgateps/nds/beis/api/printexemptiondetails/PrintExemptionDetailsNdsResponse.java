package com.northgateps.nds.beis.api.printexemptiondetails;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.AbstractNdsResponse;

/**
 * Response to printing the exemption details.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PrintExemptionDetailsNdsResponse", propOrder = { "fileName", "fileSize", "source", "contentType" })
@XmlRootElement(name = "PrintExemptionDetailsNdsResponse")
public class PrintExemptionDetailsNdsResponse extends AbstractNdsResponse {
    private String fileName;

    private long fileSize;

    private byte[] source;

    private String contentType;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getSource() {
        return source;
    }

    public void setSource(byte[] source) {
        this.source = source;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
