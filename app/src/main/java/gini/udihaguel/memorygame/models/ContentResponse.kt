package gini.udihaguel.memorygame.models

import com.google.gson.annotations.SerializedName

data class ContentResponse (
    @SerializedName(value="numbers", alternate = ["Letters"]) val contentList:List<Content<*>>
    )