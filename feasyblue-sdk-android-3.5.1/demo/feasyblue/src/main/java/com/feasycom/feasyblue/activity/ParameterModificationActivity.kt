package com.feasycom.feasyblue.activity

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.interfaces.ICommandCallback
import com.feasycom.feasyblue.presenters.CommandPresenters
import com.feasycom.feasyblue.utils.*
import kotlinx.android.synthetic.main.activity_parameter_modification.*
import kotlinx.android.synthetic.main.activity_password.toolbar
import kotlinx.android.synthetic.main.activity_password.toolbarTitle

class ParameterModificationActivity : BaseActivity(), View.OnClickListener, ICommandCallback {
    private lateinit var commandSet: LinkedHashSet<String>

    override fun initView() {
        initToolbar()
        toolbarButton.isEnabled = false
        toolbarButton.text = getString(R.string.begin)
        toolbarButton.setOnClickListener(this)
        CommandPresenters.mICommandCallback = this
        initData()
        initEvent()
        commandSet = LinkedHashSet()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbarTitle.text = getString(R.string.parameterDefining)
        toolbar.setNavigationOnClickListener {
            MainActivity.activityStart(this, 1)
            finish()
        }
    }

    private fun initData() {
        name_para_et.setText(getStr("namePara", ""))
        pin_para_et.setText(getStr("pinPara", ""))
        baud_para_et.setText(getStr("baudPara", ""))
        custom_command_et1.setText(getStr("customCommandEt1", ""))
        custom_parameter_et1.setText(getStr("customParameterEt1", ""))
        custom_command_et2.setText(getStr("customCommandEt2", ""))
        custom_parameter_et2.setText(getStr("customParameterEt2", ""))
        custom_command_et3.setText(getStr("customCommandEt3", ""))
        custom_parameter_et3.setText(getStr("customParameterEt3", ""))
        custom_command_et4.setText(getStr("customCommandEt4", ""))
        custom_parameter_et4.setText(getStr("customParameterEt4", ""))
        custom_command_et5.setText(getStr("customCommandEt5", ""))
        custom_parameter_et5.setText(getStr("customParameterEt5", ""))
        custom_command_et6.setText(getStr("customCommandEt6", ""))
        custom_parameter_et6.setText(getStr("customParameterEt6", ""))
        custom_command_et7.setText(getStr("customCommandEt7", ""))
        custom_parameter_et7.setText(getStr("customParameterEt7", ""))
        custom_command_et8.setText(getStr("customCommandEt8", ""))
        custom_parameter_et8.setText(getStr("customParameterEt8", ""))
        custom_command_et9.setText(getStr("customCommandEt9", ""))
        custom_parameter_et9.setText(getStr("customParameterEt9", ""))

        name_para_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("namePara", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        pin_para_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("pinPara", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        baud_para_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("baudPara", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_command_et1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customCommandEt1", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_parameter_et1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customParameterEt1", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_command_et2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customCommandEt2", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_parameter_et2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customParameterEt2", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_command_et3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customCommandEt3", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_parameter_et3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customParameterEt3", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_command_et4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customCommandEt4", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_parameter_et4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customParameterEt4", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_command_et5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customCommandEt5", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_parameter_et5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customParameterEt5", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_command_et6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customCommandEt6", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_parameter_et6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customParameterEt6", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_command_et7.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customCommandEt7", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_parameter_et7.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customParameterEt7", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_command_et8.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customCommandEt8", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_parameter_et8.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customParameterEt8", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_command_et9.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customCommandEt9", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        custom_parameter_et9.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                putStr("customParameterEt9", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

    private fun initEvent() {
        name_switch.setOnClickListener(this)
        pin_switch.setOnClickListener(this)
        baud_switch.setOnClickListener(this)
        custom_switch1.setOnClickListener(this)
        custom_switch2.setOnClickListener(this)
        custom_switch3.setOnClickListener(this)
        custom_switch4.setOnClickListener(this)
        custom_switch5.setOnClickListener(this)
        custom_switch6.setOnClickListener(this)
        custom_switch7.setOnClickListener(this)
        custom_switch8.setOnClickListener(this)
        custom_switch9.setOnClickListener(this)
    }

    override fun getLayout() = R.layout.activity_parameter_modification

    companion object {
        private const val TAG = "ParameterModify"
        fun activityStart(context: Context) {
            val intent = Intent(context, ParameterModificationActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbarButton -> {
                saveCommand()
                ParameterModificationDeviceListActivity.activityStart(this)
            }
            R.id.name_switch -> {
                if (getStr("namePara", "").isNotEmpty()) {
                    if (name_switch.isOpened) {
                        name_para_et.isEnabled = !name_switch.isOpened
                        CommandPresenters.plus()
                    } else {
                        name_para_et.isEnabled = !name_switch.isOpened
                        CommandPresenters.minus()
                    }
                } else {
                    if (name_switch.isOpened) {
                        name_switch.isOpened = false
                    }
                    ToastUtil.show(this, resources.getString(R.string.none))
                }
            }
            R.id.pin_switch -> {
                if (getStr("pinPara", "").isNotEmpty()) {
                    if (pin_switch.isOpened) {
                        pin_para_et.isEnabled = !pin_switch.isOpened
                        CommandPresenters.plus()
                    } else {
                        pin_para_et.isEnabled = !pin_switch.isOpened
                        CommandPresenters.minus()
                    }
                } else {
                    if (pin_switch.isOpened) {
                        pin_switch.isOpened = false
                    }
                    ToastUtil.show(this, resources.getString(R.string.none))
                }
            }
            R.id.baud_switch -> {
                if (getStr("baudPara", "").isNotEmpty()) {
                    if (baud_switch.isOpened) {
                        baud_para_et.isEnabled = !baud_switch.isOpened
                        CommandPresenters.plus()
                    } else {
                        baud_para_et.isEnabled = !baud_switch.isOpened
                        CommandPresenters.minus()
                    }
                } else {
                    if (baud_switch.isOpened) {
                        baud_switch.isOpened = false
                    }
                    ToastUtil.show(this, resources.getString(R.string.none))
                }
            }
            R.id.custom_switch1 -> {
                if (custom_switch1.isOpened) {
                    if (getStr("customCommandEt1", "").isNotEmpty()) {
                        custom_command_et1.isEnabled = !custom_switch1.isOpened
                        custom_parameter_et1.isEnabled = !custom_switch1.isOpened
                        CommandPresenters.plus()
                    } else {
                        custom_switch1.isOpened = false
                        ToastUtil.show(this, resources.getString(R.string.none))
                    }
                } else {
                    custom_command_et1.isEnabled = !custom_switch1.isOpened
                    custom_parameter_et1.isEnabled = !custom_switch1.isOpened
                    CommandPresenters.minus()
                }
            }
            R.id.custom_switch2 -> {
                if (custom_switch2.isOpened) {
                    if (getStr("customCommandEt2", "").isNotEmpty()) {
                        custom_command_et2.isEnabled = !custom_switch2.isOpened
                        custom_parameter_et2.isEnabled = !custom_switch2.isOpened
                        CommandPresenters.plus()
                    } else {
                        custom_switch2.isOpened = false
                        ToastUtil.show(this, resources.getString(R.string.none))
                    }
                } else {
                    custom_command_et2.isEnabled = !custom_switch2.isOpened
                    custom_parameter_et2.isEnabled = !custom_switch2.isOpened
                    CommandPresenters.minus()
                }
            }
            R.id.custom_switch3 -> {
                if (custom_switch3.isOpened) {
                    if (getStr("customCommandEt3", "").isNotEmpty()) {
                        custom_command_et3.isEnabled = !custom_switch3.isOpened
                        custom_parameter_et3.isEnabled = !custom_switch3.isOpened
                        CommandPresenters.plus()
                    } else {
                        custom_switch3.isOpened = false
                        ToastUtil.show(this, resources.getString(R.string.none))
                    }
                } else {
                    custom_command_et3.isEnabled = !custom_switch3.isOpened
                    custom_parameter_et3.isEnabled = !custom_switch3.isOpened
                    CommandPresenters.minus()
                }
            }
            R.id.custom_switch4 -> {
                if (custom_switch4.isOpened) {
                    if (getStr("customCommandEt4", "").isNotEmpty()) {
                        custom_command_et4.isEnabled = !custom_switch4.isOpened
                        custom_parameter_et4.isEnabled = !custom_switch4.isOpened
                        CommandPresenters.plus()
                    } else {
                        custom_switch4.isOpened = false
                        ToastUtil.show(this, resources.getString(R.string.none))
                    }
                } else {
                    custom_command_et4.isEnabled = !custom_switch4.isOpened
                    custom_parameter_et4.isEnabled = !custom_switch4.isOpened
                    CommandPresenters.minus()
                }
            }
            R.id.custom_switch5 -> {
                if (custom_switch5.isOpened) {
                    if (getStr("customCommandEt5", "").isNotEmpty()) {
                        custom_command_et5.isEnabled = !custom_switch5.isOpened
                        custom_parameter_et5.isEnabled = !custom_switch5.isOpened
                        CommandPresenters.plus()
                    } else {
                        custom_switch5.isOpened = false
                        ToastUtil.show(this, resources.getString(R.string.none))
                    }
                } else {
                    custom_command_et5.isEnabled = !custom_switch5.isOpened
                    custom_parameter_et5.isEnabled = !custom_switch5.isOpened
                    CommandPresenters.minus()
                }
            }
            R.id.custom_switch6 -> {
                if (custom_switch6.isOpened) {
                    if (getStr("customCommandEt6", "").isNotEmpty()) {
                        custom_command_et6.isEnabled = !custom_switch6.isOpened
                        custom_parameter_et6.isEnabled = !custom_switch6.isOpened
                        CommandPresenters.plus()
                    } else {
                        custom_switch6.isOpened = false
                        ToastUtil.show(this, resources.getString(R.string.none))
                    }
                } else {
                    custom_command_et6.isEnabled = !custom_switch6.isOpened
                    custom_parameter_et6.isEnabled = !custom_switch6.isOpened
                    CommandPresenters.minus()
                }
            }
            R.id.custom_switch7 -> {
                if (custom_switch7.isOpened) {
                    if (getStr("customCommandEt7", "").isNotEmpty()) {
                        custom_command_et7.isEnabled = !custom_switch7.isOpened
                        custom_parameter_et7.isEnabled = !custom_switch7.isOpened
                        CommandPresenters.plus()
                    } else {
                        custom_switch7.isOpened = false
                        ToastUtil.show(this, resources.getString(R.string.none))
                    }
                } else {
                    custom_command_et7.isEnabled = !custom_switch7.isOpened
                    custom_parameter_et7.isEnabled = !custom_switch7.isOpened
                    CommandPresenters.minus()
                }
            }
            R.id.custom_switch8 -> {
                if (custom_switch8.isOpened) {
                    if (getStr("customCommandEt8", "").isNotEmpty()) {
                        custom_command_et8.isEnabled = !custom_switch8.isOpened
                        custom_parameter_et8.isEnabled = !custom_switch8.isOpened
                        CommandPresenters.plus()
                    } else {
                        custom_switch8.isOpened = false
                        ToastUtil.show(this, resources.getString(R.string.none))
                    }
                } else {
                    custom_command_et8.isEnabled = !custom_switch8.isOpened
                    custom_parameter_et8.isEnabled = !custom_switch8.isOpened
                    CommandPresenters.minus()
                }
            }
            R.id.custom_switch9 -> {
                if (custom_switch9.isOpened) {
                    if (getStr("customCommandEt9", "").isNotEmpty()) {
                        custom_command_et9.isEnabled = !custom_switch9.isOpened
                        custom_parameter_et9.isEnabled = !custom_switch9.isOpened
                        CommandPresenters.plus()
                    } else {
                        custom_switch9.isOpened = false
                        ToastUtil.show(this, resources.getString(R.string.none))
                    }
                } else {
                    custom_command_et9.isEnabled = !custom_switch9.isOpened
                    custom_parameter_et9.isEnabled = !custom_switch9.isOpened
                    CommandPresenters.minus()
                }
            }
        }
    }

    private fun saveCommand() {
        commandSet.clear()
        if (getStr("namePara", "").isNotEmpty()) {
            if (name_switch.isOpened) {
                commandSet.add("AT+NAME=" + getStr("namePara"))
                commandSet.add("AT+NAME")
            }
        }
        if (getStr("pinPara", "").isNotEmpty()) {
            if (pin_switch.isOpened) {
                commandSet.add("AT+PIN=" + getStr("pinPara"))
                commandSet.add("AT+PIN")
            }
        }
        if (getStr("baudPara", "").isNotEmpty()) {
            if (baud_switch.isOpened) {
                commandSet.add("AT+BAUD=" + getStr("baudPara"))
                commandSet.add("AT+BAUD")
            }
        }
        if (getStr("customCommandEt1", "").isNotEmpty()) {
            if (custom_switch1.isOpened) {
                if (getStr("customParameterEt1", "").isNotEmpty()) {
                    commandSet.add("AT+" + getStr("customCommandEt1") + "=" + getStr("customParameterEt1"))
                    commandSet.add("AT+" + getStr("customCommandEt1"))
                } else {
                    commandSet.add("AT+" + getStr("customCommandEt1"))
                }
            }
        }
        if (getStr("customCommandEt2", "").isNotEmpty()) {
            if (custom_switch2.isOpened) {
                if (getStr("customParameterEt2", "").isNotEmpty()) {
                    commandSet.add("AT+" + getStr("customCommandEt2") + "=" + getStr("customParameterEt2"))
                    commandSet.add("AT+" + getStr("customCommandEt2"))
                } else {
                    commandSet.add("AT+" + getStr("customCommandEt2"))
                }
            }
        }
        if (getStr("customCommandEt3", "").isNotEmpty()) {
            if (custom_switch3.isOpened) {
                if (getStr("customParameterEt3", "").isNotEmpty()) {
                    commandSet.add("AT+" + getStr("customCommandEt3") + "=" + getStr("customParameterEt3"))
                    commandSet.add("AT+" + getStr("customCommandEt3"))
                } else {
                    commandSet.add("AT+" + getStr("customCommandEt3"))
                }
            }
        }
        if (getStr("customCommandEt4", "").isNotEmpty()) {
            if (custom_switch4.isOpened) {
                if (getStr("customParameterEt4", "").isNotEmpty()) {
                    commandSet.add("AT+" + getStr("customCommandEt4") + "=" + getStr("customParameterEt4"))
                    commandSet.add("AT+" + getStr("customCommandEt4"))
                } else {
                    commandSet.add("AT+" + getStr("customCommandEt4"))
                }
            }
        }
        if (getStr("customCommandEt5", "").isNotEmpty()) {
            if (custom_switch5.isOpened) {
                if (getStr("customParameterEt5", "").isNotEmpty()) {
                    commandSet.add("AT+" + getStr("customCommandEt5") + "=" + getStr("customParameterEt5"))
                    commandSet.add("AT+" + getStr("customCommandEt5"))
                } else {
                    commandSet.add("AT+" + getStr("customCommandEt5"))
                }
            }
        }
        if (getStr("customCommandEt6", "").isNotEmpty()) {
            if (custom_switch6.isOpened) {
                if (getStr("customParameterEt6", "").isNotEmpty()) {
                    commandSet.add("AT+" + getStr("customCommandEt6") + "=" + getStr("customParameterEt6"))
                    commandSet.add("AT+" + getStr("customCommandEt6"))
                } else {
                    commandSet.add("AT+" + getStr("customCommandEt6"))
                }
            }
        }
        if (getStr("customCommandEt7", "").isNotEmpty()) {
            if (custom_switch7.isOpened) {
                if (getStr("customParameterEt7", "").isNotEmpty()) {
                    commandSet.add("AT+" + getStr("customCommandEt7") + "=" + getStr("customParameterEt7"))
                    commandSet.add("AT+" + getStr("customCommandEt7"))
                } else {
                    commandSet.add("AT+" + getStr("customCommandEt7"))
                }
            }
        }
        if (getStr("customCommandEt8", "").isNotEmpty()) {
            if (custom_switch8.isOpened) {
                if (getStr("customParameterEt8", "").isNotEmpty()) {
                    commandSet.add("AT+" + getStr("customCommandEt8") + "=" + getStr("customParameterEt8"))
                    commandSet.add("AT+" + getStr("customCommandEt8"))
                } else {
                    commandSet.add("AT+" + getStr("customCommandEt8"))
                }
            }
        }
        if (getStr("customCommandEt9", "").isNotEmpty()) {
            if (custom_switch9.isOpened) {
                if (getStr("customParameterEt9", "").isNotEmpty()) {
                    commandSet.add("AT+" + getStr("customCommandEt9") + "=" + getStr("customParameterEt9"))
                    commandSet.add("AT+" + getStr("customCommandEt9"))
                } else {
                    commandSet.add("AT+" + getStr("customCommandEt9"))
                }
            }
        }
        if (commandSet.size == 0) {
            ToastUtil.show(this, getString(R.string.commandNull))
        }

        saveOrderedStringSet(this,commandSet)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            saveCommand()
            MainActivity.activityStart(this, 1)
            finish()
            false
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun update(i: Int) {
        MsgLogger.e("数据 => $i")
        toolbarButton.isEnabled = i != 0
    }
}