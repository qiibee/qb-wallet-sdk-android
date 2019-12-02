package com.qiibee.wallet_sdk.client

import com.qiibee.wallet_sdk.util.Assertion
import com.qiibee.wallet_sdk.util.InvalidMnemonicPhrase

class Mnemonic(phrase: String) {
    init {
        if (!Assertion.isValidMnemonic(phrase)) {
            throw InvalidMnemonicPhrase("Mnemonic phrase not valid")
        }
    }
}