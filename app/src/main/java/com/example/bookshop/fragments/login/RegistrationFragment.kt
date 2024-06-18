package com.example.bookshop.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.bookshop.databinding.FragmentRegistrationBinding
import com.example.bookshop.misc.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationFragment : Fragment() {
    private var databaseUsersReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var binding: FragmentRegistrationBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){

            buttonLogin.setOnClickListener {

                it.findNavController().popBackStack()
            }
            buttonRegistration.setOnClickListener {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        editTextEmail.text.toString(),
                        editTextPassword.text.toString()
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            databaseUsersReference.child(Firebase.auth.currentUser!!.uid).setValue(
                                User(uidUser = Firebase.auth.currentUser!!.uid,
                                    name = editTextNickname.text.toString()))
                            it.findNavController().popBackStack()
                        } else {
                            // Обработка ошибки
                            try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthWeakPasswordException) {
                                Toast.makeText(activity, "Слабый пароль", Toast.LENGTH_SHORT).show()
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(activity, "Неверные учетные данные", Toast.LENGTH_SHORT).show()
                            } catch (e: FirebaseAuthUserCollisionException) {
                                Toast.makeText(activity, "Пользователь уже существует", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(activity, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

}