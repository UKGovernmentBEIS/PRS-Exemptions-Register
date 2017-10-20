package com.northgateps.nds.beis.api.getreferencevalues;

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
 * Get Reference Values service definition.
 * 
 * Just a definition of the service. No implementation needed.
 * 
 * Needs to be a class so that Spring or CXF can call a default constructor (which it
 * can't do with an interface).
 */
@Path("/getReferenceValuesService")
public class GetReferenceValuesNdsServiceInterface {

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @ElementClass(request = GetReferenceValuesNdsRequest.class, response = GetReferenceValuesNdsResponse.class)
    @Path("/getReferenceValues/")
    @Descriptions({
            @Description(value = "Fetch reference values or master list as name-value pairs ", target = DocTarget.METHOD),
            @Description(value = "Response reporting the success or not for fetching reference values", target = DocTarget.RETURN),
            @Description(value = "Request", target = DocTarget.REQUEST),
            @Description(value = "Response", target = DocTarget.RESPONSE),
            @Description(value = "Resource", target = DocTarget.RESOURCE) })
    public GetReferenceValuesNdsResponse getReferenceValues(GetReferenceValuesNdsRequest getReferenceValues) {
        return null;
    }
}
