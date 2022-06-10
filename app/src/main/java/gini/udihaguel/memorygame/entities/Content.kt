package gini.udihaguel.memorygame.entities

import com.google.gson.annotations.SerializedName

data class Content<T> (
    @SerializedName(value="number", alternate = ["Letter"]) var content:T
)
