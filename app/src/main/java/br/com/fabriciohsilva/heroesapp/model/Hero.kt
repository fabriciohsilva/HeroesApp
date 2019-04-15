package br.com.fabriciohsilva.heroesapp.model

import com.google.gson.annotations.SerializedName

data class Hero (
    val id: String? = null,
    @SerializedName("nome") val name: String,
    @SerializedName("poder") val power: String
//    ,@SerializedName("fraqueza") val weakness: String,  //descomentar após estes atributos serem incluídos na api
//    @SerializedName("heroi") val hero: Boolean
)