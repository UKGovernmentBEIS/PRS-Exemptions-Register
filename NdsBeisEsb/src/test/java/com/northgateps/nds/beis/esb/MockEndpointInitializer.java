package com.northgateps.nds.beis.esb;

import org.apache.camel.component.mock.MockEndpoint;

/**
 * Interface for configuring the routes by call back during the route tests
 */
public interface MockEndpointInitializer {

    void request(MockEndpoint requestMock) throws Exception;

    void response(MockEndpoint responseMock) throws Exception;

}
