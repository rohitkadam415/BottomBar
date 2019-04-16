package com.myapp.bottombar;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
  private final List<Fragment> mFragmentList = new ArrayList<>();
  private final List<String> mFragmentTitleList = new ArrayList<>();
  private boolean isSwipeClick;

  public ViewPagerAdapter(FragmentManager manager) {
    super(manager);
  }

  @Override
  public Fragment getItem(int position) {
    return mFragmentList.get(position);
  }

  @Override
  public int getCount() {
    return mFragmentList.size();
  }

  public void addFragment(Fragment fragment) {
    mFragmentList.add(fragment);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return (!mFragmentTitleList.isEmpty()) ? mFragmentTitleList.get(position) :
        "";
  }

  /**
   * Used by ViewPager.  "Object" represents the page; tell the ViewPager where the
   * page should be displayed, from left-to-right.  If the page no longer exists,
   * return POSITION_NONE.
   *
   * @param object item object
   * @return position
   */
  public int getItemPosition(@NonNull Object object) {
    if (isSwipeClick) {
      return POSITION_NONE;
    }
    int index = mFragmentList.indexOf(object);
    if (index == -1) {
      return POSITION_NONE;
    } else {
      return index;
    }
  }

  @Override
  @NonNull
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    Fragment fragment = (Fragment) super.instantiateItem(container, position);
    mFragmentList.set(position, fragment);
    return fragment;
  }

}
