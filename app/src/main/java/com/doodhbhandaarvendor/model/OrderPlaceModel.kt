package com.doodhbhandaarvendor.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
class OrderPlaceModel (
        var products :ArrayList<OrderPlaceProductModel> = ArrayList(),
        var userId :String ="",
        var address :String="",
        var schedule : String="",
        var lastOrderDate :String= "",
        var paymentMode :String="",
        var orderDate :String="",
        var status :String="",
        var totalCost :String="",
        var orderId :String =""
): Comparable<OrderPlaceModel>,Parcelable{

    @SuppressLint("SimpleDateFormat")
    override fun compareTo(other: OrderPlaceModel): Int {
        val formatter = SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy")
        val date1: Date = formatter.parse(orderDate)
        val date2: Date = formatter.parse(other.orderDate)
        return date1.compareTo(date2)
    }

}

@Parcelize
class OrderPlaceProductModel(
    var productName:String="",
    var productCost :String="",
    var variants :ArrayList<VariantModel> = ArrayList(),
    var subscriptionPlan :String="",
    var coupon :String="",
    var paymentCollectionDay :String="",
    var nextDate :String = ""
):Parcelable {}

