package com.northgateps.nds.beis.api.getconstrainedvalues;

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
 * NDS REST service interface that gets constrained values
 *
 */
@Path("/getConstrainedValuesService")
public class GetConstrainedValuesServiceInterface {
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @ElementClass(request=GetConstrainedValuesNdsRequest.class, response=GetConstrainedValuesNdsResponse.class)
    @Path("/getConstrainedValues/")
    @Descriptions({
            @Description(value = "Request to get Constrained Values", target = DocTarget.METHOD),
            @Description(value = "Success response or unsuccess response with errors", target = DocTarget.RETURN),
            @Description(value = "Request", target = DocTarget.REQUEST),
            @Description(value = "Response", target = DocTarget.RESPONSE),
            @Description(value = "Resource", target = DocTarget.RESOURCE) })
    public GetConstrainedValuesNdsResponse getConstrainedValues (GetConstrainedValuesNdsRequest request) {
        return null;
    }
}
  