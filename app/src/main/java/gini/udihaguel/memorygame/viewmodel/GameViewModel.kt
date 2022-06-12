package gini.udihaguel.memorygame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gini.udihaguel.memorygame.models.Card
import gini.udihaguel.memorygame.models.CardResource
import gini.udihaguel.memorygame.models.Game
import gini.udihaguel.memorygame.networking.ApiManager

class GameViewModel : ViewModel() {

    private val _allCardsLiveData = MutableLiveData<List<Card<*>>>()
    val allCardsLiveData: LiveData<List<Card<*>>> get() = _allCardsLiveData

    private val _gameLiveData = MutableLiveData<Game>()
    val gameLiveData: LiveData<Game> get() = _gameLiveData

    private val _lockUiLiveData = MutableLiveData<Boolean>()
    val lockUiLiveData: LiveData<Boolean> get() = _lockUiLiveData

    private val apiManager = ApiManager()

    private val imagesMap = mutableMapOf<String, Int>()


    fun apiCall(){
        apiManager.getContentFromApi(){ response ->
            val cardList = mutableListOf<Card<*>>()
            response.contentList.forEach { content->
                cardList.add(Card(content.toString()))
            }
            cardList.sortBy { card -> card.content.toString() }
            initImagesMap(cardList)
            _allCardsLiveData.postValue(cardList)
        }
    }

    private fun initImagesMap(cards: List<Card<*>>) {
        if (imagesMap.isEmpty()){
            cards.forEachIndexed { index, card ->
                if (!imagesMap.containsKey(card.content.toString()))
                    imagesMap[card.content.toString()] = CardResource.cardsDrawableIds[(index%16)/2]
            }
        }
    }


     fun startGame(difficulty: Int) {
        val game = Game()
         game.startGame(difficulty,allCardsLiveData.value!!)
         game.currentGameCards.forEach {
             it.isFaceUp = false
             it.isMatched = false
             it.isCardDirty = true
         }
        _gameLiveData.postValue(game)
    }

    fun convertToDrawable(index: Int):Int = imagesMap[_gameLiveData.value!!.currentGameCards[index].content.toString()]!!

    fun onCardClicked(index: Int) {
        val game = _gameLiveData.value
        game?.handleCardClicked(index)
        if (game?.isGameDirty == true){
            _lockUiLiveData.postValue(true)
            _gameLiveData.postValue(game)
        } else
            _lockUiLiveData.postValue(false)


    }

    fun setDirtyFalse(index: Int) {
        val game = _gameLiveData.value
        game?.setDirtyState(index, false)
    }

    fun checkForMatch(){
        val game = _gameLiveData.value
        game?.detectAndHandleMatch()
        if (game?.isGameDirty == true)
            _gameLiveData.postValue(game)
        _lockUiLiveData.postValue(false)
    }


}