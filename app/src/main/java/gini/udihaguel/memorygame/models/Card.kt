package gini.udihaguel.memorygame.models

import java.util.*

data class Card<T : Comparable<T>> (val content:T,
                                    val uniqueId:String = UUID.randomUUID().toString(),
                                    var isFaceUp:Boolean = false,
                                    var isDirty:Boolean = false,
                                    var isMatched:Boolean = false
)