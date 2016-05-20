package com.mirrorchannelth.internship.util;

import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.mirrorchannelth.internship.R;

/**
 * Created by boss on 5/21/16.
 */
public class AnimationUtil {

    public static AnimationSet animationSlideUp(Context context){
        Animation slideup = AnimationUtils.loadAnimation(context, R.anim.from_bottom);
        slideup.setInterpolator(new AccelerateDecelerateInterpolator());
        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        AnimationSet animationSetIn = new AnimationSet(false);
        animationSetIn.addAnimation(fadeIn);
        animationSetIn.addAnimation(slideup);
        return animationSetIn;
    }

}
