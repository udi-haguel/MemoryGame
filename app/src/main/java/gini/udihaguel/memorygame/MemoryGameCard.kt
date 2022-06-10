package gini.udihaguel.memorygame

data class MemoryGameCard(
    val cardNumber:Int,
    val front:Int,
    val back:Int = R.drawable.card_back,
    var isBack:Boolean = true
) {
}