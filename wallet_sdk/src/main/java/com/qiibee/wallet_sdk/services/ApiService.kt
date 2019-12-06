package com.qiibee.wallet_sdk.services

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.qiibee.wallet_sdk.client.Token
import com.qiibee.wallet_sdk.client.TokenBalances
import com.qiibee.wallet_sdk.client.Transaction
import com.qiibee.wallet_sdk.client.WalletAddress
import com.qiibee.wallet_sdk.interfaces.HttpClient
import com.qiibee.wallet_sdk.util.*
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.SignedRawTransaction
import java.lang.Exception
import java.math.BigDecimal

internal object ApiService: HttpClient {
    private val HTTPS = "https://"
    private val QB_API = "${HTTPS}api.qiibee.com"
    private val QB_APP_API = "${QB_API}/app"

    override fun getBalances(
        walletAddress: WalletAddress,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<TokenBalances, Exception>) -> Unit
    ) {
        Fuel.get( "$QB_APP_API/addresses/${walletAddress.address}?public=true")
            .responseObject(TokenBalances.Deserializer()) { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val (_, error) = result
                        responseHandler.invoke(Failure(GetTokenBalancesFailed("${error?.message}")))
                    }
                    is Result.Success -> {
                        val (data, _) = result
                        responseHandler.invoke(Success(data as TokenBalances))
                    }
                }
            }
    }

    override fun getRawTransaction(
        address: WalletAddress,
        toAddress: WalletAddress,
        contractAddress: WalletAddress,
        sendTokenValue: BigDecimal,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<RawTransaction, Exception>) -> Unit
    ) {
        TODO("not implemented")
    }

    override fun getTokens(
        walletAddress: WalletAddress,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<List<Token>, Exception>) -> Unit
    ) {
        TODO("not implemented")
    }

    override fun getTransactions(
        walletAddress: WalletAddress,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<List<Transaction>, Exception>) -> Unit
    ) {
        TODO("not implemented")
    }

    override fun sendSignedTransaction(
        signedTx: SignedRawTransaction,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<SignedRawTransaction, Exception>) -> Unit
    ) {
        TODO("not implemented")
    }
}