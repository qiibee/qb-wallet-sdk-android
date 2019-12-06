package com.qiibee.wallet_sdk.client

import com.qiibee.wallet_sdk.interfaces.CryptoProvider
import com.qiibee.wallet_sdk.interfaces.HttpClient
import com.qiibee.wallet_sdk.interfaces.SDKProvider
import com.qiibee.wallet_sdk.interfaces.WalletProvider
import com.qiibee.wallet_sdk.services.ApiService
import com.qiibee.wallet_sdk.services.CryptoService
import com.qiibee.wallet_sdk.util.Result
import org.web3j.crypto.Hash
import java.math.BigDecimal

object CryptoWallet: SDKProvider {
    private val apiService: HttpClient = ApiService
    // TODO needs correct storage
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
    override fun getBalances(
        walletAddress: WalletAddress,
        responseHandler: (result: Result<TokenBalances, Exception>) -> Unit
    ) {
        apiService.getBalances(walletAddress, responseHandler)
    }

    override fun getTokens(
        walletAddress: WalletAddress,
        responseHandler: (result: Result<List<Token>, java.lang.Exception>) -> Unit
    ) {
        TODO("not implemented")
    }

    override fun getTransactions(
        walletAddress: WalletAddress,
        responseHandler: (result: Result<List<Transaction>, java.lang.Exception>) -> Unit
    ) {
        TODO("not implemented")
    }

    override fun transferTokens(
        toAddress: WalletAddress,
        contractAddress: WalletAddress,
        sendTokenValue: BigDecimal,
        responseHandler: (result: Result<Hash, java.lang.Exception>) -> Unit
    ) {
        TODO("not implemented")
    }


}