package com.example.bookshop.misc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshop.R
import com.example.bookshop.databinding.ItemCardBookShopBinding
import com.example.bookshop.fragments.InfoBookFragment
import com.example.bookshop.fragments.InfoBookFragmentDirections
import com.google.firebase.auth.FirebaseAuth


class BookDiffCallback : DiffUtil.ItemCallback<Book>(){
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.uidBook==newItem.uidBook
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return  oldItem == newItem
    }

}
class BooksViewHolder(private val binding: ItemCardBookShopBinding)
    :RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book, listener: BooksAdapter.Listener) {
        binding.apply {
            nameBook.text = book.nameBook
            authorBook.text = book.authorBook
            castBook.text = book.cast.toString()
            buttonAdd.setBackgroundResource(if (!book.usersWont.contains(FirebaseAuth.getInstance().currentUser!!.uid)) R.drawable.ic_add_favourites else R.drawable.ic_remove_favourites)
            if (!book.usersBought.contains(FirebaseAuth.getInstance().currentUser!!.uid)){
                buttonBuy.visibility = View.VISIBLE
                castBook.visibility = View.VISIBLE
                buttonBuy.setOnClickListener {
                    listener.addBookInMyLibrary(book)
                }
            } else{
                buttonBuy.visibility = View.GONE
                castBook.visibility = View.GONE
                root.setOnClickListener {
                    it.findNavController().navigate(R.id.infoBookFragment, InfoBookFragment.setUidBook(book.uidBook))
                }
            }

            buttonAdd.setOnClickListener {
                listener.addBookFavourites(book)
            }


        }
    }
}

class BooksAdapter(
    private val listener: Listener,
):ListAdapter<Book, BooksViewHolder>(BookDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val binding = ItemCardBookShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BooksViewHolder(binding)
    }
    override fun onBindViewHolder(holder: BooksViewHolder, position:Int){
        val post = getItem(position)
        holder.bind(post, listener)
    }

    interface Listener{
        fun addBookFavourites(book: Book)
        fun addBookInMyLibrary(book: Book)
    }
}
