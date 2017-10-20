package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.application.api.metadata.CharacterSetRestrictionFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.StringLengthFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonNameDetail", propOrder = { "firstname", "surname" })
public class PersonNameDetail extends AbstractValidatableModel {

    @StringLengthFieldMetadata(maxLength = 50)
    @CharacterSetRestrictionFieldMetadata("PersonNameCharacterFieldValidator")
    @RequiredFieldMetadata
    private String firstname;

    @StringLengthFieldMetadata(maxLength = 100)
    @CharacterSetRestrictionFieldMetadata("PersonNameCharacterFieldValidator")
    @RequiredFieldMetadata
    private String surname;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

}
