package com.qiibee.wallet_sdk.util

import org.web3j.crypto.MnemonicUtils
import org.web3j.crypto.WalletUtils

object Assertion {
    fun isValidAddress(address: String) : Boolean {
        return WalletUtils.isValidAddress(address)
    }

    fun isValidMnemonic(phrase: String): Boolean {
        return MnemonicUtils.validateMnemonic(phrase)
    }

    fun isValidPrivateKey(key: String): Boolean {
        return WalletUtils.isValidPrivateKey(key)
    }

    fun isValidTokenSymbol(name: String): Boolean {
        return name.length >= 2 && name.length <= 4
    }
}