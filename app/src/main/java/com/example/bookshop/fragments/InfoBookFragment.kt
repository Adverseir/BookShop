package com.example.bookshop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.bookshop.R
import com.example.bookshop.databinding.FragmentInfoBookBinding
import com.example.bookshop.misc.DataViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
private const val ARG_UID_BOOK = "uidBook"
class InfoBookFragment : Fragment() {
    private lateinit var binding: FragmentInfoBookBinding
    private val viewModel:DataViewModel by activityViewModels()
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
        binding = FragmentInfoBookBinding.inflate(inflater, container, false)
        return binding.root
    }
    private suspend fun loadAndParseURL(url: String): String {
        return withContext(Dispatchers.IO) {
            val doc = Jsoup.connect(url).get()
            val bookText = doc.select("noindex").text() // Измените этот селектор в соответствии с вашим сайтом
            bookText
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            val book = uidBook?.let { viewModel.getBook(it) }!!

            GlobalScope.launch {
                val bookText = loadAndParseURL(book.path)
                withContext(Dispatchers.Main) {
                    textViewText.text = bookText
                }
            }
            textViewName.text = book.nameBook
            textViewAuthor.text = book.authorBook
            textViewCast.text = book.cast.toString()
            buttonRead.setOnClickListener {
                it.findNavController().navigate(R.id.readFragment2, ReadFragment.setUidBook(textViewText.text.toString()))
            }
        }
    }
    companion object {
        @JvmStatic
        fun setUidBook(uidBook: String):Bundle{
            return bundleOf(Pair(ARG_UID_BOOK,uidBook))
        }
    }
}