package com.northgateps.nds.beis.ui.controller.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;
import com.northgateps.nds.platform.api.FileDetails;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.Upload;
import com.northgateps.nds.platform.api.fileresourceoperation.FileAddNdsRequest;
import com.northgateps.nds.platform.api.fileresourceoperation.FileAddNdsResponse;
import com.northgateps.nds.platform.api.model.utilities.ModelField;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.application.api.utils.ErrorContext;
import com.northgateps.nds.platform.application.api.validation.SimpleValidationProperty;
import com.northgateps.nds.platform.application.api.validation.StickyValidationProperty;
import com.northgateps.nds.platform.application.api.validation.ValidationProperty;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.controller.NdsAction;
import com.northgateps.nds.platform.ui.controller.UploadLimitations;
import com.northgateps.nds.platform.ui.controller.UploadPropertiesLoader;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;

/**
 * Capture a file upload from the browser, and call the ESB to store it
 */
public class BeisFileAddEventHandler extends AbstractViewEventHandler {

    private String multipartPath = "uiData.multipartFile";

    private String path;
    private String kindName;
    private String matchButton = "AddResource";
    private UploadPropertiesLoader uploadPropertiesLoader;
    private String deliveredResourcesPath;
    private String targetSlice = null;

    protected final NdsLogger logger = NdsLogger.getLogger(getClass());
    private static String fileUploadServiceEndPoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.addFile");

    @Override
    public void onBeforeValidation(NdsFormModel allModel, ControllerState<?> controllerState) {

        if (kindName == null || path == null) {
            return;
        }

        final AbstractNdsMvcModel model = (AbstractNdsMvcModel) allModel;

        final MultipartFile file = (MultipartFile) new ModelField().setPath(multipartPath).getValue(model);

        Object uploadLocation = new ModelField().setPath(path).getValue(model);

        final Upload upload = (uploadLocation instanceof Upload) ? (Upload) uploadLocation : null;

        Map<String, Object> validationMessageParameters = new HashMap<String, Object>();
        String validationMessage = null;

        if (file == null || file.isEmpty()) {

            // The same page may have multiple FileAddEventHandlers for dealing with different types of files being
            // uploaded. If the requested action is for the file type that this event handler is dealing with but there
            // is no uploaded data, then report an error. If the request was not meant to be for this upload file type
            // just return quietly.
            validationMessage = "File_Select";           
            controllerState.getBindingResult().reject("", validationMessage);
            model.setAction(NdsAction.NONE);
            return; 
        } else {
            FileDetails fileDetails = new FileDetails();

            fileDetails.setContentType(file.getContentType());
            fileDetails.setFileName(file.getOriginalFilename());
            fileDetails.setFileSize(file.getSize());
            fileDetails.setFileType(kindName);

            UploadLimitations limitations = uploadPropertiesLoader.getProperties().getLimitationsMap().get(
                    kindName.toLowerCase());

            if (limitations == null) {

                // limitations may be null because of a configuration error, but because "kindName" has come from
                // the client, it's more
                // likely that the kindName has been corrupted, quite possibly intentionally, by the user or
                // user-agent.
                logger.error("File upload failure. No upload limitations found for " + kindName);
                validationMessage = "File_NoAvailableLimitations";
                validationMessageParameters.put("kindName", kindName);

            } else if ((upload != null) && (upload.getResources().size()
                    + deliveredResourcesSize(model) >= limitations.getMaxResources(model))) {
                validationMessage = "File_TooManyFiles";

            } else if (!Arrays.stream(limitations.getSupportedTypes(model).split(" ")).collect(
                    Collectors.toList()).contains(fileDetails.getContentType())) {

                validationMessage = "File_InvalidFileType";
                validationMessageParameters.put("contentType", fileDetails.getContentType());

            } else if (file.getSize() > limitations.getMaxResourceSize(model)) {

                validationMessage = "File_SizeLimit";
                validationMessageParameters.put("fileSize", fileDetails.getFileSize() / 1048576);
                validationMessageParameters.put("maxResourceSizeMB", limitations.getMaxResourceSizeMB(model));
            } else {

                // The uploaded file meets all the metadata criteria for an correct upload, so try to get the
                // payload data.
                try {
                    fileDetails.setSource(file.getBytes());

                } catch (IOException e) {
                    validationMessage = "File_UploadFailed";
                    validationMessageParameters.put("contentType", fileDetails.getContentType());
                }

                if (fileDetails.getSource() == null) {
                    validationMessage = "File_NoData";
                }
            }

            if (validationMessage == null) {
                uploadFile(controllerState, upload, fileDetails, model);
            } else {

                ArrayList<ValidationProperty> properties = new ArrayList<ValidationProperty>();
                properties.add(new StickyValidationProperty());

                for (Map.Entry<String, Object> entry : validationMessageParameters.entrySet()) {

                    SimpleValidationProperty property = new SimpleValidationProperty();
                    property.setParameter(entry.getKey());
                    property.setValue(entry.getValue().toString(), ErrorContext.PAGE);
                    property.setValue(entry.getValue().toString(), ErrorContext.FIELD);
                    property.setValue(entry.getValue().toString());

                    properties.add(property);
                }

                // set the error and bind it back to the UI
                controllerState.getBindingResult().rejectValue(path, "", properties.toArray(), validationMessage);
                model.setAction(NdsAction.NONE);
            }
        }
    }

    private int deliveredResourcesSize(AbstractNdsMvcModel model) {
        if (deliveredResourcesPath != null) {
            @SuppressWarnings("unchecked")
            List<FileDetails> deliveredResources = (List<FileDetails>) new ModelField().setPath(
                    deliveredResourcesPath).getValue(model);
            return deliveredResources.size();
        }
        return 0;
    }

    void uploadFile(ControllerState<?> controllerState, Upload upload, FileDetails fileDetails,
            AbstractNdsMvcModel model) {

        // Store the file details for the benefit of validation, but don't add it to the uploaded file list
        // yet because the ESB store action might fail.
        if (upload != null) {
            upload.setTentative(fileDetails);
        }

        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                FileAddNdsResponse.class);
        if (targetSlice != null) {
            uiSvcClient.setTargetSlice(targetSlice);
        }

        FileAddNdsRequest fileAddNdsRequest = new FileAddNdsRequest();
        fileAddNdsRequest.setContentType(fileDetails.getContentType());
        fileAddNdsRequest.setFileName(fileDetails.getFileName());
        fileAddNdsRequest.setSource(fileDetails.getSource());
        fileAddNdsRequest.setFileSize(fileDetails.getFileSize());

        // Request the ESB stores the uploaded file.
        if (!controllerState.isSyncController()) {
            uiSvcClient.post(fileUploadServiceEndPoint, fileAddNdsRequest);
        } else {
            uiSvcClient.postSync(fileUploadServiceEndPoint, fileAddNdsRequest);
        }

        uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

            @Override
            public boolean consume(NdsResponse response) {

                if (!(response instanceof FileAddNdsResponse)) {
                    model.setPostSubmitMessage(null);  // defer message construction back to controller
                    return false;
                }
                FileAddNdsResponse fileAddNdsResponse = ((FileAddNdsResponse) (response));
                if (fileAddNdsResponse.isSuccess()) {

                    fileDetails.setFileId(fileAddNdsResponse.getObjectId());
                    fileDetails.setSource(null);

                    if (upload != null) {
                        // If the app can upload multiple files of a kind (e.g. PE), then these are stored
                        // in an "upload" object
                        // Add the newly uploaded file to to existing list
                        upload.add(fileDetails);
                    } else {
                        // If the app only stores one file of each kind (e.g. BB), then there's no need for
                        // an "upload" container
                        // and the fileDetails object is stored directly in the model
                        new ModelField().setPath(path).setValue(model, fileDetails);
                    }
                    return true;
                }
                return false;

            }
        });

        controllerState.registerUiServiceClient(uiSvcClient);

    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public UploadPropertiesLoader getUploadPropertiesLoader() {
        return uploadPropertiesLoader;
    }

    public void setUploadPropertiesLoader(UploadPropertiesLoader uploadPropertiesLoader) {
        this.uploadPropertiesLoader = uploadPropertiesLoader;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMultipartPath() {
        return multipartPath;
    }

    public void setMultipartPath(String multipartPath) {
        this.multipartPath = multipartPath;
    }

    public String getMatchButton() {
        return matchButton;
    }

    public void setMatchButton(String matchButton) {
        this.matchButton = matchButton;
    }

    public String getDeliveredResourcesPath() {
        return deliveredResourcesPath;
    }

    public void setDeliveredResourcesPath(String deliveredResourcesPath) {
        this.deliveredResourcesPath = deliveredResourcesPath;
    }

    public String getTargetSlice() {
        return targetSlice;
    }

    public void setTargetSlice(String targetSlice) {
        this.targetSlice = targetSlice;
    }
}
