package com.xxx.nightmodel.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.xxx.nightmodel.attr.Attr;
import com.xxx.nightmodel.attr.AttrType;
import com.xxx.nightmodel.attr.impl.AttrTypeBackground;
import com.xxx.nightmodel.attr.impl.AttrTypeButton;
import com.xxx.nightmodel.attr.impl.AttrTypeDrawableBottom;
import com.xxx.nightmodel.attr.impl.AttrTypeDrawableEnd;
import com.xxx.nightmodel.attr.impl.AttrTypeDrawableStart;
import com.xxx.nightmodel.attr.impl.AttrTypeDrawableTop;
import com.xxx.nightmodel.attr.impl.AttrTypeImageSrc;
import com.xxx.nightmodel.attr.impl.AttrTypeImageSrcCompat;
import com.xxx.nightmodel.attr.impl.AttrTypeProgressDrawable;
import com.xxx.nightmodel.attr.impl.AttrTypeTextColor;
import com.xxx.nightmodel.attr.impl.AttrTypeTint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by like on 16/7/21.
 */
public class AttrUtils {

    private static final HashMap<String, AttrType> attrTypeHashMap = new HashMap<>();

    static {
        AttrType background = new AttrTypeBackground();
        attrTypeHashMap.put(background.getAttrType(), background);

        AttrType imageSrc = new AttrTypeImageSrc();
        attrTypeHashMap.put(imageSrc.getAttrType(), imageSrc);

        AttrType imageSrcCompat = new AttrTypeImageSrcCompat();
        attrTypeHashMap.put(imageSrcCompat.getAttrType(), imageSrcCompat);

        AttrType progressDrawable = new AttrTypeProgressDrawable();
        attrTypeHashMap.put(progressDrawable.getAttrType(), progressDrawable);

        AttrType textColor = new AttrTypeTextColor();
        attrTypeHashMap.put(textColor.getAttrType(), textColor);

        AttrType tint = new AttrTypeTint();
        attrTypeHashMap.put(tint.getAttrType(), tint);

        AttrType drawableStart = new AttrTypeDrawableStart();
        attrTypeHashMap.put(drawableStart.getAttrType(), drawableStart);

        AttrType drawableTop = new AttrTypeDrawableTop();
        attrTypeHashMap.put(drawableTop.getAttrType(), drawableTop);

        AttrType drawableEnd = new AttrTypeDrawableEnd();
        attrTypeHashMap.put(drawableEnd.getAttrType(), drawableEnd);

        AttrType drawableBottom = new AttrTypeDrawableBottom();
        attrTypeHashMap.put(drawableBottom.getAttrType(), drawableBottom);

        AttrType button = new AttrTypeButton();
        attrTypeHashMap.put(button.getAttrType(), button);



    }



    public static void addExpandAttrType(AttrType... attrTypes) {
        if (attrTypes == null || attrTypes.length == 0) return;
        for (AttrType attrType : attrTypes) {
            attrTypeHashMap.put(attrType.getAttrType(), attrType);
        }
    }

    private static AttrType getSupportAttrType(String attrName) {
        return attrTypeHashMap.get(attrName);
    }

    public static List<Attr> getNightModelAttr(Object[] args, Activity activity) {
        Resources resources = activity.getResources();
        Resources.Theme theme = activity.getTheme();

        List<Attr> nightModelAttrs = new ArrayList<>();
        if (args != null && args.length > 0) {
            for (Object obj : args) {
                if (!(obj instanceof AttributeSet)) {
                    continue;
                }
                AttributeSet attrs = (AttributeSet) obj;
                for (int i = 0; i < attrs.getAttributeCount(); i++) {
                    String attrName = attrs.getAttributeName(i);
                    String attrValue = attrs.getAttributeValue(i);

                    if (TextUtils.equals(attrName, "style")) {
                        int id = Integer.parseInt(attrValue.substring(1));

                        int[] parseAttrs = new int[]{
                                android.R.attr.background,
                                android.R.attr.textColor
                        };

                        TypedArray typedArray = theme.obtainStyledAttributes(attrs, parseAttrs, 0, id);
                        for (int j = 0; j < typedArray.length(); j++) {
                            int resourceId = typedArray.getResourceId(j, -1);
                            if (resourceId < 0) continue;
                            String entryName = resources.getResourceEntryName(resourceId);
                            String resourceTypeName = resources.getResourceTypeName(resourceId);
                            AttrType attrType = getSupportAttrType(j == 0 ? "background" : "textColor");

                            Attr attr = new Attr(entryName,resourceTypeName, attrType);
                            nightModelAttrs.add(attr);
                        }
                        typedArray.recycle();

                        continue;
                    }

                    AttrType attrType = getSupportAttrType(attrName);
                    if (attrType == null) continue;

                    if (attrValue.startsWith("@")) {
                        int resId = attrType.getResourceId(attrValue);
                        String resourceName = attrType.getIntResourceName(resId, resources);
                        String resourceType = attrType.getIntResourceType(resId, resources);
                        Attr attr = new Attr(resourceName,resourceType, attrType);
                        nightModelAttrs.add(attr);
                    }
                }
            }
        }
        return nightModelAttrs;
    }
}
