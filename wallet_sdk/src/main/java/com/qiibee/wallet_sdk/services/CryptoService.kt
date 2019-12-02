package com.qiibee.wallet_sdk.services

import com.qiibee.wallet_sdk.interfaces.CryptoProvider
import com.qiibee.wallet_sdk.client.Mnemonic
import org.web3j.crypto.MnemonicUtils
import java.security.SecureRandom

object CryptoService: CryptoProvider {
    override fun createMnemonic(): Mnemonic {
        val initialEntropy = ByteArray(16)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(initialEntropy)
        return Mnemonic(
            MnemonicUtils.generateMnemonic(
                initialEntropy
            )
        )
    }

    override fun createWallet() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //val k = ECKeyPair.create(WalletSimple.mnemonic.toByteArray(Compat.UTF_8))
        //val credentials = Credentials.create(ECKeyPair(k.privateKey, k.publicKey))
        // return PublicAddress(credentials.address)
    }
}