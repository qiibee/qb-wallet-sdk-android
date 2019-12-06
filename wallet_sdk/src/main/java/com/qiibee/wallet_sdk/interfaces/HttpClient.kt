package com.qiibee.wallet_sdk.interfaces

import com.qiibee.wallet_sdk.client.Token
import com.qiibee.wallet_sdk.client.TokenBalances
import com.qiibee.wallet_sdk.client.Transaction
import com.qiibee.wallet_sdk.client.WalletAddress
import com.qiibee.wallet_sdk.util.Result
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.SignedRawTransaction
import java.lang.Exception
import java.math.BigDecimal

interface HttpClient {
    fun getBalances(
        walletAddress: WalletAddress,
        responseHandler: (result: Result<TokenBalances, Exception>) -> Unit
    )

    fun getTokens(
        walletAddress: WalletAddress,
        responseHandler: (result: Result<List<Token>, Exception>) -> Unit
    )

    fun getTransactions(
        walletAddress: WalletAddress,
        responseHandler: (result: Result<List<Transaction>, Exception>) -> Unit
    )

    fun getRawTransaction(
        address: WalletAddress,
        toAddress: WalletAddress,
        contractAddress: WalletAddress,
        sendTokenValue: BigDecimal,
        responseHandler: (result: Result<RawTransaction, Exception>) -> Unit
    )

    fun sendSignedTransaction(
        signedTx: SignedRawTransaction,
        responseHandler: (result: Result<SignedRawTransaction, Exception>) -> Unit
    )
}