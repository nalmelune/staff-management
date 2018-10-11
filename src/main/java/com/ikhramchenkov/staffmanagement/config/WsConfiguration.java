package com.ikhramchenkov.staffmanagement.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@EnableCaching
@Configuration
public class WsConfiguration {

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
        messageDispatcherServlet.setApplicationContext(context);
        messageDispatcherServlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(messageDispatcherServlet, "/ws/*");
    }

    /**
     * /ws/staffManagement.wsdl
     */
    @Bean(name = "staffManagement")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema staffManagementSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("StaffManagementPort");
        definition.setTargetNamespace("http://ikhramchenkov.com/staffmanagement");
        definition.setLocationUri("/ws");
        definition.setSchema(staffManagementSchema);
        return definition;
    }

    @Bean
    public XsdSchema staffManagementSchema() {
        return new SimpleXsdSchema(new ClassPathResource("staffManagementWebService.xsd"));
    }
}
