package com.feasycom.feasyblue.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.fragment.AboutFragment
import com.feasycom.feasyblue.fragment.CommunicationFragment
import com.feasycom.feasyblue.fragment.SettingFragment
import com.feasycom.feasyblue.fragment.StoreFragment
import com.feasycom.feasyblue.utils.putBoolean
import com.feasycom.spp.controler.FscSppCentralApi
import com.feasycom.spp.controler.FscSppCentralApiImp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    override fun getLayout() = R.layout.activity_main

    override fun initView() {
        with(viewPager2) {
            adapter = object : FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount() = 4
                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> CommunicationFragment.newInstance()
                        1 -> SettingFragment.newInstance()
                        2 -> StoreFragment.newInstance()
                        else -> AboutFragment.newInstance()
                    }
                }
            }
            offscreenPageLimit = 1
            isUserInputEnabled = false
        }
        navigation_bar.setOnNavigationItemSelectedListener {
            viewPager2.setCurrentItem(
                when (it.itemId) {
                    R.id.communication -> 0
                    R.id.setting -> 1
                    R.id.store -> 2
                    else -> 3
                }, false
            )
            true
        }
        navigation_bar.selectedItemId = when (intent.getIntExtra("position", 0)) {
            0 -> R.id.communication
            1 -> R.id.setting
            2 -> R.id.store
            else -> R.id.about
        }
    }

    companion object {
        fun activityStart(context: Context, position: Int) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

fun String?.showStr(context: Activity) {
    if (this?.isNotEmpty() == true) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(context, this@showStr, Toast.LENGTH_SHORT).show()
        }
    }
}

fun Activity.showStr(str: String) {
    if (str.isNotEmpty()) {
        runOnUiThread {
            Toast.makeText(this@showStr, str, Toast.LENGTH_SHORT).show()
        }
    }
}