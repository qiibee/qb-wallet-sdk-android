package com.qiibee.wallet_sdk.util

import org.web3j.crypto.*

internal object CryptoUtils {
    fun signTx(rawTx: RawTransaction, credentials: Credentials): ByteArray {
        return TransactionEncoder.signMessage(rawTx, credentials)
    }
}