package com.doodhbhandaarvendor.model

import com.google.gson.annotations.SerializedName


data class NotificationModel(
        @SerializedName("to")
        var token: String? = "",
        @SerializedName("collapse_key")
        var collapseKey: String = "type_a",
        @SerializedName("notification")
        var notificationModel: NotificationData
)

data class NotificationData(
        @SerializedName("body")
        var body: String? = "",
        @SerializedName("title")
        var title: String = ""
)