package com.xxx.nightmodel.attr.impl;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.xxx.nightmodel.attr.AttrType;


/**
 * Created by like on 2017/7/25.
 */

public class AttrTypeImageSrc extends AttrType {
    public AttrTypeImageSrc() {
        super("src");
    }

    @Override
    public void apply(View view, String resName,String resType) {
        if (TextUtils.isEmpty(resName)) return;
        Drawable drawable = getDrawable(view.getContext(), resName,resType);
        if (view instanceof ImageView) {
            if (drawable == null) return;
            ((ImageView) view).setImageDrawable(drawable);
        }
    }

}
