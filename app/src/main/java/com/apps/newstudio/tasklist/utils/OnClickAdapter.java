package com.apps.newstudio.tasklist.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.apps.newstudio.tasklist.R;

abstract public class OnClickAdapter implements View.OnClickListener {

    private View view_object;
    private int start_or_end;
    private int animation_number;

    /**
     * Action which does some task and is animating View object during this process
     *
     * @param view View object which make action
     */
    @Override
    public void onClick(View view) {
        view_object=view;
        Context context=view_object.getContext();
        Animation animation;
        if(animation_number==1) {
            animation = AnimationUtils.loadAnimation(context, R.anim.animation_click_1);
        }else{
            animation = AnimationUtils.loadAnimation(context, R.anim.animation_click_0);
        }
        if(start_or_end==2) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    myFunc(view_object);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }else{
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    myFunc(view_object);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        view.startAnimation(animation);


    }

    /**
     * Constructor for OnClickAdapter object
     * @param animation_number number of animation type
     * @param start_or_end value which shows when animation will be done
     */
    protected OnClickAdapter(int animation_number, int start_or_end) {
        this.start_or_end=start_or_end;
        this.animation_number=animation_number;
    }

    /**
     * Some task which will be done
     * @param view main View object
     */
    public abstract void myFunc(View view);
}
