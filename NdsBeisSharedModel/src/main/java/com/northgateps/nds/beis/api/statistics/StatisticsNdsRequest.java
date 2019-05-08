package com.northgateps.nds.beis.api.statistics;

import java.time.ZonedDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.PlatformNdsRequest;

/**
 * Request containing statistics *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatisticsNdsRequest", propOrder = { "numberOfRegisteredExemptions", "fromDateTime", "toDateTime",
        "numberOfSuccessfulLogins", "numberOfFailedLogins", "numberOfDomesticExemptions",
        "numberOfNonDomesticExemptions", "domesticExemptions", "nonDomesticExemptions", "numberOfRegistration",
        "numberOfChangePassword", "numberOfResetPassword" })
@XmlRootElement(name = "StatisticsNdsRequest")
public class StatisticsNdsRequest extends PlatformNdsRequest {

    protected int numberOfRegisteredExemptions;

    private ZonedDateTime fromDateTime;

    private ZonedDateTime toDateTime;

    protected int numberOfSuccessfulLogins;

    protected int numberOfFailedLogins;

    protected int numberOfDomesticExemptions;

    protected int numberOfNonDomesticExemptions;

    private List<ExemptionInformation> domesticExemptions;

    private List<ExemptionInformation> nonDomesticExemptions;

    private int numberOfRegistration;

    private int numberOfChangePassword;

    private int numberOfResetPassword;

    public int getNumberOfRegisteredExemptions() {
        return numberOfRegisteredExemptions;
    }

    public void setNumberOfRegisteredExemptions(int numberOfRegisteredExemptions) {
        this.numberOfRegisteredExemptions = numberOfRegisteredExemptions;
    }

    public ZonedDateTime getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(ZonedDateTime fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public ZonedDateTime getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(ZonedDateTime toDateTime) {
        this.toDateTime = toDateTime;
    }

    public int getNumberOfSuccessfulLogins() {
        return numberOfSuccessfulLogins;
    }

    public void setNumberOfSuccessfulLogins(int numberOfSuccessfulLogins) {
        this.numberOfSuccessfulLogins = numberOfSuccessfulLogins;
    }

    public int getNumberOfFailedLogins() {
        return numberOfFailedLogins;
    }

    public void setNumberOfFailedLogins(int numberOfFailedLogins) {
        this.numberOfFailedLogins = numberOfFailedLogins;
    }

    public int getNumberOfDomesticExemptions() {
        return numberOfDomesticExemptions;
    }

    public void setNumberOfDomesticExemptions(int numberOfDomesticExemptions) {
        this.numberOfDomesticExemptions = numberOfDomesticExemptions;
    }

    public int getNumberOfNonDomesticExemptions() {
        return numberOfNonDomesticExemptions;
    }

    public void setNumberOfNonDomesticExemptions(int numberOfNonDomesticExemptions) {
        this.numberOfNonDomesticExemptions = numberOfNonDomesticExemptions;
    }

    public List<ExemptionInformation> getNonDomesticExemptions() {
        return nonDomesticExemptions;
    }

    public void setNonDomesticExemptions(List<ExemptionInformation> nonDomesticExemptions) {
        this.nonDomesticExemptions = nonDomesticExemptions;
    }

    public List<ExemptionInformation> getDomesticExemptions() {
        return domesticExemptions;
    }

    public void setDomesticExemptions(List<ExemptionInformation> domesticExemptions) {
        this.domesticExemptions = domesticExemptions;
    }

    public int getNumberOfRegistration() {
        return numberOfRegistration;
    }

    public void setNumberOfRegistration(int numberOfRegistration) {
        this.numberOfRegistration = numberOfRegistration;
    }

    public int getNumberOfChangePassword() {
        return numberOfChangePassword;
    }

    public void setNumberOfChangePassword(int numberOfChangePassword) {
        this.numberOfChangePassword = numberOfChangePassword;
    }

    public int getNumberOfResetPassword() {
        return numberOfResetPassword;
    }

    public void setNumberOfResetPassword(int numberOfResetPassword) {
        this.numberOfResetPassword = numberOfResetPassword;
    }


}
