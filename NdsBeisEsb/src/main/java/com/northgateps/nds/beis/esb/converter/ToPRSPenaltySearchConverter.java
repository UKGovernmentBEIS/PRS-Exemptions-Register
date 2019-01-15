
package com.northgateps.nds.beis.esb.converter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Converter;
import org.apache.commons.lang.StringUtils;

import com.northgateps.nds.beis.api.GetPenaltySearchResponseDetail;
import com.northgateps.nds.beis.api.PRSPenaltySearchNdsRequest;
import com.northgateps.nds.beis.api.PRSPenaltySearchNdsResponse;
import com.northgateps.nds.beis.api.PenaltyData;
import com.northgateps.nds.beis.backoffice.service.prspenaltysearch.PRSPenaltySearchRequest;
import com.northgateps.nds.beis.backoffice.service.prspenaltysearch.PRSPenaltySearchResponse;
import com.northgateps.nds.beis.backoffice.service.prspenaltysearch.PRSPenaltySearchResponse.Penalties.Penalty;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converts between a NDS object and an external back office object
 * 
 */
@Converter
@DoNotWeaveLoggingSystem
public final class ToPRSPenaltySearchConverter {

    private static final NdsLogger logger = NdsLogger.getLogger(ToPRSPenaltySearchConverter.class);

    /**
     * Convert the passed in PRSPenaltySearchRequest to the passed out Penalty search result
     * 
     * @param class PRSPenaltySearchNdsRequest
     * @return
     *         class PRSPenaltySearchRequest
     */
    @Converter
    public static PRSPenaltySearchRequest converting(PRSPenaltySearchNdsRequest prsPenaltySearchNdsRequest) {

        logger.info("Starting Conversion For PRSPenaltySearchNdsRequest");

        /* Class being converted to */
        PRSPenaltySearchRequest prsPenaltySearchRequest = new PRSPenaltySearchRequest();

        prsPenaltySearchRequest.setLanguageCode(prsPenaltySearchNdsRequest.getPenaltySearch().getLanguageCode());

        prsPenaltySearchRequest.setPostcode(
                prsPenaltySearchNdsRequest.getPenaltySearch().getPenaltyPostcodesCriteria());

        prsPenaltySearchRequest.setLandlordName(
                prsPenaltySearchNdsRequest.getPenaltySearch().getPenaltyLandlordsNameCriteria());

        prsPenaltySearchRequest.setPropertyDetails(
                prsPenaltySearchNdsRequest.getPenaltySearch().getRentalPropertyDetails());

        prsPenaltySearchRequest.setPropertyTown(prsPenaltySearchNdsRequest.getPenaltySearch().getTown());

        if (!StringUtils.equals(prsPenaltySearchNdsRequest.getPenaltySearch().getPropertyType(), "ALL")) {
            prsPenaltySearchRequest.setService(prsPenaltySearchNdsRequest.getPenaltySearch().getPropertyType());
        }

        if ((!StringUtils.equals(prsPenaltySearchNdsRequest.getPenaltySearch().getPenaltyType_PRSD(), "ALL"))
                && StringUtils.equals(prsPenaltySearchNdsRequest.getPenaltySearch().getPropertyType(), "PRSD")) {
            prsPenaltySearchRequest.setPenaltyReasonCode(
                    prsPenaltySearchNdsRequest.getPenaltySearch().getPenaltyType_PRSD());
        }

        if ((!StringUtils.equals(prsPenaltySearchNdsRequest.getPenaltySearch().getPenaltyType_PRSN(), "ALL"))
                && StringUtils.equals(prsPenaltySearchNdsRequest.getPenaltySearch().getPropertyType(), "PRSN")) {
            prsPenaltySearchRequest.setPenaltyReasonCode(
                    prsPenaltySearchNdsRequest.getPenaltySearch().getPenaltyType_PRSN());
        }
        
        if (!StringUtils.isEmpty(prsPenaltySearchNdsRequest.getPenaltySearch().getPenaltyRefNo())) {
            prsPenaltySearchRequest.setPenaltyRefNo(
                    new BigInteger(prsPenaltySearchNdsRequest.getPenaltySearch().getPenaltyRefNo()));
        }

        logger.info("Finished Conversion For PRSPenaltySearchNdsRequest");

        return prsPenaltySearchRequest;
    }

    @Converter
    public static PRSPenaltySearchNdsResponse converting(PRSPenaltySearchResponse prsPenaltySearchResponse) {

        logger.info("Starting Conversion For PRSPenaltySearchResponse");

        /* Class being converted to */
        PRSPenaltySearchNdsResponse prsPenaltySearchNdsResponse = new PRSPenaltySearchNdsResponse();
        List<PenaltyData> penaltyDataList = new ArrayList<PenaltyData>();
        prsPenaltySearchNdsResponse.setSuccess(prsPenaltySearchResponse.isSuccess());
        PenaltyData penaltyData = null;
        GetPenaltySearchResponseDetail getPenaltySearchResponseDetail = new GetPenaltySearchResponseDetail();
        if (prsPenaltySearchNdsResponse.isSuccess()) {
            if (prsPenaltySearchResponse.getPenalties() != null) {
                for (Penalty penalty : prsPenaltySearchResponse.getPenalties().getPenalty()) {
                    penaltyData = new PenaltyData();
                    penaltyData.setAddress(penalty.getAddress());
                    penaltyData.setPenaltyReasonCode(penalty.getPenaltyReasonCode());
                    penaltyData.setPenaltyRefNo(penalty.getPenaltyRefNo());
                    penaltyData.setLandLordName(penalty.getLandLordName());
                    penaltyData.setPwsDescription(penalty.getPWSDescription());
                    penaltyData.setService(penalty.getService());
                    penaltyData.setAmount(penalty.getAmount());
                    penaltyDataList.add(penaltyData);

                }
                if (penaltyDataList.size() > 0) {
                    getPenaltySearchResponseDetail.setPenalties(penaltyDataList);
                    prsPenaltySearchNdsResponse.setGetPenaltySearchResponseDetail(getPenaltySearchResponseDetail);
                }
            }

        } else {

            ToNdsMessagesConverter.SetNdsMessages(prsPenaltySearchNdsResponse, prsPenaltySearchResponse.getMessages());

        }
        logger.info("Finished Conversion For PRSPenaltySearchResponse");
        return prsPenaltySearchNdsResponse;
    }

}
