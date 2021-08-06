package com.example.renovate_.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
open class staff : Serializable {

    var id :String? = null
    var phoneNumber :String? = null
    var password :String? = null
    var userName :String? = null
    var mail :String? = null
    var collegeCode :String? = null
    var collegeName :String? = null

    constructor(){}

    constructor(id: String?, phoneNumber:String, password:String, userName:String, mail:String, collegeCode:String, collegeName:String  )
    {
        this.id =id
        this.phoneNumber =phoneNumber
        this.password =password
        this.userName =userName
        this.mail =mail
        this.collegeCode =collegeCode
        this.collegeName =collegeName

    }

}