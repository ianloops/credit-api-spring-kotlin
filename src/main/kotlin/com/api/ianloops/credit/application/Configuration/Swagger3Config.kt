package com.api.ianloops.credit.application.Configuration

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger3Config {
    @Bean
    fun publicApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder().group("springcreditapplication-public")
            .pathsToMatch("/customers/**", "/credits/**").build()
    }
}