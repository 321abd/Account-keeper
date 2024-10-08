package com.example.accosort.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Account")
data class Account (
    @PrimaryKey(autoGenerate=true) val id:Int=0,
    val name:String,
    val username: String,
    val password: String,
    val accountType: String
)