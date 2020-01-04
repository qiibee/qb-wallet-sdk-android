package com.qiibee.wallet_sdk.services

import android.content.Context
import com.qiibee.wallet_sdk.client.Mnemonic
import com.qiibee.wallet_sdk.client.PrivateKey
import com.qiibee.wallet_sdk.client.Address
import com.qiibee.wallet_sdk.interfaces.StorageProvider
import com.qiibee.wallet_sdk.util.*
import de.adorsys.android.securestoragelibrary.SecurePreferences
import org.web3j.crypto.Credentials
import org.web3j.utils.Numeric

internal object StorageService: StorageProvider {

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

    override fun walletAddress(context: Context): Result<Address, Exception> {
        val decryptedMessage = SecurePreferences.getStringValue(
            context,
            Constants.WALLET_ADDRESS,
            ""
        )

        return if (decryptedMessage == "") {
            Failure(WalletAddressNotFound())
        } else {
            Success(Address("$decryptedMessage"))
        }
    }

    override fun storeWalletDetails(
        context: Context,
        credentials: Credentials,
        mnemonic: Mnemonic
    ): Result<Unit, Exception> {
        return try {
            val privateKey = formatPrivateKey(credentials)
            SecurePreferences.setValue(context, Constants.PRIVATE_KEY, privateKey)
            SecurePreferences.setValue(context, Constants.WALLET_ADDRESS, credentials.address)
            SecurePreferences.setValue(context, Constants.MNEMONIC_PHRASE, mnemonic.phrase)
            Success(Unit)
        } catch (e: Exception) {
            Failure(StoreWalletDetailsFailed(e.message.toString()))
        }
    }

    override fun removeWallet(context: Context): Result<Unit, Exception> {
        return try {
            SecurePreferences.clearAllValues(context)
            Success(Unit)
        } catch (e: Exception) {
            Failure(RemoveWalletFailed(e.message.toString()))
        }
    }

    private fun formatPrivateKey(credentials: Credentials): String {
        return Constants.ZERO_PREFIX +
                Numeric.toHexStringNoPrefix(credentials.ecKeyPair.privateKey)
    }
}