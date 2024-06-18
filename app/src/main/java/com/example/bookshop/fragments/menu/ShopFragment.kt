package com.example.bookshop.fragments.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.bookshop.databinding.FragmentShopBinding
import com.example.bookshop.misc.Book
import com.example.bookshop.misc.BooksAdapter
import com.example.bookshop.misc.DataViewModel

class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding
    private val viewModel:DataViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            val adapter = BooksAdapter(object : BooksAdapter.Listener{
                override fun addBookFavourites(book: Book) {
                    viewModel.addBookInFavourites(book.uidBook)
                }
                override fun addBookInMyLibrary(book: Book) {
                    viewModel.addBookInMyLibrary(book.uidBook)
                }
            })

            recyclerView.adapter = adapter

            viewModel.data.observe(viewLifecycleOwner){
                adapter.submitList(it.listBook)
            }

            buttonBack.setOnClickListener {
                it.findNavController().popBackStack()
            }

        }
    }
}