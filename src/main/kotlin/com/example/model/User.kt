package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.*

@Serializable
data class User(var id: String? = null, val name: String) 

object Users : UUIDTable() {
    val name = varchar("name", 50)

    fun toUser(row: ResultRow): User {
        return User(
            id = row[id].toString(),
            name = row[name]
        )
    }

}