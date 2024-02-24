package com.flydubai.core.route;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.flydubai.core.service.GreetingService;
import com.flydubai.hellosoap.HelloSoapRequest;
import com.flydubai.hellosoap.HelloSoapResponse;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpointsAndSkip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@CamelSpringBootTest
//@MockEndpointsAndSkip("spring-ws:*")
@SpringBootTest
@ContextConfiguration
public class GreetingRouterTest {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @MockBean
    private GreetingService greetingService;

    @EndpointInject("mock:spring-ws:rootqname:{http://www.flydubai.com/HelloSoap}HelloSoapRequest?endpointMapping=#endpointMapping")
    private MockEndpoint mockGreetingResponse;

    @Test
    void testGreetingRoute() throws Exception {
        // Arrange
        String clientName = "TestClient";
        String expectedGreeting = "Welcome " + clientName;

        HelloSoapRequest request = new HelloSoapRequest();
        request.setClientName(clientName);

        HelloSoapResponse response = new HelloSoapResponse();
        response.setResponse(expectedGreeting);

        when(greetingService.getGreetingMessage(any(String.class))).thenReturn(response);

        mockGreetingResponse.expectedMessageCount(1);
        mockGreetingResponse.expectedBodiesReceived(response);

        // Act
        producerTemplate.sendBody("spring-ws:rootqname:{http://www.flydubai.com/HelloSoap}HelloSoapRequest?endpointMapping=#endpointMapping", request);

        // Assert
        mockGreetingResponse.assertIsSatisfied();
    }
}
