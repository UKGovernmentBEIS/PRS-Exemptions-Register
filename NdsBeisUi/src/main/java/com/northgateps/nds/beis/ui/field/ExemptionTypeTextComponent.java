package com.northgateps.nds.beis.ui.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import com.northgateps.nds.beis.api.ExemptionTypeDetails;
import com.northgateps.nds.beis.api.getprsexemptionrefdata.GetPrsExemptionRefDataNdsRequest;
import com.northgateps.nds.beis.api.getprsexemptionrefdata.GetPrsExemptionRefDataNdsResponse;
import com.northgateps.nds.beis.ui.model.ExemptionTypeText;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.component.FieldComponent;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.field.NamedEnumerationList;
import com.northgateps.nds.platform.ui.field.NamedEnumerationListLoader;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;
import com.northgateps.nds.platform.ui.view.ViewMessage;

/**
 * Requests the ESB supply a exemption type text list.
 */
public class ExemptionTypeTextComponent implements FieldComponent, NamedEnumerationListLoader {

    private static final NdsLogger logger = NdsLogger.getLogger(ExemptionTypeTextComponent.class);

    private Map<String, NamedEnumerationList> namedEnumerationLists = new HashMap<String, NamedEnumerationList>();

    private String serviceEndPoint;

    public ExemptionTypeTextComponent() {

        try {
            serviceEndPoint = ConfigurationFactory.getConfiguration().getString("esbEndpoint.getPrsExemptionRefData");
        } catch (NoSuchElementException e) {
            // If this occurs, then it just means that the service is not available for this application
            // In itself that's not a problem, so defer any error throwing to an attempt to use this endPoint
        }
    }

    @Override
    public void add(NamedEnumerationList list) {
        // keep track of the data we want to get
        namedEnumerationLists.put(list.getListName(), list);
    }

    /** 
     * Get the ExemptionTypeText reference data from the ESB.
     * Calls namedEnumerationList.getBeanWrapper() to update the model and cache.
     */
    @Override
    public void prepare(AbstractNdsMvcModel model, ControllerState<?> controllerState, String targetSlice) {
        for (NamedEnumerationList namedEnumerationList : namedEnumerationLists.values()) {

            // create and post the request
            GetPrsExemptionRefDataNdsRequest submitRequest = new GetPrsExemptionRefDataNdsRequest();

            final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                    GetPrsExemptionRefDataNdsResponse.class);

            // load the list before calling the onAfterResolvedView handlers
            uiSvcClient.setTargetSlice("before-prepare-view");
            
            if (!controllerState.isSyncController()) {
                uiSvcClient.post(serviceEndPoint, submitRequest);
            } else {
                uiSvcClient.postSync(serviceEndPoint, submitRequest);
            }
            
            uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {
 
                @Override
                public boolean consume(NdsResponse response) {
                	
                    if (!(response instanceof GetPrsExemptionRefDataNdsResponse)) {
                        return false;
                    }

                    GetPrsExemptionRefDataNdsResponse fetchResponse = (GetPrsExemptionRefDataNdsResponse) response;
                    
                    if (fetchResponse.getNdsMessages() != null) {
                        model.setPostSubmitMessage(new ViewMessage("Error_ESB_withCausedBy", fetchResponse.getNdsMessages()));
                    } else {
                    	
                        try {
                        	
                            // convert the returned list to the UI list
                            List<ExemptionTypeDetails> returnedValues = fetchResponse.getExemptionTypeTextList();
                            ArrayList<ExemptionTypeText> values = new ArrayList<ExemptionTypeText>();
                            
                            for (int i = 0; i < returnedValues.size(); i++) {
                                values.add(convertExemptionTypeText(returnedValues.get(i)));
                            }

                            // update the model and cache, effectively calls NamedEnumerationListProxy.setCvList()
                            namedEnumerationList.getBeanWrapper().setPropertyValue(namedEnumerationList.getName(), values);
                        } catch (Exception e) {
                            logger.error("Could not set reference data set", e);
                        }
                    }
                    return true;
                }
            });
            controllerState.registerUiServiceClient(uiSvcClient);
        }
    }

    /* Convert the exemption type text */
    private ExemptionTypeText convertExemptionTypeText(ExemptionTypeDetails exemptionTypeDetails) {
        ExemptionTypeText exemptionTypeText = new ExemptionTypeText();
        exemptionTypeText.setService(exemptionTypeDetails.getService());
        exemptionTypeText.setPwsDescription(exemptionTypeDetails.getPwsDescription());
        exemptionTypeText.setCode(exemptionTypeDetails.getCode());
        exemptionTypeText.setDescription(exemptionTypeDetails.getDescription());
        exemptionTypeText.setPwsText(exemptionTypeDetails.getPwsText());
        exemptionTypeText.setStartDateRequired(exemptionTypeDetails.getStartDateRequired());
        exemptionTypeText.setStartDatePwsLabel(exemptionTypeDetails.getStartDatePwsLabel());
        exemptionTypeText.setStartDatePwsText(exemptionTypeDetails.getStartDatePwsText());
        exemptionTypeText.setSequence(exemptionTypeDetails.getSequence());
        exemptionTypeText.setTextPwsLabel(exemptionTypeDetails.getTextPwsLabel());
        exemptionTypeText.setTextPwsText(exemptionTypeDetails.getTextPwsText());
        exemptionTypeText.setTextRequired(exemptionTypeDetails.getTextRequired());
        exemptionTypeText.setDurationUnit(exemptionTypeDetails.getDurationUnit());
        exemptionTypeText.setDuration(exemptionTypeDetails.getDuration());
        exemptionTypeText.setDocumentsRequired(exemptionTypeDetails.getDocumentsRequired());
        exemptionTypeText.setMaxDocuments(exemptionTypeDetails.getMaxDocuments());
        exemptionTypeText.setMinDocuments(exemptionTypeDetails.getMinDocuments());
        exemptionTypeText.setDocumentsPwsLabel(exemptionTypeDetails.getDocumentsPwsLabel());
        exemptionTypeText.setDocumentsPwsText(exemptionTypeDetails.getDocumentsPwsText());
        exemptionTypeText.setFrvPwsLabel(exemptionTypeDetails.getFrvPwsLabel());
        exemptionTypeText.setFrvPwsText(exemptionTypeDetails.getFrvPwsText());
        exemptionTypeText.setFrvDomain(exemptionTypeDetails.getFrvDomain());
        exemptionTypeText.setFrvRequired(exemptionTypeDetails.getFrvRequired());
        exemptionTypeText.setExemptionTypeLovList(exemptionTypeDetails.getExemptionTypeLovList());
        exemptionTypeText.setConfirmationPagetitle(exemptionTypeDetails.getConfirmationPagetitle());
        exemptionTypeText.setConfirmationcheckbox(exemptionTypeDetails.getConfirmationcheckbox());
        exemptionTypeText.setConfirmationwording(exemptionTypeDetails.getConfirmationwording());
        return exemptionTypeText;
    }

}
