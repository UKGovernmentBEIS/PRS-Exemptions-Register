package com.northgateps.nds.beis.api;

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
 * Get List of Penalty from Beis service definition.
 * 
 * Just a definition of the service. No implementation needed.
 * 
 * Needs to be a class so that Spring or CXF can call a default constructor (which it
 * can't do with an interface).
 * 
 */
@Path("/prsPenaltySearchService")
public class PRSPenaltySearchInterface {

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @ElementClass(request = PRSPenaltySearchNdsRequest.class, response = PRSPenaltySearchNdsResponse.class)
    @Path("/prsPenaltySearch/")
    @Descriptions({ @Description(value = "Request a exemption search", target = DocTarget.METHOD),
            @Description(value = "Success response or unsuccess response with errors", target = DocTarget.RETURN),
            @Description(value = "Request", target = DocTarget.REQUEST),
            @Description(value = "Response", target = DocTarget.RESPONSE),
            @Description(value = "Resource", target = DocTarget.RESOURCE) })
    public PRSPenaltySearchNdsResponse prsPenaltySearch(PRSPenaltySearchNdsRequest request) {
        return null;
    }
}
