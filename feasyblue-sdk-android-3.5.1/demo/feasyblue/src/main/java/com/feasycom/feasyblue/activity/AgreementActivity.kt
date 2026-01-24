package com.feasycom.feasyblue.activity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.network.DeviceNetwork
import com.feasycom.network.bean.ProtocolParams
import com.feasycom.network.bean.ProtocolResponse
import kotlinx.android.synthetic.main.activity_agreement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AgreementActivity : BaseActivity() {
    var type: Int = 0
    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        type = intent.getIntExtra("type", 0)
        toolbarTitle.text = when (type) {
            1 -> getString(R.string.userAgreement)
            else -> getString(R.string.privacyAgreement)
        }
        toolbar.setNavigationOnClickListener { finish() }
        request(type)
    }

    override fun getLayout() = R.layout.activity_agreement

    @RequiresApi(Build.VERSION_CODES.N)
    private fun request(type: Int) {
        // TODO 隐私协议开发者：深圳市飞易通科技有限公司
         val protocolParams = ProtocolParams("blue", type )
        // TODO 隐私协议开发者：长沙市飞易通科技有限公司
//        val protocolParams = ProtocolParams("blue", type,"changsha")
        lifecycleScope.launch(Dispatchers.IO) {
            var protocol: ProtocolResponse? = null
            try {
                protocol = try {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        val locales = if (resources.configuration.locales[0].language.endsWith("zh")) "cn" else "en"
                        DeviceNetwork.getProtocol(protocolParams, locales)
                    }else{
                        val locales = if (resources.configuration.locale.language.endsWith("zh")) "cn" else "en"
                        DeviceNetwork.getProtocol(protocolParams, locales)
                    }
                }catch (e: Exception){
                    DeviceNetwork.getProtocol(protocolParams, "en")
                }

                if (protocol?.code == 200){
                    launch(Dispatchers.Main){
                        val url = protocol.data.url
                        MsgLogger.e("AgreementActivity url => $url")
                        myWebView.setUrl(url)
                    }
                }else {
                    launch(Dispatchers.Main){
                        val alertDialog2 = AlertDialog.Builder(this@AgreementActivity)
                            .setTitle(R.string.error)
                            .setMessage(getString(R.string.network_error))
                            .setIcon(R.mipmap.ic_launcher)
                            .setNegativeButton(R.string.close, null)
                            .create()
                        alertDialog2.show()
                    }
                }
            }catch (e: Exception){
                launch(Dispatchers.Main){
                    val alertDialog2 = AlertDialog.Builder(this@AgreementActivity)
                        .setTitle(R.string.error)
                        .setMessage(getString(R.string.network_error))
                        .setIcon(R.mipmap.ic_launcher)
                        .setNegativeButton(R.string.close, null)
                        .create()
                    alertDialog2.show()
                }
            }
        }
    }
}