package com.qiibee.wallet_sdk.interfaces

import android.content.Context
import com.qiibee.wallet_sdk.client.*
import com.qiibee.wallet_sdk.util.Result
import org.web3j.crypto.Hash
import java.math.BigDecimal

interface SDKProvider {
    // Storage related
    fun walletAddress(context: Context): Result<WalletAddress, Exception>

    fun privateKey(context: Context): Result<PrivateKey, Exception>

    fun mnemonicPhrase(context: Context): Result<Mnemonic, Exception>

    fun walletExists(context: Context): Boolean

    // Wallet Related
    fun createWallet(context: Context): Result<WalletAddress, Exception>

    fun restoreWallet(context: Context, mnemonic: Mnemonic): Result<WalletAddress, Exception>

    fun removeWallet(context: Context)

    // Backend API Related
    fun getBalances(
        context: Context,
        responseHandler: (result: Result<TokenBalances, Exception>) -> Unit
    )

    fun getTokens(
        context: Context,
        responseHandler: (result: Result<Tokens, Exception>) -> Unit
    )

    fun getTransactions(
        context: Context,
        responseHandler: (result: Result<List<Transaction>, Exception>) -> Unit
    )

    fun transferTokens(
        context: Context,
        toAddress: WalletAddress,
        contractAddress: WalletAddress,
        sendTokenValue: BigDecimal,
        responseHandler: (result: Result<Hash, Exception>) -> Unit
    )
}

