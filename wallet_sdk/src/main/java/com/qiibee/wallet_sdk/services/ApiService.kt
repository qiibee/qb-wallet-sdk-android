package com.qiibee.wallet_sdk.services

import com.qiibee.wallet_sdk.client.WalletAddress
import com.qiibee.wallet_sdk.interfaces.HttpClient

internal object ApiService: HttpClient {
    override fun redeem(callback: () -> Unit) {
        TODO("not implemented")
    }

    override fun transfer(callback: () -> Unit) {
        TODO("not implemented")
    }

    override fun getBalance(walletAddress: WalletAddress): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendTokens(walletAddress: WalletAddress): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTransactions(walletAddress: WalletAddress): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}