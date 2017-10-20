package com.northgateps.nds.beis.ui.controller.handler;

import com.northgateps.nds.beis.api.ExemptionDetail;
import com.northgateps.nds.beis.api.ExemptionType;
import com.northgateps.nds.beis.api.ExemptionTypeDetails;
import com.northgateps.nds.beis.api.printexemptiondetails.PrintExemptionDetailsNdsRequest;
import com.northgateps.nds.beis.api.printexemptiondetails.PrintExemptionDetailsNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.api.FileDetails;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;

/**
 * Generate the exemption details report PDF.
 */
public class PrintExemptionDetailsUtility {

    protected final NdsLogger logger = NdsLogger.getLogger(getClass());

    private static final String serviceEndpoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.printExemptionDetails");

    public static FileDetails generateReport(BeisAllModel model, ControllerState<?> controllerState) {
        final BeisAllModel beisModel = model;

        String tenant = beisModel.getTenant();
        ExemptionDetail exemptionDetails = beisModel.getExemptionDetails();

        if (exemptionDetails != null) {
            beisModel.populateDefaults();

            UiData uiData = (UiData) beisModel.getUiData();
            String exemptionReason = uiData.getSelectedExemptionTypeLovText();
            if (exemptionReason != null && !exemptionReason.trim().isEmpty()) {
                exemptionDetails.setExemptionReason(exemptionReason);
            }            
            exemptionDetails.getEpc().getPropertyAddress().setSingleLineAddressPostcode(exemptionDetails.getEpc().getPropertyAddress().getSingleLineAddressPostcode());
            PrintExemptionDetailsNdsRequest svcRequest = new PrintExemptionDetailsNdsRequest();
            svcRequest.setExemptionDetail(exemptionDetails);
            svcRequest.setExemptionType(populateExemptionType(beisModel.getExemptionType()));
            svcRequest.setTenant(tenant);

            UiServiceClient svcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                    PrintExemptionDetailsNdsResponse.class);

            // We need to do this synchronously, otherwise the browser will not receive the file
            svcClient.postSync(serviceEndpoint, svcRequest);

            PrintExemptionDetailsNdsResponse svcResponse = (PrintExemptionDetailsNdsResponse) svcClient.getResponse();

            if (svcResponse != null && svcResponse.isSuccess()) {
                final FileDetails fileDetails = new FileDetails();
                fileDetails.setContentType(svcResponse.getContentType());
                fileDetails.setFileName(svcResponse.getFileName());
                fileDetails.setFileSize(svcResponse.getFileSize());
                fileDetails.setSource(svcResponse.getSource());

                return fileDetails;
            }
        }

        return null;
    }

    private static ExemptionTypeDetails populateExemptionType(ExemptionType exemptionType) {
        /*
         * We need to copy data out of the UiData's ExemptionType (an ExemptionTypeText) into an concrete implementation
         * that we can actually see from the ESB.
         */
        if (exemptionType == null) {
            return null;
        }
        ExemptionTypeDetails details = new ExemptionTypeDetails();

        details.setMinDocuments(exemptionType.getMinDocuments());
        details.setMaxDocuments(exemptionType.getMaxDocuments());
        details.setDocumentsRequired(exemptionType.getDocumentsRequired());
        details.setDocumentsPwsLabel(exemptionType.getDocumentsPwsLabel());
        details.setDocumentsPwsText(exemptionType.getDocumentsPwsText());
        details.setService(exemptionType.getService());
        details.setDescription(exemptionType.getDescription());
        details.setPwsDescription(exemptionType.getPwsDescription());
        details.setCode(exemptionType.getCode());
        details.setPwsText(exemptionType.getPwsText());
        details.setStartDateRequired(exemptionType.getStartDateRequired());
        details.setStartDatePwsLabel(exemptionType.getStartDatePwsLabel());
        details.setStartDatePwsText(exemptionType.getStartDatePwsText());
        details.setSequence(exemptionType.getSequence());
        details.setTextPwsLabel(exemptionType.getTextPwsLabel());
        details.setTextPwsText(exemptionType.getTextPwsText());
        details.setDurationUnit(exemptionType.getDurationUnit());
        details.setDuration(exemptionType.getDuration());
        details.setFrvPwsLabel(exemptionType.getFrvPwsLabel());
        details.setFrvPwsText(exemptionType.getFrvPwsText());
        details.setFrvDomain(exemptionType.getFrvDomain());
        details.setTextRequired(exemptionType.getTextRequired());
        details.setFrvRequired(exemptionType.getFrvRequired());

        return details;
    }
}
