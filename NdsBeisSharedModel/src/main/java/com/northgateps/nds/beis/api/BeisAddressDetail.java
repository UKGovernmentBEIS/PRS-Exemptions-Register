package com.northgateps.nds.beis.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.northgateps.nds.platform.application.api.metadata.EnumeratedValues;
import com.northgateps.nds.platform.application.api.metadata.FieldLabelTransformer;
import com.northgateps.nds.platform.application.api.metadata.PostcodeFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.StringLengthFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.StripIndicesLabelTransformer;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;
import com.northgateps.nds.platform.application.api.validation.js.FirstElementOnlyFilter;
import com.northgateps.nds.platform.application.api.depend.FieldDependency;
import com.northgateps.nds.platform.application.api.depend.FieldDependencyPart;


/**
 * Class to contain address information.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BeisAddressDetail", propOrder = { "line", "town", "county",
        "postcode", "country", "reference", "singleLineAddressPostcode", "singleLineAddress", "qasMoniker",
        "uprn", "easting", "northing" ,"localEducationAuthorityCode"})
public class BeisAddressDetail extends AbstractValidatableModel {

    @RequiredFieldMetadata(filter = FirstElementOnlyFilter.class)
    @StringLengthFieldMetadata(maxLength = 255)
    @FieldLabelTransformer(transformer = StripIndicesLabelTransformer.class)
    private List<String> line;

    @RequiredFieldMetadata
    @StringLengthFieldMetadata(maxLength = 50)
    @FieldLabelTransformer(transformer = StripIndicesLabelTransformer.class)
    private String town;

    @StringLengthFieldMetadata(maxLength = 50)
    @FieldLabelTransformer(transformer = StripIndicesLabelTransformer.class)
    private String county;

    @PostcodeFieldMetadata(dependencies = {
            @FieldDependency(path = "~.country", values = { "GB" }, comparator = "contains")           
    })
    @RequiredFieldMetadata(dependencies = {
            @FieldDependency(type = "anyOf", parts = {
            @FieldDependencyPart(path = "~.country", values = { "GB" }, comparator = "contains"),
            @FieldDependencyPart(path = "~.country", values = { "" }, comparator = "contains")
    }) })
    @FieldLabelTransformer(transformer = StripIndicesLabelTransformer.class)
    private String postcode;

    protected String qasMoniker;

    @EnumeratedValues(name = "beisCountryList")
    @RequiredFieldMetadata
    private String country = "GB";

    private String reference;

    private String singleLineAddressPostcode;

    private String singleLineAddress;

    private String uprn;

    private String easting;
    
    private String northing;
    
    private String localEducationAuthorityCode;
    
    public List<String> getLine() {
        return line;
    }

    public void setLine(List<String> line) {
        this.line = line;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSingleLineAddressPostcode() {
        // Single line address postcode is not always populated, if it is, use it...
        if (singleLineAddressPostcode != null && !singleLineAddressPostcode.isEmpty() && (line == null || line.isEmpty())) {
            return singleLineAddressPostcode;
        }

        // ... otherwise create join the individual address fields with ", ".
        List<String> allAddressFields = new ArrayList<>(line.size() + 4);
        for( String addressLine : line){
         addToListIfNotEmpty(allAddressFields, addressLine);
        }
        addToListIfNotEmpty(allAddressFields, town);
        addToListIfNotEmpty(allAddressFields, county);
        addToListIfNotEmpty(allAddressFields, postcode);
        if (getCountry() != null && !getCountry().trim().isEmpty() && !getCountry().trim().equals("GB")) {
            allAddressFields.add(getCountry().trim());
        }

        return allAddressFields.stream().collect(Collectors.joining(", "));
    }

    private void addToListIfNotEmpty(List<String> allAddressFields, String str) {
        if (str != null) {
            String temp = str.trim();
            if (!temp.isEmpty()) {
                allAddressFields.add(temp);
            }
        }
    }

    public void setSingleLineAddressPostcode(String singleLineAddressPostcode) {
        this.singleLineAddressPostcode = singleLineAddressPostcode;
    }

    public String getSingleLineAddress() {
        return singleLineAddress;
    }

    public void setSingleLineAddress(String singleLineAddress) {
        this.singleLineAddress = singleLineAddress;
    }

    public String getQasMoniker() {
        return qasMoniker;
    }

    public void setQasMoniker(String qasMoniker) {
        this.qasMoniker = qasMoniker;
    }

    public String getUprn() {
        return uprn;
    }

    public void setUprn(String uprn) {
        this.uprn = uprn;
    }
    
    public String getEasting() {
        return easting;
    }

    public void setEasting(String easting) {
        this.easting = easting;
    }

    public String getNorthing() {
        return northing;
    }

    public void setNorthing(String northing) {
        this.northing = northing;
    }
    
    public String getLocalEducationAuthorityCode() {
        return localEducationAuthorityCode;
    }

    
    public void setLocalEducationAuthorityCode(String localEducationAuthorityCode) {
        this.localEducationAuthorityCode = localEducationAuthorityCode;
    }
    
}
