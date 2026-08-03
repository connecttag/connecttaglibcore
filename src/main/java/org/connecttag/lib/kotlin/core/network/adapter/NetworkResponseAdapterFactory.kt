package org.connecttag.lib.kotlin.core.network.adapter

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * A [CallAdapter.Factory] for [Result] type.
 */
class NetworkResponseAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions return Call
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        // check if the return type is Call<Result<*>>
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<Result<Foo>> or Call<Result<out Foo>>"
        }

        val innerType = getParameterUpperBound(0, returnType)
        if (getRawType(innerType) != Result::class.java) {
            return null
        }

        check(innerType is ParameterizedType) {
            "Response must be parameterized as Result<Foo> or Result<out Foo>"
        }

        val successType = getParameterUpperBound(0, innerType)
        return NetworkResponseAdapter<Any>(successType)
    }
}
