package com.example.helperapp.model

class User{
    var uid:String=""
    var firstName: String=""
    var lastName: String=""
    var email: String=""
    var pass: String=""
    var profilePictureUrl:String?=null
    var isAuthenticated: Boolean=false

    constructor(
        uid: String,
        firstName: String,
        lastName: String,
        email: String,
        pass: String,
        isAuthenticated: Boolean
    )
    {
        this.uid=uid
        this.firstName=firstName
        this.lastName=lastName
        this.email=email
        this.pass=pass
        this.isAuthenticated=isAuthenticated


    }
    constructor()

}