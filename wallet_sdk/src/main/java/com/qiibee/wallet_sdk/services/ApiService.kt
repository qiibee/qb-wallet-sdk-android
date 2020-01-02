package com.qiibee.wallet_sdk.services

import android.net.Uri
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.qiibee.wallet_sdk.client.*
import com.qiibee.wallet_sdk.interfaces.HttpClient
import com.qiibee.wallet_sdk.util.*
import org.json.JSONObject
import org.web3j.crypto.Hash
import org.web3j.crypto.RawTransaction
import java.math.BigDecimal

internal object ApiService: HttpClient {
    private const val QB_API = "https://api.qiibee.com"
    private const val QB_APP_API = "${QB_API}/app"

    override fun getBalances(
        walletAddress: WalletAddress,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<TokenBalances, Exception>) -> Unit
    ) {

        val uri = Uri.parse("$QB_APP_API/addresses/${walletAddress.address}")
            .buildUpon()
            .appendQueryParameter("public", "true")
            .toString()

        Fuel.get(uri)
            .responseObject(JsonDeserializer.TokenBalancesDeserializer()) { _, _, result ->
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

    override fun getTokens(
        walletAddress: WalletAddress,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<Tokens, Exception>) -> Unit
    ) {
        val uri = Uri.parse("$QB_API/tokens")
            .buildUpon()
            .appendQueryParameter("public", "true")
            .appendQueryParameter("walletAddress", walletAddress.address)
            .toString()

        Fuel.get(uri)
            .responseObject(JsonDeserializer.TokensDeserializer()) { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val (_, error) = result
                        responseHandler.invoke(Failure(GetTokenBalancesFailed("${error?.message}")))
                    }
                    is Result.Success -> {
                        val (data, _) = result
                        responseHandler.invoke(Success(data as Tokens))
                    }
                }
            }
    }

    override fun getTransactions(
        walletAddress: WalletAddress,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<List<Transaction>, Exception>) -> Unit
    ) {
        Fuel.get("$QB_API/transactions/${walletAddress.address}/history")
            .responseObject(JsonDeserializer.TransactionsDeserializer()) { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val (_, error) = result
                        responseHandler.invoke(Failure(GetTransactionsFailed("${error?.message}")))
                    }
                    is Result.Success -> {
                        val (data, _) = result
                        responseHandler.invoke(Success(data as List<Transaction>))
                    }
                }
            }
    }

    override fun getRawTransaction(
        fromAddress: WalletAddress,
        toAddress: WalletAddress,
        contractAddress: WalletAddress,
        sendTokenValue: BigDecimal,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<RawTransaction, Exception>) -> Unit
    ) {
        val uri = Uri.parse("$QB_API/transactions/raw")
            .buildUpon()
            .appendQueryParameter("from", fromAddress.address)
            .appendQueryParameter("to", toAddress.address)
            .appendQueryParameter("contractAddress", contractAddress.address)
            .appendQueryParameter("transferAmount", sendTokenValue.toString())
            .toString()

        Fuel.get(uri)
            .responseObject(JsonDeserializer.RawTxDeserializer()) { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val (_, error) = result
                        responseHandler.invoke(Failure(GetTokenBalancesFailed("${error?.message}")))
                    }
                    is Result.Success -> {
                        val (data, _) = result
                        responseHandler.invoke(Success(data as RawTransaction))
                    }
                }
            }
    }

    override fun sendSignedTransaction(
        signedTx: ByteArray,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<Hash, Exception>) -> Unit
    ) {
        Fuel.post( "$QB_API/transactions/").body(stringifySignedTx(signedTx))
            .responseObject(JsonDeserializer.SendTransactionDeserializer()) { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val (_, error) = result
                        responseHandler.invoke(Failure(GetTransactionsFailed("${error?.message}")))
                    }
                    is Result.Success -> {
                        val (data, _) = result
                        responseHandler.invoke(Success(data as Hash))
                    }
                }
            }
    }

    private fun stringifySignedTx(signedTx: ByteArray): String {
        val jsonObject = JSONObject()
        return jsonObject.put("data", signedTx.toString()).toString()
    }
}