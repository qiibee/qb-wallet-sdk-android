package com.qiibee.wallet_sdk.client

import android.content.Context
import org.web3j.crypto.Hash
import java.math.BigDecimal
import com.qiibee.wallet_sdk.interfaces.CryptoProvider
import com.qiibee.wallet_sdk.interfaces.HttpClient
import com.qiibee.wallet_sdk.interfaces.SDKProvider
import com.qiibee.wallet_sdk.interfaces.StorageProvider
import com.qiibee.wallet_sdk.services.ApiService
import com.qiibee.wallet_sdk.services.CryptoService
import com.qiibee.wallet_sdk.services.StorageService
import com.qiibee.wallet_sdk.util.*

object CryptoWallet: SDKProvider {
    private val apiService: HttpClient = ApiService
    private val walletStorage: StorageProvider = StorageService
    private val cryptoService: CryptoProvider = CryptoService

    // STORAGE RELATED
    override fun walletAddress(context: Context): Result<WalletAddress, Exception> {
        return walletStorage.walletAddress(context)
    }

    override fun walletExists(context: Context): Boolean {
        return walletStorage.walletExists(context)
    }

    override fun mnemonicPhrase(context: Context): Result<Mnemonic, Exception> {
        return walletStorage.mnemonicPhrase(context)
    }

    override fun privateKey(context: Context): Result<PrivateKey, Exception> {
        return walletStorage.privateKey(context)
    }

    // WALLET RELATED
    override fun createWallet(context: Context): Result<WalletAddress, Exception> {
        val mnemonicResult = cryptoService.createMnemonic()

        return when (mnemonicResult) {
            is Success -> {
                restoreWallet(context, mnemonicResult.value)
            }
            is Failure -> Failure(WalletCreationFailed("${mnemonicResult.reason.message}"))
        }
    }

    override fun restoreWallet(
        context: Context,
        mnemonic: Mnemonic
    ): Result<WalletAddress, Exception> {
        val walletResult = cryptoService.createWallet(mnemonic)

        return when (walletResult) {
            is Success -> {
                val credentials = walletResult.value
                walletStorage.storeWalletDetails(context, credentials, mnemonic)
                Success(WalletAddress(credentials.address))
            }
            is Failure -> Failure(WalletCreationFailed("${walletResult.reason.message}"))
        }
    }

    override fun removeWallet(context: Context) {
        walletStorage.removeWallet(context)
    }

    // BACKEND API RELATED
    override fun getBalances(context: Context, responseHandler: (result: Result<TokenBalances, Exception>) -> Unit) {
        val result = walletStorage.walletAddress(context)
        when (result) {
            is Success -> apiService.getBalances(result.value, responseHandler)
            is Failure -> responseHandler.invoke(result)
        }
    }

    override fun getTokens(context: Context, responseHandler: (result: Result<Tokens, Exception>) -> Unit) {
        val result = walletStorage.walletAddress(context)
        when (result) {
            is Success -> apiService.getTokens(result.value, responseHandler)
            is Failure -> responseHandler.invoke(result)
        }
    }

    override fun getTransactions(
        context: Context,
        responseHandler: (result: Result<List<Transaction>, Exception>) -> Unit
    ) {
        val result = walletStorage.walletAddress(context)
        when (result) {
            is Success -> apiService.getTransactions(result.value, responseHandler)
            is Failure -> responseHandler.invoke(result)
        }
    }

    override fun transferTokens(
        context: Context,
        toAddress: WalletAddress,
        contractAddress: WalletAddress,
        sendTokenValue: BigDecimal,
        responseHandler: (result: Result<Hash, Exception>) -> Unit
    ) {
        val walletResult = walletStorage.walletAddress(context)

        when (walletResult) {
            is Success -> {
                val fromAddress = walletResult.value
                val privateKeyResult = walletStorage.privateKey(context)

                when (privateKeyResult) {
                    is Success -> {
                        val credentials = cryptoService.deriveCredentials(privateKeyResult.value)

                        apiService.sendTransaction(
                            fromAddress,
                            credentials,
                            toAddress,
                            contractAddress,
                            sendTokenValue,
                            responseHandler
                        )
                    }
                    is Failure -> responseHandler.invoke(privateKeyResult)
                }
            }
            is Failure -> responseHandler.invoke(walletResult)
        }
    }
}