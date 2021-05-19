package com.xxx.nightmodel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import com.xxx.nightmodel.attr.Attr;
import com.xxx.nightmodel.attr.AttrType;
import com.xxx.nightmodel.attr.AttrView;
import com.xxx.nightmodel.utils.AttrUtils;
import com.xxx.nightmodel.utils.DayNightUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.collection.ArrayMap;
import androidx.collection.SparseArrayCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.LayoutInflaterFactory;

/**
 * Created by like on 16/7/20.
 */
public class NightModelManager {

    private boolean modelChanged = false;

    private final SparseArrayCompat<List<AttrView>> attrViewMaps = new SparseArrayCompat<>();

    private static final Map<String, Constructor<? extends View>> sConstructorMap
            = new ArrayMap<>();
    private final Object[] mConstructorArgs = new Object[2];
    private static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    /**
     * ths method should be called in Application onCreate method
     */
    public void init() {
        boolean isNightModel = DayNightUtils.isNightModel();
        AppCompatDelegate.setDefaultNightMode(isNightModel ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    /**
     * this method should be called in Activity onCreate method,
     * and before method super.onCreate(savedInstanceState);
     */
    @SuppressWarnings("deprecation")
    public void attach(AppCompatActivity activity) {

        if (activity.getDelegate() instanceof LayoutInflaterFactory) {
            LayoutInflaterFactory originInflaterFactory = (LayoutInflaterFactory) activity.getDelegate();
            LayoutInflaterFactory proxyInflaterFactory = (LayoutInflaterFactory) Proxy.newProxyInstance(
                    originInflaterFactory.getClass().getClassLoader(),
                    new Class[]{LayoutInflaterFactory.class},
                    new InflateFactoryHandler<>(originInflaterFactory, activity));

            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            LayoutInflaterCompat.setFactory(layoutInflater, proxyInflaterFactory);
        } else if (activity.getDelegate() instanceof LayoutInflater.Factory2) {
            LayoutInflater.Factory2 originInflaterFactory = (LayoutInflater.Factory2) activity.getDelegate();
            LayoutInflater.Factory2 proxyInflaterFactory = (LayoutInflater.Factory2) Proxy.newProxyInstance(
                    originInflaterFactory.getClass().getClassLoader(),
                    new Class[]{LayoutInflater.Factory2.class},
                    new InflateFactoryHandler<>(originInflaterFactory, activity));

            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            LayoutInflaterCompat.setFactory2(layoutInflater, proxyInflaterFactory);
        }
    }

    /**
     * this method should be called in Activity onDestroy method
     */
    public void detach(AppCompatActivity activity) {
        attrViewMaps.remove(activity.hashCode());
    }

    public boolean isCurrentNightModel() {
        return DayNightUtils.isNightModel();
    }

    public void applyNight(boolean isNight) {
        AppCompatDelegate.setDefaultNightMode(isNight ? AppCompatDelegate.MODE_NIGHT_YES :
                AppCompatDelegate.MODE_NIGHT_NO);

        applyNewModel();

        DayNightUtils.setNightModel(isNight);
        ModelChangeManager.getInstance().notifyChange(isNight);
    }

    public void addExpandAttrType(AttrType... attrTypes) {
        AttrUtils.addExpandAttrType(attrTypes);
    }

    /**
     * it's used for update StateListDrawable, otherwise StateListDrawable
     * will not be updated.
     */
    private void invokeResources(AppCompatActivity activity) {
        try {
            Field resources = AppCompatActivity.class.getDeclaredField("mResources");
            resources.setAccessible(true);
            resources.set(activity, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void applyNewModel() {
        modelChanged = true;
        int count = attrViewMaps.size();
        for (int i = 0; i < count; i++) {
            List<AttrView> attrViews = attrViewMaps.valueAt(i);
            for (AttrView attrView : attrViews) {
                attrView.apply();
            }
        }
    }

    private static class NightModelManagerHolder {
        static NightModelManager instance = new NightModelManager();
    }

    public static NightModelManager getInstance() {
        return NightModelManagerHolder.instance;
    }

    private class InflateFactoryHandler<T> implements InvocationHandler {
        private final T inflaterFactory;
        private final AppCompatActivity activity;

        public InflateFactoryHandler(T inflaterFactory, AppCompatActivity activity) {
            this.inflaterFactory = inflaterFactory;
            this.activity = activity;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            try {
                result = method.invoke(inflaterFactory, args);
            } catch (Exception ignore) {
            }

            List<Attr> attrs = AttrUtils.getNightModelAttr(args, activity);
            if (attrs.isEmpty())
                return result;

            if (result == null) {
                result = createViewFromTag((Context) args[2], (String) args[1], (AttributeSet) args[3]);
            }

            if (attrs.size() > 0) {
                AttrView attrView = new AttrView((View) result, attrs);
                putAttrView(attrView, activity.hashCode());

                // if model changed once, should apply once, otherwise, the View will use old style
                if (result != null && modelChanged) {
                    attrView.apply();
                }
            }

            return result;
        }

        private void putAttrView(AttrView attrView, int hashCode) {
            List<AttrView> attrViews;
            List<AttrView> list = attrViewMaps.get(hashCode);
            if (list != null) {
                attrViews = list;
            } else {
                attrViews = new ArrayList<>();
            }
            attrViews.add(attrView);
            attrViewMaps.put(hashCode, attrViews);
        }

        private View createViewFromTag(Context context, String name, AttributeSet attrs) {
            if (name.equals("view")) {
                name = attrs.getAttributeValue(null, "class");
            }


            try {
                mConstructorArgs[0] = context;
                mConstructorArgs[1] = attrs;

                if (-1 == name.indexOf('.')) {
                    if (name.equals("View")) {
                        //View
                        return createView(context, name, "android.view.");
                    }

                    if (name.equals("WebView")) {
                        return createView(context, name, "android.webkit.");
                    }

                    // try the android.widget prefix first...
                    return createView(context, name, "android.widget.");
                } else {
                    return createView(context, name, null);
                }
            } catch (Exception e) {
                // We do not want to catch these, lets return null and let the actual LayoutInflater
                // try
                return null;
            } finally {
                // Don't retain references on context.
                mConstructorArgs[0] = null;
                mConstructorArgs[1] = null;
            }
        }

        private View createView(Context context, String name, String prefix)
                throws InflateException {
            Constructor<? extends View> constructor = sConstructorMap.get(name);

            try {
                if (constructor == null) {
                    String className = prefix != null ? (prefix + name) : name;
                    Class<? extends View> clazz = context.getClassLoader().loadClass(
                            className).asSubclass(View.class);

                    constructor = clazz.getConstructor(sConstructorSignature);
                    sConstructorMap.put(name, constructor);
                }
                constructor.setAccessible(true);
                return constructor.newInstance(mConstructorArgs);
            } catch (Exception e) {
                return null;
            }
        }
    }


    /**
     * 添加夜间模式切换监听
     */
    public void addModelChangeListener(ModelChangeListener listener) {
        ModelChangeManager.getInstance().addListener(listener);
    }

    /**
     * 移除夜间模式切换监听
     */
    public void removeModelChangeListener(ModelChangeListener listener) {
        ModelChangeManager.getInstance().removeListener(listener);
    }
}
