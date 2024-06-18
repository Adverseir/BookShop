package com.example.bookshop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.bookshop.R
import com.example.bookshop.databinding.FragmentLoginBinding
import com.example.bookshop.databinding.FragmentReadBinding

private const val ARG_UID_BOOK = "uidBook"

class ReadFragment : Fragment() {
    private lateinit var binding:FragmentReadBinding
    private var uidBook: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uidBook = it.getString(ARG_UID_BOOK)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            textViewText.text = uidBook
        }
    }

    companion object {
        @JvmStatic
        fun setUidBook(uidBook: String): Bundle {
            return bundleOf(Pair(ARG_UID_BOOK,uidBook))
        }
    }
}