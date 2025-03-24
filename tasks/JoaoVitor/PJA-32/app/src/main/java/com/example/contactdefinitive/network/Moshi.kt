package com.example.contactdefinitive.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object Moshi {

    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    inline fun <reified T, reified F> getMapFromJson(json: String): Map<T, F>? {

        val type = Types.newParameterizedType(Map::class.java, T::class.java, F::class.java)
        val adapter: JsonAdapter<Map<T, F>> = moshi.adapter(type)

        return adapter.fromJson(json)
    }

}