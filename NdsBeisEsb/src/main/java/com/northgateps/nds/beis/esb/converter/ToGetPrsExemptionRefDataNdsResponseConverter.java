package com.northgateps.nds.beis.esb.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.ExemptionTypeDetails;
import com.northgateps.nds.beis.api.ExemptionTypeLov;
import com.northgateps.nds.beis.api.getprsexemptionrefdata.GetPrsExemptionRefDataNdsResponse;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse.ServiceDetails.ServiceDetail;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse.ServiceDetails.ServiceDetail.ExemptionTypes;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse.ServiceDetails.ServiceDetail.ExemptionTypes.ExemptionType;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse.ServiceDetails.ServiceDetail.ExemptionTypes.ExemptionType.ExemptionOptions;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse.ServiceDetails.ServiceDetail.ExemptionTypes.ExemptionType.ExemptionOptions.ExemptionOption;
import com.northgateps.nds.beis.esb.util.BeisHtmlSanitizer;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converter class to convert an external back office object to a standard NDS object.
 * From GetPRSExemptionReferenceDataResponse to GetPrsExemptionRefDataNdsResponse.
 */
@Converter
@DoNotWeaveLoggingSystem
public class ToGetPrsExemptionRefDataNdsResponseConverter {

    private static final NdsLogger logger = NdsLogger.getLogger(ToGetPrsExemptionRefDataNdsResponseConverter.class);

    @Converter
    public static GetPrsExemptionRefDataNdsResponse converting(
            GetPRSExemptionReferenceDataResponse getPRSExemptionReferenceDataResponse) {

        logger.info("Conversion of back office response object into NDS response object started.");

        GetPrsExemptionRefDataNdsResponse getPrsExemptionRefDataNdsResponse = getNdsResponseFromBackOfficeResponse(
                getPRSExemptionReferenceDataResponse);

        getPrsExemptionRefDataNdsResponse.setSuccess(getPRSExemptionReferenceDataResponse.isSuccess());

        ToNdsMessagesConverter.SetNdsMessages(getPrsExemptionRefDataNdsResponse,
                getPRSExemptionReferenceDataResponse.getMessages());

        logger.info("Conversion of back office response object into NDS response object completed.");
        return getPrsExemptionRefDataNdsResponse;
    }

    private static GetPrsExemptionRefDataNdsResponse getNdsResponseFromBackOfficeResponse(
            GetPRSExemptionReferenceDataResponse getPRSExemptionReferenceDataResponse) {

        GetPrsExemptionRefDataNdsResponse getPrsExemptionRefDataNdsResponse = new GetPrsExemptionRefDataNdsResponse();
        List<ExemptionTypeDetails> exemptionTypeTextList = new ArrayList<ExemptionTypeDetails>();

        if (getPRSExemptionReferenceDataResponse.getServiceDetails() != null
                && getPRSExemptionReferenceDataResponse.getServiceDetails().getServiceDetail() != null) {

            List<ServiceDetail> serviceDetailsList = getPRSExemptionReferenceDataResponse.getServiceDetails().getServiceDetail();

            for (ServiceDetail serviceDetail : serviceDetailsList) {

                ExemptionTypes exemptionTypes = serviceDetail.getExemptionTypes();

                if (exemptionTypes != null && exemptionTypes.getExemptionType() != null) {
                    List<ExemptionType> exemptionTypeList = exemptionTypes.getExemptionType();

                    for (ExemptionType exemptionType : exemptionTypeList) {

                        ExemptionTypeDetails exemptionTypeDetails = new ExemptionTypeDetails();
                        exemptionTypeDetails.setSequence(exemptionType.getSequence().toString());
                        exemptionTypeDetails.setService(serviceDetail.getService());
                        exemptionTypeDetails.setCode(exemptionType.getExemptionReasonCode());
                        exemptionTypeDetails.setDescription(exemptionType.getDescription());
                        exemptionTypeDetails.setPwsDescription(exemptionType.getPWSDescription());
                        exemptionTypeDetails.setPwsText(BeisHtmlSanitizer.sanitizeHtml(exemptionType.getPWSText()));
                        exemptionTypeDetails.setDurationUnit(exemptionType.getDurationUnit());

                        exemptionTypeDetails.setDuration((exemptionType.getDuration().intValue() > 0)
                                ? exemptionType.getDuration().toString() : "0");
                        exemptionTypeDetails.setMinDocuments((exemptionType.getMinDocuments().intValue() > 0)
                                ? exemptionType.getMinDocuments().toString() : "0");
                        exemptionTypeDetails.setMaxDocuments((exemptionType.getMaxDocuments().intValue() > 0)
                                ? exemptionType.getMaxDocuments().toString() : "0");

                        if (exemptionType.isDocumentsRequired()) {
                            exemptionTypeDetails.setDocumentsRequired(
                                    String.valueOf(exemptionType.isDocumentsRequired()));
                        } else {
                            exemptionTypeDetails.setDocumentsRequired(String.valueOf(false));
                        }

                        exemptionTypeDetails.setDocumentsPwsLabel(exemptionType.getDocumentsPWSLabel());
                        exemptionTypeDetails.setDocumentsPwsText(
                                BeisHtmlSanitizer.sanitizeHtml(exemptionType.getDocumentsPWSText()));

                        if (exemptionType.isTextRequired()) {
                            exemptionTypeDetails.setTextRequired(String.valueOf(exemptionType.isTextRequired()));
                        } else {
                            exemptionTypeDetails.setTextRequired(String.valueOf(false));
                        }

                        exemptionTypeDetails.setTextPwsLabel(exemptionType.getTextPWSLabel());
                        exemptionTypeDetails.setTextPwsText(exemptionType.getTextPWSText());

                        if (exemptionType.isStartDateRequired()) {
                            exemptionTypeDetails.setStartDateRequired(
                                    String.valueOf(exemptionType.isStartDateRequired()));
                        } else {
                            exemptionTypeDetails.setStartDateRequired(String.valueOf(false));
                        }

                        exemptionTypeDetails.setStartDatePwsLabel(exemptionType.getStartDatePWSLabel());
                        exemptionTypeDetails.setStartDatePwsText(exemptionType.getStartDatePWSText());

                        if (exemptionType.isFRVRequired()) {
                            exemptionTypeDetails.setFrvRequired(String.valueOf(exemptionType.isFRVRequired()));
                        } else {
                            exemptionTypeDetails.setFrvRequired(String.valueOf(false));
                        }

                        exemptionTypeDetails.setFrvPwsLabel(exemptionType.getFRVPWSLabel());
                        exemptionTypeDetails.setFrvPwsText(exemptionType.getFRVPWSText());
                        exemptionTypeDetails.setFrvDomain(exemptionType.getFRVDomain());

                        ExemptionOptions exemptionOptions = exemptionType.getExemptionOptions();
                        if (exemptionOptions!=null && exemptionOptions.getExemptionOption() != null) {
                            List<ExemptionOption> exemptionOptionList = exemptionOptions.getExemptionOption();
                            List<ExemptionTypeLov> exemptionTypeLovList = new ArrayList<ExemptionTypeLov>();
                            for (ExemptionOption exemptionOption : exemptionOptionList) {
                                ExemptionTypeLov ExemptionTypeLov = new ExemptionTypeLov();
                                ExemptionTypeLov.setService(serviceDetail.getService());
                                ExemptionTypeLov.setDomain(exemptionType.getFRVDomain());
                                ExemptionTypeLov.setCode(exemptionOption.getCode());
                                ExemptionTypeLov.setDescription(exemptionOption.getDescription());
                                ExemptionTypeLov.setText(BeisHtmlSanitizer.sanitizeHtml(
                                        (exemptionOption.getPWSDescriptionTranslations().getTranslation().get(
                                                0) != null)
                                                        ? exemptionOption.getPWSDescriptionTranslations().getTranslation().get(
                                                                0).getText()
                                                        : ""));
                                ExemptionTypeLov.setSequence(exemptionOption.getSequence().toString());
                                exemptionTypeLovList.add(ExemptionTypeLov);
                            }
                            
                            Collections.sort(exemptionTypeLovList, new Comparator<ExemptionTypeLov>() {
                                public int compare(ExemptionTypeLov eLov1, ExemptionTypeLov eLov2) {
                                    Integer seq1 = new Integer(eLov1.getSequence());
                                    Integer seq2 = new Integer(eLov2.getSequence());
                                    return seq1.compareTo(seq2);
                                }
                            });
                            
                            exemptionTypeDetails.setExemptionTypeLovList(exemptionTypeLovList);
                        }
                        
						if (exemptionType.getExemptionConfirmation() != null) {
							exemptionTypeDetails.setConfirmationPagetitle(
									exemptionType.getExemptionConfirmation().getConfirmationPagetitle());
							exemptionTypeDetails.setConfirmationcheckbox(
									exemptionType.getExemptionConfirmation().getConfirmationcheckbox());
							exemptionTypeDetails.setConfirmationwording(
									exemptionType.getExemptionConfirmation().getConfirmationwording());
						}
                        
                        exemptionTypeTextList.add(exemptionTypeDetails);

                    }
                }

            }
            getPrsExemptionRefDataNdsResponse.setExemptionTypeTextList(exemptionTypeTextList);
        }
        return getPrsExemptionRefDataNdsResponse;
    }
}
