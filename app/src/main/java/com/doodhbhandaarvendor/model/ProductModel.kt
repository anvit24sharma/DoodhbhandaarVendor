package com.doodhbhandaarvendor.model

data class ProductModel (
    var product_name:String,
    var product_cost :String,
    var product_image_url :String,
    var productId :String,
    var variants : ArrayList<VariantModel>,
    var subscriptionPlan :String
)
