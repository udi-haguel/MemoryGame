package gini.udihaguel.memorygame

import android.R
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.os.Bundle
import android.text.TextPaint
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.lifecycle.ViewModelProvider
import gini.udihaguel.memorygame.databinding.ActivityMainBinding
import gini.udihaguel.memorygame.networking.ApiManager
import java.util.*


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var gameViewModel: GameViewModel

    private val apiManager = ApiManager()

    private lateinit var imageViews:List<ImageView>

    private var cards = mutableListOf<MemoryGameCard>()

    private var twoCards:Pair<MemoryGameCard?,MemoryGameCard?> = Pair(null,null)
    private var twoImageViews:Pair<ImageView?, ImageView?> = Pair(null,null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]

        CardResource().cardsDrawableIds.forEachIndexed { i, frontImage ->
            if (cards.size == 16) return@forEachIndexed
            cards.add(MemoryGameCard(i, frontImage))
            cards.add(MemoryGameCard(i, frontImage))
        }

        cards.shuffle()

        apiManager.getContentFromApi{
            println("#### $it")
        }

        setupTextViewColor()

        imageViews = binding.cardsFlow.referencedIds.map(this::findViewById)

        imageViews.forEachIndexed { i, imageView ->
            imageView.setOnClickListener {
                startFlipAnimation(imageView, cards[i])

                if (twoCards.first == null) {
                    twoCards = twoCards.copy(first = cards[i])
                    twoImageViews = twoImageViews.copy(first = imageView)
                }else if (twoCards.second == null) {
                    twoCards = twoCards.copy(second = cards[i])
                    twoImageViews = twoImageViews.copy(second = imageView)
                }
                checkIfMatched()
            }
        }
    }




    private fun startFlipAnimation(iv:ImageView, card:MemoryGameCard, delay:Long = 0L){
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
            if (card.isBack)
                iv.setImageResource(card.front)
            else
                iv.setImageResource(card.back)

            card.isBack = !card.isBack
            anim2.start()
        }
        anim1.start()
    }


    private fun checkIfMatched() {
        if (twoCards.first != null && twoCards.second != null) {
            if (twoCards.first!!.cardNumber != twoCards.second!!.cardNumber) {
                startFlipAnimation(twoImageViews.first!!, twoCards.first!!, 1000)
                startFlipAnimation(twoImageViews.second!!, twoCards.second!!, 1000)
            } else {
                twoImageViews.first!!.isClickable = false
                twoImageViews.second!!.isClickable = false
            }
            twoCards = Pair(null,null)
            twoImageViews = Pair(null,null)
        }
    }
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

    private fun addImageView(){
        val imageView = ImageView(this)
        imageView.id = View.generateViewId()
        binding.root.addView(imageView)
        binding.cardsFlow.referencedIds += imageView.id
    }
}