package com.bignerdranch.android.employees.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.employees.utils.api.EmployeesApi
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

private const val BASE_URL = "https://stoplight.io/mocks/kode-education/trainee-test/25143926/"
private const val TAG = "DataFetcher"

class LocalDateTypeAdapter : TypeAdapter<LocalDate>() {// Этот адаптер нужен для ковертации в тип LocalDate

    override fun write(out: JsonWriter, value: LocalDate) {
        out.value(DateTimeFormatter.ISO_LOCAL_DATE.format(value))
    }

    override fun read(input: JsonReader): LocalDate = LocalDate.parse(input.nextString())
}

class DataFetcher {
    private val employeesApi: EmployeesApi

    init {
//        val httpClient = OkHttpClient.Builder() // использую только для тестов
//        httpClient.addInterceptor(object : Interceptor{
//            override fun intercept(chain: Interceptor.Chain):okhttp3.Response {
//                val request: Request = chain.request().newBuilder().addHeader("Prefer", "code=500").build()//dynamic=true
//                return chain.proceed(request)
//            }
//        })
        // ЭТОТ КОД БУДЕТ КОВРЕТИРОВАДЬ ДЫТЫ В ТИП java.util.Date
       // val gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()//1991-08-13 = "yyyy-MM-dd"   2013-07-16T22:52:36Z = "yyyy-MM-dd'T'HH:mm:ss"    "07/14/2011 00:00" = "MM/dd/yyyy hh:mm"
        val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter().nullSafe()).create() // Подключаем конвертр string to LocalDate
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            //.addConverterFactory(GsonConverterFactory.create())
            //.client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        employeesApi = retrofit.create(EmployeesApi::class.java)
    }

    fun fetchEmployees(): LiveData<List<Employee>>{
        val responseLiveData: MutableLiveData<List<Employee>> = MutableLiveData()
        employeesApi.getEmployees().enqueue(object: Callback<EmployeesModel>{
            override fun onResponse(call: Call<EmployeesModel>, response: Response<EmployeesModel>) {
                if (response.isSuccessful){
                    val employeesModel = response.body()
                    employeesModel?.items?.let {// проходим по всем элементам и меняем URL т.к. срок действия домена (где хранились изображения) истек
                        for (i in it.indices){
                            it[i].avatarUrl = "https://i.pravatar.cc/200?img=" + i
                        }
                    }
                    responseLiveData.value = employeesModel?.items ?: listOf()
                }else{
                    responseLiveData.value = listOf()
                    Log.e(TAG, "ERROR! Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<EmployeesModel>, t: Throwable) {
                responseLiveData.value = listOf()
                Log.e(TAG, "Failure! Throwable message: ${t.message}")
            }
        })
        return responseLiveData
    }
}