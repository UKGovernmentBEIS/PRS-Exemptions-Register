package com.northgateps.nds.beis.esb.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.PenaltyTypeDetails;
import com.northgateps.nds.beis.api.getprspenaltyrefdata.GetPrsPenaltyRefDataNdsResponse;
import com.northgateps.nds.beis.backoffice.service.getprspenaltyreferencedata.GetPRSPenaltyReferenceDataResponse;
import com.northgateps.nds.beis.backoffice.service.getprspenaltyreferencedata.GetPRSPenaltyReferenceDataResponse.ServiceDetails.ServiceDetail;
import com.northgateps.nds.beis.backoffice.service.getprspenaltyreferencedata.GetPRSPenaltyReferenceDataResponse.ServiceDetails.ServiceDetail.PenaltyTypes;
import com.northgateps.nds.beis.backoffice.service.getprspenaltyreferencedata.GetPRSPenaltyReferenceDataResponse.ServiceDetails.ServiceDetail.PenaltyTypes.PenaltyType;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Convert the passed in GetPRSPenaltyReferenceDataResponse to the passed out GetPrsPenaltyRefDataNdsResponse
 * 
 * @param com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse
 * @return class com.northgateps.nds.beis.api.getprsexemptionrefdata.GetPrsExemptionRefDataNdsResponse
 * 
 */

@Converter
@DoNotWeaveLoggingSystem
public class ToGetPrsPenaltyReferenceDataNdsResponseConverter {

    private static final NdsLogger logger = NdsLogger.getLogger(ToGetPrsPenaltyReferenceDataNdsResponseConverter.class);

    @Converter
    public static GetPrsPenaltyRefDataNdsResponse converting(
            GetPRSPenaltyReferenceDataResponse getPRSPenaltyReferenceDataResponse) {

        logger.info("Conversion of back office response object into NDS response object started.");

        GetPrsPenaltyRefDataNdsResponse getPrsPenaltyRefDataNdsResponse = getNdsResponseFromBackOfficeResponse(
                getPRSPenaltyReferenceDataResponse);

        getPrsPenaltyRefDataNdsResponse.setSuccess(getPRSPenaltyReferenceDataResponse.isSuccess());

        ToNdsMessagesConverter.SetNdsMessages(getPrsPenaltyRefDataNdsResponse,
                getPRSPenaltyReferenceDataResponse.getMessages());

        logger.info("Conversion of back office response object into NDS response object completed.");
        return getPrsPenaltyRefDataNdsResponse;
    }

    private static GetPrsPenaltyRefDataNdsResponse getNdsResponseFromBackOfficeResponse(
            GetPRSPenaltyReferenceDataResponse getPRSPenaltyReferenceDataResponse) {

        GetPrsPenaltyRefDataNdsResponse getPrsPenaltyRefDataNdsResponse = new GetPrsPenaltyRefDataNdsResponse();
        List<PenaltyTypeDetails> penaltyTypeTextList = new ArrayList<PenaltyTypeDetails>();

        if (getPRSPenaltyReferenceDataResponse.getServiceDetails() != null
                && getPRSPenaltyReferenceDataResponse.getServiceDetails().getServiceDetail() != null) {
            List<ServiceDetail> serviceDetailsList = getPRSPenaltyReferenceDataResponse.getServiceDetails().getServiceDetail();

            for (ServiceDetail serviceDetail : serviceDetailsList) {

                PenaltyTypes penaltyTypes = serviceDetail.getPenaltyTypes();
                if (penaltyTypes != null && penaltyTypes.getPenaltyType() != null) {
                    List<PenaltyType> penaltyTypeList = penaltyTypes.getPenaltyType();

                    for (PenaltyType penaltyType : penaltyTypeList) {
                        PenaltyTypeDetails penaltyTypeDetails = new PenaltyTypeDetails();
                        penaltyTypeDetails.setCode(penaltyType.getPenaltyReasonCode());
                        penaltyTypeDetails.setService(serviceDetail.getService());
                        penaltyTypeDetails.setDescription(penaltyType.getDescription());
                        penaltyTypeDetails.setPwsBreachDescription(penaltyType.getPWSBreachDescription());
                        penaltyTypeDetails.setMaxValue(penaltyType.getMaximumValue().intValue() > 0
                                ? penaltyType.getMaximumValue().toString() : "0");

                        if (penaltyType.isExemptionRequired()) {
                            penaltyTypeDetails.setExemptionRequired(String.valueOf(penaltyType.isExemptionRequired()));

                        } else {
                            penaltyTypeDetails.setExemptionRequired(String.valueOf(false));
                        }

                        penaltyTypeTextList.add(penaltyTypeDetails);
                    }

                }
            }

            getPrsPenaltyRefDataNdsResponse.setPenaltyTypeTextList(penaltyTypeTextList);

        }

        return getPrsPenaltyRefDataNdsResponse;

    }

}
