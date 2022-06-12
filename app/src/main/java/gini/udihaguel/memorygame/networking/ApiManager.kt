package gini.udihaguel.memorygame.networking

import gini.udihaguel.memorygame.models.OnGetContentListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ApiManager {


    private val contentService: ContentService = RetrofitHelper.getInstance()
        .create(ContentService::class.java)


    @OptIn(DelicateCoroutinesApi::class)
    fun getContentFromApi(listener: OnGetContentListener) {
        GlobalScope.launch {
            val response = contentService.getContent(LETTERS_KEY)
            response.body()?.let { listener.onGetContent(it) } ?: throw NoDataException()
        }
    }


    companion object {
        const val NUMBERS_KEY = "w8LSbsZb"
        const val LETTERS_KEY = "cZd0y0DL"
    }
}