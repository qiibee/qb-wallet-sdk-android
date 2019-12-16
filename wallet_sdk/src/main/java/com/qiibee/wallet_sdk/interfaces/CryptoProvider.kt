package com.qiibee.wallet_sdk.interfaces

import com.qiibee.wallet_sdk.client.Mnemonic
import com.qiibee.wallet_sdk.client.PrivateKey
import com.qiibee.wallet_sdk.util.Result
import org.web3j.crypto.Credentials

interface CryptoProvider {
    fun createMnemonic(): Result<Mnemonic, Exception>
    fun createWallet(mnemonic: Mnemonic): Result<Credentials, Exception>
    fun deriveCredentials(privateKey: PrivateKey): Credentials
}