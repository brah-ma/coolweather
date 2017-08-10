package com.example.brahma.coolweather.util;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;

/**
 * Created by 10750 on 2017/8/3.
 */

public class MyDrawerLayout extends DrawerLayout {

    public MyDrawerLayout(Context context) {
        super(context, null);
    }

    public MyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//-2147482892,-2147481812
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);//756
        int widthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);//1073742580

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);//1836
        int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);//1073743660

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
