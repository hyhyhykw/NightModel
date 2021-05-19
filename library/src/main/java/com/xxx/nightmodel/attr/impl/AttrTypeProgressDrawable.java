package com.xxx.nightmodel.attr.impl;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.xxx.nightmodel.attr.AttrType;


/**
 * Created by like on 2017/7/25.
 */

public class AttrTypeProgressDrawable extends AttrType {
    public AttrTypeProgressDrawable() {
        super("progressDrawable");
    }


    @Override
    public void apply(View view, String resName, String resType) {
                if (TextUtils.isEmpty(resName)) return;
        Drawable drawable = getDrawable(view.getContext(), resName,resType);
        if (drawable == null) return;
        ((ProgressBar)view).setProgressDrawable(drawable);
    }
}
