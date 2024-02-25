package com.flydubai.core.service;

import com.flydubai.core.exception.BadRequestException;
import com.flydubai.hellosoap.HelloSoapResponse;
import org.springframework.stereotype.Service;

/**
 * This class provides a service for generating greeting messages.
 * It handles the construction of greeting messages based on the provided client name.
 *
 * @author nayanamadhav
 */
@Service
public class GreetingService {

    private static final String WELCOME_MESSAGE_TEMPLATE = "Welcome {{ClientName}}";

    /**
     * Generates a greeting message for the given client name.
     *
     * @param clientName The name of the client for whom the greeting message is generated.
     * @return A HelloSoapResponse object containing the greeting message.
     */
    public HelloSoapResponse getGreetingMessage(String clientName) {
        validateRequest(clientName);
        HelloSoapResponse helloSoapResponse = new HelloSoapResponse();
        String greetingMessage = WELCOME_MESSAGE_TEMPLATE.replace("{{ClientName}}", clientName);
        helloSoapResponse.setResponse(greetingMessage);

        return helloSoapResponse;
    }

    private void validateRequest(String clientName) {
        if(clientName == null || clientName.isEmpty())
            throw new BadRequestException("Client Name cannot be empty");
    }
}
