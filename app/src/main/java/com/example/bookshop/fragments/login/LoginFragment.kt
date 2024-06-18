package com.example.bookshop.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.bookshop.R
import com.example.bookshop.databinding.FragmentLoginBinding
import com.example.bookshop.misc.DataViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel:DataViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){

           /*com.google.firebase.database.FirebaseDatabase.getInstance().getReference("books").child("321").setValue(com.example.bookshop.misc.Book(
                "321",
                "KOTLIN для чайников",
                "Джексон С.",
                "4332",
                listOf("fLQOcnD2TEQ9zchQdgXlhkvI8Nn1"),
            ))*/

            buttonLogin.setOnClickListener {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextEmail.text.toString(), editTextPassword.text.toString())
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            viewModel.loadUser()
                            viewModel.loadBooks()
                            it.findNavController().navigate(R.id.action_loginFragment_to_mainMenuFragment)
                        } else {
                            // Обработка ошибки
                            try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthInvalidUserException) {
                                Toast.makeText(activity, "Пользователь не найден", Toast.LENGTH_SHORT).show()
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(activity, "Неверные учетные данные", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(activity, "Ошибка", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

            }
            buttonRegistration.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
        }
    }

}