package com.doodhbhandaarvendor.model

data class OrderPlaceModel(
    var products :ArrayList<OrderPlaceProductModel>,
    var userId :String,
    var address :String,
    var schedule : String,
    var paymentMode :String,
    var orderDate :String,
    var status :String
){}

data class OrderPlaceProductModel(
    var productName:String,
    var productCost :String,
    var variants :ArrayList<VariantModel>
) {}
