package com.opurex.ortus.client.widgets;

import android.content.Context;
import android.widget.Button;

import com.opurex.ortus.client.OrtusPOS;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.models.Place;

/**
 * Created by svirch_n on 23/05/16
 * Last edited at 17:09.
 */
public class PlaceButton extends Button {

    private final Place place;

    public PlaceButton(Context context, Place place) {
        super(context);
        this.place = place;
    }

    public void rate(int width, int height) {
        float widthRate = width / OrtusPOS.getRestaurantMapWidth();
        float heightRate = height / OrtusPOS.getRestaurantMapHeight();
        setX(place.getX() * widthRate);
        setY(place.getY() * heightRate);
        getLayoutParams().width = (width / 8);
        getLayoutParams().height = (height / 8);
    }

    private void setOccupied() {
        this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.avatar_default, 0);
    }

    public void update() {
        if (this.place.isOccupied()) {
            setOccupied();
        }
    }
}
