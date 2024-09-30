package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
data class Article (var id: String? = null, val title: String, val body: String, val author: String)


object Articles: Table(){
    val id: Column<String> = char("id", 36)
    val title: Column<String> = varchar("title", 50)
    val body: Column<String> = text("body")
    val author: Column<String> = char("author_id", 38) references Users.id

    override val primaryKey = PrimaryKey(id, name = "PK_Articles_Id")

    fun toArticle(row: ResultRow): Article = Article(
        id = row[Articles.id],
        title = row[Articles.title],
        body = row[Articles.body],
        author = row[Articles.author],
    )
}