package gini.udihaguel.memorygame.models

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.delay
import java.sql.Time
import java.time.LocalDateTime
import java.util.*

class Game() {

    //var cards:MutableList<Card<Comparable<Comparable<*>>>> = mutableListOf()

    var currentGameCards = mutableListOf<Card<*>>()
    private var difficultyLevel: Int = 1
    private var clickedIndices = mutableListOf<Int>()
    private var mismatchCounter:Int = 0
    var flipBackDelay:Long = 0L
    private lateinit var gameStartTime: Date
    private lateinit var gameEndTime: Date

    fun startGame(difficulty:Int, cards: List<Card<*>>){
        clickedIndices.clear()
        difficultyLevel = difficulty
        currentGameCards.clear()
        currentGameCards.addAll(cards.subList(0, difficulty*4))
        currentGameCards.shuffle()
        gameStartTime = Calendar.getInstance().time

    }


    fun handleCardClicked (cardIndex: Int){
        flipBackDelay = 0L
        currentGameCards[cardIndex].isFaceUp = !currentGameCards[cardIndex].isFaceUp
        currentGameCards[cardIndex].isDirty = true
        if (clickedIndices.size == 0 ) {
            clickedIndices.add(cardIndex)
        }else {
            clickedIndices.add(cardIndex)
            checkForMatch()
        }


    }

    private fun checkForMatch() {
        val card1 = currentGameCards[clickedIndices[0]]
        val card2 = currentGameCards[clickedIndices[1]]
        card1.isDirty = false
        card2.isDirty = false
        clickedIndices.clear()


        if (card1.content.toString() == card2.content.toString()){
            Log.d("TAG", "checkForMatch: matched")
            card1.isMatched = true
            card2.isMatched = true
        } else {
            Log.d("TAG", "checkForMatch: no match")
            card1.isDirty = true
            card2.isDirty = true
            card1.isFaceUp = false
            card2.isFaceUp = false
            mismatchCounter++
            flipBackDelay = 2000L
        }

    }

    fun setDirtyStateToFalse(cardIndex: Int, state:Boolean) {
        currentGameCards[cardIndex].isDirty = state
    }


}