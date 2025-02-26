package com.intuit.a4a.roundworld.network.utils

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.intuit.a4a.roundworld.network.utils.interfaces.ServiceInterface
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * Extend this base graphQlService to implement the country graphQl service layer
 * List of queries available is at https://graphql.country/
 */
abstract class GraphQLService : ServiceInterface {

    protected val apolloClient: ApolloClient by lazy {
        ApolloClient.Builder()
            .serverUrl(getBaseUrl())
            .okHttpClient(
                HttpClient.okHttpClient
                    .newBuilder()
                    .also { okhttpBuilder ->
                        okhttpBuilder
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .addInterceptor(
                            HttpLoggingInterceptor().setLevel(
                                HttpLoggingInterceptor.Level.BASIC
                            )
                        )
                        getInterceptors().forEach { okhttpBuilder.addInterceptor(it) }
                    }
                    .build()
            )
            .build()
    }

    /**
     * Implementation to be provided for base URL of the graphQL call
     */
    abstract fun getBaseUrl(): String

    /**
     * Add additional okhttp interceptors to the GraphQL call interface.
     */
    abstract fun getInterceptors(): List<Interceptor>
}