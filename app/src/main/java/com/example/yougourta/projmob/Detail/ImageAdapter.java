package com.example.yougourta.projmob.Detail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.yougourta.projmob.Classes.Logement;
import com.example.yougourta.projmob.R;

/**
 * Created by Yougourta on 6/26/17.
 */

public class ImageAdapter extends PagerAdapter {
    Context context;
    Logement logement;

    ImageAdapter(Context context, Logement logement){
        this.context=context;
        this.logement = logement;
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        //int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        //imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load("http://192.168.43.76:8888/MAMP/images/ProjMob/"+String.valueOf(logement.getIdLogement())+"/"+parametreImage()+"/"+logement.getMainImg()).skipMemoryCache(true).into(imageView);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    public String parametreImage()
    {
        return "drawable-xxhdpi";
    }
}