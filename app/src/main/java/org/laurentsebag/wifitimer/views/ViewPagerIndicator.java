package org.laurentsebag.wifitimer.views;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;

public class ViewPagerIndicator extends LinearLayoutCompat implements ViewPager.OnPageChangeListener {

    private static final String TAG = ViewPagerIndicator.class.getSimpleName();
    private ViewPager viewPager;

    public ViewPagerIndicator(Context context) {
        super(context);
        init();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public void setIndicatorWithViewPager(final ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        viewPager.getAdapter().registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                int count = viewPager.getAdapter().getCount();
                Log.d(TAG, "Adapter data set changed " + count);
                setupIndicators(count);
            }
        });
    }

    private void setupIndicators(int pagesCount) {
        // TODO for loop to add drawables to layout
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // TODO fade away indicator becoming non selected
    }

    @Override
    public void onPageSelected(int position) {
        // TODO set disabled state on previously selected item
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
