package com.qiibee.wallet_sdk.util

import java.lang.Exception

class InvalidPublicAddress(message: String): Exception(message)
class WalletNotFound(message: String): Exception(message)
class WalletCreateFailed(message: String): Exception(message)
class InvalidMnemonicPhrase(message: String): Exception(message)
class InvalidPrivateKey(message: String): Exception(message)