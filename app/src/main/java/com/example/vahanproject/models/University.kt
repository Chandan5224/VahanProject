package com.example.vahanproject.models

import com.google.gson.annotations.SerializedName
import org.json.JSONArray

data class University(
    @SerializedName("web_pages")
    val webPages: List<String>,
    @SerializedName("domains")
    val domains: List<String>,
    @SerializedName("country")
    val country: String,
    @SerializedName("alpha_two_code")
    val alphaTwoCode: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("state-province")
    val stateProvince: String?
)
