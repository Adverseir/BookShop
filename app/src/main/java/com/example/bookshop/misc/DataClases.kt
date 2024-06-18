package com.example.bookshop.misc

data class DataClasses(
    var user:User,
    var listBook: List<Book>
)
data class User(
    var uidUser: String = "",
    var name:String = "",
)
data class Book(
    var uidBook: String = "",
    var nameBook:String = "",
    var authorBook:String = "",
    var cast:Int = 0,
    var path:String = "",
    var usersBought:List<String> = emptyList(),
    var usersWont:List<String> = emptyList(),
)

