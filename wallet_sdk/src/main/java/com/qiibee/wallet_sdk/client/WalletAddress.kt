package com.qiibee.wallet_sdk.client

import com.qiibee.wallet_sdk.util.Assertion
import com.qiibee.wallet_sdk.util.InvalidPublicAddress

class WalletAddress(val address: String) {
    init {
        if (!Assertion.isValidAddress(address)) {
            throw InvalidPublicAddress("Wallet Address not valid")
        }
    }
}