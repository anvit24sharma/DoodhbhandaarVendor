package com.doodhbhandaarvendor.model

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
        @SerializedName("success")
        var success: Boolean = true
)