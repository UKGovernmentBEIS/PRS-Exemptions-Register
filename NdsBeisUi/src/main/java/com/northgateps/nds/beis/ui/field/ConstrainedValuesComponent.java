package com.northgateps.nds.beis.ui.field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.PropertyAccessException;
import com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesNdsRequest;
import com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesNdsResponse;
import com.northgateps.nds.beis.api.getreferencevalues.ReferenceValuesDetails;
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
import com.northgateps.nds.platform.ui.model.ReferenceValue;
import com.northgateps.nds.platform.ui.view.ViewMessage;

/**
 * Requests the ESB supply a reference data list.
 */
public class ConstrainedValuesComponent implements FieldComponent, NamedEnumerationListLoader {

    private static final NdsLogger logger = NdsLogger.getLogger(ConstrainedValuesComponent.class);

    private String serviceEndPoint;

    private Map<String, NamedEnumerationList> namedEnumerationLists = new HashMap<String, NamedEnumerationList>();

    public ConstrainedValuesComponent() {
        serviceEndPoint = ConfigurationFactory.getConfiguration().getString("esbEndpoint.getReferenceValues");
    }

    @Override
    public void add(NamedEnumerationList list) {
        namedEnumerationLists.put(list.getListName(), list);
    }

    @Override
    public void prepare(final AbstractNdsMvcModel model, ControllerState<?> controllerState, String targetSlice) {
        for (NamedEnumerationList namedEnumerationList : namedEnumerationLists.values()) {
            GetReferenceValuesNdsRequest submitRequest = new GetReferenceValuesNdsRequest();
            ReferenceValuesDetails referenceValuesDetails = new ReferenceValuesDetails();
            referenceValuesDetails.setDomainCode("COUNTRIES");
            submitRequest.setReferenceValuesDetails(referenceValuesDetails);

            final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                    GetReferenceValuesNdsResponse.class);

            if (!controllerState.isSyncController()) {
                uiSvcClient.post(serviceEndPoint, submitRequest);
            } else {
                uiSvcClient.postSync(serviceEndPoint, submitRequest);
            }
            uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

                @Override
                public boolean consume(NdsResponse response) {
                    if (!(response instanceof GetReferenceValuesNdsResponse)) {
                        return false;
                    }
                    GetReferenceValuesNdsResponse fetchResponse = ((GetReferenceValuesNdsResponse) (response));

                    // we are already checking response instanceOf GetReferenceValuesNdsResponse, if response is null
                    // then false will be returned.
                    if (fetchResponse.getNdsMessages() != null) {
                        model.setPostSubmitMessage(
                                new ViewMessage("Error_ESB_withCausedBy", fetchResponse.getNdsMessages()));
                    } else {
                        try {
                            // convert the returned list to the UI list
                            if (fetchResponse.getGetReferenceValuesResponseDetails() != null) {
                                List<ReferenceValuesDetails> returnedValues = fetchResponse.getGetReferenceValuesResponseDetails().getReferenceValuesDetails();
                                List<ReferenceValue> values = mapToReferenceValues(returnedValues);

                                namedEnumerationList.getBeanWrapper().setPropertyValue(namedEnumerationList.getName(),
                                        values);
                            }
                        } catch (InvalidPropertyException | IllegalArgumentException | PropertyAccessException e) {
                            logger.error("Could not set reference data set", e);
                        }
                    }

                    // yes because if there will be any errors in response then it will be handled by if part of above
                    // code
                    return true;
                }

                private List<ReferenceValue> mapToReferenceValues(List<ReferenceValuesDetails> referenceValuesDetails) {
                    ArrayList<ReferenceValue> values = new ArrayList<ReferenceValue>();
                    if (referenceValuesDetails != null) {
                        for (int i = 0; i < referenceValuesDetails.size(); i++) {
                            ReferenceValue referenceValue = new ReferenceValue();
                            referenceValue.setCode(referenceValuesDetails.get(i).getCode());
                            referenceValue.setName(referenceValuesDetails.get(i).getName());
                            values.add(referenceValue);
                        }
                        Collections.sort(values, new Comparator<ReferenceValue>() {

                            public int compare(ReferenceValue r1, ReferenceValue r2) {
                                return r1.getName().compareTo(r2.getName());
                            }
                        });
                        ReferenceValue referenceValue = new ReferenceValue();
                        String pleaseSelectKey = "Please_Select";
                        referenceValue.setCode("");
                        referenceValue.setName(controllerState.getBundle().getString(pleaseSelectKey));
                        values.add(0, referenceValue);
                    }
                    return values;
                }
            });
            controllerState.registerUiServiceClient(uiSvcClient);
        }
    }
}
