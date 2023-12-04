package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.application.api.depend.FieldDependency;
import com.northgateps.nds.platform.application.api.metadata.InternationalPhoneNumberFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.StringLengthFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

/**
 * 
 * Stores the account details
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BeisAccountDetails", propOrder = { "accountType", "accountId", "organisationNameDetail",
        "personNameDetail", "agentNameDetails","telNumber", "languageCode" })
public class BeisAccountDetails extends AbstractValidatableModel {

    @RequiredFieldMetadata(dependencies = {
            @FieldDependency(path = "~~.userDetails.userType", values = { "LANDLORD" }, comparator = "contains") })
    private AccountType accountType;

    @RequiredFieldMetadata(dependencies = {
            @FieldDependency(path = "~.accountType", values = { "ORGANISATION" }, comparator = "contains") })
    private OrganisationNameDetail organisationNameDetail;

    @RequiredFieldMetadata(dependencies = {
            @FieldDependency(path = "~.accountType", values = { "PERSON" }, comparator = "contains") })
    private PersonNameDetail personNameDetail;
    
    @RequiredFieldMetadata(dependencies = {
            @FieldDependency(path = "~~.userType", values = { "AGENT" }, comparator = "contains") })
    private AgentNameDetails agentNameDetails;   

    /*
     * back office saves in a 100 character field, ITU says actual length of phone nos is 15 
     * but we allow + and spaces so this is arbitrarily in between to enforce a limit without
     * being too prescriptive.
     */
    @StringLengthFieldMetadata(maxLength = 25)
    @InternationalPhoneNumberFieldMetadata
    @RequiredFieldMetadata
    private String telNumber;

    @StringLengthFieldMetadata(maxLength = 20)
    private String accountId;

    @StringLengthFieldMetadata(maxLength = 10)
    private String languageCode;

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    /**
     * @return For BEIS this is the PartyRef from the foundation layer and currently is held in LDAP
     *         as the uid against the FOUNDATION_LAYER_PARTY_SERVICE against each user.
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * @param accountId - For BEIS this is the PartyRef from the foundation layer and currently is held in LDAP
     *            as the uid against the FOUNDATION_LAYER_PARTY_SERVICE against each user.
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
    
    
    public AgentNameDetails getAgentNameDetails() {
        return agentNameDetails;
    }
    
    public void setAgentNameDetails(AgentNameDetails agentNameDetails) {
        this.agentNameDetails = agentNameDetails;
    }



}
