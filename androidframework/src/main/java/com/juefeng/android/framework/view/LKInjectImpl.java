package com.juefeng.android.framework.view;

import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.juefeng.android.framework.LKInject;
import com.juefeng.android.framework.common.util.LogUtil;
import com.juefeng.android.framework.view.annotation.LKContentView;
import com.juefeng.android.framework.view.annotation.LKInjectView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/27
 * Time: 15:10
 * Description:implements LKInjectImpl
 */
public class LKInjectImpl implements LKInject {


    private static final HashSet<Class<?>> IGNORED = new HashSet<Class<?>>();

    static {
        IGNORED.add(Object.class);
        IGNORED.add(Activity.class);
        IGNORED.add(android.app.Fragment.class);
        try {
            IGNORED.add(Class.forName("android.support.v4.app.Fragment"));
            IGNORED.add(Class.forName("android.support.v4.app.FragmentActivity"));
        } catch (Throwable ignored) {
        }
    }

    private static final Object lock = new Object();

    private static LKInject lkInject;

    public static LKInject registerInstance() {
        if (lkInject == null) {
            synchronized (lock) {
                if (lkInject == null) {
                    lkInject = new LKInjectImpl();
                }
            }
        }
        return lkInject;
    }


    @Override
    public void inject(View view) {
        injectObject(view, view.getClass(), new ViewFinder(view));
    }

    @Override
    public void inject(Activity activity) {
        //获取Activity的ContentView的注解
        Class<?> handlerType = activity.getClass();
        try {
            LKContentView contentView = findContentView(handlerType);
            if (contentView != null) {
                int viewId = contentView.value();
                if (viewId > 0) {
                    Method setContentViewMethod = handlerType.getMethod("setContentView", int.class);
                    setContentViewMethod.invoke(activity, viewId);
                }
            }
        } catch (Throwable ex) {
            LogUtil.e(ex.getMessage(), ex);
        }

        injectObject(activity, handlerType, new ViewFinder(activity));

    }

    @Override
    public void inject(Object handler, View view) {
        injectObject(view, view.getClass(), new ViewFinder(view));
    }

    @Override
    public View inject(Object fragment, LayoutInflater inflater, ViewGroup container) {
        // inject ContentView
        View view = null;
        Class<?> handlerType = fragment.getClass();
        try {
            LKContentView contentView = findContentView(handlerType);
            if (contentView != null) {
                int viewId = contentView.value();
                if (viewId > 0) {
                    view = inflater.inflate(viewId, container, false);
                }
            }
        } catch (Throwable ex) {
            LogUtil.e(ex.getMessage(), ex);
        }

        // inject res
        injectObject(fragment, handlerType, new ViewFinder(view));

        return view;
    }

    /**
     * 从父类获取注解View
     */
    private static LKContentView findContentView(Class<?> thisCls) {
        if (thisCls == null || IGNORED.contains(thisCls)) {
            return null;
        }
        LKContentView contentView = thisCls.getAnnotation(LKContentView.class);
        if (contentView == null) {
            return findContentView(thisCls.getSuperclass());
        }
        return contentView;
    }

    /**
     * bind view activity
     *
     * @param activity
     */
    private static void setContentView(Activity activity) {
        // judge class whether have LKContentView Annotation
        if (activity.getClass().isAnnotationPresent(LKContentView.class)) {
            LKContentView contentView = activity.getClass().getAnnotation(
                    LKContentView.class);
            int layoutResID = contentView.value();
            activity.setContentView(layoutResID);
        }
    }


    /**
     * bind view widget
     *
     * @param activity
     */
    private static void findView(Activity activity) {
        // get all field of activity
        Field[] fields = activity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            // judge field whether have LKInjectView Annotation
            if (field.isAnnotationPresent(com.juefeng.android.framework.view.annotation.LKInjectView.class)) {
                com.juefeng.android.framework.view.annotation.LKInjectView vi = field.getAnnotation(com.juefeng.android.framework.view.annotation.LKInjectView.class);
                int resourceId = vi.value();
                try {
                    field.set(activity, activity.findViewById(resourceId));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static void injectObject(Object handler, Class<?> handlerType, ViewFinder finder) {

        if (handlerType == null || IGNORED.contains(handlerType)) {
            return;
        }

        // 从父类到子类递归
        injectObject(handler, handlerType.getSuperclass(), finder);

        // inject view
        Field[] fields = handlerType.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {

                Class<?> fieldType = field.getType();
                if (
                /* Don't inject static field */     Modifier.isStatic(field.getModifiers()) ||
                /* Don't inject final field */    Modifier.isFinal(field.getModifiers()) ||
                /* Don't inject base type field */  fieldType.isPrimitive() ||
                /* Don't inject array field */  fieldType.isArray()) {
                    continue;
                }

                LKInjectView viewInject = field.getAnnotation(LKInjectView.class);
                if (viewInject != null) {
                    try {
                        View view = finder.findViewById(viewInject.value());
                        if (view != null) {
                            field.setAccessible(true);
                            field.set(handler, view);
                        } else {
                            throw new RuntimeException("Invalid @ViewInject for "
                                    + handlerType.getSimpleName() + "." + field.getName());
                        }
                    } catch (Throwable ex) {
                        LogUtil.e(ex.getMessage(), ex);
                    }
                }
            }
        } // end inject view

    }


}
