package com.example

import com.example.plugins.*
import com.example.routes.registerArticleRoutes
import com.example.routes.registerUserRoutes
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    initDB()
    configureSerialization()
    configureRouting()

}
