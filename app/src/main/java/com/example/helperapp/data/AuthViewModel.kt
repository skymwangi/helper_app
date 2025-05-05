package com.example.helperapp.data


import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import com.example.helperapp.navigation.route_login
import com.google.firebase.auth.FirebaseAuth
import com.example.helperapp.model.User
import com.example.helperapp.navigation.route_home
import com.example.helperapp.navigation.route_register
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.FirebaseDatabase


class AuthViewModel(
    var navController: NavHostController,
    var context: Context
) {

    var mAuth: FirebaseAuth


    init {
        mAuth = FirebaseAuth.getInstance()

    }

    fun signup(
        firstName: String,
        lastName: String,
        email: String,
        pass: String,
        confpass: String
    ) {


        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || pass.isBlank() || confpass.isBlank()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG)
                .show()
            return
        } else if (pass != confpass) {
            Toast.makeText(context, "Password do not match", Toast.LENGTH_LONG).show()
            return
        } else {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid
                    if (uid != null) {
                        val userdata = User(
                            firstName, lastName, email, pass,uid,
                            isAuthenticated = true
                        )
                        val regRef = FirebaseDatabase.getInstance().getReference()
                            .child("Users/" + mAuth.currentUser!!.uid)
                        regRef.setValue(userdata).addOnCompleteListener {

                            if (it.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Registered Successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.navigate(route_home)

                            } else {
                                val error = it.exception
                                Toast.makeText(
                                    context,
                                    "Login failed: ${error?.localizedMessage ?:"Unknown error"}",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.navigate(route_register)
                            }
                        }
                    }
                } else {
                    navController.navigate(route_register)
                }

            }
        }

    }


    fun login(email: String, pass: String) {

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Succeffully Logged in", Toast.LENGTH_LONG).show()
                navController.navigate(route_home)
//                navController.navigate(route_home)TO TAKE YOU TO A DIIFFERNT PAGE
            } else {

                val exception = task.exception
                val userMessage=when(exception){
                    is FirebaseAuthInvalidUserException ->
                        "You do not have an account"
                    is FirebaseAuthInvalidCredentialsException ->
                        "Incorrect password"
                    else -> "Login failed:${exception?.localizedMessage ?:"Unknown error"}"
                }
                Toast.makeText(context,userMessage, Toast.LENGTH_LONG).show()
            }
        }

    }

    fun logout(){
        mAuth.signOut()
        navController.navigate(route_login)
    }
    fun isloggedin():Boolean{
        return mAuth.currentUser !=null
    }

}