package com.example.alumniapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val firstName:String, val lastName:String, val uid:String):Parcelable {
    constructor() : this("","","")
}