package com.energy0124.swiplist.feature.model

import com.squareup.moshi.Json
import java.io.Serializable

data class MinifiedUser(
        @Json(name = "_id") val id: String = "",
        val username: String = "",
        val iconUrl: String? = null
) : Serializable