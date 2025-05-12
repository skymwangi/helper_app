package com.example.helperapp.model

class User{
    var uid:String=""
    var firstName: String=""
    var lastName: String=""
    var email: String=""
    var pass: String=""
    var county: String=""// Add this// And this
    var isAdmin: Boolean = false
    var profilePictureUrl:String?=null

    constructor(
        uid: String,
        firstName: String,
        lastName: String,
        email: String,
        pass: String,
        county: String,
        profilePictureUrl: String?,
        isAdmin: Boolean
    )
    {
        this.uid=uid
        this.firstName=firstName
        this.lastName=lastName
        this.email=email
        this.county=county
        this.profilePictureUrl=profilePictureUrl
        this.isAdmin=isAdmin
        this.pass=pass


    }
    constructor()

}