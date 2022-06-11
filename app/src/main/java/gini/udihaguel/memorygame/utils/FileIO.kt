package gini.udihaguel.memorygame.utils

import android.content.Context
import java.io.File

class FileIO {
    companion object{
        fun read(context: Context, filename: String): String {
            return try {
                val file = File(context.filesDir, filename)
                file.readText()
            }catch (e: Exception){
                ""
            }
        }

        fun write(context:Context, filename:String, message:String){
            context.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(message.toByteArray())
            }
        }
    }
}