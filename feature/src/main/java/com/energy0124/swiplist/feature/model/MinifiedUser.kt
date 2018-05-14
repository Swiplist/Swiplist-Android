package com.energy0124.swiplist.feature.model

import com.squareup.moshi.Json
import java.io.Serializable

data class MinifiedUser(
        @Json(name = "_id") var id: String = "",
        var username: String = "",
        var iconUrl: String? = null
) : Serializable