package com.northgateps.nds.beis.ui.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.northgateps.nds.beis.ui.controller.handler.PRSExemptionSearchHandler;
import com.northgateps.nds.beis.ui.controller.handler.PrintExemptionDetailsUtility;
import com.northgateps.nds.beis.ui.controller.handler.SearchGdarGdipHandler;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.api.FileDetails;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.controller.PlatformApplicationController;
import com.northgateps.nds.platform.ui.controller.handler.FileReadUtility;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;

/**
 * Controls the page processing, the HTML master controller.
 */
@Controller
public class BeisController extends PlatformApplicationController {

    protected final NdsLogger logger = NdsLogger.getLogger(getClass());

    @Override
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        super.initBinder(binder);
    }

    @Override
    protected void addControllerStateAttributes(Map<String, Object> attributes){
    	
    	// put the feedback url in the controller attributes
        attributes.put("feedbackEmail", ConfigurationFactory.getConfiguration().getString("feedback.email"));
        
        attributes.put("greenDealFeedbackEmail", ConfigurationFactory.getConfiguration().getString("greenDeal.feedback.email"));
        
        //used service before urls
        attributes.put("usedBeforeMinStandardUrl", ConfigurationFactory.getConfiguration().getString("usedServiceBefore.minStandardsUrl"));
 
        //dashboard urls
        attributes.put("moreAboutExemptionsUrl", ConfigurationFactory.getConfiguration().getString("moreAboutExemptions.url"));
        
        //register Search Exemptions url
        attributes.put("registerSearchExemptionsUrl", ConfigurationFactory.getConfiguration().getString("registerSearchExemptions.moreAboutExemptions.url"));
        
        //register Search Penalties url
        attributes.put("registerSearchPenaltiesUrl", ConfigurationFactory.getConfiguration().getString("registerSearchPenalties.moreAboutPenalties.url"));
        
        //GOV UK site
        attributes.put("govUkUrl", ConfigurationFactory.getConfiguration().getString("govUk.url"));
        attributes.put("contactInfoEmailAddress", ConfigurationFactory.getConfiguration().getString("contactInfoEmail.address"));
    }
    
    /**
     * Handler for index.htm GET and POST non-ajax requests
     * 
     * @param model - The entire model
     * @param page - A specific page request via the requested URL, only supported in prototyping mode
     * @return A model and view, when call can be completed synchronously, or
     *         otherwise, a callable object so that the request doesn't block
     *         the thread wailing for a back end service to complete.
     */
    @RequestMapping(value = { "/*.htm" })
    public Object handleRequest(final @ModelAttribute("command") BeisAllModel model, final BindingResult bindingResult,
            HttpServletResponse response) throws ServletException, IOException {

        return handleRequest(model, bindingResult, null, response);
    }

    /**
     * Handle the form response from the used-service-before page and redirect to the dashboard
     * This allows us to do a redirect to dashboard which triggers the redirect to CAS
     */
    @RequestMapping(value = { "/used-service-handler" })
    public Object handleUsedServiceBeforeRequest(final @ModelAttribute("command") BeisAllModel model, final BindingResult bindingResult,
            HttpServletResponse response) throws ServletException, IOException {

    	UiData data = null;
		
		if( model != null ){
			data = (UiData)model.getUiData();		
		
			if( data != null &&
				data.getUsedServiceBefore() != null &&
				data.getUsedServiceBefore() )
			{
				//redirect to the dashboard via the logon page
				response.sendRedirect("personalised-dashboard?tenant=" + model.getTenant());
				return null;
			}
		}
		
		//use the sitemap for progress
		return handleRequest(model, bindingResult, null, response);		
    }
    
    /**
     * Handler for GET and POST non-ajax requests. Named entry point.
     * 
     * @see processRequest
     */
    @RequestMapping(value = "/{entryPoint}", method = RequestMethod.GET)
    public Object handleRequest(final @ModelAttribute("command") @Validated BeisAllModel model,
            BindingResult bindingResult, @PathVariable String entryPoint, HttpServletResponse response)
                    throws ServletException, IOException {
    	    	
        setCacheControl(CacheControl.noStore());
        prepareResponse(response);

        return processRequest(model, bindingResult, entryPoint);
    }

    @RequestMapping(value = "/download-document/{id}")
    public void getFile(HttpServletResponse response, final @ModelAttribute("command") @Validated BeisAllModel allModel,
            BindingResult bindingResult, @PathVariable("id") String fileID) throws IOException {

        final ControllerState<?> controllerState = new ControllerState<AbstractNdsMvcModel>(this);
        controllerState.setSyncController(isSyncController());
        controllerState.initialisePrototypeMode();
        controllerState.setBindingResult(bindingResult);

        FileDetails fileDetails = FileReadUtility.readFile(fileID, allModel, controllerState);

        if (fileDetails != null) {
            response.setContentType(fileDetails.getContentType());
            response.setHeader("Content-Disposition", "inline; filename=\"" + fileDetails.getFileName() + "\"");            
            response.setStatus(HttpServletResponse.SC_OK);
            byte[] buffer = fileDetails.getSource();
            InputStream in1 = new ByteArrayInputStream(buffer);
            final int copied = IOUtils.copy(in1, response.getOutputStream());
            System.out.println(copied);
            response.flushBuffer();

        }
    }

    private ResponseEntity<?> downloadFile(FileDetails fileDetails) throws IOException {
        if (fileDetails != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(fileDetails.getContentType()));
            headers.setContentDispositionFormData(fileDetails.getFileName(), fileDetails.getFileName());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(fileDetails.getSource(), headers, HttpStatus.OK);
        }

        return null;
    }

    @RequestMapping(value = "/generate-exemption-details")
    public ResponseEntity<?> generateExemptionDetails(HttpServletResponse response,
            final @ModelAttribute("command") @Validated BeisAllModel allModel, BindingResult bindingResult) {

        final ControllerState<?> controllerState = new ControllerState<>(this);
        controllerState.setSyncController(isSyncController());
        controllerState.initialisePrototypeMode();
        controllerState.setBindingResult(bindingResult);

        ResponseEntity<?> responseEntity = null;
        try {
            responseEntity = downloadFile(PrintExemptionDetailsUtility.generateReport(allModel, controllerState));
        } catch (IOException ioe) {
            logger.error("Unable to generate exemption details report: " + ioe.getMessage());
            // there's nothing a user will be able to do with an error here, so the default behaviour of the
            // responseEntity being left as null is fine.
        }

        // In the case of there not being a file to download (e.g. due to error), redirect to an error page.
        if (responseEntity == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "personalised-print-exemption-summary-error");
            responseEntity = new ResponseEntity<String>(headers, HttpStatus.FOUND);
        }

        return responseEntity;
    }
    
    @RequestMapping(value = "/download-gdip-gdar/{searchTerm}")
	public Object downloadGdarGdip(HttpServletResponse response,
			final @ModelAttribute("command") @Validated BeisAllModel allModel, BindingResult bindingResult,
			@PathVariable("searchTerm") String searchTerm)
			throws ServletException, IOException {
			
		//do a search on the file to get the file content and then download the file
		final ControllerState<?> controllerState = new ControllerState<>(this);
		controllerState.setSyncController(isSyncController());
		controllerState.initialisePrototypeMode();
		controllerState.setBindingResult(bindingResult);
		
		FileDetails details = SearchGdarGdipHandler.performSearch(searchTerm, controllerState);
		
		if (details != null){			
			return downloadFile(details);
		} else{		
			//direct to the gdip gdar seach page as no file could be found
			return handleRequest(allModel, bindingResult, "register-search-gdip-gdar", response );
		}		 
	}
    
    
    @RequestMapping(value = "/download-epc-file/{exemptionRefNo}")
    public Object downloadEPC(HttpServletResponse response,
            final @ModelAttribute("command") @Validated BeisAllModel allModel, BindingResult bindingResult,
            @PathVariable("exemptionRefNo") String exemptionRefNo)
            throws ServletException, IOException {
            
        //do a search on the file to get the file content and then download the file
        final ControllerState<?> controllerState = new ControllerState<>(this);
        controllerState.setSyncController(isSyncController());
        controllerState.initialisePrototypeMode();
        controllerState.setBindingResult(bindingResult);
        
        FileDetails details = PRSExemptionSearchHandler.performSearch(exemptionRefNo, controllerState);
        
        if( details != null){           
            return downloadFile(details);
        }
        else{       
            //direct to the gdip gdar seach page as no file could be found
            return handleRequest(allModel, bindingResult, "register-exemptions", response );
        }        
    }
    
    
    @Override
    protected boolean isSyncController() {
        return false;
    }
}
