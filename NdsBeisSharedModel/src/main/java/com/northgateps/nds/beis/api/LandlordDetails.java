package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.validation.js.BeisAddressConsolidationWithCountryJoiner;
import com.northgateps.nds.platform.application.api.depend.FieldDependency;
import com.northgateps.nds.platform.application.api.metadata.EmailFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.GenericFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.InternationalPhoneNumberFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.StringLengthFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.ViolationFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

/** 
 * Class contain information about landlord 
  */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LandlordDetails", propOrder = {
    "accountType",
    "organisationNameDetail",
    "personNameDetail",
    "emailAddress",
    "confirmEmail",
    "phoneNumber",
    "landlordAddress"
})
public class LandlordDetails extends AbstractValidatableModel {
    
    @RequiredFieldMetadata    
    private AccountType accountType;
    
    @RequiredFieldMetadata(dependencies = {
            @FieldDependency(path = "~.accountType", values = { "ORGANISATION" }, comparator = "contains") })
    private OrganisationNameDetail organisationNameDetail;

    @RequiredFieldMetadata(dependencies = {
            @FieldDependency(path = "~.accountType", values = { "PERSON" }, comparator = "contains") })
    private PersonNameDetail personNameDetail;
    
    @RequiredFieldMetadata
    @EmailFieldMetadata
    @StringLengthFieldMetadata(maxLength = 100)
    private String emailAddress;
   
    @EmailFieldMetadata
    @RequiredFieldMetadata
    @StringLengthFieldMetadata(maxLength = 100)
    @ViolationFieldMetadata(invalidMessage = "ValidationField_RegistrationModel_confirmEmail", dependencies = {
            @FieldDependency(path = "~.emailAddress", comparator = "notSameAs") })
    private String confirmEmail;
    
    @InternationalPhoneNumberFieldMetadata
    @RequiredFieldMetadata
    private String phoneNumber;
     
    @RequiredFieldMetadata
    @GenericFieldMetadata(joiner = BeisAddressConsolidationWithCountryJoiner.class)
    private BeisAddressDetail landlordAddress;
    
    
    public BeisAddressDetail getLandlordAddress() {
        return landlordAddress;
    }

    
    public void setLandlordAddress(BeisAddressDetail landlordAddress) {
        this.landlordAddress = landlordAddress;
    }


    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    public OrganisationNameDetail getOrganisationNameDetail() {
        return organisationNameDetail;
    }

    public void setOrganisationNameDetail(OrganisationNameDetail organisationNameDetail) {
        this.organisationNameDetail = organisationNameDetail;
    }

    public PersonNameDetail getPersonNameDetail() {
        return personNameDetail;
    }

    public void setPersonNameDetail(PersonNameDetail personNameDetail) {
        this.personNameDetail = personNameDetail;
    }    
    
    public String getEmailAddress() {
        return emailAddress;
    }

    
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getConfirmEmail() {
        return confirmEmail;
    }
    
    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }
   
}
