package com.energy0124.swiplist.feature.model

import com.squareup.moshi.Json
import java.io.Serializable

//class Item {
//    @Json(name = "_id") lateinit var id: String
//    lateinit var name: String
//    lateinit var category: String
//    var description: String? = null
//    var imageUrl: String? = null
//    var dataUrl: String? = null
//    var src: String? = null
//    var createdBy: User? = null
//    var metadata: JSONObject? = null
//}

data class Item(
        @Json(name = "_id") val id: String = "",
        val name: String = "",
        val category: String = "",
        val description: String? = null,
        val imageUrl: String? = null,
        val dataUrl: String? = null,
        val src: String? = null,
        val createdBy: String? = null
//        val metadata: JSONObject? = null
): Serializable