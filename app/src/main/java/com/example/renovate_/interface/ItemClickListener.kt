package com.example.renovate_.`interface`

import android.view.View

open interface ItemClickListener {

    fun onClick(view : View?, position: Int, isLongClick : Boolean )

}