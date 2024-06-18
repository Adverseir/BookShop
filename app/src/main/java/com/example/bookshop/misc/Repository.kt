package com.example.bookshop.misc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RepositoryInMemoryImpl {

    private var databaseUsersReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private var databaseBooksReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("books")
    private val uid:String = Firebase.auth.currentUser!!.uid

    var dataClasses = DataClasses(
        User(),
        emptyList()
    )
    private val data = MutableLiveData(dataClasses)

    fun getAll() = data

    fun loadUser() {
        val listener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNull { it.getValue(User::class.java) }.forEach{
                    if (it.uidUser == uid)
                        dataClasses.user = it
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        databaseUsersReference.addValueEventListener(listener)
        data.value = dataClasses

    }
    fun loadBooks() {
        val listener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataClasses.listBook = emptyList()
                for (snapshot in dataSnapshot.children){
                    val book = snapshot.getValue(Book::class.java)!!
                    dataClasses.listBook = dataClasses.listBook.plus(book)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        databaseBooksReference.addValueEventListener(listener)

        data.value = dataClasses
    }

    fun addBookInFavourites(uidBook: String) {
        var  book = Book()
        dataClasses.listBook.forEach{ itemBook ->
            if (itemBook.uidBook == uidBook)
                book = if (!itemBook.usersWont.contains(uid))
                            itemBook.copy(usersWont = itemBook.usersWont.plus(uid))
                        else
                            itemBook.copy(usersWont = itemBook.usersWont.filter { it != uid})
        }
        data.value = dataClasses
        databaseBooksReference.child(uidBook).removeValue()
        databaseBooksReference.child(uidBook).setValue(book)
    }
    fun addBookInMyLibrary(uidBook: String) {
        var  book = Book()
        dataClasses.listBook.forEach{ itemBook ->
            if (itemBook.uidBook == uidBook)
                book = if (!itemBook.usersBought.contains(uid))
                    itemBook.copy(usersBought = itemBook.usersBought.plus(uid))
                else
                    itemBook.copy(usersBought = itemBook.usersBought.filter { it != uid})
        }
        data.value = dataClasses
        databaseBooksReference.child(uidBook).removeValue()
        databaseBooksReference.child(uidBook).setValue(book)
    }

    fun getBook(uidBook: String):Book{
        var book = Book()
        book = dataClasses.listBook.first{ it.uidBook == uidBook }
        return book
    }
}


class DataViewModel : ViewModel() {
    private val repository = RepositoryInMemoryImpl()
    val data = repository.getAll()
    fun addBookInFavourites(uidBook: String) = repository.addBookInFavourites(uidBook)
    fun addBookInMyLibrary(uidBook: String) = repository.addBookInMyLibrary(uidBook)
    fun getNameUser() = repository.dataClasses.user.name
    fun loadBooks()  = repository.loadBooks()
    fun loadUser() = repository.loadUser()
    fun getBook(uidBook: String) = repository.getBook(uidBook)
}