package com.example.renovate_.model

import java.io.Serializable

open class Sample : Serializable {

    var status : String? = null
    var sampleId :String? = null
    var phone :String? = null
    var image :String? = null
    var date :String? = null
    var title :String? = null
    var description :String? = null


    constructor(){}

    constructor(phone : String , sampleId: String?, image:String, date:String, title:String, description:String  , status:String)
    {
        this.sampleId =sampleId
        this.image =image
        this.date =date
        this.title =title
        this.description =description
        this.phone = phone
        this.status = status

    }

}