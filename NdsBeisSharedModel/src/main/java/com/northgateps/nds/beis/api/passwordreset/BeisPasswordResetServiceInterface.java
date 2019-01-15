package com.northgateps.nds.beis.api.passwordreset;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.xml.ElementClass;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import com.northgateps.nds.platform.api.passwordreset.PasswordResetNdsRequest;
import com.northgateps.nds.platform.api.passwordreset.PasswordResetNdsResponse;

/**
 * NDS REST service interface that requests a password reset token be sent to a user 
 *
 */
@Path("/beisPasswordResetService")
public class BeisPasswordResetServiceInterface {
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @ElementClass(request=PasswordResetNdsRequest.class, response=PasswordResetNdsResponse.class)
    @Path("/passwordReset/")
    @Descriptions({
            @Description(value = "Request a password reset", target = DocTarget.METHOD),
            @Description(value = "Success response or unsuccess response with errors", target = DocTarget.RETURN),
            @Description(value = "Request", target = DocTarget.REQUEST),
            @Description(value = "Response", target = DocTarget.RESPONSE),
            @Description(value = "Resource", target = DocTarget.RESOURCE) })
    public PasswordResetNdsResponse passwordReset (PasswordResetNdsRequest request) {
        return null;
    }
}
  