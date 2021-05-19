package com.xxx.nightmodel.attr.impl;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xxx.nightmodel.attr.AttrType;


/**
 * Created by like on 2017/7/25.
 */

public class AttrTypeDrawableBottom extends AttrType {

    public AttrTypeDrawableBottom() {
        super("drawableTop");
    }

    @Override
    public void apply(View view, String resName, String resType) {
        if (TextUtils.isEmpty(resName)) return;

        Drawable drawable = getDrawable(view.getContext(), resName, resType);
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            Drawable[] drawables = tv.getCompoundDrawablesRelative();
            Drawable bottomDrawable = drawables[3];
            if (null != bottomDrawable && drawable != null) {
                drawable.setBounds(bottomDrawable.getBounds());
            }
            tv.setCompoundDrawablesRelative(drawables[0], drawables[1], drawables[2], drawable);
        }

    }

}
