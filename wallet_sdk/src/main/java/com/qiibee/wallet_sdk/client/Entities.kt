package com.qiibee.wallet_sdk.client

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.qiibee.wallet_sdk.util.Assertion
import com.qiibee.wallet_sdk.util.InvalidTokenName
import com.qiibee.wallet_sdk.util.JsonDeserializer
import java.math.BigDecimal
import java.sql.Timestamp

data class TokenBalances(
    val transactionCount: Int,
    val balances: Balances,
    val aggValue: AggregateValue
) {
    class Deserializer: ResponseDeserializable<TokenBalances> {
        override fun deserialize(content: String): TokenBalances {
            return JsonDeserializer.deserializeTokenBalances(content)
        }
    }
}

data class AggregateValue(val USD: BigDecimal)
data class ETHBalance(val balance: BigDecimal)

data class Token(
    val name: TokenName,
    val balance: BigDecimal,
    val contractAddress: WalletAddress
)

data class Balances(
    val privateTokens: List<Token>,
    val publicTokens: List<Token>,
    val ethBalance: ETHBalance
)

data class TokenName(val name: String) {
    init {
        if (!Assertion.isValidTokenName(name)) {
            throw InvalidTokenName(name)
        }
    }
}

data class Transaction(
    val to: WalletAddress,
    val from: WalletAddress,
    val timestamp: Timestamp,
    val contractAddress: WalletAddress
) {
    class Deserializer: ResponseDeserializable<Transaction> {
        override fun deserialize(content: String): Transaction {
            return Gson().fromJson(content, Transaction::class.java)
        }
    }
}




