package com.qiibee.wallet_sdk.client

import com.qiibee.wallet_sdk.util.Assertion
import com.qiibee.wallet_sdk.util.InvalidMnemonicPhrase

data class Mnemonic(val phrase: String) {
    init {
        if (!Assertion.isValidMnemonic(phrase)) {
            throw InvalidMnemonicPhrase(phrase)
        }
    }
}