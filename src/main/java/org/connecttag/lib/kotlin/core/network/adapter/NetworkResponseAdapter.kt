package org.connecttag.lib.kotlin.core.network.adapter

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * A [CallAdapter] that converts [Call] into [Result].
 */
internal class NetworkResponseAdapter<S : Any>(
    private val successType: Type
) : CallAdapter<S, Call<Result<S>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<Result<S>> {
        return NetworkResponseCall(call)
    }
}
