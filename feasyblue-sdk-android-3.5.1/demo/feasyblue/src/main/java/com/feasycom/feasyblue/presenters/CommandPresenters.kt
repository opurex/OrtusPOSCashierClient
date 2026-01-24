package com.feasycom.feasyblue.presenters

import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.interfaces.ICommandCallback
import com.feasycom.feasyblue.interfaces.ICommandPresenters

object CommandPresenters: ICommandPresenters{

    private var checkNumber = 0

    var mICommandCallback: ICommandCallback? = null
        set(value) {
            checkNumber = 0
            MsgLogger.e("TAG","checkNumber => $checkNumber")
            field = value
        }


    override fun plus() {
        checkNumber ++
        mICommandCallback?.update(checkNumber)
    }

    override fun minus() {
        checkNumber --
        mICommandCallback?.update(checkNumber)
    }

}