package com.envios.githubfinder.utils

import retrofit2.HttpException
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

object ErrorUtils  {
    fun getErrorThrowableMsg(error: Throwable): String = when (error) {
        is HttpException ->
            when (error.code()) {
                HttpsURLConnection.HTTP_UNAUTHORIZED -> "Unauthorized. Cannot access the data. "
                HttpsURLConnection.HTTP_NOT_FOUND -> "Data Not Found"
                HttpsURLConnection.HTTP_INTERNAL_ERROR -> "There is problem on Server"
                HttpsURLConnection.HTTP_BAD_REQUEST -> "Bad Request"
                HttpsURLConnection.HTTP_FORBIDDEN -> "Session has ended"
                else -> "Oops, There is a problem. Try to connect again later"
            }
        is UnknownHostException -> "There is a problem with network connection"
        else -> "There is a problem"
    }

}