package com.example.plugins

import com.example.routes.registerArticleRoutes
import com.example.routes.registerUserRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        configureDocumentation()
    }
    registerUserRoutes()
    registerArticleRoutes()
}
