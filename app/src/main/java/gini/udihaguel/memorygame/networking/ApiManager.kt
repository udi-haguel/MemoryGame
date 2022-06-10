package gini.udihaguel.memorygame.networking

import gini.udihaguel.memorygame.models.ContentListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ApiManager {


    private val contentService: ContentService = RetrofitHelper.getInstance()
        .create(ContentService::class.java)


    @OptIn(DelicateCoroutinesApi::class)
    fun getContentFromApi(listener: ContentListener) {
        GlobalScope.launch {
            val response = contentService.getContent(NUMBERS_KEY)
            response.body()?.let { listener.onGetContent(it) } ?: throw NoDataException()
        }
    }


    companion object {
        const val NUMBERS_KEY = "w8LSbsZb"
        const val LETTERS_KEY = "cZd0y0DL"
    }
}