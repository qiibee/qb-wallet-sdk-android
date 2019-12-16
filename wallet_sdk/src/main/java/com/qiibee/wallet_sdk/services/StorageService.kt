package com.qiibee.wallet_sdk.services

import android.content.Context
import com.qiibee.wallet_sdk.client.Mnemonic
import com.qiibee.wallet_sdk.client.PrivateKey
import com.qiibee.wallet_sdk.client.WalletAddress
import com.qiibee.wallet_sdk.interfaces.StorageProvider
import com.qiibee.wallet_sdk.util.*
import de.adorsys.android.securestoragelibrary.SecurePreferences
import org.web3j.crypto.Credentials
import org.web3j.utils.Numeric

object StorageService: StorageProvider {

    override fun mnemonicPhrase(context: Context): Result<Mnemonic, Exception> {
        val decryptedMessage = SecurePreferences.getStringValue(
            context,
            Constants.MNEMONIC_PHRASE,
            ""
        )

        return if (decryptedMessage == "") {
            Failure(WalletNotFound())
        } else {
            Success(Mnemonic("$decryptedMessage"))
        }
    }

    override fun privateKey(context: Context): Result<PrivateKey, Exception> {
        val decryptedMessage = SecurePreferences.getStringValue(
            context,
            Constants.PRIVATE_KEY,
            ""
        )

        return if (decryptedMessage == "") {
            Failure(PrivateKeyNotFound())
        } else {
            Success(PrivateKey("$decryptedMessage"))
        }
    }

    override fun walletAddress(context: Context): Result<WalletAddress, Exception> {
        val decryptedMessage = SecurePreferences.getStringValue(
            context,
            Constants.WALLET_ADDRESS,
            ""
        )

        return if (decryptedMessage == "") {
            Failure(WalletAddressNotFound())
        } else {
            Success(WalletAddress("$decryptedMessage"))
        }
    }

    override fun walletExists(context: Context): Boolean {
        return when (mnemonicPhrase(context)) {
            is Success -> true
            is Failure -> false
        }
    }

    override fun storeWalletDetails(context: Context, credentials: Credentials, mnemonic: Mnemonic) {
        val privateKey = Numeric.toHexStringWithPrefix(credentials.ecKeyPair.privateKey)

        SecurePreferences.setValue(context, Constants.PRIVATE_KEY, privateKey)
        SecurePreferences.setValue(context, Constants.WALLET_ADDRESS, credentials.address)
        SecurePreferences.setValue(context, Constants.MNEMONIC_PHRASE, mnemonic.phrase)
    }

    override fun removeWallet(context: Context) {
        SecurePreferences.clearAllValues(context)
    }

}