package com.northgateps.nds.beis.api.retrieveregistereddetails;

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
 * NDS REST service interface that retrieves registered user details of a user 
 *
 */
@Path("/retrieveRegisteredDetailsService")
public class RetrieveRegisteredDetailsServiceInterface {
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @ElementClass(request=RetrieveRegisteredDetailsNdsRequest.class, response=RetrieveRegisteredDetailsNdsResponse.class)
    @Path("/retrieveRegisteredDetails/")
    @Descriptions({
            @Description(value = "Request to retrieve registered user details", target = DocTarget.METHOD),
            @Description(value = "Success response or unsuccess response with errors", target = DocTarget.RETURN),
            @Description(value = "Request", target = DocTarget.REQUEST),
            @Description(value = "Response", target = DocTarget.RESPONSE),
            @Description(value = "Resource", target = DocTarget.RESOURCE) })
    public RetrieveRegisteredDetailsNdsResponse retrieveRegisteredDetails (RetrieveRegisteredDetailsNdsRequest request) {
        return null;
    }
}
  