package com.qiibee.wallet

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import java.math.BigDecimal
import com.qiibee.wallet_sdk.client.CryptoWallet
import com.qiibee.wallet_sdk.client.Mnemonic
import com.qiibee.wallet_sdk.client.Address
import com.qiibee.wallet_sdk.util.Failure
import com.qiibee.wallet_sdk.util.Success
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Initialize Context
        CryptoWallet.initialize(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getWalletAddress() {
        when (val result = CryptoWallet.walletAddress()) {
            is Success -> {
                Logger.log("wallet address is ${result.value.address}")
            }
            is Failure -> {
                Logger.log("failed with ${result.reason.message}")
            }
        }
    }

    fun getPrivateKey() {
        when (val result = CryptoWallet.privateKey()) {
            is Success -> {
                Logger.log("private key is ${result.value.privateKey}")
            }
            is Failure -> {
                Logger.log("failed with ${result.reason.message}")
            }
        }
    }

    fun getMnemonicPhrase() {
        when (val result = CryptoWallet.mnemonicPhrase()) {
            is Success -> {
                Logger.log("phrase is ${result.value.phrase}")
            }
            is Failure -> {
                Logger.log("failed with ${result.reason.message}")
            }
        }
    }

    fun createWallet() {
        when (val result = CryptoWallet.createWallet()) {
            is Success -> {
                Logger.log("created wallet with address: ${result.value.address}")
            }
            is Failure -> {
                Logger.log("failed with ${result.reason.message}")
            }
        }
    }

    fun restoreWallet() {
        try {
            val validMnemonic = Mnemonic("your mnemonic address original here payment book process stuff work remote mercy")
            when (val result = CryptoWallet.restoreWallet(validMnemonic)) {
                is Success -> {
                    Logger.log("restored wallet with address: ${result.value.address}")
                }
                is Failure -> {
                    Logger.log("failed with ${result.reason.message}")
                }
            }
        } catch (e: Error) {
            Logger.log(e.toString())
        }


    }

    fun removeStoredWallet() {
        when (val result = CryptoWallet.removeWallet()) {
            is Success -> {
                Logger.log("wallet successfully removed")
            }
            is Failure -> {
                Logger.log("failed with ${result.reason.message}")
            }
        }
    }

    fun getBalances() {
        CryptoWallet.getBalances {
            when (it) {
                is Success -> {
                    Logger.log("balances: ${it.value.balances}")
                }
                is Failure -> {
                    Logger.log("failed with ${it.reason.message}")
                }
            }
        }
    }

    fun getTokens() {
        CryptoWallet.getTokens {
            when (it) {
                is Success -> {
                    Logger.log("public tokens: ${it.value.publicTokens}, private tokens ${it.value.privateTokens}")
                }
                is Failure -> {
                    Logger.log("failed with ${it.reason.message}")
                }
            }
        }
    }

    fun getTransactions() {
        CryptoWallet.getTransactions{
            when (it) {
                is Success -> {
                    for (tx in it.value) {
                        Logger.log("contractAddress: ${tx.contractAddress} - from:${tx.from}")
                    }
                }
                is Failure -> {
                    Logger.log("failed with ${it.reason.message}")
                }
            }
        }
    }

    fun sendTransaction() {
        val toAddress = Address("0x6d5p603bnE331f045bft74Ee84AdjEj6j9b1251f")
        val contractAddress = Address("0xf2e71f41e670c2823684ac3dbdf48166084e5af3")
        val value = BigDecimal(3.4)

        CryptoWallet.sendTransaction(toAddress, contractAddress, value){
            when (it) {
                is Success -> {
                    Logger.log("Hash of the transactions is ${it.value}")
                }
                is Failure -> {
                    Logger.log("failed with ${it.reason.message}")
                }
            }
        }
    }
}

object Logger {
    fun log(s: String) {
        Log.d("TAG", s)
    }
}