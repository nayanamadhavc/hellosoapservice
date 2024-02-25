package com.flydubai.core.config;

import org.apache.camel.component.spring.ws.bean.CamelEndpointMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointAdapter;
import org.springframework.ws.server.EndpointMapping;
import org.springframework.ws.server.endpoint.adapter.MessageEndpointAdapter;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;


@EnableWs
@Configuration
public class WSConfig extends WsConfigurerAdapter {

    @Autowired
    EnvironmentConfig environmentConfig;

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();

        messageDispatcherServlet.setApplicationContext(applicationContext);
        messageDispatcherServlet.setTransformWsdlLocations(true);

        return new ServletRegistrationBean<>(messageDispatcherServlet, "/*");
    }

    // To generate wsdl from xsd schema and access it at helloschema.wsdl location
    @Bean(name="helloschema")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema helloSoapSchema) {
        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
        defaultWsdl11Definition.setPortTypeName(environmentConfig.getPortTypeName());
        defaultWsdl11Definition.setLocationUri(environmentConfig.getLocationUri());
        defaultWsdl11Definition.setTargetNamespace(environmentConfig.getTargetNamespace());
        defaultWsdl11Definition.setSchema(helloSoapSchema);
        defaultWsdl11Definition.setCreateSoap12Binding(true);

        return defaultWsdl11Definition;
    }

    @Bean
    public XsdSchema helloSoapSchema() {
        return new SimpleXsdSchema(new ClassPathResource(environmentConfig.getSchemaLocation()));
    }

    @Bean
    public EndpointMapping endpointMapping() {
        CamelEndpointMapping mapping = new CamelEndpointMapping();
        return mapping;
    }

    @Bean
    public EndpointAdapter messageEndpointAdapter() {
        return new MessageEndpointAdapter();
    }

    @Bean
    public SaajSoapMessageFactory messageFactory() throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SaajSoapMessageFactory soapMessageFactory = new SaajSoapMessageFactory(messageFactory);
        return soapMessageFactory;
    }
}
