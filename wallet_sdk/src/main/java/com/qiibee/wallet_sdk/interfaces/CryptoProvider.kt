package com.qiibee.wallet_sdk.interfaces

import com.qiibee.wallet_sdk.client.Mnemonic

interface CryptoProvider {
    fun createMnemonic(): Mnemonic
    // TODO add return type here
    fun createWallet()
}