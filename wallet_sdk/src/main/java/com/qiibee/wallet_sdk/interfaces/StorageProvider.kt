package com.qiibee.wallet_sdk.interfaces

import android.content.Context
import com.qiibee.wallet_sdk.client.Mnemonic
import com.qiibee.wallet_sdk.client.PrivateKey
import com.qiibee.wallet_sdk.client.WalletAddress
import com.qiibee.wallet_sdk.util.Result
import org.web3j.crypto.Credentials

interface StorageProvider {
    // GETTERS
    fun walletAddress(context: Context): Result<WalletAddress, Exception>

    fun privateKey(context: Context): Result<PrivateKey, Exception>

    fun mnemonicPhrase(context: Context): Result<Mnemonic, Exception>

    fun walletExists(context: Context): Boolean

    // SETTERS
    fun storeWalletDetails(context: Context, credentials: Credentials, mnemonic: Mnemonic)

    fun removeWallet(context: Context)
}