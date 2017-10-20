package com.northgateps.nds.beis.api.printexemptiondetails;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.xml.ElementClass;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

/**
 * NDS REST service interface that prints exemption details
 *
 */
@Path("/printExemptionDetailsService")
public class PrintExemptionDetailsServiceInterface {
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @ElementClass(request=PrintExemptionDetailsNdsRequest.class, response=PrintExemptionDetailsNdsResponse.class)
    @Path("/printExemptionDetails")
    @Descriptions({
            @Description(value = "Request to print details of an exemption", target = DocTarget.METHOD),
            @Description(value = "Success response or failure response with errors", target = DocTarget.RETURN),
            @Description(value = "Request", target = DocTarget.REQUEST),
            @Description(value = "Response", target = DocTarget.RESPONSE),
            @Description(value = "Resource", target = DocTarget.RESOURCE) })
    public PrintExemptionDetailsNdsResponse printExemptionDetails(PrintExemptionDetailsNdsRequest request) {
        return null;
    }
}
