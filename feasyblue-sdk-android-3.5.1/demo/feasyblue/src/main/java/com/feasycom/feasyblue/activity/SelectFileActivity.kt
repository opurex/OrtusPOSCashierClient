package com.feasycom.feasyblue.activity

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.adapter.SelectFileSizeAdapter
import com.feasycom.feasyblue.decoration.DividerGridItemDecoration
import com.feasycom.feasyblue.utils.ToastUtil
import com.feasycom.feasyblue.utils.isKitkatOrAbove
import kotlinx.android.synthetic.main.activity_select_file.*

const val FILE_SIZE = 2000
const val FILE_PATH = 2001

class SelectFileActivity : BaseActivity() {

    lateinit var mFileAdapter: SelectFileSizeAdapter
    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbarTitle.text = getString(R.string.selectFile)
        toolbar.setNavigationOnClickListener { finish() }

        recyclerView?.let {
            it.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
            it.adapter = SelectFileSizeAdapter().apply {
                mFileAdapter = this
                onClickListener = { size, data ->
                    returnFileSize(size, data)
                }
            }
            it.addItemDecoration(DividerGridItemDecoration(this))
        } ?: let {
            MsgLogger.e("initView => 空.")
        }
        // mFileAdapter.notifyDataSetChanged()

        customizeButton.setOnClickListener {
            if (getTestFileSize() <= 0) {
                ToastUtil.show(this, resources.getString(R.string.size_cannot_zero))
            } else {
                returnFileSize(getTestFileSize(), getTestFileSizeStr())
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(edit_tv.windowToken, 0)
            }
        }

        back_btn.setOnClickListener {
            finish()
        }
        clear.setOnClickListener {
            edit_tv.text.clear()
        }
        /*segmented.check(R.id.button1)*/
        photo_btn.setOnClickListener {
            performFileSearch()
        }



        edit_tv.addTextChangedListener {
            it?.let {
                if (it.isNotBlank()) {
                    if (it.toString().toInt() > when (my_group.getSelectRadioButton()) {
                            0 -> {
                                50000000
                            }
                            1 -> {
                                50000
                            }
                            2 -> {
                                50
                            }
                            else -> 50
                        }
                    ) {
                        edit_tv.text = it.delete(it.toString().length - 1, it.toString().length)
                        edit_tv.setSelection(it.toString().length)
                    }
                }
            }
        }

        my_group.onClickListener = {
            if (edit_tv.text.isNotBlank()) {
                when (it) {
                    2 -> {
//                        when {
//                            edit_tv.text.length <= 1 -> {
//                            }
//                            edit_tv.text.subSequence(0 ,2).toString().toInt() > 50 -> {
//                                edit_tv.setText("5")
//                            }
//                            else -> {
//                                edit_tv.setText("50")
//                            }
//                        }
                        if (edit_tv.text.toString().toInt() > 50) {
                            edit_tv.setText("50")
                        }
                        edit_tv.setSelection(edit_tv.text.toString().length)
                    }
                    else -> {}
                }
            }
        }
    }

    override fun getLayout() = R.layout.activity_select_file

    /**
     * Fires an intent to spin up the "file chooser" UI to select a file
     */
    private fun performFileSearch() {
        val intent = if (isKitkatOrAbove()) {
            Intent(Intent.ACTION_OPEN_DOCUMENT)
        } else {
            Intent(Intent.ACTION_GET_CONTENT)
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        // intent.type = "image/*"
        intent.type = "*/*"
        startActivityForResult(
            intent,
            FILE_PATH
        )
    }

    private fun returnFileSize(size: Int, data: String) {
        val intent = Intent()
        intent.putExtra("size", size)
        intent.putExtra("sizeStr", data)
        setResult(FILE_SIZE, intent)
        finish()
    }

    companion object {
        private const val TAG = "SelectFileActivity"
    }

    private fun getTestFileSize(): Int {
        return if (edit_tv.text.isNotBlank()) {
            when (my_group.getSelectRadioButton()) {
                0 -> {
                    edit_tv.text.toString().toInt()
                }
                1 -> {
                    edit_tv.text.toString().toInt() * 1000
                }
                2 -> {
                    edit_tv.text.toString().toInt() * 1000000
                }
                else -> 0
            }
        } else {
            when (my_group.getSelectRadioButton()) {
                0 -> {
                    50
                }
                1 -> {
                    50 * 1000
                }
                2 -> {
                    50 * 1000 * 1000
                }
                else -> 0
            }
        }
    }

    private fun getTestFileSizeStr(): String {
        return if (edit_tv.text.isNotBlank()) {
            when (my_group.getSelectRadioButton()) {
                0 -> {
                    "${edit_tv.text} B"
                }
                1 -> {
                    "${edit_tv.text} KB"
                }
                2 -> {
                    "${edit_tv.text} MB"
                }
                else -> "0 B"
            }
        } else {
            when (my_group.getSelectRadioButton()) {
                0 -> {
                    50
                    "50 B"
                }
                1 -> {
                    50 * 1000
                    "50 KB"
                }
                2 -> {
                    50 * 1000 * 1000
                    "50 MB"
                }
                else -> "0 B"
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            setResult(FILE_PATH, data)
            finish()
        }
    }
}