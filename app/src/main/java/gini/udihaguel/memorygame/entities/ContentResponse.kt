package gini.udihaguel.memorygame.entities

import com.google.gson.annotations.SerializedName

data class ContentResponse (
    @SerializedName(value="numbers", alternate = ["Letters"]) val contentList:List<Content<*>>
    )