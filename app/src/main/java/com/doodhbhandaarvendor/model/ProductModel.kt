package com.doodhbhandaarvendor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductModel (
    var product_name:String="",
    var product_cost :String="",
    var product_image_url :String="",
    var productId :String="",
    var variants : ArrayList<VariantModel>,
    var subscriptionPlan :String="",
    var paymentCollectionDay : String="",
    var couponName :String=""
): Parcelable
