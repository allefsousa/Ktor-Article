package com.example.plugins

import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Routing.configureDocumentation() {
    swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml") {
        version = "4.15.5"
    }
}