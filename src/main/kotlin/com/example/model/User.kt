package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
data class User (var id: String? = null, val name: String)

object Users : Table(){
    val id: Column<String> = char("id", 50)
    val name = varchar("name", 50)

    override val primaryKey = PrimaryKey(id, name = "PK_Users_Id")

    fun toUser(row: ResultRow): User {
        return User(
            id = row[id],
            name = row[name]
        )
    }

}