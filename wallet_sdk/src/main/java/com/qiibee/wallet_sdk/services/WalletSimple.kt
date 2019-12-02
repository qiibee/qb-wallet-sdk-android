package com.qiibee.wallet_sdk.services

import com.qiibee.wallet_sdk.interfaces.WalletProvider
import com.qiibee.wallet_sdk.client.PublicAddress
import com.qiibee.wallet_sdk.util.Result
import com.qiibee.wallet_sdk.util.Success
import java.lang.Exception

// This is a mock implementation of Wallet Storage/Provider
object WalletSimple: WalletProvider {
    val mnemonic: String = "switch rule drill tornado pause conduct horse twelve ivory beach hybrid east"
    val address: PublicAddress =
        PublicAddress("0xbBf0458e845E1fd7EEfAd6d5689b13A3E3312510")

    init {
        // TODO("get from storage")
    }
    override fun privateKey(): Result<PublicAddress, Exception> {
        return Success(PublicAddress("0x123124"))
    }

    override fun publicAddress(): Result<PublicAddress, Exception> {
        return Success(address)
    }

    override fun walletExists(): Boolean {
        return true
    }

}