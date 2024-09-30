package com.example.plugins

import com.example.routes.registerArticleRoutes
import com.example.routes.registerUserRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
    registerUserRoutes()
    registerArticleRoutes()
}
