package com.example.renovate_.globaldata

import com.example.renovate_.model.staff

open class staffglobaldata {
    companion object{
        open var currentOnlineStaff : staff? = null

        var userPhoneKey  = "UserPhone"
        var userPasswordKey = "UserPassword"
        var userCollegeCode = "UserCollegeCode"


    }

}