package com.qiibee.wallet_sdk.util

class InvalidWalletAddress(address: String): Exception("Wallet address not valid => $address")
class WalletNotFound: Exception("Wallet not stored")
class WalletCreateFailed(error: String): Exception(error)
class InvalidMnemonicPhrase(phrase: String): Exception("Invalid Mnemonic Phrase: $phrase")
class InvalidPrivateKey(key: String): Exception("Invalid Private Key: $key")
class InvalidTokenSymbol(name: String): Exception("Token Name not valid: $name")
class GetTokenBalancesFailed(error: String) : Exception(error)
class GetRawTransactionFailed(message: String) : Exception("Get Raw Transaction failed $message")
class GetTransactionsFailed(message: String): Exception("Get Transactions failed $message")