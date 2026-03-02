package me.goldze.mvvmhabit.entity;

import android.view.MotionEvent;
import android.view.View;

/**
 * Description：传递onTouchListener的MotionEvent和View
 *
 * @author Gej
 * @date 2024/3/5
 */
public class MotionEventView {
    private MotionEvent event;
    private View view;

    public MotionEventView(MotionEvent event, View view) {
        this.event = event;
        this.view = view;
    }

    public MotionEvent getEvent() {
        return event;
    }

    public void setEvent(MotionEvent event) {
        this.event = event;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
