package com.northgateps.nds.beis.api.beisregistration;

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
 * NDS REST service interface that registers a new user
 *
 */
@Path("/beisregistrationService")
public class BeisRegistrationServiceInterface {

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @ElementClass(request = BeisRegistrationNdsRequest.class, response = BeisRegistrationNdsResponse.class)
    @Path("/beisregistration/")
    @Descriptions({ @Description(value = "Request a citizen registration", target = DocTarget.METHOD),
            @Description(value = "Success response or unsuccess response with errors", target = DocTarget.RETURN),
            @Description(value = "Request", target = DocTarget.REQUEST),
            @Description(value = "Response", target = DocTarget.RESPONSE),
            @Description(value = "Resource", target = DocTarget.RESOURCE) })
    public BeisRegistrationNdsResponse registration(BeisRegistrationNdsRequest request) {
        return null;
    }
}
