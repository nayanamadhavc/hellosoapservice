package com.flydubai.core.route;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.flydubai.core.service.GreetingService;
import com.flydubai.hellosoap.HelloSoap;
import com.flydubai.hellosoap.HelloSoapResponse;
import org.apache.camel.*;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@CamelSpringBootTest
@SpringBootTest
@ContextConfiguration
public class GreetingRouterTest {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Mock
    private GreetingService greetingService;

    @Test
    void testGreetingRoute() throws Exception {
        // Arrange
        String clientName = "TestClient";
        String expectedGreeting = "Welcome " + clientName;

        HelloSoap request = new HelloSoap();
        request.setClientName(clientName);
        HelloSoapResponse response = new HelloSoapResponse();
        response.setResponse(expectedGreeting);

        when(greetingService.getGreetingMessage(any(String.class))).thenReturn(response);
        Exchange exchange = ExchangeBuilder.anExchange(camelContext).withBody(response).build();

        // Act
        Exchange resultExchange = producerTemplate.send("spring-ws:rootqname:{http://www.flydubai.com/HelloSoap}HelloSoap?endpointMapping=#endpointMapping",
                exchange);
        HelloSoapResponse actualResponse = resultExchange.getMessage().getBody(HelloSoapResponse.class);

        // Assert
        Assertions.assertEquals(response, actualResponse);
        Assertions.assertEquals(response.getResponse(), actualResponse.getResponse());
    }
}
