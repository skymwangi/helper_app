package com.example.helperapp.ui.theme.screens.register


import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalFocusManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.helperapp.data.AuthViewModel
import com.example.helperapp.navigation.route_home
import com.example.helperapp.navigation.route_login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun Registerscreen(navController: NavHostController) {
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var pass by remember { mutableStateOf(TextFieldValue("")) }
    var confpass by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController= LocalSoftwareKeyboardController.current
    BackHandler {focusManager.clearFocus()
    keyboardController?.hide()
    navController.popBackStack()}



    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val counties = listOf(
        "Mombasa", "Kwale", "Kilifi", "Tana River", "Lamu", "Taita Taveta",
        "Garissa", "Wajir", "Mandera", "Marsabit", "Isiolo", "Meru",
        "Tharaka-Nithi", "Embu", "Kitui", "Machakos", "Makueni", "Nyandarua",
        "Nyeri", "Kirinyaga", "Murang'a", "Kiambu", "Turkana", "West Pokot",
        "Samburu", "Trans Nzoia", "Uasin Gishu", "Elgeyo Marakwet", "Nandi", "Baringo",
        "Laikipia", "Nakuru", "Narok", "Kajiado", "Kericho", "Bomet", "Kakamega",
        "Vihiga", "Bungoma", "Busia", "Siaya", "Kisumu", "Homa Bay", "Migori",
        "Kisii", "Nyamira", "Nairobi"
    )
    var county by remember { mutableStateOf(TextFieldValue("")) }
    var countyError by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val authViewModel= AuthViewModel(navController, LocalContext.current)





    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF7D5260),
                    Color(0xFFFAF3E0),
                    Color.Magenta
                )
            ))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Register", style = MaterialTheme.typography.headlineSmall,
            fontSize = 45.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.Black)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(text = "First Name",
                color = Color.Black) },
            isError = errorMessage.isNotBlank(),
            colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Magenta,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(text = "Last Name",
                color = Color.Black) },
            isError = errorMessage.isNotBlank(),
            colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Magenta,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email",
                color = Color.Black) },
            isError = errorMessage.isNotBlank(),
            colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Magenta,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text(text = "Password",
                color = Color.Black) },
            isError = errorMessage.isNotBlank(),
            colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Magenta,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confpass,
            onValueChange = { confpass = it },
            label = { Text(text = "Confirm Password",
                color = Color.Black) },
            isError = errorMessage.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Magenta,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )



        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = county,
            onValueChange = {county=it
                            countyError=false},
            label = {Text(text = "Which county do you live in?",
                color = Color.Black)},
            isError = countyError,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Magenta,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black)
        )
        if (countyError) {
            Text(
                text = "Please enter a valid Kenyan county",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }


        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = Button@{
                showError = false
                errorMessage = ""

                if (firstName.text.isBlank()) {
                    showError = true
                    errorMessage = "First name is required"
                    return@Button
                }

                if (lastName.text.isBlank()) {
                    showError = true
                    errorMessage = "Last name is required"
                    return@Button
                }

                if (email.text.isBlank()) {
                    showError = true
                    errorMessage = "Email is required"
                    return@Button
                }

                if (pass.text.isBlank()) {
                    showError = true
                    errorMessage = "Password is required"
                    return@Button
                }

                if (confpass.text != pass.text) {
                    showError = true
                    errorMessage = "Passwords do not match"
                    return@Button
                }

                if (county.text.isBlank()) {
                    showError = true
                    errorMessage = "County is required"
                    return@Button
                }

                if (!counties.any { it.equals(county.text.trim(), ignoreCase = true) }) {
                    showError = true
                    errorMessage = "Please enter a valid Kenyan county"
                    return@Button
                }

                isLoading = true
//
//                Button(onClick = {
//                    val error = validateInputs(
//                        firstName = firstName.text,
//                        lastName = lastName.text,
//                        email = email.text,
//                        pass = pass.text,
//                        confpass = confpass.text,
//                        county = county.text,
//                        counties = counties
//                    )
//                    if (error != null) {
//                        showError = true
//                        errorMessage = error
//                        return@Button
//                    }

                    authViewModel.signup(
                        firstName = firstName.text,
                        lastName = lastName.text,
                        email = email.text,
                        pass = pass.text,
                        county = county.text,
                        confpass= confpass.text,
                        isAdmin = email.text.endsWith("@adminvolunteers.org")
                    )
                    { userId: String? ->
                if (userId != null) {
                    val userData = mapOf(
                        "firstName" to firstName.text,
                        "lastName" to lastName.text,
                        "email" to email.text,
                        "county" to county.text,
                        "password" to pass.text // Save the password in Firestore
                    )
                    FirebaseFirestore.getInstance().collection("users").document(userId)
                        .set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                            navController.navigate(route_home)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }else{
                    Toast.makeText(context, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }



                },
                colors = ButtonDefaults.buttonColors(Color.Magenta)) {


                    Text(text = "Register")
                }
            }


        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = {
                navController.navigate(route_login)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Already have an account? Log in",
                color = Color.Black,
                fontSize = 16.sp
            )
        }





//        Spacer(modifier = Modifier.height(12.dp))
//        Text(text = "OR",
//            fontSize = 24.sp,
//            modifier = Modifier.align(Alignment.CenterHorizontally),
//            color = Color.Black)
//
//
//        OutlinedButton(
//            onClick = {
//                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
//            },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(Nude)
//        ) {
//            Text("Continue with Email")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))




    }
}

private fun validateInputs(
    firstName: String,
    lastName: String,
    email: String,
    pass: String,
    confpass: String,
    county: String,
    counties: List<String>
): String? {
    if (firstName.isBlank()) return "First name is required"
    if (lastName.isBlank()) return "Last name is required"
    if (email.isBlank()) return "Email is required"
    if (pass.isBlank()) return "Password is required"
    if (confpass != pass) return "Passwords do not match"
    if (county.isBlank()) return "County is required"
    if (!counties.any {
            it.equals(
                county.trim(),
                ignoreCase = true
            )
        }) return "Please enter a valid Kenyan county"
    return null
}

@Preview
@Composable
private fun Registerprev() {
    Registerscreen(rememberNavController())

}
