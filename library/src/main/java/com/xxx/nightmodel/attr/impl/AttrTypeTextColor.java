package com.xxx.nightmodel.attr.impl;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xxx.nightmodel.attr.AttrType;


/**
 * Created by like on 2017/7/25.
 */

public class AttrTypeTextColor extends AttrType {

    public AttrTypeTextColor() {
        super("textColor");
    }


    @Override
    public void apply(View view, String resName, String resType) {
        if (TextUtils.isEmpty(resName)) return;
        Resources mResources = view.getResources();
        int resId = mResources.getIdentifier(resName, resType, view.getContext().getPackageName());
        if (0 != resId) {
            @SuppressLint("UseCompatLoadingForColorStateLists") ColorStateList colorList = mResources.getColorStateList(resId);

            if (view instanceof TextView) {
                ((TextView) view).setTextColor(colorList);
            }
        }
    }
}
