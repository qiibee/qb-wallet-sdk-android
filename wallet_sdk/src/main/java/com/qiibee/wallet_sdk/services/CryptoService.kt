package com.qiibee.wallet_sdk.services

import com.qiibee.wallet_sdk.interfaces.CryptoProvider
import com.qiibee.wallet_sdk.client.Mnemonic
import com.qiibee.wallet_sdk.client.PrivateKey
import com.qiibee.wallet_sdk.util.*
import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Bip32ECKeyPair.HARDENED_BIT
import org.web3j.crypto.Credentials
import org.web3j.crypto.MnemonicUtils
import java.security.SecureRandom

internal object CryptoService: CryptoProvider {
    override fun createMnemonic(): Result<Mnemonic, Exception> {
        return try {
            val arraySize = 16
            val initialEntropy = ByteArray(arraySize)
            val secureRandom = SecureRandom()
            secureRandom.nextBytes(initialEntropy)
            val mnemonic = MnemonicUtils.generateMnemonic(initialEntropy)
            Success(Mnemonic(mnemonic))

        } catch (error: Exception) {
            Failure(MnemonicCreationFailed("${error.message}"))
        }
    }

    override fun createWallet(mnemonic: Mnemonic) : Result<Credentials, Exception> {
        return try {
            Success(createCredentialsFrom(mnemonic))
        } catch (error: Exception) {
            Failure(WalletCreationFailed("${error.message}"))
        }
    }

    override fun deriveCredentials(privateKey: PrivateKey): Credentials {
        return Credentials.create(privateKey.privateKey)
    }

    // Custom function for creating Credentials in order to avoid
    // problem with different privateKey being generated compered to other Crypto libraries
    // URL: https://github.com/web3j/web3j/issues/919
    private fun createCredentialsFrom(mnemonic: Mnemonic): Credentials {
        // "m/44'/60'/0'/0/0"
        val path = intArrayOf(44 or HARDENED_BIT, 60 or HARDENED_BIT, 0 or HARDENED_BIT, 0, 0)
        val seed = MnemonicUtils.generateSeed(mnemonic.phrase, "")
        val masterKeyPair = Bip32ECKeyPair.generateKeyPair(seed)
        val bip44Keypair = Bip32ECKeyPair.deriveKeyPair(masterKeyPair, path)

        return Credentials.create(bip44Keypair)
    }


}