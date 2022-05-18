package com.bignerdranch.android.employees.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.employees.utils.api.EmployeesApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://stoplight.io/mocks/kode-education/trainee-test/25143926/"
private const val TAG = "DataFetcher"

class DataFetcher {
    private val employeesApi: EmployeesApi

    init {
//        val httpClient = OkHttpClient.Builder()
//        httpClient.addInterceptor(object : Interceptor{
//            override fun intercept(chain: Interceptor.Chain):okhttp3.Response {
//                val request: Request = chain.request().newBuilder().addHeader("key", "value").build()
//                return chain.proceed(request)
//            }
//        })
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            //.client(httpClient.build())
            .build()
        employeesApi = retrofit.create(EmployeesApi::class.java)
    }

    fun fetchEmployees(): LiveData<List<Employee>>{
        val responseLiveData: MutableLiveData<List<Employee>> = MutableLiveData()
        employeesApi.getEmployees().enqueue(object: Callback<EmployeesModel>{
            override fun onResponse(call: Call<EmployeesModel>, response: Response<EmployeesModel>) {
                if (response.isSuccessful){
                    val employeesModel = response.body()
                    responseLiveData.value = employeesModel?.items ?: listOf()
                    Log.d(TAG, "onResponse isSuccessful DATA:")
                    for (empl in employeesModel?.items!!){
                        Log.d(TAG, empl.toString())
                    }
                }else{
                    Log.e(TAG, "ERROR! Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<EmployeesModel>, t: Throwable) {
                Log.e(TAG, "Failure! Throwable message: ${t.message}")
            }
        })
        return responseLiveData
    }
}