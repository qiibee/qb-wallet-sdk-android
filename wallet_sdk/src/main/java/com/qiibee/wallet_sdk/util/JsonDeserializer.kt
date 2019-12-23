package com.qiibee.wallet_sdk.util

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.qiibee.wallet_sdk.client.*
import org.web3j.crypto.Hash
import org.web3j.crypto.RawTransaction
import java.math.BigDecimal
import java.sql.Timestamp

internal object JsonDeserializer {
    class TokenBalancesDeserializer: ResponseDeserializable<TokenBalances> {
        override fun deserialize(content: String): TokenBalances {
            val balancesHelper = Gson().fromJson(content, TokenBalancesDeserializerHelper::class.java)
            val privateTokens = balancesHelper.balances.fromPrivateTokens()
            val ethBalance = balancesHelper.balances.fromETHBalance()
            val publicTokens = balancesHelper.balances.fromPublicTokens()

            return TokenBalances(
                balancesHelper.transactionCount,
                Balances(privateTokens, publicTokens, ethBalance),
                balancesHelper.aggregateValue
            )
        }
    }

    class RawTxDeserializer: ResponseDeserializable<RawTransaction> {
        override fun deserialize(content: String): RawTransaction {
            return Gson().fromJson(content, RawTransaction::class.java)
        }
    }

    class TokensDeserializer: ResponseDeserializable<Tokens> {
        override fun deserialize(content: String): Tokens {
            val tokensHelper = Gson().fromJson(content, TokensDeserializerHelper::class.java)
            val privateTokens = tokensHelper.fromPrivateTokens()
            val publicTokens = tokensHelper.fromPublicTokens()

            return Tokens(privateTokens, publicTokens)
        }
    }

    class TransactionsDeserializer: ResponseDeserializable<List<Transaction>> {
        override fun deserialize(content: String): List<Transaction> {
            val listType = object : TypeToken<List<TransactionIntermediate>>() {}.type
            return Gson().fromJson<List<TransactionIntermediate>>(content, listType).map {
                it.formatTransaction()
            }
        }
    }

    class SendTransactionDeserializer: ResponseDeserializable<Hash> {
        override fun deserialize(content: String): Hash {
            return Gson().fromJson(content, Hash::class.java)
        }
    }

}

private data class TransactionIntermediate(
    val to: String,
    val from: String,
    val hash: String,
    val contractAddress: String,
    val timestamp: Long,
    val token: TokenIntermediate
) {
    fun formatTransaction(): Transaction {
        return Transaction(
            WalletAddress(to),
            WalletAddress(from),
            WalletAddress(contractAddress),
            Timestamp(timestamp),
            token.formatToToken()
        )
    }
}

private data class TokensDeserializerHelper (
    @SerializedName("private") val privateTokens: List<TokenIntermediate>,
    @SerializedName("public") val publicTokens: List<TokenIntermediate>
) {

    fun fromPrivateTokens(): List<Token> {
        val list = mutableListOf<Token>()
        for (token in privateTokens) {
            list.add(token.formatToToken())
        }
        return list
    }

    fun fromPublicTokens(): List<Token> {
        val list = mutableListOf<Token>()
        val filteredTokens = publicTokens.filter { token -> token.symbol != Constants.ETH }
        for (token in filteredTokens) {
            list.add(token.formatToToken())
        }
        return list
    }

}

private data class TokenIntermediate (
    val balance: String?,
    val contractAddress: String,
    val decimals: Number,
    val description: String,
    val logoUrl: String,
    val name: String,
    val symbol: String,
    val totalSupply: String,
    val website: String
) {
    fun formatToToken(): Token {
        return Token(
            symbol,
            BigDecimal(balance?: "0"),
            WalletAddress(contractAddress)
        )
    }
}

private data class TokenBalancesDeserializerHelper(
    val transactionCount: Int,
    val balances: BalancesDeserializerHelper,
    val aggregateValue: AggregateValue
)

private data class BalancesDeserializerHelper(
    @SerializedName("private") val privateTokens: Any,
    @SerializedName("public") val publicTokens: Any
) {

    fun fromPrivateTokens(): List<Token> {
        val map = Gson().fromJson(privateTokens.toString(), Map::class.java)
        val tokens = mutableListOf<Token>()

        for ((k,v) in map) {
            val props = Gson().fromJson(v.toString(), BalanceContractIntermediate::class.java)
            val token = createToken(k.toString(), props.balance, props.contractAddress)
            tokens.add(token)
        }

        return tokens
    }

    fun fromPublicTokens(): List<Token> {
        val map = Gson().fromJson(publicTokens.toString(), Map::class.java)
        val tokens = mutableListOf<Token>()

        for ((k, v) in map) {
            if (k.toString() != Constants.ETH) {
                val props = Gson().fromJson(v.toString(), BalanceContractIntermediate::class.java)
                val token = createToken(k.toString(), props.balance, props.contractAddress)
                tokens.add(token)
            }
        }
        return tokens
    }

    fun fromETHBalance(): ETHBalance {
        val map = Gson().fromJson(publicTokens.toString(), Map::class.java)

        for ((key, value) in map) {
            if (key.toString() == Constants.ETH) {
                val props = Gson().fromJson(value.toString(), BalanceIntermediate::class.java)
                return ETHBalance(BigDecimal(props.balance))
            }
        }

        return ETHBalance(BigDecimal(0))
    }

    private fun createToken(symbol: String, balance: String, address: String): Token {
        return Token(symbol, BigDecimal(balance), WalletAddress(address))
    }

    private data class BalanceContractIntermediate(
        val balance: String,
        val contractAddress: String
    )

    private data class BalanceIntermediate(
        val balance: String
    )
}
