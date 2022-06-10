package gini.udihaguel.memorygame.networking

import gini.udihaguel.memorygame.entities.ContentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ContentService {

    @GET("{key}")
    suspend fun getContent(@Path("key") key:String) : Response<ContentResponse>
}