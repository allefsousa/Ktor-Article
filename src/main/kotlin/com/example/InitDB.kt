package com.example

import com.example.model.Articles
import com.example.model.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDB() {
    Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")

    //Using migration to create the table
    transaction {
        SchemaUtils.create(Users)
        SchemaUtils.create(Articles)
    }
}
