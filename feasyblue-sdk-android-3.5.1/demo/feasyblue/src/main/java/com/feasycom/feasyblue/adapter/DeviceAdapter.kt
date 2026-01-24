package com.feasycom.feasyblue.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feasycom.common.bean.FscDevice
import com.feasycom.common.utils.Constant
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import kotlinx.android.synthetic.main.device_item.view.*

class DeviceAdapter(private val devices: List<FscDevice>): RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    var mOnClickListener: ((position: Int) -> Unit)? = null

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false))
    }

    @SuppressLint("SetTextI18n", "MissingPermission")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = devices[position]
        holder.itemView.deviceName.text =
            if (device.device.bondState == BluetoothDevice.BOND_BONDED /** && device.mode == Constant.SPP_MODE **/) {
                "${holder.itemView.resources.getString(R.string.paired)} ${if(device.device.name != null){
                    if(device.device.name.length > 29){
                        device.device.name.substring(0,29)
                    }else{
                        device.device.name
                    }
                }else{
                    "unknown"
                }}"
            }else {
                if(device.device.name != null){
                    if(device.device.name.length > 29){
                        device.device.name.substring(0,29)
                    }else{
                        device.device.name
                    }
                }else{
                    "unknown"
                }
            }
        holder.itemView.deviceAddress.text = device.address
        holder.itemView.deviceMode.text = device.mode
        holder.itemView.deviceRssi.text = "${holder.itemView.resources.getString(R.string.rssi)} (${device.rssi})"
        holder.itemView.deviceRssiProgressBar.progress = 100 + device.rssi
        holder.itemView.setOnClickListener {
            mOnClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int{
        return devices.size
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    companion object{
        private const val TAG = "DeviceAdapter"
    }
}