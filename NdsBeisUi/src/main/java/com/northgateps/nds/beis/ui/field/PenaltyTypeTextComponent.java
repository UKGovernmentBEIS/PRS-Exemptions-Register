package com.northgateps.nds.beis.ui.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import com.northgateps.nds.beis.api.PenaltyTypeDetails;
import com.northgateps.nds.beis.api.getprspenaltyrefdata.GetPrsPenaltyRefDataNdsRequest;
import com.northgateps.nds.beis.api.getprspenaltyrefdata.GetPrsPenaltyRefDataNdsResponse;
import com.northgateps.nds.beis.ui.model.PenaltyTypeText;
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

public class PenaltyTypeTextComponent implements FieldComponent, NamedEnumerationListLoader{
    
    private static final NdsLogger logger = NdsLogger.getLogger(PenaltyTypeTextComponent.class);

    private Map<String, NamedEnumerationList> namedEnumerationLists = new HashMap<String, NamedEnumerationList>();
    
    private String serviceEndPoint;
    
    public PenaltyTypeTextComponent()
    {
        try {
            serviceEndPoint = ConfigurationFactory.getConfiguration().getString("esbEndpoint.getPrsPenaltyRefData");
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

    @Override
    public void prepare(AbstractNdsMvcModel model, ControllerState<?> controllerState, String targetSlice) {
        for (NamedEnumerationList namedEnumerationList : namedEnumerationLists.values()) {
            
         // create and post the request
            GetPrsPenaltyRefDataNdsRequest submitRequest = new GetPrsPenaltyRefDataNdsRequest();
            final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                    GetPrsPenaltyRefDataNdsResponse.class);
            
            if (!controllerState.isSyncController()) {
                uiSvcClient.post(serviceEndPoint, submitRequest);
            } else {
                uiSvcClient.postSync(serviceEndPoint, submitRequest);
            }
            
            uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

                @Override
                public boolean consume(NdsResponse response) {
                    if (!(response instanceof GetPrsPenaltyRefDataNdsResponse)) {
                        return false;
                    }

                    GetPrsPenaltyRefDataNdsResponse fetchResponse = (GetPrsPenaltyRefDataNdsResponse) response;
                    if (fetchResponse.getNdsMessages() != null) {
                        model.setPostSubmitMessage(
                                new ViewMessage("Error_ESB_withCausedBy", fetchResponse.getNdsMessages()));
                    } else {
                        try {
                            // convert the returned list to the UI list
                            List<PenaltyTypeDetails> returnedValues = fetchResponse.getPenaltyTypeTextList();
                            ArrayList<PenaltyTypeText> values = new ArrayList<PenaltyTypeText>();
                            for (int i = 0; i < returnedValues.size(); i++) {
                                values.add(convertPenaltyTypeText(returnedValues.get(i)));
                            }

                            namedEnumerationList.getBeanWrapper().setPropertyValue(namedEnumerationList.getName(),
                                    values);
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
    
    /* Convert the penalty type text */
    private PenaltyTypeText convertPenaltyTypeText(PenaltyTypeDetails penaltyTypeDetails) {
        PenaltyTypeText penaltyTypeText = new PenaltyTypeText();
        penaltyTypeText.setService(penaltyTypeDetails.getService());
        penaltyTypeText.setCode(penaltyTypeDetails.getCode());
        penaltyTypeText.setDescription(penaltyTypeDetails.getDescription());
        penaltyTypeText.setPwsBreachDescription(penaltyTypeDetails.getPwsBreachDescription());
        penaltyTypeText.setMaxValue(penaltyTypeDetails.getMaxValue());
        penaltyTypeText.setExemptionRequired(penaltyTypeDetails.getExemptionRequired());
        return penaltyTypeText;
        
    }


}
