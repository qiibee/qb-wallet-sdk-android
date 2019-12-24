package com.qiibee.wallet_sdk.interfaces

import android.content.Context
import com.qiibee.wallet_sdk.client.*
import com.qiibee.wallet_sdk.util.Result
import org.web3j.crypto.Hash
import java.math.BigDecimal

internal interface SDKProvider {
    // Construction related
    fun initialize(context: Context)

    // Storage related
    fun walletAddress(): Result<WalletAddress, Exception>

    fun privateKey(): Result<PrivateKey, Exception>

    fun mnemonicPhrase(): Result<Mnemonic, Exception>

    // Wallet Related
    fun createWallet(): Result<WalletAddress, Exception>

    fun restoreWallet(mnemonic: Mnemonic): Result<WalletAddress, Exception>

    fun removeWallet(): Result<Unit, Exception>

    // Backend API Related
    fun getBalances(
        responseHandler: (result: Result<TokenBalances, Exception>) -> Unit
    )

    fun getTokens(
        responseHandler: (result: Result<Tokens, Exception>) -> Unit
    )

    fun getTransactions(
        responseHandler: (result: Result<List<Transaction>, Exception>) -> Unit
    )

    fun transferTokens(
        toAddress: WalletAddress,
        contractAddress: WalletAddress,
        sendTokenValue: BigDecimal,
        responseHandler: (result: Result<Hash, Exception>) -> Unit
    )
}

