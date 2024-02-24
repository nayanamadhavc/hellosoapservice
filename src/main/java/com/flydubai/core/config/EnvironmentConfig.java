package com.flydubai.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvironmentConfig {
    @Value("${hello.soap.portTypeName}")
    private String portTypeName;

    @Value("${hello.soap.locationUri}")
    private String locationUri;

    @Value("${hello.soap.targetNamespace}")
    private String targetNamespace;

    @Value("${hello.soap.schema.location}")
    private String schemaLocation;

    public String getPortTypeName() {
        return portTypeName;
    }

    public void setPortTypeName(String portTypeName) {
        this.portTypeName = portTypeName;
    }

    public String getLocationUri() {
        return locationUri;
    }

    public void setLocationUri(String locationUri) {
        this.locationUri = locationUri;
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }
}
