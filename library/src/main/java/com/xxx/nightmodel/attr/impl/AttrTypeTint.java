package com.xxx.nightmodel.attr.impl;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.xxx.nightmodel.attr.AttrType;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.ImageViewCompat;


/**
 * Created by like on 2017/7/25.
 */

public class AttrTypeTint extends AttrType {

    public AttrTypeTint() {
        super("tint");
    }

    @Override
    public void apply(View view, String resName, String resType) {
        if (TextUtils.isEmpty(resName)) return;
        Resources mResources = view.getResources();
        int resId = mResources.getIdentifier(resName, DEFTYPE_COLOR, view.getContext().getPackageName());
        if (0 != resId) {
            ColorStateList colorStateList = ResourcesCompat.
                    getColorStateList(mResources, resId, null);

            if (colorStateList == null) {
                ((ImageView) view).setColorFilter(mResources.getColor(resId));
            } else {
                ImageViewCompat.setImageTintList(((ImageView) view), colorStateList);
            }
        }
    }
}
