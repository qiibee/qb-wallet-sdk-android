package com.qiibee.wallet_sdk.client

import com.qiibee.wallet_sdk.util.Assertion
import com.qiibee.wallet_sdk.util.InvalidPublicAddress

class PublicAddress(val address: String) {
    init {
        if (!Assertion.isValidAddress(address)) {
            throw InvalidPublicAddress("Public Address not valid")
        }
    }
}