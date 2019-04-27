package br.com.fabriciohsilva.heroesapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Hero  (
    val _id: String? = null,
    @SerializedName("nome") val name: String? = null,
    @SerializedName("poder") val power: String? = null,
    @SerializedName("fraqueza") val weakness: String? = null,
    @SerializedName("vilao") val villain: Boolean = false,
    val avatar: String? = null
) : Serializable