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

        when (val result = CryptoWallet.loadWallet()) {
            is Success -> loadBalances(result.value)
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

    fun loadBalances(walletAddress: WalletAddress) {
        CryptoWallet.getBalances(walletAddress) { result ->
            when (result) {
                is Success -> {
                    Logger.log(result.toString())
                }

                is Failure -> {
                    Logger.log(result.toString())
                }
            }
        }
    }
}
