package com.northgateps.nds.beis.esb.converter;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.viewdocument.ViewPdfNdsRequest;
import com.northgateps.nds.beis.backoffice.service.viewdocumentgdipgdar.ViewPdf;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converter class to convert a standard NDS object to an external back office object.
 * From ViewPdfNdsRequest to ViewPdf.
 */
@Converter
@DoNotWeaveLoggingSystem
public final class ToViewPdfRequestConverter {

    private static final NdsLogger logger = NdsLogger.getLogger(ToViewPdfRequestConverter.class);
      
  
    @Converter   
    public static ViewPdf converting(ViewPdfNdsRequest viewPdfNdsRequest) throws NdsApplicationException, DatatypeConfigurationException {
        
        logger.info("Starting Conversion For viewPdfNdsRequest");
        
        ViewPdf request = new ViewPdf();
        
        if(viewPdfNdsRequest.getReferenceNumber() != null){
            request.setRRN(viewPdfNdsRequest.getReferenceNumber());
            request.setIncludeDocument(viewPdfNdsRequest.isIncludeDocument());
        }
        logger.info("Finished Conversion For viewPdfNdsRequest");
        
        return request;        
    }

}
