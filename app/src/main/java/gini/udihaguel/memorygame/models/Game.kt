package gini.udihaguel.memorygame.models

import android.util.Log
import org.json.JSONObject
import java.util.*

class Game() {

    var currentGameCards = mutableListOf<Card<*>>()
    private var difficultyLevel: Int = 1
    private var clickedIndices = mutableListOf<Int>()
    private var mismatchCounter:Int = 0
    var flipBackDelay:Long = 0L
    var isGameDirty:Boolean = false
    var hasGameEnded:Boolean = false
    private lateinit var gameStartTime: Date
    private lateinit var gameEndTime: Date


    fun startGame(difficulty:Int, cards: List<Card<*>>){
        clickedIndices.clear()
        difficultyLevel = difficulty
        currentGameCards.clear()
        currentGameCards.addAll(cards.subList(0, difficulty*4))
        currentGameCards.shuffle()
        gameStartTime = Calendar.getInstance().time
        flipBackDelay = 0L
        mismatchCounter = 0
        isGameDirty = false
        hasGameEnded = false
    }


    fun handleCardClicked (cardIndex: Int){
        if (currentGameCards[cardIndex].isFaceUp){
            isGameDirty = false
            return
        }

        flipBackDelay = 0L
        currentGameCards[cardIndex].isFaceUp = !currentGameCards[cardIndex].isFaceUp
        currentGameCards[cardIndex].isCardDirty = true
        if (clickedIndices.size != 2 ) {
            clickedIndices.add(cardIndex)
        }
        isGameDirty = true

        //todo: check for game over/match
    }


    fun detectAndHandleMatch() {
        if (clickedIndices.size != 2) {
            isGameDirty = false
            return
        }

        val card1 = currentGameCards[clickedIndices[0]]
        val card2 = currentGameCards[clickedIndices[1]]
        card1.isCardDirty = false
        card2.isCardDirty = false
        clickedIndices.clear()


        if (card1.content.toString() == card2.content.toString()){
            Log.d("checkMatch", "detectAndHandleMatch: matched")
            card1.isMatched = true
            card2.isMatched = true
        } else {
            Log.d("checkMatch", "detectAndHandleMatch: no match")
            card1.isCardDirty = true
            card2.isCardDirty = true
            card1.isFaceUp = false
            card2.isFaceUp = false
            mismatchCounter++
            flipBackDelay = 1000L
        }
        checkForGameOver()
        isGameDirty = true

    }

    private fun checkForGameOver() {
        currentGameCards.forEach {
            hasGameEnded = true
            if (!it.isMatched)
                hasGameEnded = false
        }
    }

    fun setDirtyState(cardIndex: Int, state:Boolean) {
        currentGameCards[cardIndex].isCardDirty = state
    }

    fun createJSON(): JSONObject {
        val gameJS:JSONObject = JSONObject()
        gameJS.apply {
            put("difficulty","$difficultyLevel")
            //TODO: save more data
        }

        return gameJS
    }
}


//var cards:MutableList<Card<Comparable<Comparable<*>>>> = mutableListOf()