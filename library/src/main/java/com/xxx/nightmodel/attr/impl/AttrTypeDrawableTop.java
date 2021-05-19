package com.xxx.nightmodel.attr.impl;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xxx.nightmodel.attr.AttrType;


/**
 * Created by like on 2017/7/25.
 */

public class AttrTypeDrawableTop extends AttrType {

    public AttrTypeDrawableTop() {
        super("drawableTop");
    }

    @Override
    public void apply(View view, String resName, String resType) {
        if (TextUtils.isEmpty(resName)) return;

        Drawable drawable = getDrawable(view.getContext(), resName, resType);
        if (view instanceof TextView){
            TextView tv= (TextView) view;
            Drawable[] drawables = tv.getCompoundDrawablesRelative();
            Drawable topDrawable = drawables[1];
            if (null!=topDrawable&&drawable!=null){
                drawable.setBounds(topDrawable.getBounds());
            }
            tv.setCompoundDrawablesRelative(drawables[0],drawable,drawables[2],drawables[3]);
        }

    }

}
