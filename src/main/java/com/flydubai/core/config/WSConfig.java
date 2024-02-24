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
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.Properties;

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

    // To generate wsdl from xsd schema
    @Bean(name="helloschema")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema helloSoapSchema) {
        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
        defaultWsdl11Definition.setPortTypeName(environmentConfig.getPortTypeName());
        defaultWsdl11Definition.setLocationUri(environmentConfig.getLocationUri());
        defaultWsdl11Definition.setTargetNamespace(environmentConfig.getTargetNamespace());
        defaultWsdl11Definition.setSchema(helloSoapSchema);

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
    public SoapFaultMappingExceptionResolver soapFaultMappingExceptionResolver() {
        SoapFaultMappingExceptionResolver soapFaultMappingExceptionResolver = new SoapFaultMappingExceptionResolver();

        SoapFaultDefinition soapFaultDefinition = new SoapFaultDefinition();
        soapFaultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        soapFaultMappingExceptionResolver.setDefaultFault(soapFaultDefinition);

        Properties properties = new Properties();
        properties.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());

        soapFaultMappingExceptionResolver.setExceptionMappings(properties);
        soapFaultMappingExceptionResolver.setOrder(1);

        return soapFaultMappingExceptionResolver;
    }

//    @Bean
//    public SaajSoapMessageFactory messageFactory() throws SOAPException {
//
//        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
//        SaajSoapMessageFactory soapMessageFactory = new SaajSoapMessageFactory(messageFactory);
//
////        messageFactory.setSoapVersion(SoapVersion.SOAP_12);
//        return soapMessageFactory;
//    }
//
//    @Bean
//    public WebServiceTemplate webServiceTemplate() throws SOAPException {
//        WebServiceTemplate webServiceTemplate = new WebServiceTemplate(messageFactory());
//        return webServiceTemplate;
//    }
}