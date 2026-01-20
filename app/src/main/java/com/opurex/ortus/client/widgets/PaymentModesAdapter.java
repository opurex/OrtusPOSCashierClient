package com.opurex.ortus.client.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.opurex.ortus.client.R;
import com.opurex.ortus.client.models.PaymentMode;

import java.util.List;

public class PaymentModesAdapter extends RecyclerView.Adapter<PaymentModesAdapter.PaymentModeViewHolder> {
    
    private List<PaymentMode> paymentModes;
    private Context context;
    private int selectedIndex = -1; // Track selected item
    private OnPaymentModeClickListener listener;
    
    public interface OnPaymentModeClickListener {
        void onPaymentModeClick(PaymentMode mode, int position);
    }
    
    public PaymentModesAdapter(Context context, List<PaymentMode> paymentModes) {
        this.context = context;
        this.paymentModes = paymentModes;
    }
    
    public void setOnPaymentModeClickListener(OnPaymentModeClickListener listener) {
        this.listener = listener;
    }
    
    public void setSelectedIndex(int index) {
        int oldSelectedIndex = this.selectedIndex;
        this.selectedIndex = index;
        if (oldSelectedIndex != -1) {
            notifyItemChanged(oldSelectedIndex);
        }
        if (selectedIndex != -1) {
            notifyItemChanged(selectedIndex);
        }
    }
    
    @NonNull
    @Override
    public PaymentModeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.payment_mode_item_material, parent, false);
        return new PaymentModeViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PaymentModeViewHolder holder, int position) {
        PaymentMode mode = paymentModes.get(position);
        holder.bind(mode, position);
        
        // Highlight selected item
        if (position == selectedIndex) {
            holder.itemView.setBackgroundColor(context.getColor(R.color.colorPrimary));
            holder.modeName.setTextColor(context.getColor(R.color.colorOnPrimary));
        } else {
            holder.itemView.setBackgroundColor(context.getColor(R.color.colorSurface));
            holder.modeName.setTextColor(context.getColor(R.color.colorOnSurface));
        }
    }
    
    @Override
    public int getItemCount() {
        return paymentModes.size();
    }
    
    public class PaymentModeViewHolder extends RecyclerView.ViewHolder {
        ImageView modeIcon;
        TextView modeName;
        
        public PaymentModeViewHolder(@NonNull View itemView) {
            super(itemView);
            modeIcon = itemView.findViewById(R.id.mode_icon);
            modeName = itemView.findViewById(R.id.mode_name);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onPaymentModeClick(paymentModes.get(position), position);
                    setSelectedIndex(position); // Update selection
                }
            });
        }
        
        public void bind(PaymentMode mode, int position) {
            modeName.setText(mode.getLabel());
            // Set icon based on payment mode type - you might need to customize this
            // For now, using a generic payment icon
            modeIcon.setImageResource(R.drawable.ic_payment); // You'll need to add this drawable
        }
    }
}