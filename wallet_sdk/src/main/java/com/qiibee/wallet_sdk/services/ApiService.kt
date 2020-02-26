package com.qiibee.wallet_sdk.services

import android.net.Uri
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.qiibee.wallet_sdk.client.*
import com.qiibee.wallet_sdk.interfaces.HttpClient
import com.qiibee.wallet_sdk.util.*
import org.json.JSONObject
import org.web3j.crypto.RawTransaction
import java.math.BigDecimal

internal object ApiService: HttpClient {
    private const val QB_API = "https://api.qiibee.com"
    private const val QB_APP_API = "${QB_API}/app"

    override fun getBalances(
        address: Address,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<TokenBalances, Exception>) -> Unit
    ) {

        val uri = Uri.parse("$QB_APP_API/addresses/${address.address}")
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
        address: Address,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<Tokens, Exception>) -> Unit
    ) {
        val uri = Uri.parse("$QB_API/tokens")
            .buildUpon()
            .appendQueryParameter("public", "true")
            .appendQueryParameter("walletAddress", address.address)
            .toString()

        Fuel.get(uri)
            .responseObject(JsonDeserializer.TokensDeserializer()) { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val (_, error) = result
                        responseHandler.invoke(Failure(GetTokensFailed("${error?.message}")))
                    }
                    is Result.Success -> {
                        val (data, _) = result
                        responseHandler.invoke(Success(data as Tokens))
                    }
                }
            }
    }

    override fun getTransactions(
        address: Address,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<List<Transaction>, Exception>) -> Unit
    ) {
        Fuel.get("$QB_API/transactions/${address.address}/history")
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
        fromAddress: Address,
        toAddress: Address,
        contractAddress: Address,
        sendTokenValue: BigDecimal,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<RawTransaction, Exception>) -> Unit
    ) {
        val weiValue = CryptoUtils.etherToWeiString(sendTokenValue)
        val uri = Uri.parse("$QB_API/transactions/raw")
            .buildUpon()
            .appendQueryParameter("from", fromAddress.address)
            .appendQueryParameter("to", toAddress.address)
            .appendQueryParameter("contractAddress", contractAddress.address)
            .appendQueryParameter("transferAmount", weiValue)
            .toString()

        Fuel.get(uri)
            .responseObject(JsonDeserializer.RawTxDeserializer()) { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val (_, error) = result
                        responseHandler.invoke(Failure(GetRawTransactionFailed("${error?.message}")))
                    }
                    is Result.Success -> {
                        val (data, _) = result
                        responseHandler.invoke(Success(data as RawTransaction))
                    }
                }
            }
    }

    override fun sendSignedTransaction(
        signedTx: String,
        responseHandler: (result: com.qiibee.wallet_sdk.util.Result<Hash, Exception>) -> Unit
    ) {
        val uri = Uri.parse("$QB_API/transactions/")
            .buildUpon()
            .toString()

        Fuel.post(uri)
            .header("Content-Type", "application/json")
            .body(formatToBodyType(signedTx))
            .responseObject(JsonDeserializer.SendTransactionDeserializer()) { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val (_, error) = result
                        responseHandler.invoke(Failure(SendTransactionFailed("${error?.message}")))
                    }
                    is Result.Success -> {
                        val (data, _) = result
                        responseHandler.invoke(Success(data as Hash))
                    }
                }
            }
    }

    private fun formatToBodyType(signedTx: String): String {
        val jsonObject = JSONObject()
        return jsonObject.put("data", signedTx).toString()
    }
}