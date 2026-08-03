package org.connecttag.lib.kotlin.core.network.adapter

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A custom [Call] implementation that wraps the response into a [Result].
 */
internal class NetworkResponseCall<S : Any>(
    private val delegate: Call<S>
) : Call<Result<S>> {

    override fun enqueue(callback: Callback<Result<S>>) {
        delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val errorBody = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(Result.success(body))
                        )
                    } else {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(Result.failure(Exception("Response body is null")))
                        )
                    }
                } else {
                    val message = errorBody?.string() ?: "Unknown error"
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(Result.failure(Exception("HTTP $code: $message")))
                    )
                }
            }

            override fun onFailure(call: Call<S>, t: Throwable) {
                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(Result.failure(t))
                )
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted
    override fun clone() = NetworkResponseCall(delegate.clone())
    override fun isCanceled() = delegate.isCanceled
    override fun cancel() = delegate.cancel()
    override fun execute(): Response<Result<S>> = throw UnsupportedOperationException("ResultCall doesn't support execute")
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}
