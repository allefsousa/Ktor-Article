package com.example.routes

import com.example.model.Article
import com.example.model.Articles
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
                transaction { Articles.selectAll().where { Articles.id eq id.toInt() }.map { Articles.toArticle(it) } }

            if (article.isNotEmpty()) {
                return@get call.respond(article.first())
            }
            return@get call.respondText("Article not found", status = HttpStatusCode.NotFound)
        }

        post {
            val article = call.receive<Article>()

            transaction {
                Articles.insert {
                    it[title] = article.title
                    it[body] = article.body
                    it[author] = UUID.fromString(article.author)
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
                Articles.deleteWhere { Articles.id eq id.toInt() }
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