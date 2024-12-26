package com.birth.forumhub.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "ForumHub API",
                version = "1.0",
                description = "ForumHub is a social networking platform designed to foster a collaborative environment for discussions and interactions among users. This document outlines the ForumHub RESTful API"
        ),
        servers = {
                @Server(url = "/", description = "Default Server"),
                @Server(url = "https://api.example.com", description = "Production Server")
        }
)
public class SwaggerConfig {}
