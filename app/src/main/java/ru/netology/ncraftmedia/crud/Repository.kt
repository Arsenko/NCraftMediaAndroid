package ru.netology.ncraftmedia.crud

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.netology.ncraftmedia.crud.api.API
import ru.netology.ncraftmedia.crud.api.AuthRequestParams
import ru.netology.ncraftmedia.crud.api.RegistrationRequestParams
import ru.netology.ncraftmedia.crud.api.interceptor.InjectAuthTokenInterceptor
import ru.netology.ncraftmedia.crud.dto.CounterChange
import ru.netology.ncraftmedia.crud.dto.CounterType
import ru.netology.ncraftmedia.crud.dto.Post


object Repository {

    private var retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl("https://srv-ncms.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun createRetrofitWithAuth(authToken: String) {
        val httpLoggerInterceptor = HttpLoggingInterceptor()
        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor(authToken))
            .addInterceptor(httpLoggerInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://srv-ncms.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        API = retrofit.create(ru.netology.ncraftmedia.crud.api.API::class.java)
    }

    private var API: API =
        retrofit.create(ru.netology.ncraftmedia.crud.api.API::class.java)


    suspend fun authenticate(login: String, password: String) =
        API.authenticate(
            AuthRequestParams(login, password)
        )

    suspend fun register(login: String, password: String) =
        API.register(
            RegistrationRequestParams(
                login,
                password
            )
        )

    suspend fun createPost(post: Post) = API.createPost(post)

    suspend fun getPosts() = API.getPosts()

    suspend fun likeChange(item: Post){
        if (item.likedByMe) {
            item.likedByMe=false
            API.changeCounter(
                item.id?.let { CounterChange(it, item.likeCounter--, CounterType.Like) }!!
            )
        } else {
            item.likedByMe=true
            API.changeCounter(
                item.id?.let { CounterChange(it, item.likeCounter++, CounterType.Like) }!!
            )
        }
    }

    suspend fun shareChange(item: Post) {
        API.changeCounter(
            item.id?.let{ CounterChange(it, item.shareCounter++,CounterType.Share) }!!
        )
    }
}
