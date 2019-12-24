package com.qiibee.wallet_sdk.interfaces

import com.qiibee.wallet_sdk.client.*
import com.qiibee.wallet_sdk.util.Result
import org.web3j.crypto.Hash
import org.web3j.crypto.RawTransaction
import java.math.BigDecimal

internal interface HttpClient {
    fun getBalances(
        walletAddress: WalletAddress,
        responseHandler: (result: Result<TokenBalances, Exception>) -> Unit
    )

    fun getTokens(
        walletAddress: WalletAddress,
        responseHandler: (result: Result<Tokens, Exception>) -> Unit
    )

    fun getTransactions(
        walletAddress: WalletAddress,
        responseHandler: (result: Result<List<Transaction>, Exception>) -> Unit
    )

    fun sendSignedTransaction(
        signedTx: ByteArray,
        responseHandler: (result: Result<Hash, Exception>) -> Unit
    )

    fun getRawTransaction(
        fromAddress: WalletAddress,
        toAddress: WalletAddress,
        contractAddress: WalletAddress,
        sendTokenValue: BigDecimal,
        responseHandler: (result: Result<RawTransaction, Exception>) -> Unit
    )
}