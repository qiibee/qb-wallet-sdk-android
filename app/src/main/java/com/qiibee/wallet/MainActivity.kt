package com.qiibee.wallet

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.qiibee.wallet_sdk.client.CryptoWallet
import com.qiibee.wallet_sdk.client.Mnemonic
import com.qiibee.wallet_sdk.client.WalletAddress
import com.qiibee.wallet_sdk.util.Failure
import com.qiibee.wallet_sdk.util.Success
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private val INFO = "INFO_LOG"
    private val ERROR = "ERROR_LOG"

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
                Log.d(INFO, "wallet address is ${result.value.address}")
            }
            is Failure -> {
                Log.e(ERROR, "failed with ${result.reason.message}")
            }
        }
    }

    fun getPrivateKey() {
        when (val result = CryptoWallet.privateKey()) {
            is Success -> {
                Log.d(INFO, "private key is ${result.value.privateKey}")
            }
            is Failure -> {
                Log.e(ERROR, "failed with ${result.reason.message}")
            }
        }
    }

    fun getMnemonicPhrase() {
        when (val result = CryptoWallet.mnemonicPhrase()) {
            is Success -> {
                Log.d(INFO, "phrase is ${result.value.phrase}")
            }
            is Failure -> {
                Log.e(ERROR, "failed with ${result.reason.message}")
            }
        }
    }

    fun createWallet() {
        when (val result = CryptoWallet.createWallet()) {
            is Success -> {
                Log.d(INFO, "created wallet with address: ${result.value.address}")
            }
            is Failure -> {
                Log.e(ERROR, "failed with ${result.reason.message}")
            }
        }
    }

    fun restoreWallet() {
        val validMnemonic = Mnemonic("station hard under bus always prompts film story tires valid fake phrase")
        when (val result = CryptoWallet.restoreWallet(validMnemonic)) {
            is Success -> {
                Log.d(INFO, "restored wallet with address: ${result.value.address}")
            }
            is Failure -> {
                Log.e(ERROR, "failed with ${result.reason.message}")
            }
        }
    }

    fun removeStoredWallet() {
        when (val result = CryptoWallet.removeWallet()) {
            is Success -> {
                Log.d(INFO, "wallet successfully removed")
            }
            is Failure -> {
                Log.e(ERROR, "failed with ${result.reason.message}")
            }
        }
    }

    fun getBalances() {
        CryptoWallet.getBalances {
            when (it) {
                is Success -> {
                    Log.d(INFO, "balances: ${it.value.balances}")
                }
                is Failure -> {
                    Log.e(ERROR, "failed with ${it.reason.message}")
                }
            }
        }
    }

    fun getTokens() {
        CryptoWallet.getTokens {
            when (it) {
                is Success -> {
                    Log.d(INFO, "public tokens: ${it.value.publicTokens}, private tokens ${it.value.privateTokens}")
                }
                is Failure -> {
                    Log.e(ERROR, "failed with ${it.reason.message}")
                }
            }
        }
    }

    fun getTransactions() {
        CryptoWallet.getTransactions{
            when (it) {
                is Success -> {
                    for (tx in it.value) {
                        Log.d(INFO, "contractAddress: ${tx.contractAddress} - from:${tx.from}")
                    }
                }
                is Failure -> {
                    Log.e(ERROR, "failed with ${it.reason.message}")
                }
            }
        }
    }

    fun sendTransaction() {
        val toAddress = WalletAddress("valid address here")
        val contractAddress = WalletAddress("valid address here")
        val value = BigDecimal(0.324)

        CryptoWallet.sendTransaction(toAddress, contractAddress, value){
            when (it) {
                is Success -> {
                    Log.d(INFO, "Hash of the transactions is ${it.value}")
                }
                is Failure -> {
                    Log.e(ERROR, "failed with ${it.reason.message}")
                }
            }
        }
    }





}
