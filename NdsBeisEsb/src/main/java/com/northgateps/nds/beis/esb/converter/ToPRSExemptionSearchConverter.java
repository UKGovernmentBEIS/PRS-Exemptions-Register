package com.northgateps.nds.beis.esb.converter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Converter;
import org.apache.commons.lang3.StringUtils;

import com.northgateps.nds.beis.api.ExemptionData;
import com.northgateps.nds.beis.api.GetExemptionSearchResponseDetail;
import com.northgateps.nds.beis.api.PRSExemptionSearchNdsRequest;
import com.northgateps.nds.beis.api.PRSExemptionSearchNdsResponse;
import com.northgateps.nds.beis.backoffice.service.prsexemptionsearch.PRSExemptionSearchRequest;
import com.northgateps.nds.beis.backoffice.service.prsexemptionsearch.PRSExemptionSearchResponse;
import com.northgateps.nds.beis.backoffice.service.prsexemptionsearch.PRSExemptionSearchResponse.Exemptions.Exemption;

import com.northgateps.nds.platform.esb.converter.XMLGregorianCalendarToZonedDateTimeConverter;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converter class to convert a standard NDS object to an external back office object.
 * From PRSExemptionSearchNdsRequest to PRSExemptionSearchRequest.
 */
@Converter
@DoNotWeaveLoggingSystem
public final class ToPRSExemptionSearchConverter {

    /**
     * Convert the passed in PRSExemptionSearchRequest to the passed out GetLocalAuthorities
     * 
     * @param prsExemptionSearchNdsRequest request to be converted to PRSExemptionSearchNdsRequest
     * @return class PRSExemptionSearchRequest
     */
    @Converter
    public static PRSExemptionSearchRequest converting(PRSExemptionSearchNdsRequest prsExemptionSearchNdsRequest) {

        /* Class being converted to */
        PRSExemptionSearchRequest prsExemptionSearchRequest = new PRSExemptionSearchRequest();

        prsExemptionSearchRequest.setLanguageCode(prsExemptionSearchNdsRequest.getExemptionSearch().getLanguageCode());

        prsExemptionSearchRequest.setPostcode(prsExemptionSearchNdsRequest.getExemptionSearch().getExemptionPostcodeCriteria());

        prsExemptionSearchRequest.setLandlordName(prsExemptionSearchNdsRequest.getExemptionSearch().getExemptionLandlordsNameCriteria());

        prsExemptionSearchRequest.setPropertyDetails(prsExemptionSearchNdsRequest.getExemptionSearch().getExemptPropertyDetails());

        prsExemptionSearchRequest.setPropertyTown(prsExemptionSearchNdsRequest.getExemptionSearch().getTown());
        
        if (!StringUtils.equals(prsExemptionSearchNdsRequest.getExemptionSearch().getService(), "ALL")) {
            prsExemptionSearchRequest.setService(prsExemptionSearchNdsRequest.getExemptionSearch().getService());
        }

        if ((!StringUtils.equals(prsExemptionSearchNdsRequest.getExemptionSearch().getExemptionType_PRSD(), "ALL")) && 
                StringUtils.equals(prsExemptionSearchNdsRequest.getExemptionSearch().getService(), "PRSD")) {
            prsExemptionSearchRequest.setExemptionReasonCode(prsExemptionSearchNdsRequest.getExemptionSearch().getExemptionType_PRSD());
        }
        
        if ((!StringUtils.equals(prsExemptionSearchNdsRequest.getExemptionSearch().getExemptionType_PRSN(), "ALL")) && 
                StringUtils.equals(prsExemptionSearchNdsRequest.getExemptionSearch().getService(), "PRSN")) {
            prsExemptionSearchRequest.setExemptionReasonCode(prsExemptionSearchNdsRequest.getExemptionSearch().getExemptionType_PRSN());
        }
        
        if (!StringUtils.isEmpty(prsExemptionSearchNdsRequest.getExemptionSearch().getExemptionRefNo()))
        {
            prsExemptionSearchRequest.setExemptionRefNo(
                    new BigInteger(prsExemptionSearchNdsRequest.getExemptionSearch().getExemptionRefNo()));
        }
        
        prsExemptionSearchRequest.setEPCRequired(prsExemptionSearchNdsRequest.getExemptionSearch().isEpcRequired());

        // Assign each child object on its parent

        return prsExemptionSearchRequest;
    }

    @Converter
    public static PRSExemptionSearchNdsResponse converting(PRSExemptionSearchResponse prsExemptionSearchResponse) {

        /* Class being converted to */
        PRSExemptionSearchNdsResponse prsExemptionSearchNdsResponse = new PRSExemptionSearchNdsResponse();
        List<ExemptionData> ExemptionDataList = new ArrayList<ExemptionData>();
        prsExemptionSearchNdsResponse.setSuccess(prsExemptionSearchResponse.isSuccess());
        ExemptionData exemptionData = null;
        GetExemptionSearchResponseDetail getExemptionSearchResponseDetail = new GetExemptionSearchResponseDetail();
        if (prsExemptionSearchNdsResponse.isSuccess()) {
            for (Exemption exemption : prsExemptionSearchResponse.getExemptions().getExemption()) {
                exemptionData = new ExemptionData();
                exemptionData.setAddress(exemption.getAddress());
                exemptionData.setEpcBand(exemption.getEPCBand());
                exemptionData.setEpcRating(exemption.getEPCRating());
                exemptionData.setExemptionReasonCode(exemption.getExemptionReasonCode());
                exemptionData.setExemptionRefNo(exemption.getExemptionRefNo().toString());
                exemptionData.setLandLordName(exemption.getLandlordName());
                exemptionData.setPwsDescription(exemption.getPWSDescription());
                exemptionData.setRegisteredDate(
                        XMLGregorianCalendarToZonedDateTimeConverter.convert(exemption.getRegisteredDate()));
                exemptionData.setService(exemption.getService());
                exemptionData.setEpcExists(exemption.isEPCExists());
                exemptionData.setEpcObject(exemption.getEPCObject());
                exemptionData.setMimeType(exemption.getMIMEtype());
                ExemptionDataList.add(exemptionData);

            }
            if (ExemptionDataList.size() > 0) {
                getExemptionSearchResponseDetail.setExemptions(ExemptionDataList);
                prsExemptionSearchNdsResponse.setGetExemptionSearchResponseDetail(getExemptionSearchResponseDetail);
            }

        }

        return prsExemptionSearchNdsResponse;
    }

}
