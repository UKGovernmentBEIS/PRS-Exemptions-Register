package com.northgateps.nds.beis.api.viewdocument;

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
 * NDS REST service interface to view pdf
 *
 */
@Path("/ViewPdfService")
public class ViewPdfServiceInterface {
       
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @ElementClass(request=ViewPdfNdsRequest.class, response=ViewPdfNdsResponse.class)
    @Path("/viewPdfDocument/")
    @Descriptions({
            @Description(value = "Request to view pdf for Green Deals", target = DocTarget.METHOD),
            @Description(value = "Success response or failure response with errors", target = DocTarget.RETURN),
            @Description(value = "Request", target = DocTarget.REQUEST),
            @Description(value = "Response", target = DocTarget.RESPONSE),
            @Description(value = "Resource", target = DocTarget.RESOURCE) })
    public ViewPdfNdsResponse viewPdfDocument(ViewPdfNdsRequest request) {
        return null;
    }
}
