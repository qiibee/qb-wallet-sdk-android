package com.qiibee.wallet_sdk.interfaces

import com.qiibee.wallet_sdk.client.WalletAddress

interface HttpClient {
    fun redeem(callback: () -> Unit)

    fun transfer(callback: () -> Unit)

    fun getBalance(walletAddress: WalletAddress): String

    fun sendTokens(walletAddress: WalletAddress): String

    fun getTransactions(walletAddress: WalletAddress): String
}