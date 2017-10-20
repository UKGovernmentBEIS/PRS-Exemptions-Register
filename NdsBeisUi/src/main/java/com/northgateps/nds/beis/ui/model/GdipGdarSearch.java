package com.northgateps.nds.beis.ui.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.metadata.GdipGdarReferenceNumberFieldMetaData;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.StringLengthFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

/**
 * 
 * Model to contain the search term and details about the search result for gdar / gdip
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GdipGdarSearch", propOrder = { "searchTerm", "fileName", "gdipGdarAvailable", "includeDocument" })
public class GdipGdarSearch extends AbstractValidatableModel {

    public enum SearchResultStatus {
        FILE_AVAILABLE, NO_RESULTS, SUPERSEDED
        
    };

    @RequiredFieldMetadata
    @StringLengthFieldMetadata(maxLength = 24)
    @GdipGdarReferenceNumberFieldMetaData
    private String searchTerm;

    private String fileName;

    private SearchResultStatus status;

    private boolean includeDocument;

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public SearchResultStatus getStatus() {
        return status;
    }

    public void setStatus(SearchResultStatus status) {
        this.status = status;
    }

    public boolean isIncludeDocument() {
        return includeDocument;
    }

    public void setIncludeDocument(boolean includeDocument) {
        this.includeDocument = includeDocument;
    }

    public void resetFileContent() {
        fileName = null;
        status = SearchResultStatus.NO_RESULTS;
    }

}
