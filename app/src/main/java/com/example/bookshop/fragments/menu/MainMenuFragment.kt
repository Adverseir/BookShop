package com.example.bookshop.fragments.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.bookshop.R
import com.example.bookshop.databinding.FragmentMainMenuBinding
import com.example.bookshop.misc.DataViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
class MainMenuFragment : Fragment() {
    private lateinit var binding: FragmentMainMenuBinding
    private val viewModel:DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding)
        {
            viewLifecycleOwner.lifecycleScope.launch {
                while (viewModel.getNameUser() == "")
                    delay(1000)
                textViewLogin.text = viewModel.getNameUser()
            }

            buttonShop.setOnClickListener {
                it.findNavController().navigate(R.id.action_mainMenuFragment_to_shopFragment)
            }
            buttonFavourites.setOnClickListener {
                it.findNavController().navigate(R.id.action_mainMenuFragment_to_planFragment)
            }
            buttonMyLibrary.setOnClickListener {
                it.findNavController().navigate(R.id.action_mainMenuFragment_to_boughtFragment)
            }

        }
    }
}