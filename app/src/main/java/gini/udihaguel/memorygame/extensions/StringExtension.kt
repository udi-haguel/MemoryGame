package gini.udihaguel.memorygame.extensions

import org.json.JSONArray

fun String.toJSONArray():JSONArray{
    return try{
        JSONArray(this)
    } catch (e: Exception){
        JSONArray()
    }
}