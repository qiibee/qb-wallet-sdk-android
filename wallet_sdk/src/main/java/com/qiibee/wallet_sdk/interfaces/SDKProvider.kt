package com.qiibee.wallet_sdk.interfaces

import com.qiibee.wallet_sdk.client.PublicAddress
import com.qiibee.wallet_sdk.util.Result
import java.lang.Exception

interface SDKProvider {
    fun loadWallet(): Result<PublicAddress, Exception>

    fun walletExists(): Boolean
}

