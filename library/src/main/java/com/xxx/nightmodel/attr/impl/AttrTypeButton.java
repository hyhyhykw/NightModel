package com.xxx.nightmodel.attr.impl;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.xxx.nightmodel.attr.AttrType;


/**
 * Created by like on 2017/7/25.
 */

public class AttrTypeButton extends AttrType {

    public AttrTypeButton() {
        super("button");
    }

    @Override
    public void apply(View view, String resName, String resType) {
        if (TextUtils.isEmpty(resName)) return;

        Drawable drawable = getDrawable(view.getContext(), resName, resType);
        if (drawable == null) return;
        if (view instanceof CompoundButton) {
            CompoundButton compoundButton = (CompoundButton) view;
            compoundButton.setButtonDrawable(drawable);
        }
    }

}
