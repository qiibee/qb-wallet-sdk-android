package com.qiibee.wallet_sdk.util

import org.web3j.crypto.*
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigDecimal

internal object CryptoUtils {
    fun signTx(rawTx: RawTransaction, credentials: Credentials): String {
        return Numeric.toHexString(TransactionEncoder.signMessage(rawTx, credentials))
    }

    fun etherToWeiString(value: BigDecimal): String {
        return Convert.toWei(value, Convert.Unit.ETHER).toBigInteger().toString()
    }
}