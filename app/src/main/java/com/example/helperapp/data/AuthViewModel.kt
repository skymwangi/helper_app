package com.example.helperapp.data


import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.navigation.NavHostController
import com.example.helperapp.navigation.route_login
import com.google.firebase.auth.FirebaseAuth
import com.example.helperapp.model.User
import com.example.helperapp.navigation.route_home
import com.example.helperapp.navigation.route_register
import com.google.firebase.database.FirebaseDatabase


class AuthViewModel(
    var navController: NavHostController,
    var context: ProvidableCompositionLocal<Context>
) {

    var mAuth: FirebaseAuth


    init {
        mAuth = FirebaseAuth.getInstance()

    }

    fun signup(
        firstName: String,
        lastName: String,
        profilePictureUrl: String? = null,
        email: String,
        pass: String,
        confpass: String
    ) {


        if (email.isBlank() || pass.isBlank() || confpass.isBlank()) {
            Toast.makeText(context, "Please email and password cannot be blank", Toast.LENGTH_LONG)
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
                            firstName, lastName, email, pass, uid,
                            profilePictureUrl = null,
                            isAuthenticated = false
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
                                Toast.makeText(
                                    context,
                                    "${it.exception!!.message}",
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

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Succeffully Logged in", Toast.LENGTH_LONG).show()
                navController.navigate(route_home)
//                navController.navigate(route_home)TO TAKE YOU TO A DIIFFERNT PAGE
            } else {
                Toast.makeText(context, "${it.exception!!.message}", Toast.LENGTH_LONG).show()
                navController.navigate(route_login)
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