package com.xxx.nightmodel.attr;

import android.view.View;

/**
 * Created by like on 16/7/20.
 */
public class Attr {
    String resName;
    String resType;
    AttrType attrType;

    public Attr(String resName, String resType, AttrType attrType) {
        this.resName = resName;
        this.resType = resType;
        this.attrType = attrType;
    }

    public void apply(View view) {
        attrType.apply(view, resName, resType);
    }
}
