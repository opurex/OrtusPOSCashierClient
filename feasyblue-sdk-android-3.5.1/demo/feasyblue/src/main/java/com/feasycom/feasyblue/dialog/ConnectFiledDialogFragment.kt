package com.feasycom.feasyblue.dialog

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.feasycom.feasyblue.R
import kotlinx.android.synthetic.main.connect_failed_dialog.*
import kotlinx.android.synthetic.main.first_dialog.contentTextView

class ConnectFiledDialogFragment: DialogFragment() {

    var onAgree: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setCanceledOnTouchOutside(false);
        dialog?.setOnKeyListener { dialog, keyCode, event ->
            requireActivity().finish()
            keyCode == KeyEvent.KEYCODE_BACK
        };

        return inflater.inflate(R.layout.connect_failed_dialog, null)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(contentTextView) {
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = ContextCompat.getColor(requireContext(), R.color.transparent)
        }

        agreeConnetFiledTextView.setOnClickListener {
            onAgree?.invoke()
        }
    }


    override fun onResume() {
        val params: ViewGroup.LayoutParams = dialog?.window!!.attributes
        /**
         * remove the system dialog title and border background
         */
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        super.onResume()
    }

    companion object {
        const val TAG = "ConnectFiledDialogFragment"
    }
}