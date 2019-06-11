package com.myapp.bottombar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

class CustomPagerAdapter extends PagerAdapter {

  private List<Integer> mPhotoUrl;
  Context mContext;
  LayoutInflater mLayoutInflater;

  public CustomPagerAdapter(Context context, List<Integer> photosUrl) {
    mContext = context;
    mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mPhotoUrl = photosUrl;
  }

  @Override
  public int getCount() {
    return mPhotoUrl.size();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == ((LinearLayout) object);
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

    ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_slider);
    imageView.setImageResource(mPhotoUrl.get(position));

    container.addView(itemView);

    return itemView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((LinearLayout) object);
  }
}