package com.northgateps.nds.beis.esb.converter;

import java.math.BigInteger;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.AccountType;
import com.northgateps.nds.beis.api.BeisAddressDetail;
import com.northgateps.nds.beis.api.EnergyPerformanceCertificate;
import com.northgateps.nds.beis.api.ExemptionDetail;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsRequest;
import com.northgateps.nds.beis.backoffice.service.registerprsexemption.ExemptionDocumentType;
import com.northgateps.nds.beis.backoffice.service.registerprsexemption.ExemptionDocumentType.ExemptionDocument;
import com.northgateps.nds.beis.backoffice.service.registerprsexemption.RegisterPRSExemptionRequest;
import com.northgateps.nds.beis.backoffice.service.registerprsexemption.RegisterPRSExemptionRequest.LandlordDetails;
import com.northgateps.nds.platform.api.FileDetails;
import com.northgateps.nds.platform.api.Upload;
import com.northgateps.nds.platform.esb.converter.ZonedDateTimeToXMLGregorianCalendarConverter;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Convert the passed in RegisterPrsExemptionNdsRequest to the passed out RegisterPRSExemptionRequest
 * 
 * @param com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsRequest
 * @return class com.northgateps.nds.beis.backoffice.service.registerprsexemption.RegisterPRSExemptionRequest
 * 
 */

@Converter
@DoNotWeaveLoggingSystem
public class ToRegisterPRSExemptionRequestConverter {

    private static final NdsLogger logger = NdsLogger.getLogger(ToRegisterPRSExemptionRequestConverter.class);

    /**
     * Convert the passed in RegisterPrsExemptionNdsRequest to the passed out RegisterPRSExemptionRequest
     * 
     * @param class RegisterPrsExemptionNdsRequest
     * @return RegisterPRSExemptionRequest
     */
    @Converter
    public RegisterPRSExemptionRequest converting(RegisterPrsExemptionNdsRequest registerPrsExemptionNdsRequest)
            throws DatatypeConfigurationException {
        logger.info("Conversion of Nds request object into back office request object started.");

        /* Class being converted to */
        RegisterPRSExemptionRequest registerPRSExemptionRequest = new RegisterPRSExemptionRequest();
        ExemptionDetail exemptionDetails = null;

        if (registerPrsExemptionNdsRequest != null) {
            exemptionDetails = registerPrsExemptionNdsRequest.getRegisterPrsExemptionDetails().getExemptionDetails();
        }

        if (exemptionDetails != null) {
            registerPRSExemptionRequest.setExemptionReference(exemptionDetails.getReferenceId());

            if (exemptionDetails.getPropertyType() != null) {
                registerPRSExemptionRequest.setService(exemptionDetails.getPropertyType().name());
            }
            if (exemptionDetails.getLandlordDetails() != null) {
                setLandlordDetails(registerPRSExemptionRequest, registerPrsExemptionNdsRequest);
            } else {
                registerPRSExemptionRequest.setLandlordPartyRef(
                        new BigInteger(registerPrsExemptionNdsRequest.getAccountId()));
            }
            if (exemptionDetails.getEpc() != null) {
                setPropertyAddress(registerPRSExemptionRequest, exemptionDetails);
            }
            registerPRSExemptionRequest.setExemptionCode(exemptionDetails.getExemptionType());

            registerPRSExemptionRequest.setExemptionOptionCode(exemptionDetails.getExemptionReason());
            registerPRSExemptionRequest.setExemptionText(exemptionDetails.getExemptionText());
            registerPRSExemptionRequest.setExemptionStartDate(
                    ZonedDateTimeToXMLGregorianCalendarConverter.convert(exemptionDetails.getExemptionStartDate()));
            registerPRSExemptionRequest.setExemptionOptionText(exemptionDetails.getExemptionReasonAdditionalText());
            
            registerPRSExemptionRequest.setExemptionConfirmation(exemptionDetails.getExemptionConfirmationText());
            registerPRSExemptionRequest.setExemptionConfirmationInd(exemptionDetails.getExemptionConfirmationIndicator());

            ExemptionDocumentType exemptionDocumentType = new ExemptionDocumentType();

            if (exemptionDetails.getEpcEvidenceFiles() != null) {
                setEpcDetails(exemptionDocumentType, exemptionDetails);
            }

            if (exemptionDetails.getEpc() != null) {
                addExemptionDocuments(exemptionDocumentType, exemptionDetails);
            }

            if (exemptionDetails.getExemptionTextFile() != null) {
                getExemptionTextFiles(exemptionDocumentType, exemptionDetails);
                registerPRSExemptionRequest.setExemptionDocuments(exemptionDocumentType);
            }
        }
        logger.info("Conversion of Nds request object into back office request object completed.");
        return registerPRSExemptionRequest;
    }

    /* set EPC details */
    public void setEpcDetails(ExemptionDocumentType exemptionDocumentType, ExemptionDetail exemptionDetails) {
        Upload epcEvidenceFiles = exemptionDetails.getEpcEvidenceFiles();
        if (epcEvidenceFiles != null) {
            List<FileDetails> fileDetailsList = epcEvidenceFiles.getResources();
            for (FileDetails fileDetails : fileDetailsList) {
                ExemptionDocument exemptionDocument = new ExemptionDocument();
                exemptionDocument.setDocumentType("PRS_PROOF");
                exemptionDocument.setDocumentFileName(fileDetails.getFileName());
                exemptionDocument.setDocumentFileType(fileDetails.getContentType());
                exemptionDocument.setDocumentDescription(fileDetails.getDescription());
                exemptionDocument.setDocumentObject(fileDetails.getSource());
                exemptionDocumentType.getExemptionDocument().add(exemptionDocument);
            }
        }
    }
    
    /* add exemption documents */
    public void addExemptionDocuments(ExemptionDocumentType exemptionDocumentType, ExemptionDetail exemptionDetails) {
        EnergyPerformanceCertificate epc = exemptionDetails.getEpc();
        Upload epcFiles = epc.getFiles();
        if (epcFiles != null) {
            List<FileDetails> fileDetailsList = epcFiles.getResources();
            for (FileDetails fileDetails : fileDetailsList) {
                ExemptionDocument exemptionDocument = new ExemptionDocument();
                exemptionDocument.setDocumentType("EPC");
                exemptionDocument.setDocumentFileName(fileDetails.getFileName());
                exemptionDocument.setDocumentFileType(fileDetails.getContentType());
                exemptionDocument.setDocumentDescription(fileDetails.getDescription());
                exemptionDocument.setDocumentObject(fileDetails.getSource());
                exemptionDocumentType.getExemptionDocument().add(exemptionDocument);
            }
        }
    }
    
    /* get exemption text files */
    public void getExemptionTextFiles(ExemptionDocumentType exemptionDocumentType, ExemptionDetail exemptionDetails) {
        Upload exemptionTextFiles = exemptionDetails.getExemptionTextFile();
        if (exemptionTextFiles != null) {
            List<FileDetails> fileDetailsList = exemptionTextFiles.getResources();
            for (FileDetails fileDetails : fileDetailsList) {
                ExemptionDocument exemptionDocument = new ExemptionDocument();
                exemptionDocument.setDocumentType("PRS_NOTES");
                exemptionDocument.setDocumentFileName(fileDetails.getFileName());
                exemptionDocument.setDocumentFileType(fileDetails.getContentType());
                exemptionDocument.setDocumentDescription(fileDetails.getDescription());
                exemptionDocument.setDocumentObject(fileDetails.getSource());
                exemptionDocumentType.getExemptionDocument().add(exemptionDocument);
            }
        }
    }
    
    /* set property address */ 
    public void setPropertyAddress(RegisterPRSExemptionRequest registerPRSExemptionRequest,
            ExemptionDetail exemptionDetails) {
        com.northgateps.nds.beis.backoffice.service.core.AddressType address = new com.northgateps.nds.beis.backoffice.service.core.AddressType();
        BeisAddressDetail propertyAddress = exemptionDetails.getEpc().getPropertyAddress();

        if (propertyAddress != null) {
            List<String> addressLine = propertyAddress.getLine();
            int i = 0;
            for (String line : addressLine) {
                if (i == 0)
                    address.setAddressLine1(line);
                if (i == 1)
                    address.setAddressLine2(line);
                if (i == 2)
                    address.setAddressLine3(line);
                if (i == 3)
                    address.setAddressLine4(line);
                i++;
            }
        }
        address.setAddressTownOrCity(propertyAddress.getTown());
        address.setAddressCountyOrRegion(propertyAddress.getCounty());
        address.setAddressCountryCode(propertyAddress.getCountry());
        address.setAddressPostcodeOrZip(propertyAddress.getPostcode());
        address.setQASMoniker(propertyAddress.getQasMoniker());

        registerPRSExemptionRequest.setAddressMoniker(propertyAddress.getQasMoniker());
        registerPRSExemptionRequest.setAddress(address);
    }
    
    /* Set landlord details if user is agent */
    public void setLandlordDetails(RegisterPRSExemptionRequest registerPRSExemptionRequest,
            RegisterPrsExemptionNdsRequest registerPrsExemptionNdsRequest) {

        LandlordDetails landlordDetails = new LandlordDetails();
        registerPRSExemptionRequest.setAgentPartyRef(new BigInteger(registerPrsExemptionNdsRequest.getAccountId()));
        ExemptionDetail exemptionDetails = registerPrsExemptionNdsRequest.getRegisterPrsExemptionDetails().getExemptionDetails();
        // set name details
        if (exemptionDetails.getLandlordDetails().getAccountType() == AccountType.PERSON) {

            landlordDetails.setFirstName(exemptionDetails.getLandlordDetails().getPersonNameDetail().getFirstname());
            landlordDetails.setLastName(exemptionDetails.getLandlordDetails().getPersonNameDetail().getSurname());

        } else {

            landlordDetails.setOrganisationName(
                    exemptionDetails.getLandlordDetails().getOrganisationNameDetail().getOrgName());

        }

        landlordDetails.setEmailAddress(exemptionDetails.getLandlordDetails().getEmailAddress());
        landlordDetails.setPhoneNumber(exemptionDetails.getLandlordDetails().getPhoneNumber());

        // set address

        if (exemptionDetails.getLandlordDetails().getLandlordAddress() != null) {

            BeisAddressDetail ndsContactAddressDetail = exemptionDetails.getLandlordDetails().getLandlordAddress();

            com.northgateps.nds.beis.backoffice.service.registerprsexemption.AddressType addressNode = new com.northgateps.nds.beis.backoffice.service.registerprsexemption.AddressType();

            // We will have one or two lines of address
            addressNode.setLine1(ndsContactAddressDetail.getLine().get(0));

            if (ndsContactAddressDetail.getLine().size() > 1) {
                addressNode.setLine2(ndsContactAddressDetail.getLine().get(1));
            }

            addressNode.setTown(ndsContactAddressDetail.getTown());
            addressNode.setCounty(ndsContactAddressDetail.getCounty());
            addressNode.setCountry(ndsContactAddressDetail.getCountry());
            addressNode.setPostcode(ndsContactAddressDetail.getPostcode());
            landlordDetails.setAddress(addressNode);
            landlordDetails.setAddressMoniker(ndsContactAddressDetail.getQasMoniker());

        }
        registerPRSExemptionRequest.setLandlordDetails(landlordDetails);
    }

}
