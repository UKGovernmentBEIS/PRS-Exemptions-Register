package com.northgateps.nds.beis.esb.registerexemption;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.TypeConversionException;
import org.apache.camel.TypeConverter;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsRequest;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsResponse;
import com.northgateps.nds.beis.backoffice.service.registerprsexemption.RegisterPRSExemptionRequest;
import com.northgateps.nds.beis.backoffice.service.registerprsexemption.RegisterPRSExemptionResponse;
import com.northgateps.nds.platform.api.FileDetails;
import com.northgateps.nds.platform.api.NdsFile;
import com.northgateps.nds.platform.esb.adapter.NdsSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.adapter.persistence.AbstractPersistenceAdapter;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsDbException;
import com.northgateps.nds.platform.esb.persistence.FileResult;
import com.northgateps.nds.platform.esb.persistence.PersistenceManager;
import com.northgateps.nds.platform.logger.NdsLogger;


/**
 * Make a register Prs exemption request to the ESB.
 */
public class RegisterPrsExemptionPersistenceAdapter extends
        NdsSoapAdapter<RegisterPrsExemptionNdsRequest, RegisterPrsExemptionNdsResponse, RegisterPRSExemptionRequest, RegisterPRSExemptionResponse> {

    private static final NdsLogger logger = NdsLogger.getLogger(RegisterPrsExemptionPersistenceAdapter.class);
    public static final String EXEMPTION_REGISTERED_SUCCESSFULLY = "exemptionRegisteredSuccessfully";
    public static final String IS_USER_AGENT = "isUserAgent";
    public static final String UPLOADED_FILES = "uploadedFiles";
    
    private PersistenceManager persistenceManager = null; 

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
    
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
        } catch (TypeConversionException e) {
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

            RegisterPrsExemptionPersistenceNestedAdapter persistenceAdapter = new RegisterPrsExemptionPersistenceNestedAdapter() {
                @Override
                public PersistenceManager getPersistenceManager() {
                    return (persistenceManager != null) ? persistenceManager : super.getPersistenceManager();
                }
            };       
            persistenceAdapter.logEvent(ndsExchange,(String) ndsExchange.getAnExchangeProperty(RegisterPrsExemptionLookupIdComponent.PROPERTY_TYPE));
        } catch (TypeConversionException e) {
            throw new NdsApplicationException("Error occured during response conversion process:  " + e.getMessage(),
                    e);
        }
        logger.info("Successfully created the NDS response");
        return registerPrsExemptionNdsResponse;
    }

    private RegisterPrsExemptionNdsRequest readFile(NdsSoapRequestAdapterExchangeProxy ndsExchange,
            RegisterPrsExemptionNdsRequest request) throws NdsApplicationException {

        List<FileDetails> updatedList = new ArrayList<FileDetails>();
        List<FileDetails> uploadedFiles = new ArrayList<FileDetails>();

        if (request != null && request.getRegisterPrsExemptionDetails() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails() != null
                && request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpcEvidenceFiles() != null) {
            
            RegisterPrsExemptionPersistenceNestedAdapter persistenceAdapter = new RegisterPrsExemptionPersistenceNestedAdapter() {
                @Override
                public PersistenceManager getPersistenceManager() {
                    return (persistenceManager != null) ? persistenceManager : super.getPersistenceManager();
                }
            };            
            
            if (request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpc().getFiles().getResources() != null
                    && request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpc().getFiles().getResources().size() > 0) {
                List<FileDetails> fileDetailsList = request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpc().getFiles().getResources();
                updatedList = persistenceAdapter.getFiles(ndsExchange, fileDetailsList);

                request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpc().getFiles().setResources(
                        updatedList);
                uploadedFiles.addAll(updatedList);
            }

            if (request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpcEvidenceFiles().getResources() != null
                    && request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpcEvidenceFiles().getResources().size() > 0) {

                List<FileDetails> fileDetailsList = request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpcEvidenceFiles().getResources();
                updatedList = persistenceAdapter.getFiles(ndsExchange, fileDetailsList);
                request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpcEvidenceFiles().setResources(
                        updatedList);
                uploadedFiles.addAll(updatedList);
            }

            if (request.getRegisterPrsExemptionDetails().getExemptionDetails().getExemptionTextFile() != null
                    && request.getRegisterPrsExemptionDetails().getExemptionDetails().getExemptionTextFile().getResources().size() > 0) {

                List<FileDetails> fileDetailsList = request.getRegisterPrsExemptionDetails().getExemptionDetails().getExemptionTextFile().getResources();
                updatedList = persistenceAdapter.getFiles(ndsExchange, fileDetailsList);
                request.getRegisterPrsExemptionDetails().getExemptionDetails().getExemptionTextFile().setResources(
                        updatedList);
                uploadedFiles.addAll(updatedList);
            }

        }

        ndsExchange.setAnExchangeProperty(UPLOADED_FILES, uploadedFiles);

        ndsExchange.setResponseMessageBody("");
        return request;
    }
    
    private class RegisterPrsExemptionPersistenceNestedAdapter extends AbstractPersistenceAdapter {
        
        private List<FileDetails> getFiles(NdsSoapRequestAdapterExchangeProxy ndsExchange, List<FileDetails> fileDetailsList) throws NdsDbException {
            
            List<FileDetails> updatedList = new ArrayList<FileDetails>();
            
            for (FileDetails fileDetails : fileDetailsList) {
                if (StringUtils.isNotEmpty(fileDetails.getFileId())) {
                    FileResult fileResult = getPersistenceManager().fetchFile(getGridFsCollectionName(), new ObjectId(fileDetails.getFileId()));
                    NdsFile ndsFile = fileResult.getNdsFile();                
                    fileDetails.setSource(ndsFile.getSource());                    
                    updatedList.add(fileDetails);
                }            
            }
            return updatedList;
        }
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
