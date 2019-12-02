package com.qiibee.wallet_sdk.interfaces

import com.qiibee.wallet_sdk.client.PublicAddress

interface HttpClient {
    fun redeem(callback: () -> Unit)

    fun transfer(callback: () -> Unit)

    fun getBalance(publicAddress: PublicAddress): String

    fun getTokens(publicAddress: PublicAddress): String

    fun getTransactions(publicAddress: PublicAddress): String
}