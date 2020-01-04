package com.qiibee.wallet_sdk.client

import java.math.BigDecimal
import java.sql.Timestamp

data class Hash(
    val hash: String
)

data class TokenBalances(
    val transactionCount: Int,
    val balances: Balances,
    val aggValue: AggregateValue
)

data class AggregateValue(val USD: BigDecimal)
data class ETHBalance(val balance: BigDecimal)

data class Tokens (
    val privateTokens: List<Token>,
    val publicTokens: List<Token>
)

data class Token (
    val symbol: String,
    val balance: BigDecimal,
    val contractAddress: Address
)

data class Balances(
    val privateTokens: List<Token>,
    val publicTokens: List<Token>,
    val ethBalance: ETHBalance
)

data class Transaction(
    val to: Address,
    val from: Address,
    val contractAddress: Address,
    val timestamp: Timestamp,
    val token: Token
)



