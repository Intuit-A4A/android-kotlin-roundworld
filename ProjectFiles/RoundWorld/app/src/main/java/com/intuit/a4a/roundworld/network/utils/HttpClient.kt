package com.intuit.a4a.roundworld.network.utils

import okhttp3.OkHttpClient

object HttpClient {
    /**
     * Note: This is part of the network layer infrastructure, and not required to be changed
     * in user stories as part of the craft A4A
     *
     * Single shared instance of OkHttpClient
     * Each specific Rest/GraphQL service interface will customize this shared okHttpClient through
     * newBuilder() call.
     * https://square.github.io/okhttp/3.x/okhttp/okhttp3/OkHttpClient.html
     */
    internal val okHttpClient by lazy { OkHttpClient() }
}