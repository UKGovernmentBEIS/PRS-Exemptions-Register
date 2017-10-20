package com.northgateps.nds.beis.esb.registerexemption;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.TypeConverter;

import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsRequest;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsResponse;
import com.northgateps.nds.beis.backoffice.service.registerprsexemption.RegisterPRSExemptionRequest;
import com.northgateps.nds.beis.backoffice.service.registerprsexemption.RegisterPRSExemptionResponse;
import com.northgateps.nds.platform.api.FileDetails;
import com.northgateps.nds.platform.api.NdsFile;
import com.northgateps.nds.platform.api.db.NdsDbRequestDetails;
import com.northgateps.nds.platform.api.db.NdsDbResponseDetails;
import com.northgateps.nds.platform.esb.adapter.NdsSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.db.NdsDbService;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.util.xml.JaxbXmlMarshaller;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Make a register Prs exemption request to the ESB.
 */
public class RegisterPrsExemptionAdapter extends
        NdsSoapAdapter<RegisterPrsExemptionNdsRequest, RegisterPrsExemptionNdsResponse, RegisterPRSExemptionRequest, RegisterPRSExemptionResponse> {

    private static final NdsLogger logger = NdsLogger.getLogger(RegisterPrsExemptionAdapter.class);
    public static final String EXEMPTION_REGISTERED_SUCCESSFULLY = "exemptionRegisteredSuccessfully";
    public static final String IS_USER_AGENT = "isUserAgent";
    /**
     * converts NDS request to BEIS request
     */
    @Override
    protected RegisterPRSExemptionRequest doRequestProcess(RegisterPrsExemptionNdsRequest request,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
        ndsExchange.setOperationName("RegisterPRSExemptionWSDL");

        RegisterPRSExemptionRequest registerPRSExemptionRequest = new RegisterPRSExemptionRequest();

        try {
            request = readFile(ndsExchange, request);
            ndsExchange.setRequestMessageBody(request);
            TypeConverter converter = ndsExchange.getTypeConverter();
            registerPRSExemptionRequest = converter.convertTo(RegisterPRSExemptionRequest.class, request);
            ndsExchange.setResponseMessageBody(registerPRSExemptionRequest);
            if(request.getRegisterPrsExemptionDetails().getExemptionDetails().getLandlordDetails() != null){
                ndsExchange.setAnExchangeProperty(IS_USER_AGENT,"true");
            }
        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during request conversion process:  " + e.getMessage(), e);
        }         
        logger.info("Successfully created the BEIS request");
        return registerPRSExemptionRequest;
    }

    /**
     * converts beis response to NDS response
     */
    @Override
    protected RegisterPrsExemptionNdsResponse doResponseProcess(RegisterPRSExemptionResponse externalResponse,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {

        RegisterPrsExemptionNdsResponse registerPrsExemptionNdsResponse = new RegisterPrsExemptionNdsResponse();

        try {
            TypeConverter converter = ndsExchange.getTypeConverter();
            registerPrsExemptionNdsResponse = converter.convertTo(RegisterPrsExemptionNdsResponse.class,
                    externalResponse);
            ndsExchange.setAnExchangeProperty(EXEMPTION_REGISTERED_SUCCESSFULLY, registerPrsExemptionNdsResponse.isSuccess());
        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during response conversion process:  " + e.getMessage(),
                    e);
        }
        logger.info("Successfully created the NDS response");
        return registerPrsExemptionNdsResponse;
    }

    private RegisterPrsExemptionNdsRequest readFile(NdsSoapRequestAdapterExchangeProxy ndsExchange,
            RegisterPrsExemptionNdsRequest request) throws NdsApplicationException {

        List<FileDetails> updatedList = new ArrayList<FileDetails>();

        if (request != null && request.getRegisterPrsExemptionDetails() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpcEvidenceFiles() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpc().getFiles().getResources() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpc().getFiles().getResources().size() > 0) {
            List<FileDetails> fileDetailsList = request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpc().getFiles().getResources();

            try {
                updatedList = getFiles(ndsExchange, fileDetailsList);
                request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpc().getFiles().setResources(
                        updatedList);
            } catch (KeyManagementException | NoSuchAlgorithmException | ClassNotFoundException | IOException e) {
                logger.error("File retrieve failure. " + e.getMessage(), e);
                throw new NdsApplicationException("File retrieve failure. " + e.getMessage(), e);

            }
        }
        if (request != null && request.getRegisterPrsExemptionDetails() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpcEvidenceFiles() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpcEvidenceFiles().getResources() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpcEvidenceFiles().getResources().size() > 0) {

            List<FileDetails> fileDetailsList = request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpcEvidenceFiles().getResources();
            try {
                updatedList = getFiles(ndsExchange, fileDetailsList);
                request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpcEvidenceFiles().setResources(
                        updatedList);
            } catch (KeyManagementException | NoSuchAlgorithmException | ClassNotFoundException | IOException e) {
                logger.error("File retrieve failure. " + e.getMessage(), e);
                throw new NdsApplicationException("File retrieve failure. " + e.getMessage(), e);

            }
        }
        if (request != null && request.getRegisterPrsExemptionDetails() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpcEvidenceFiles() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails().getExemptionTextFile() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails().getExemptionTextFile().getResources().size() > 0) {

            List<FileDetails> fileDetailsList = request.getRegisterPrsExemptionDetails().getExemptionDetails().getExemptionTextFile().getResources();
            try {
                updatedList = getFiles(ndsExchange, fileDetailsList);
                request.getRegisterPrsExemptionDetails().getExemptionDetails().getExemptionTextFile().setResources(
                        updatedList);
            } catch (KeyManagementException | NoSuchAlgorithmException | ClassNotFoundException | IOException e) {
                logger.error("File retrieve failure. " + e.getMessage(), e);
                throw new NdsApplicationException("File retrieve failure. " + e.getMessage(), e);

            }
        }

        ndsExchange.setResponseMessageBody("");
        return request;
    }

    private List<FileDetails> getFiles(NdsSoapRequestAdapterExchangeProxy ndsExchange,
            List<FileDetails> fileDetailsList)
                    throws KeyManagementException, NoSuchAlgorithmException, ClassNotFoundException, IOException {
        List<FileDetails> updatedList = new ArrayList<FileDetails>();
        NdsDbRequestDetails ndsDbRequestDetails = new NdsDbRequestDetails();
        for (FileDetails fileDetails : fileDetailsList) {
            ndsDbRequestDetails.setReferenceId(fileDetails.getFileId());
            ndsExchange.setRequestMessageBody(ndsDbRequestDetails);
            NdsDbService.readFile(ndsExchange);
            NdsDbResponseDetails ndsDbResponseDetails = (NdsDbResponseDetails) ndsExchange.getResponseMessageBody();
            String xmlData = (String) ndsDbResponseDetails.getData();
            NdsFile ndsFile = JaxbXmlMarshaller.convertFromXml(xmlData, NdsFile.class);
            fileDetails.setSource(ndsFile.getSource());
            updatedList.add(fileDetails);
        }
        return updatedList;
    }

    @Override
    protected String getResponseClassName() {
        return RegisterPRSExemptionResponse.class.getName();
    }

    @Override
    protected String getRequestClassName() {
        return RegisterPrsExemptionNdsRequest.class.getName();
    }
}
