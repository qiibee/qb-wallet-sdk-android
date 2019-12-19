package com.qiibee.wallet_sdk.services

import com.qiibee.wallet_sdk.interfaces.CryptoProvider
import com.qiibee.wallet_sdk.client.Mnemonic
import com.qiibee.wallet_sdk.util.Failure
import com.qiibee.wallet_sdk.util.Result
import com.qiibee.wallet_sdk.util.Success
import com.qiibee.wallet_sdk.util.WalletCreateFailed
import org.web3j.crypto.MnemonicUtils
import java.security.SecureRandom

object CryptoService: CryptoProvider {
    override fun createMnemonic(): Result<Mnemonic, Exception> {
        try {
            val initialEntropy = ByteArray(16)
            val secureRandom = SecureRandom()
            secureRandom.nextBytes(initialEntropy)
            val mnemonic = MnemonicUtils.generateMnemonic(initialEntropy)
            return Success(Mnemonic(mnemonic))

        } catch (error: Exception) {
            return Failure(WalletCreateFailed("Wallet creation failed with ${error.message}"))
        }
    }

    override fun createWallet() {
        TODO("not implemented")
        //val k = ECKeyPair.create(WalletSimple.mnemonic.toByteArray(Compat.UTF_8))
        //val credentials = Credentials.create(ECKeyPair(k.privateKey, k.publicKey))
        // return PublicAddress(credentials.address)
    }
}