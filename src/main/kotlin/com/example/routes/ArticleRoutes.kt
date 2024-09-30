package com.example.routes

import com.example.model.Article
import com.example.model.Articles
import com.example.model.Users.name
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.text.get

fun Route.articleRouting() {
    route("/article") {
        get {
            val articles = transaction {
                Articles.selectAll().map { Articles.toArticle(it) }
            }

            return@get call.respond(articles)
        }

        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Article not found", status = HttpStatusCode.NotFound
            )

            val article: List<Article> =
                transaction { Articles.select { Articles.id eq id }.map { Articles.toArticle(it) } }

            if (article.isNotEmpty()) {
                return@get call.respond(article.first())
            }
            return@get call.respondText("Article not found", status = HttpStatusCode.NotFound)
        }

        post {
            val article = call.receive<Article>()

            article.id = UUID.randomUUID().toString()

            transaction {
                Articles.insert {
                    it[id] = article.id!!
                    it[title] = article.title
                }
            }

            call.respondText("Created", status = HttpStatusCode.Created)
        }

        delete("id") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Insert article ID to delete a article",
                status = HttpStatusCode.BadRequest
            )

            val delete: Int = transaction {
                Articles.deleteWhere { Articles.id eq id }
            }

            if (delete == 1) {
                return@delete call.respondText("Deleted", status = HttpStatusCode.OK)
            }
            return@delete call.respondText("Article not found", status = HttpStatusCode.NotFound)
        }
    }
}

fun Application.registerArticleRoutes() {
    routing {
        articleRouting()
    }
}