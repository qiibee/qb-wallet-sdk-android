package com.qiibee.wallet_sdk.client

import android.content.Context
import java.math.BigDecimal
import org.web3j.crypto.Credentials
import com.qiibee.wallet_sdk.interfaces.CryptoProvider
import com.qiibee.wallet_sdk.interfaces.HttpClient
import com.qiibee.wallet_sdk.interfaces.SDKProvider
import com.qiibee.wallet_sdk.interfaces.StorageProvider
import com.qiibee.wallet_sdk.services.ApiService
import com.qiibee.wallet_sdk.services.CryptoService
import com.qiibee.wallet_sdk.services.StorageService
import com.qiibee.wallet_sdk.util.*

/**
 * This object is your main entry point to Wallet SDK
 *
 * @property HttpClient
 * @property StorageProvider
 * @property CryptoProvider
 * @property Context?
 */
object CryptoWallet: SDKProvider {
    private val apiService: HttpClient = ApiService
    private val walletStorage: StorageProvider = StorageService
    private val cryptoService: CryptoProvider = CryptoService
    private var context: Context? = null

    /**
     * Initializes context used for accessing Phone's storage.
     *
     * @apiNote must initialize context before using CryptoWallet
     * @param context Context
     *
     */
    override fun initialize(context: Context) {
        this.context = context
    }

    // STORAGE RELATED
    /**
     * @return currently stored Wallet Address
     * or Exception<WalletSDKNotInitialized || WalletNotFound>.
     */
    override fun walletAddress(): Result<Address, Exception> {
        return when (val contextResult = getContext()) {
            is Success -> walletStorage.walletAddress(contextResult.value)
            is Failure -> contextResult
        }
    }

    /**
     * @return currently stored Mnemonic Phrase
     * or Exception<WalletSDKNotInitialized || WalletNotFound>.
     */
    override fun mnemonicPhrase(): Result<Mnemonic, Exception> {
        return when (val contextResult = getContext()) {
            is Success -> walletStorage.mnemonicPhrase(contextResult.value)
            is Failure -> contextResult
        }
    }

    /**
     * @return currently stored Private Key
     * or Exception<WalletSDKNotInitialized || WalletNotFound>.
     */
    override fun privateKey(): Result<PrivateKey, Exception> {
        return when (val contextResult = getContext()) {
            is Success -> walletStorage.privateKey(contextResult.value)
            is Failure -> contextResult
        }
    }

    // WALLET RELATED
    /**
     * Creates new Ether wallet.
     * @return Address of newly created wallet
     * or Exception<MnemonicCreationFailed || WalletSDKNotInitialized || WalletCreationFailed>.
     */
    override fun createWallet(): Result<Address, Exception> {
        return when (val mnemonicResult = cryptoService.createMnemonic()) {
            is Success -> {
                restoreWallet(mnemonicResult.value)
            }
            is Failure -> mnemonicResult
        }
    }

    /**
     * Restores existing Ether wallet.
     * @param mnemonic Mnemonic
     * @return Address of restored wallet
     * or Exception<WalletSDKNotInitialized || WalletCreationFailed>
     */
    override fun restoreWallet(
        mnemonic: Mnemonic
    ): Result<Address, Exception> {
        return when (val contextResult = getContext()) {
            is Success -> restoreWalletHelper(contextResult.value, mnemonic)
            is Failure -> contextResult
        }
    }

    /**
     * Removes existing Wallet data.
     * @return Succes<Unit> if deletion succeeds,
     * or Exception<WalletSDKNotInitialized || RemoveWalletFailed>
     */
    override fun removeWallet(): Result<Unit, Exception> {
        return when (val contextResult = getContext()) {
            is Success -> walletStorage.removeWallet(contextResult.value)
            is Failure -> contextResult
        }
    }

    // BACKEND API RELATED
    /**
     *
     * @param responseHandler Result<TokenBalances, Exception) -> Unit
     *
     * Gets current balances and passes them to callback,
     * Exception<WalletSDKNotInitialized || WalletNotFound || GetTokenBalancesFailed>
     * is passed if the call fails.
     */
    override fun getBalances(responseHandler: (result: Result<TokenBalances, Exception>) -> Unit) {
        return when (val contextResult = getContext()) {
            is Success -> getBalancesHelper(contextResult.value, responseHandler)
            is Failure -> responseHandler.invoke(contextResult)
        }
    }

    /**
     *
     * @param responseHandler Result<Tokens, Exception> -> Unit
     *
     * Gets current tokens and passes them to callback,
     * Exception<WalletSDKNotInitialized || WalletNotFound || GetTokensFailed>
     * is passed if the call fails.
     */
    override fun getTokens(responseHandler: (result: Result<Tokens, Exception>) -> Unit) {
        return when (val contextResult = getContext()) {
            is Success -> getTokensHelper(contextResult.value, responseHandler)
            is Failure -> responseHandler.invoke(contextResult)
        }
    }

    /**
     *
     * @param responseHandler Result<List<Transaction>, Exception> -> Unit
     *
     * Gets current transactions and passes them to callback,
     * Exception<WalletSDKNotInitialized || WalletNotFound || GetTransactionsFailed>
     * is passed if the call fails.
     */
    override fun getTransactions(
        responseHandler: (result: Result<List<Transaction>, Exception>) -> Unit
    ) {
        return when (val contextResult = getContext()) {
            is Success -> getTransactionsHelper(contextResult.value, responseHandler)
            is Failure -> responseHandler.invoke(contextResult)
        }
    }

    /**
     *
     * @param toAddress Address
     * @param contractAddress Address
     * @param sendTokenValue BigDecimal
     * @param responseHandler Result<Hash, Exception> -> Unit     *
     *
     * Performs send transaction, on Success Hash is passed to the callback,
     * Exception<WalletSDKNotInitialized || WalletNotFound ||
     * GetRawTransactionFailed || SendTransactionFailed>
     * is passed if the call fails.
     */
    override fun sendTransaction(
        toAddress: Address,
        contractAddress: Address,
        sendTokenValue: BigDecimal,
        responseHandler: (result: Result<Hash, Exception>) -> Unit
    ) {
        return when (val contextResult = getContext()) {
            is Success ->
                sendTransactionHelper(
                    contextResult.value,
                    toAddress,
                    contractAddress,
                    sendTokenValue,
                    responseHandler
                )
            is Failure -> responseHandler.invoke(contextResult)
        }
    }

    // Private Helpers
    private fun getContext(): Result<Context, Exception> {
        context?.let {
            return Success(it)
        }
        return Failure(WalletSDKNotInitialized())
    }

    private fun restoreWalletHelper(context: Context, mnemonic: Mnemonic): Result<Address, Exception> {
        return when (val walletResult = cryptoService.createWallet(mnemonic)) {
            is Success -> {
                val credentials = walletResult.value
                walletStorage.storeWalletDetails(context, credentials, mnemonic)
                Success(Address(credentials.address))
            }
            is Failure -> walletResult
        }
    }

    private fun getBalancesHelper(
        context: Context,
        responseHandler: (result: Result<TokenBalances, Exception>) -> Unit)
    {
        when (val result = walletStorage.walletAddress(context)) {
            is Success -> apiService.getBalances(result.value, responseHandler)
            is Failure -> responseHandler.invoke(result)
        }
    }

    private fun getTokensHelper(
        context: Context,
        responseHandler: (result: Result<Tokens, Exception>) -> Unit
    ) {
        when (val result = walletStorage.walletAddress(context)) {
            is Success -> apiService.getTokens(result.value, responseHandler)
            is Failure -> responseHandler.invoke(result)
        }
    }

    private fun getTransactionsHelper(
        context: Context,
        responseHandler: (result: Result<List<Transaction>, Exception>) -> Unit
    ) {
        when (val result = walletStorage.walletAddress(context)) {
            is Success -> apiService.getTransactions(result.value, responseHandler)
            is Failure -> responseHandler.invoke(result)
        }
    }

    private fun sendTransactionHelper(
        context: Context,
        toAddress: Address,
        contractAddress: Address,
        sendTokenValue: BigDecimal,
        responseHandler: (result: Result<Hash, Exception>) -> Unit
    ) {
        when (val walletResult = walletStorage.walletAddress(context)) {
            is Success -> {
                when (val privateKeyResult = walletStorage.privateKey(context)) {
                    is Success -> {
                        val credentials = cryptoService.deriveCredentials(privateKeyResult.value)

                        signAndSendTransaction(
                            walletResult.value,
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

    private fun signAndSendTransaction(
        fromAddress: Address,
        credentials: Credentials,
        toAddress: Address,
        contractAddress: Address,
        sendTokenValue: BigDecimal,
        responseHandler: (result: Result<Hash, Exception>) -> Unit
    ) {
        apiService.getRawTransaction(fromAddress, toAddress, contractAddress, sendTokenValue) {
            when (it) {
                is Success -> {
                    val rawTx = it.value
                    val signedTx = CryptoUtils.signTx(rawTx, credentials)
                    apiService.sendSignedTransaction(signedTx, responseHandler)
                }
                is Failure -> {
                    responseHandler.invoke(it)
                }
            }
        }
    }

}