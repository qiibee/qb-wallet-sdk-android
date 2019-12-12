package com.qiibee.wallet_sdk.interfaces

import com.qiibee.wallet_sdk.client.Mnemonic
import com.qiibee.wallet_sdk.util.Result

interface CryptoProvider {
    fun createMnemonic(): Result<Mnemonic, Exception>

    // TODO add return type here
    fun createWallet()
}