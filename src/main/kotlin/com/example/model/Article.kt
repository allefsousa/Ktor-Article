package com.example.model

import com.example.model.Users.autoIncrement
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import java.util.*

@Serializable
data class Article(val title: String, val body: String, val author: String) {
    constructor(id: Int, title: String, body: String, author: String) : this(title, body, author)
}


object Articles : Table() {
    val id = integer("id").autoIncrement()
    val title: Column<String> = varchar("title", 50)
    val body: Column<String> = text("body")
    val author: Column<UUID> = uuid("author_id").references(Users.id)

    override val primaryKey = PrimaryKey(id, name = "PK_Articles_Id")

    fun toArticle(row: ResultRow): Article = Article(
        id = row[Articles.id],
        title = row[Articles.title],
        body = row[Articles.body],
        author = row[Articles.author].toString(),
    )
}