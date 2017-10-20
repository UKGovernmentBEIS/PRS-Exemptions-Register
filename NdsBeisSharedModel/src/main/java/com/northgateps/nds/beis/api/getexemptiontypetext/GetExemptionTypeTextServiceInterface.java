package com.northgateps.nds.beis.api.getexemptiontypetext;

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
 * NDS REST service interface to fetch exemption type text from back office
 *
 */
@Path("/getExemptionTypeTextService")
public class GetExemptionTypeTextServiceInterface {

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @ElementClass(request = GetExemptionTypeTextNdsRequest.class, response = GetExemptionTypeTextNdsResponse.class)
    @Path("/getExemptionTypeText/")
    @Descriptions({ @Description(value = "Request to get Exemption type text", target = DocTarget.METHOD),
            @Description(value = "Success response or unsuccess response with errors", target = DocTarget.RETURN),
            @Description(value = "Request", target = DocTarget.REQUEST),
            @Description(value = "Response", target = DocTarget.RESPONSE),
            @Description(value = "Resource", target = DocTarget.RESOURCE) })
    public GetExemptionTypeTextNdsResponse getExemptionTypeText(GetExemptionTypeTextNdsRequest request) {
        return null;
    }
}
