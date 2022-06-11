package gini.udihaguel.memorygame

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.lifecycle.ViewModelProvider
import gini.udihaguel.memorygame.databinding.ActivityMainBinding
import gini.udihaguel.memorygame.extensions.toJSONArray
import gini.udihaguel.memorygame.models.Card
import gini.udihaguel.memorygame.models.Game
import gini.udihaguel.memorygame.utils.FileIO
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var gameViewModel: GameViewModel
    private lateinit var imageViews:List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]
        gameViewModel.apiCall()
        gameViewModel.allCardsLiveData.observe(this) {
            gameViewModel.startGame(1)
        }
        gameViewModel.gameLiveData.observe(this){
            if (it.hasGameEnded) {
                saveGame(it)
                gameViewModel.startGame(1)
            }
            notifyGameChange()
        }

        imageViews = binding.cardsFlow.referencedIds.map(this::findViewById)

    }


    private fun notifyGameChange(){
        val activeGame = gameViewModel.gameLiveData.value
        imageViews.forEachIndexed { index, imageView ->
            val activeGameCards = activeGame!!.currentGameCards

            if (index < activeGameCards.count()) {
                if (activeGameCards[index].isCardDirty){
                    startFlipAnimation(imageView, activeGameCards[index], index, activeGame.flipBackDelay)

                } else {
                    val drawableRes = if (activeGameCards[index].isFaceUp) gameViewModel.convertToDrawable(index) else R.drawable.card_back
                    imageView.setImageResource(drawableRes)
                }
                imageView.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        onCardClicked(index)
                    }
                }
            } else {
                imageView.apply {
                    visibility = View.GONE
                    setOnClickListener(null)
                    setImageResource(R.drawable.card_back)
                }
            }
        }

    }

    private fun onCardClicked(index: Int) {
        gameViewModel.onCardClicked(index)
    }

    private fun startFlipAnimation(iv:ImageView, card:Card<*>, index:Int, delay:Long = 0L){
        val faceUpResource = gameViewModel.convertToDrawable(index)
        val faceDownResource = R.drawable.card_back
        val anim1 = ObjectAnimator.ofFloat(iv,
            "scaleX",
            1f, 0f)
            .apply {
                startDelay = delay
                interpolator = DecelerateInterpolator()
        }
        val anim2 = ObjectAnimator.ofFloat(iv,
            "scaleX",
            0f, 1f)
            .apply {
                interpolator = AccelerateInterpolator()
        }
        anim1.doOnEnd {
            if (card.isFaceUp)
                iv.setImageResource(faceUpResource)
            else
                iv.setImageResource(faceDownResource)

            anim2.start()
        }
        anim2.doOnEnd {
            gameViewModel.setDirtyFalse(index)
            gameViewModel.checkForMatch()
        }
        anim1.start()

    }



    private fun saveGame(game: Game){
        val gameJS:JSONObject = game.createJSON()
        val gameJSArray:JSONArray = FileIO.read(this, "GamesArray.txt").toJSONArray()
        gameJSArray.put(gameJS)
        FileIO.write(this, "GamesArray.txt", gameJSArray.toString())
    }

/*


    private fun addIVs(c:List<Card<*>>){
        // removing all views from container
        binding.cardsFlow.referencedIds = intArrayOf()
        if (binding.cardsContainer.childCount > 1){
        for (i in 1 until binding.cardsContainer.childCount){
            binding.cardsContainer.removeViewAt(1)
        }
        }



        // re-creating the views
        var intArrIds = intArrayOf()
        c.forEachIndexed { index, card ->
            var view = addCard()
            binding.cardsFlow.referencedIds += view.id
        }

        binding.cardsFlow.setMaxElementsWrap(3)
        binding.cardsFlow.setOrientation(Flow.HORIZONTAL)
        binding.cardsFlow.setWrapMode(Flow.WRAP_CHAIN)


        println("")

    }

    private fun addCard():View {
        val cardView = layoutInflater.inflate(R.layout.card_view, binding.root, false)
        cardView.id = View.generateViewId()
        binding.cardsContainer.addView(cardView)
        return cardView
    }

     */
/*
    private fun setupTextViewColor() {
        val paint: TextPaint = binding.tvMemoryGame.paint
        val width = paint.measureText(binding.tvMemoryGame.text.toString())

        val textShader: Shader = LinearGradient(
            0f, 0f, width, binding.tvMemoryGame.textSize, intArrayOf(
                Color.parseColor("#0815d1"),
                Color.parseColor("#4abfff"),
            ), null, Shader.TileMode.MIRROR
        )
        binding.tvMemoryGame.paint.shader = textShader
    }

 */
/*

        for (i in 0..gameJSArray.length()){
            val currentGameJS = gameJSArray.getJSONObject(i)
            currentGameJS.getString("difficulty")
        }
     */
}