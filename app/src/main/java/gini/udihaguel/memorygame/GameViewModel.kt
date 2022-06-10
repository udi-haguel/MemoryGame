package gini.udihaguel.memorygame

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gini.udihaguel.memorygame.models.Card
import gini.udihaguel.memorygame.models.Game
import gini.udihaguel.memorygame.networking.ApiManager

class GameViewModel : ViewModel() {

    private val _cards = MutableLiveData<List<Card<*>>>()

    val cards: LiveData<List<Card<*>>> get() = _cards

    private val apiManager = ApiManager()
    private val game:Game? = null



    fun apiCall(){
        apiManager.getContentFromApi(){
            val cardList = mutableListOf<Card<*>>()
            it.contentList.forEach { content->
                cardList.add(Card(content.toString()))
            }
            _cards.postValue(cardList)
        }
    }
}