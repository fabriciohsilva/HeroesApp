package br.com.fabriciohsilva.heroesapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Hero  (
    val _id: String? = null,
    @SerializedName("nome")
    val name: String? = null,
    @SerializedName("poder")
    val power: String? = null
//    ,@SerializedName("fraqueza") val weakness: String,  //descomentar após estes atributos serem incluídos na api
//    @SerializedName("heroi") val hero: Boolean
) : Serializable