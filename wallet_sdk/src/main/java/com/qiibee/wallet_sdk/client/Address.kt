package com.qiibee.wallet_sdk.client

import com.qiibee.wallet_sdk.util.Assertion
import com.qiibee.wallet_sdk.util.InvalidWalletAddress

data class Address(val address: String) {
    init {
        if (!Assertion.isValidAddress(address)) {
            throw InvalidWalletAddress(address)
        }
    }
}