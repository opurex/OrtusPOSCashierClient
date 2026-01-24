/*
    Opurex Android com.opurex.ortus.client
    Copyright (C) Opurex contributors, see the COPYRIGHT file

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.opurex.ortus.client.widgets;

import com.opurex.ortus.client.interfaces.PaymentEditListener;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.data.ImagesData;
import com.opurex.ortus.client.models.Payment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;

public class PaymentItem extends RelativeLayout {

    private Payment payment;
    private PaymentEditListener listener;

    private TextView type;
    private TextView amount;
    private ImageView modeIcon;

    public PaymentItem(Context context, Payment payment) {
        super(context);
        Resources r = context.getResources();
        int width = r.getDimensionPixelSize(R.dimen.bigBtnWidth);
        int height = r.getDimensionPixelSize(R.dimen.bigBtnHeight);
        LayoutInflater.from(context).inflate(R.layout.payment_item,
                                                this,
                                                true);
        this.type = (TextView) this.findViewById(R.id.payment_type);
        this.amount = (TextView) this.findViewById(R.id.payment_amount);
        this.modeIcon = (ImageView) this.findViewById(R.id.payment_mode_icon);

        View delete = this.findViewById(R.id.payment_delete);
        delete.setOnClickListener(new View.OnClickListener() {
                @Override
				public void onClick(View v) {
                    delete();
                }
            });

        this.reuse(payment, context);
    }

    public void reuse(Payment p, Context ctx) {
        this.payment = p;
        this.type.setText(this.payment.getMode().getLabel());
        this.amount.setText(String.format("%.2f", this.payment.getAmount()));

        // Load payment mode image if available
        if (this.payment.getMode().hasImage()) {
            try {
                Bitmap paymentImage = ImagesData.getPaymentModeImage(this.payment.getMode().getId());
                if (paymentImage != null) {
                    this.modeIcon.setImageBitmap(paymentImage);
                    this.modeIcon.setVisibility(View.VISIBLE);
                } else {
                    // Hide the icon if image not found locally
                    this.modeIcon.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                // Log the error and hide the icon
                android.util.Log.e("PaymentItem", "Error loading payment mode image for mode ID: " + this.payment.getMode().getId(), e);
                this.modeIcon.setVisibility(View.GONE);
            }
        } else {
            // Hide the icon if no image is associated with this payment mode
            this.modeIcon.setVisibility(View.GONE);
        }
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setEditListener(PaymentEditListener l) {
        this.listener = l;
    }

    public void delete() {
        if (this.listener != null) {
            this.listener.deletePayment(this.payment);
        }
    }
}