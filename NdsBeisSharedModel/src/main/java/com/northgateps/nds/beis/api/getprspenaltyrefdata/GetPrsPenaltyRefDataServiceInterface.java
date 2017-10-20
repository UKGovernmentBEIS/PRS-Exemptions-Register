package com.northgateps.nds.beis.api.getprspenaltyrefdata;

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
 * NDS REST service interface to fetch penalty reference data from back office
 *
 */
@Path("/getPrsPenaltyRefDataService")
public class GetPrsPenaltyRefDataServiceInterface {

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @ElementClass(request = GetPrsPenaltyRefDataNdsRequest.class, response = GetPrsPenaltyRefDataNdsResponse.class)
    @Path("/getPrsPenaltyRefData/")
    @Descriptions({ @Description(value = "Request to get Penalty type text", target = DocTarget.METHOD),
            @Description(value = "Success response or unsuccess response with errors", target = DocTarget.RETURN),
            @Description(value = "Request", target = DocTarget.REQUEST),
            @Description(value = "Response", target = DocTarget.RESPONSE),
            @Description(value = "Resource", target = DocTarget.RESOURCE) })
    public GetPrsPenaltyRefDataNdsResponse getPrsPenaltyRefData(GetPrsPenaltyRefDataNdsRequest request) {
        return null;
    }
    
}
