package com.qiibee.wallet_sdk.client

import com.qiibee.wallet_sdk.util.Assertion
import com.qiibee.wallet_sdk.util.InvalidPrivateKey

class PrivateKey(val privateKey: String) {
    init {
        if (!Assertion.isValidPrivateKey(privateKey)) {
            throw InvalidPrivateKey("Private key not valid!")
        }
    }
}
