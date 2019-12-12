package com.qiibee.wallet_sdk.client

import com.qiibee.wallet_sdk.interfaces.CryptoProvider
import com.qiibee.wallet_sdk.interfaces.HttpClient
import com.qiibee.wallet_sdk.interfaces.SDKProvider
import com.qiibee.wallet_sdk.interfaces.WalletProvider
import com.qiibee.wallet_sdk.services.ApiService
import com.qiibee.wallet_sdk.services.CryptoService
import com.qiibee.wallet_sdk.services.WalletSimple
import com.qiibee.wallet_sdk.util.Failure
import com.qiibee.wallet_sdk.util.Result
import com.qiibee.wallet_sdk.util.Success
import org.web3j.crypto.Hash
import java.math.BigDecimal

object CryptoWallet: SDKProvider {
    private val apiService: HttpClient = ApiService
    // TODO needs correct storage implementation
    private val walletStorage: WalletProvider = WalletProvider
    private val cryptoService: CryptoProvider = CryptoService

    // STORAGE RELATED
    override fun loadWallet(): Result<WalletAddress, Exception> {
        return walletStorage.publicAddress()
    }

    override fun walletExists(): Boolean {
        return walletStorage.walletExists()
    }

    override fun mnemonicPhrase(): Result<Mnemonic, Exception> {
        return walletStorage.mnemonicPhrase()
    }

    override fun privateKey(): Result<PrivateKey, Exception> {
        return walletStorage.privateKey()
    }

    // WALLET RELATED
    override fun createWallet(): Result<WalletAddress, java.lang.Exception> {
        TODO("not implemented")

        // Create Wallet with crypto Service

        // Store Wallet to walletStorage

        // return back WalletAddress
    }

    // BACKEND API RELATED
    override fun getBalances(responseHandler: (result: Result<TokenBalances, Exception>) -> Unit) {
        val result = walletStorage.publicAddress()
        when (result) {
            is Success -> apiService.getBalances(result.value, responseHandler)
            is Failure -> responseHandler.invoke(result)
        }
    }

    override fun getTokens(responseHandler: (result: Result<Tokens, java.lang.Exception>) -> Unit) {
        val result = walletStorage.publicAddress()
        when (result) {
            is Success -> apiService.getTokens(result.value, responseHandler)
            is Failure -> responseHandler.invoke(result)
        }
    }

    override fun getTransactions(
        responseHandler: (result: Result<List<Transaction>, java.lang.Exception>) -> Unit
    ) {
        val result = walletStorage.publicAddress()
        when (result) {
            is Success -> apiService.getTransactions(result.value, responseHandler)
            is Failure -> responseHandler.invoke(result)
        }
    }

    override fun transferTokens(
        toAddress: WalletAddress,
        contractAddress: WalletAddress,
        sendTokenValue: BigDecimal,
        responseHandler: (result: Result<Hash, java.lang.Exception>) -> Unit
    ) {
        val result = walletStorage.publicAddress()
        val credentials = walletStorage.getCredentials()

        when (result) {
            is Success -> {
                val fromAddress = result.value
                apiService.sendTransaction(
                    fromAddress,
                    credentials,
                    toAddress,
                    contractAddress,
                    sendTokenValue,
                    responseHandler
                )
            }
            is Failure -> responseHandler.invoke(result)
        }
    }


}