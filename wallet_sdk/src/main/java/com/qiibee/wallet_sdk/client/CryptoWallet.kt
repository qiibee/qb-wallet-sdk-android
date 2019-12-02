package com.qiibee.wallet_sdk.client

import com.qiibee.wallet_sdk.interfaces.CryptoProvider
import com.qiibee.wallet_sdk.interfaces.HttpClient
import com.qiibee.wallet_sdk.interfaces.SDKProvider
import com.qiibee.wallet_sdk.interfaces.WalletProvider
import com.qiibee.wallet_sdk.services.ApiService
import com.qiibee.wallet_sdk.services.CryptoService
import com.qiibee.wallet_sdk.services.WalletSimple
import com.qiibee.wallet_sdk.util.Result

object CryptoWallet: SDKProvider {
    private val apiService: HttpClient = ApiService
    private val walletStorage: WalletProvider = WalletSimple
    private val cryptoService: CryptoProvider = CryptoService

    override fun loadWallet(): Result<PublicAddress, Exception> {
        return walletStorage.publicAddress()
    }

    override fun walletExists(): Boolean {
        return walletStorage.walletExists()
    }
}