package com.xxx.nightmodel;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 管理夜间模式监听的回调
 *
 * @author like
 */
class ModelChangeManager {

    final Set<WeakReference<ModelChangeListener>> mReferences = new LinkedHashSet<>();

    void addListener(ModelChangeListener listener) {
        mReferences.add(new WeakReference<>(listener));
    }

    void removeListener(ModelChangeListener listener) {
        Iterator<WeakReference<ModelChangeListener>> iterator = mReferences.iterator();

        while (iterator.hasNext()) {
            WeakReference<ModelChangeListener> reference = iterator.next();
            if (reference.get() == listener) {
                iterator.remove();
            }
        }
    }

    void notifyChange(final boolean isNight) {
        Iterator<WeakReference<ModelChangeListener>> iterator = mReferences.iterator();
        while (iterator.hasNext()) {
            WeakReference<ModelChangeListener> weakReference = iterator.next();
            if (weakReference.get() != null) {
                weakReference.get().onNightModeChanged(isNight);
            } else {
                iterator.remove();
            }
        }

//        Looper.myQueue().addIdleHandler(new NotifyHandler(listeners, isNight));
    }

//    static class NotifyHandler implements MessageQueue.IdleHandler {
//
//        List<ModelChangeListener> listeners;
//        boolean isNight;
//
//        NotifyHandler(List<ModelChangeListener> listeners, boolean isNight) {
//            this.listeners = listeners;
//            this.isNight = isNight;
//        }
//
//        @Override
//        public boolean queueIdle() {
//            if (listeners != null) {
//                for (ModelChangeListener listener:listeners) {
//                    listener.onNightModeChanged(isNight);
//                }
//            }
//            return false;
//        }
//    }

    static class Holder {
        static ModelChangeManager INSTANCE = new ModelChangeManager();
    }

    static ModelChangeManager getInstance() {
        return Holder.INSTANCE;
    }
}
