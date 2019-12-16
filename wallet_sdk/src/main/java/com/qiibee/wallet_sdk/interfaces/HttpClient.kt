package com.qiibee.wallet_sdk.interfaces

import com.qiibee.wallet_sdk.client.*
import com.qiibee.wallet_sdk.util.Result
import org.web3j.crypto.Credentials
import org.web3j.crypto.Hash
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

    fun sendTransaction(
        fromAddress: WalletAddress,
        credentials: Credentials,
        toAddress: WalletAddress,
        contractAddress: WalletAddress,
        sendTokenValue: BigDecimal,
        responseHandler: (result: Result<Hash, Exception>) -> Unit
    )
}