package com.example.helperapp.data


import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import com.example.helperapp.model.User
import com.example.helperapp.navigation.route_home
import com.example.helperapp.navigation.route_login
import com.example.helperapp.navigation.route_register
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class AuthViewModel(
    var navController: NavHostController,
    var context: Context
) {

    var mAuth: FirebaseAuth
    var onResult:( (Boolean) -> Unit )?= null


    init {
        mAuth = FirebaseAuth.getInstance()

    }

    fun signup(
        firstName: String,
        lastName: String,
        email: String,
        pass: String,
        county: String,
        confpass: String,
        isAdmin: Boolean,
        function: (String?) -> Any
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
                            firstName=firstName, lastName=lastName, email=email,
                            pass=pass,uid=uid,county=county,isAdmin=isAdmin,profilePictureUrl = null
                        )
                        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                        db.collection("users").document(uid).set(userdata)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Registered Successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                                // Check if the user is admin
//                                        val isAdmin = document.getBoolean("isAdmin") == true
                                if (isAdmin) {
                                    navController.navigate("admin") {
                                        popUpTo("admin") { inclusive = true }
                                    }
                                } else {
                                    navController.navigate(route_home) {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            }

                            .addOnFailureListener { error ->
                                Toast.makeText(
                                    context,
                                    "Failed to save user data: ${error.localizedMessage ?: "Unknown error"}",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.navigate(route_register)
                            }

                    }
                } else {
                    Toast.makeText(context,"Error: ${task.exception?.message}",Toast.LENGTH_SHORT).show()


                    navController.navigate(route_register)
                }

            }
        }

    }


    fun login(email: String, pass: String) {


        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = task.result?.user?.uid
                if (uid != null) {
                    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    db.collection("users").document(uid).get().addOnSuccessListener { document ->
                        val isAdmin = document.getBoolean("isAdmin") == true
                        if (isAdmin) {
                            navController.navigate("admin") // Replace "admin" with your actual admin route
                        } else {
                            navController.navigate(route_home)
                        }
                        onResult?.invoke(true)
                    }.addOnFailureListener {
                        navController.navigate(route_home)
                        onResult?.invoke(true)
                    }
                }else{
                    onResult?.invoke(false)
                }
            } else {
                val exception = task.exception
                val userMessage = when (exception) {
                    is FirebaseAuthInvalidUserException -> "You do not have an account"
                    is FirebaseAuthInvalidCredentialsException -> "Incorrect password"
                    else -> "Login failed: ${exception?.localizedMessage ?: "Unknown error"}"
                }
                Toast.makeText(context, userMessage, Toast.LENGTH_LONG).show()
                onResult?.invoke(false)
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