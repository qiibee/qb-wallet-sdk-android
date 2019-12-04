package com.qiibee.wallet_sdk.interfaces

import com.qiibee.wallet_sdk.client.Mnemonic
import com.qiibee.wallet_sdk.client.PrivateKey
import com.qiibee.wallet_sdk.client.WalletAddress
import com.qiibee.wallet_sdk.util.Result
import java.lang.Exception

interface WalletProvider {
    fun publicAddress(): Result<WalletAddress, Exception>

    fun privateKey(): Result<PrivateKey, Exception>

    fun mnemonicPhrase(): Result<Mnemonic, Exception>

    fun walletExists(): Boolean

}