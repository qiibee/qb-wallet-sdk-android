package com.qiibee.wallet

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.qiibee.wallet_sdk.client.CryptoWallet
import com.qiibee.wallet_sdk.client.WalletAddress
import com.qiibee.wallet_sdk.util.Failure
import com.qiibee.wallet_sdk.util.Logger
import com.qiibee.wallet_sdk.util.Success

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        when (CryptoWallet.loadWallet()) {
            is Success -> {
                getTokens()
                loadBalances()
                getTransactions()
            }
            is Failure -> Logger.log("LOAD WALLET FAILED")
        }
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

    fun loadBalances() {
        CryptoWallet.getBalances { result ->
            when (result) {
                is Success -> {
                    Logger.log(result.value.balances.ethBalance.balance.toString())
                }
                is Failure -> {
                    Logger.log(result.toString())
                }
            }
        }
    }

    fun getTokens() {
        CryptoWallet.getTokens { result ->
            when (result) {
                is Success -> {
                    for (token in result.value.privateTokens) {
                        Logger.log(token.contractAddress.address)
                    }
                }
                is Failure -> {
                    Logger.log(result.toString())
                }
            }
        }
    }

    fun getTransactions() {
        CryptoWallet.getTransactions { result ->
            when (result) {
                is Success -> {
                    for (tx in result.value) {
                        Logger.log(tx.toString())
                    }
                }
                is Failure -> {
                    Logger.log(result.toString())
                }
            }
        }
    }
}
