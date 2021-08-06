package com.example.renovate_.model

import java.io.Serializable

open class Admin : Serializable {

    var phoneNumber :String? = null
    var password :String? = null
    var collegeCode :String? = null

    constructor(){}

    constructor( phoneNumber:String, password:String,  collegeCode:String )
    {
        this.phoneNumber =phoneNumber
        this.password =password
        this.collegeCode =collegeCode


    }

}