package com.doodhbhandaarvendor.remote


import com.doodhbhandaarvendor.model.NotificationModel
import com.doodhbhandaarvendor.model.NotificationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiCallInterface {

    @Headers("Content-Type:application/json", "Authorization:key=AAAAr6ZUAPk:APA91bFtlit1IAeE9p3_Y3mYHsJFNE3tVs_xOFmQBRe38tmyv9TWkl7vk-pJscY1TGvzkyBoWQRU04rT1uotz08C4k4aFKzs8Br1EdU5I6dH2loE3-wR08-a6HThJv75A_80-IKsDhen")
    @POST("send")
    fun sendNotification( @Body authModel: NotificationModel): Call<NotificationResponse>
}
