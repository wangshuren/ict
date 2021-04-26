package com.ict.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/23
 * @Version 1.0
 */
@Configuration
@EnableSwagger2
@RefreshScope
@EnableSwaggerBootstrapUI
public class SwaggerConfig {
    @Value("${swagger2.module.base-package:com.ict.controller}")
    private String basePackage;
    @Value("${swagger2.name:ict}")
    private String appName;
    @Value("${swagger2.website:www.ict.cn}")
    private String webSite;
    @Value("${swagger2.email:support@ict.cn}")
    private String email;
    @Value("${swagger2.module.title:commons api}")
    private String apiTitle;
    @Value("${swagger2.module.description:commons api}")
    private String apiDesc;
    @Value("${swagger2.module.serviceUrl:www.ict.cn}")
    private String serviceUrl;

    @Value("${swagger2.module.version:V2.92}")
    private String apiVersion;

    @Bean
    @RefreshScope
    public Docket createRestApi() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("token").description("user token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的token参数非必填，传空也可以
        pars.add(ticketPar.build()); //根据每个方法名也知道当前方法在设置什么参数

        Contact contact = new Contact(appName, "", "");
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(new ApiInfoBuilder().title(apiTitle).description(apiDesc)
                /*.termsOfServiceUrl(serviceUrl)*/.contact(contact)
                .version(apiVersion).build()).select()
                .apis(RequestHandlerSelectors.basePackage(basePackage)).paths(PathSelectors.any()).build()
                .globalOperationParameters(pars);
    }
}