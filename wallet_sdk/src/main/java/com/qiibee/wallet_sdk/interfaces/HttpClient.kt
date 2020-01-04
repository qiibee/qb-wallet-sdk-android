package com.qiibee.wallet_sdk.interfaces

import com.qiibee.wallet_sdk.client.*
import com.qiibee.wallet_sdk.util.Result
import org.web3j.crypto.RawTransaction
import java.math.BigDecimal

internal interface HttpClient {
    fun getBalances(
        address: Address,
        responseHandler: (result: Result<TokenBalances, Exception>) -> Unit
    )

    fun getTokens(
        address: Address,
        responseHandler: (result: Result<Tokens, Exception>) -> Unit
    )

    fun getTransactions(
        address: Address,
        responseHandler: (result: Result<List<Transaction>, Exception>) -> Unit
    )

    fun sendSignedTransaction(
        signedTx: String,
        responseHandler: (result: Result<Hash, Exception>) -> Unit
    )

    fun getRawTransaction(
        fromAddress: Address,
        toAddress: Address,
        contractAddress: Address,
        sendTokenValue: BigDecimal,
        responseHandler: (result: Result<RawTransaction, Exception>) -> Unit
    )
}