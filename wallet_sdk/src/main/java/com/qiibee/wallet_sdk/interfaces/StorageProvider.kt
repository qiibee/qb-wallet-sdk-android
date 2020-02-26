package com.qiibee.wallet_sdk.interfaces

import android.content.Context
import com.qiibee.wallet_sdk.client.Mnemonic
import com.qiibee.wallet_sdk.client.PrivateKey
import com.qiibee.wallet_sdk.client.Address
import com.qiibee.wallet_sdk.util.Result
import org.web3j.crypto.Credentials

internal interface StorageProvider {
    // GETTERS
    fun walletAddress(context: Context): Result<Address, Exception>

    fun privateKey(context: Context): Result<PrivateKey, Exception>

    fun mnemonicPhrase(context: Context): Result<Mnemonic, Exception>

    // SETTERS
    fun storeWalletDetails(context: Context, credentials: Credentials, mnemonic: Mnemonic)
            :Result<Unit, Exception>

    fun removeWallet(context: Context): Result<Unit, Exception>
}