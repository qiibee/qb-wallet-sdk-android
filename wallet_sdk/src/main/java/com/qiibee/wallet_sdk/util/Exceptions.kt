package com.qiibee.wallet_sdk.util

// Crypto/Storage Specific Exceptions
class InvalidWalletAddress(address: String): Exception("Wallet address not valid => $address")
class MnemonicCreationFailed(error: String): Exception(error)
class WalletNotFound: Exception("Wallet not found")
class WalletCreationFailed(error: String): Exception(error)
class InvalidMnemonicPhrase(phrase: String): Exception("Invalid Mnemonic Phrase: $phrase")
class InvalidPrivateKey(key: String): Exception("Invalid Private Key: $key")
class WalletSDKNotInitialized: Exception("Wallet SDK not initialized with proper Context")
class RemoveWalletFailed(error: String): Exception(error)
class StoreWalletDetailsFailed(error: String): Exception(error)

// API Specific Exceptions
class GetTokenBalancesFailed(error: String) : Exception(error)
class GetTokensFailed(error: String) : Exception(error)
class GetRawTransactionFailed(error: String) : Exception(error)
class GetTransactionsFailed(error: String): Exception(error)
class SendTransactionFailed(error: String): Exception(error)