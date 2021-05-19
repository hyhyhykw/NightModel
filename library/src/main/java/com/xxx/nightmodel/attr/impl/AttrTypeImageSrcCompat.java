package com.xxx.nightmodel.attr.impl;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.xxx.nightmodel.attr.AttrType;

import androidx.appcompat.content.res.AppCompatResources;


/**
 * Created by like on 2017/7/25.
 */

public class AttrTypeImageSrcCompat extends AttrType {
    public AttrTypeImageSrcCompat() {
        super("srcCompat");
    }

    @Override
    public void apply(View view, String resName,String resType) {
        if (TextUtils.isEmpty(resName)) return;
        if (view instanceof ImageView) {
            Drawable drawable;
            if (((ImageView) view).getDrawable() != null
                    && ((ImageView) view).getDrawable().getClass().getName().toLowerCase().contains("vector")) {
                int resId = view.getResources().getIdentifier(resName, DEFTYPE_DRAWABLE, view.getContext().getPackageName());
                drawable = AppCompatResources.getDrawable(view.getContext(), resId);
            } else {
                drawable = getDrawable(view.getContext(), resName,resType);
            }
            if (drawable == null) return;
            ((ImageView) view).setImageDrawable(drawable);
        }
    }

}
