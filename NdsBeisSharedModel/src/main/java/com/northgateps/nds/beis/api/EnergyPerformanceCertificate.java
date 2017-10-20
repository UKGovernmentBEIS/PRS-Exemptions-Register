package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.northgateps.nds.beis.api.validation.js.EPCUploadFieldValidator;
import com.northgateps.nds.platform.api.Upload;
import com.northgateps.nds.platform.application.api.metadata.GenericFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;
import com.northgateps.nds.platform.application.api.validation.js.AddressConsolidationJoiner;

/**
 * Class to contain energy performance certificate information
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnergyPerformanceCertificate", propOrder = {"propertyAddress", "files" })
public class EnergyPerformanceCertificate extends AbstractValidatableModel{

    @RequiredFieldMetadata
    @GenericFieldMetadata(joiner = AddressConsolidationJoiner.class)
    private BeisAddressDetail propertyAddress;

    @RequiredFieldMetadata
    @GenericFieldMetadata(validator = EPCUploadFieldValidator.class, invalidMessage = "Validation_EPC_Required")
    private Upload files = new Upload();

    public BeisAddressDetail getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(BeisAddressDetail propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public Upload getFiles() {
        return files;
    }

    public void setFiles(Upload files) {
        this.files = files;
    }

}
