package com.xxx.nightmodel.attr;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

/**
 * Created by like on 16/7/20.
 */
public abstract class AttrType {
    protected static final String DEFTYPE_DRAWABLE = "drawable";
    protected static final String DEFTYPE_COLOR = "color";

    String attrType;

    public AttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getAttrType() {
        return attrType;
    }

    public abstract void apply(View view, String resName, String resType);

    public int getResourceId(String attrValue) {
        return Integer.parseInt(attrValue.substring(1));
    }

    public String getIntResourceName(int resId, Resources resources) {
        if (resId == 0) return null;
        return resources.getResourceEntryName(resId);
    }

    public String getIntResourceType(int resId, Resources resources) {
        if (resId == 0) return null;
        return resources.getResourceTypeName(resId);
    }

    protected Drawable getDrawable(Context context, String resName, String resType) {
        Drawable drawable = null;
        Resources resources = context.getResources();
        try {
            String packageName = context.getPackageName();
            int identifier = resources.getIdentifier(resName, resType, packageName);
            drawable = ResourcesCompat.getDrawable(resources,identifier, context.getTheme());
        } catch (Resources.NotFoundException ignored) {
        }
        return drawable;
    }
}
