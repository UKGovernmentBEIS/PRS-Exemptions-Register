package com.northgateps.nds.beis.api.getpartiallyregistereddetails;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.ext.xml.ElementClass;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import com.northgateps.nds.beis.api.getpartiallyregistereddetails.GetPartiallyRegisteredDetailsNdsRequest;
import com.northgateps.nds.beis.api.getpartiallyregistereddetails.GetPartiallyRegisteredDetailsNdsResponse;

/**
 * NDS REST service interface to get partially registered details
 *
 */
@Path("/GetPartiallyRegisteredDetailsService")
public class GetPartiallyRegisteredDetailsServiceInterface {
        
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @ElementClass(request = GetPartiallyRegisteredDetailsNdsRequest.class, response = GetPartiallyRegisteredDetailsNdsResponse.class)
    @Path("/getPartiallyRegisteredDetails/")
    @Descriptions({ @Description(value = "Request to get partially registered account details", target = DocTarget.METHOD),
            @Description(value = "Success response or unsuccess response with errors", target = DocTarget.RETURN),
            @Description(value = "Request", target = DocTarget.REQUEST),
            @Description(value = "Response", target = DocTarget.RESPONSE),
            @Description(value = "Resource", target = DocTarget.RESOURCE) })
    public GetPartiallyRegisteredDetailsNdsResponse getPartiallyRegisteredDetails(GetPartiallyRegisteredDetailsNdsRequest request){
        return null;
    };
}
