package com.example.routes

import com.example.model.User
import com.example.model.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

fun Route.userRouting() {
    route("/user") {
        get {
            val users = transaction {
                Users.selectAll().map { Users.toUser(it) }
            }

            return@get call.respond(users)
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.NotFound
            )
            val user = transaction {
                Users.select { Users.id eq id }.map { Users.toUser(it) }
            }

            if (user.isNotEmpty()) {
                return@get call.respond(user)
            }
            return@get call.respondText("User not found", status = HttpStatusCode.NotFound)
        }

        post {
            val user = call.receive<User>()
            user.id = UUID.randomUUID().toString()
            transaction {
                Users.insert {
                    it[id] = user.id!!
                    it[name] = user.name
                }
            }
            call.respondText("User stored correctly", status = HttpStatusCode.Created)
        }

        delete {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )


            val delete = transaction {
                Users.deleteWhere { Users.id eq id }
            }

            if (delete == 1) {
                return@delete call.respondText("User removed correctly", status = HttpStatusCode.OK)
            }

            return@delete call.respondText("User not found", status = HttpStatusCode.NotFound)
        }
    }
}

fun Application.registerUserRoutes() {
    routing {
        userRouting()
    }
}