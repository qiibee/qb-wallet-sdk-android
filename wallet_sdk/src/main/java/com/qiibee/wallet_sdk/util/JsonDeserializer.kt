package com.qiibee.wallet_sdk.util

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.qiibee.wallet_sdk.client.*
import java.math.BigDecimal

internal object JsonDeserializer {
    fun deserializeTokenBalances(content: String): TokenBalances {
        val balancesHelper = Gson().fromJson(content, TokenBalancesDeserializerHelper::class.java)
        val privateTokens = balancesHelper.balances.formatPrivateTokens()
        val ethBalance = balancesHelper.balances.formatETHBalance()
        val publicTokens = balancesHelper.balances.formatPublicTokens()

        return TokenBalances(
            balancesHelper.transactionCount,
            Balances(privateTokens, publicTokens, ethBalance),
            balancesHelper.aggregateValue
        )
    }
}

internal data class BalancesDeserializerHelper(
    @SerializedName("private") val privateTokens: Any,
    @SerializedName("public") val publicTokens: Any
) {
    fun formatPrivateTokens(): List<Token> {
        val stringify = privateTokens.toString()
        val clean = stringify.substring(1, stringify.length - 1)
        val split = clean.split(",")

        val tokens = mutableListOf<Token>()
        for (i in 0 until split.size step 2) {
            tokens.add((createToken(split[i], split[i+1])))
        }

        return tokens
    }

    fun formatPublicTokens(): List<Token> {
        val stringify = publicTokens.toString()
        val clean = stringify.substring(1, stringify.length - 1)
        val split = clean.split(",").filter { p -> !p.contains("ETH") }

        val tokens = mutableListOf<Token>()
        for (i in 0 until split.size step 2) {
            tokens.add((createToken(split[i], split[i+1])))
        }
        return tokens
    }

    fun formatETHBalance() : ETHBalance {
        val stringify = publicTokens.toString()
        val clean = stringify.substring(1, stringify.length - 1)
        val ethString = clean.split(",").find { p -> p.contains("ETH") }
        val ethValue = ethString?.split("=")!![2]

        return ETHBalance(BigDecimal(ethValue.substring(0, ethValue.length - 1)))
    }

    fun createToken(nameBalance: String, contractAddressValue: String): Token {
        val nameBalanceSplit = nameBalance.split("={")
        val name = nameBalanceSplit[0].trim()
        val balance = nameBalanceSplit[1].split("=")[1]
        val address = contractAddressValue.substring(0, contractAddressValue.length-1).split("=")[1]

        return Token(TokenName(name), BigDecimal(balance), WalletAddress(address))
    }
}

internal data class TokenBalancesDeserializerHelper(
    val transactionCount: Int,
    val balances: BalancesDeserializerHelper,
    val aggregateValue: AggregateValue
)