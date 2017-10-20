package com.northgateps.nds.beis.esb.viewdocument;

import org.apache.camel.TypeConverter;

import com.northgateps.nds.beis.api.viewdocument.ViewPdfNdsRequest;
import com.northgateps.nds.beis.api.viewdocument.ViewPdfNdsResponse;
import com.northgateps.nds.beis.backoffice.service.viewdocumentgdipgdar.ViewPdf;
import com.northgateps.nds.beis.backoffice.service.viewdocumentgdipgdar.ViewPdfResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Processes the request to and from view pdf doc
 */
public class ViewDocumentAdapter extends
        NdsSoapAdapter<ViewPdfNdsRequest, ViewPdfNdsResponse, ViewPdf, ViewPdfResponse> {

    private static final NdsLogger logger = NdsLogger.getLogger(ViewDocumentAdapter.class);

    @Override
    protected String getRequestClassName() {
        return ViewPdfNdsRequest.class.getName();
    }

    @Override
    protected String getResponseClassName() {
        return ViewPdfResponse.class.getName();
    }
   
    @Override
    protected ViewPdf doRequestProcess(ViewPdfNdsRequest request, NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {
        ViewPdf viewPdf = new ViewPdf();
        try {

            ndsExchange.setOperationName("ViewPdf");

            TypeConverter converter = ndsExchange.getTypeConverter();
            viewPdf = converter.convertTo(ViewPdf.class, request);

        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during request conversion process:  " + e.getMessage(), e);
        }

        logger.info("Successfully created the SOAP request");
        return viewPdf;
    }

    @Override
    protected ViewPdfNdsResponse doResponseProcess(ViewPdfResponse response,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
        ViewPdfNdsResponse viewPdfNdsResponse = new ViewPdfNdsResponse();
        try {

            TypeConverter converter = ndsExchange.getTypeConverter();
            viewPdfNdsResponse = converter.convertTo(ViewPdfNdsResponse.class, response);

        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during response conversion process:  " + e.getMessage(),
                    e);
        }

        logger.info("Successfully created the NDS response");
        
        return viewPdfNdsResponse;
    }
}
