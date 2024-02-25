package com.flydubai.core.route;

import com.flydubai.core.service.GreetingService;
import com.flydubai.hellosoap.HelloSoap;
import com.flydubai.hellosoap.HelloSoapResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GreetingRouter class uses Camel Router for handling SOAP requests related to greetings.
 * It extends RouteBuilder and configures routes to process SOAP requests
 * and produce SOAP responses using the GreetingService.
 *
 * @author nayanamadhav
 */
@Component
public class GreetingRouter extends RouteBuilder {
    private static final Logger logger = LoggerFactory.getLogger(GreetingRouter.class);

    @Autowired
    GreetingService greetingService;

    /**
     * Returns a greeting message for the given client name
     *
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {
        from("spring-ws:rootqname:{http://www.flydubai.com/HelloSoap}HelloSoap?endpointMapping=#endpointMapping")
                .unmarshal().jaxb(HelloSoap.class.getPackage().getName())
                .process(new Processor() {
                    @Override
                    public void process(Exchange msg) throws Exception {
                        HelloSoap request = msg.getIn().getBody(HelloSoap.class);
                        String clientName = request.getClientName();
                        HelloSoapResponse greetingMessage = greetingService.getGreetingMessage(clientName);
                        msg.getMessage().setBody(greetingMessage);
                        logger.info("Message returned: {}", msg.getMessage().getBody());
                    }
                })
                .marshal().jaxb(HelloSoapResponse.class.getPackage().getName());
    }
}
