package com.qiibee.wallet_sdk.services

import com.qiibee.wallet_sdk.client.Mnemonic
import com.qiibee.wallet_sdk.client.PrivateKey
import com.qiibee.wallet_sdk.interfaces.WalletProvider
import com.qiibee.wallet_sdk.client.WalletAddress
import com.qiibee.wallet_sdk.util.Result
import com.qiibee.wallet_sdk.util.Success
import java.lang.Exception

// This is a mock implementation of Wallet Storage/Provider
object WalletSimple: WalletProvider {
    private val mnemonic =
        Mnemonic(
            "switch rule drill tornado pause conduct horse twelve ivory beach hybrid east"
        )
    private val privateKey = PrivateKey("some private key")
    private val walletExists: Boolean
    private val ADDRESS: WalletAddress =
        WalletAddress("0xbBf0458e845E1fd7EEfAd6d5689b13A3E3312510")

    init {
        // TODO
        // check in storage does wallet exist and set this to either true or false
        // TODO - what to do upon creating the wallet later on ?
        walletExists = true
    }
    override fun privateKey(): Result<PrivateKey, Exception> {
        // TODO
        // if it exists in storage return Success(private Key)
        // else return Exception
        return Success(privateKey)
    }

    override fun publicAddress(): Result<WalletAddress, Exception> {
        // TODO
        // if it exists in storage return Success(address)
        // else return Exception
        return Success(ADDRESS)
    }

    override fun walletExists(): Boolean {
        return walletExists
    }

    override fun mnemonicPhrase(): Result<Mnemonic, Exception> {
        // TODO
        // if it exists return it with success
        // else exception
        return Success(mnemonic)
    }

}