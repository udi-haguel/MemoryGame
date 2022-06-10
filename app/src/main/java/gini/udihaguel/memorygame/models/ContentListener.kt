package gini.udihaguel.memorygame.models

import gini.udihaguel.memorygame.entities.ContentResponse

fun interface ContentListener {
    fun onGetContent(content: ContentResponse)
}