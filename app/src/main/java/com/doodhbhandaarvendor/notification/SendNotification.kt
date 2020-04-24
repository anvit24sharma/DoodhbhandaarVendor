package com.doodhbhandaarvendor.notification

import android.util.Log
import com.doodhbhandaarvendor.model.NotificationData
import com.doodhbhandaarvendor.model.NotificationModel
import com.doodhbhandaarvendor.model.NotificationResponse
import com.doodhbhandaarvendor.remote.ApiCallInterface
import com.doodhbhandaarvendor.ui.MainActivity
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SendNotification{
    companion object{
        fun sendNotification(message: String) {
            val httpClient = OkHttpClient.Builder()

            val retrofit = Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            val postsApi = retrofit.create(ApiCallInterface::class.java)

            MainActivity.tokenList.forEach { token ->

                val notificationModel =
                    NotificationModel(token, "type_a", NotificationData(message, "Doodhbhandaar"))
                val call = postsApi.sendNotification(notificationModel)

                call.enqueue(object : Callback<NotificationResponse> {

                    override fun onFailure(call: Call<NotificationResponse>?, t: Throwable?) {
                        // Toast.makeText(,"On Failure", Toast.LENGTH_SHORT).show()
                        Log.i("On failure", "Reason : " + t?.fillInStackTrace())

                    }

                    override fun onResponse(
                        call: Call<NotificationResponse>?,
                        response: Response<NotificationResponse>?
                    ) {
                        Log.i("On response", "Reason : " + response)


                    }

                })
            }
        }
    }
}