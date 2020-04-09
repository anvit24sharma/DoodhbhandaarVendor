package com.doodhbhandaarvendor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VariantModel(
    var available:Boolean = false,
    var variantName :String="",
    var qty : Int=0
):Parcelable {}
