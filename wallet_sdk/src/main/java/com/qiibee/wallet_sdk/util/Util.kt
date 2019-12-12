package com.qiibee.wallet_sdk.util

import org.web3j.crypto.*
import java.lang.Error

internal object Util {
    fun notImplemented(): Nothing = throw Error("Code not yet implemented")

}

internal object CryptoUtils {
    fun signTx(rawTx: RawTransaction, credentials: Credentials): ByteArray {
        return TransactionEncoder.signMessage(rawTx, credentials)
    }
}