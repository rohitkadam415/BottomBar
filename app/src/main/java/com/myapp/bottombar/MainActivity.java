package com.myapp.bottombar;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {
  private final String TAG = MainActivity.this.getClass().getSimpleName();
  ViewPager viewPager;
  TabLayout tabLayout;
  private TypedArray tabIcons;
  private TypedArray tabIconsSelected;
  private List<String> titleArray=new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();
    tabIcons = getResources().obtainTypedArray(R.array.tab_icons);
    tabIconsSelected = getResources().obtainTypedArray(R.array.tab_icons_selected);
    tabLayout.setupWithViewPager(viewPager);
    titleArray.add("Home");
    titleArray.add("Setting");
    titleArray.add("Home");
    titleArray.add("Setting");
    setupViewPager();
    setTabIcons();
    addListener();
  }

  private void addListener() {
    tabLayout.addOnTabSelectedListener(this);
  }

  private void initView() {
    viewPager = findViewById(R.id.viewPager);
    tabLayout = findViewById(R.id.tabs);
  }

  private void setupViewPager() {
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    adapter.addFragment(HomeFragment.newInstance(null, null));
    adapter.addFragment(SettingFragment.newInstance(null, null));
    adapter.addFragment(HomeFragment.newInstance(null, null));
    adapter.addFragment(SettingFragment.newInstance(null, null));
    viewPager.setOffscreenPageLimit(3);
    viewPager.setAdapter(adapter);
  }

  /**
   * This method adds images and tab indicator to each tab
   */
  private void setTabIcons() {

    for (int i = 0; i < tabLayout.getTabCount(); i++) {
      final View view = LayoutInflater.from(this).inflate(R.layout.view_tab, null);
      ((ImageView) view.findViewById(R.id.iv_tab_icon)).setImageResource(
          tabIcons.getResourceId(i, -1));

      tabLayout.getTabAt(i).setCustomView(view);
      setTabUnselectedView(tabLayout.getTabAt(i));

      switch (i) {
        case 0:
          view.setContentDescription(getString(R.string.home_button));
          break;
        case 1:
          view.setContentDescription(getString(R.string.setting));
          break;
        case 2:
          view.setContentDescription(getString(R.string.home_button));
          break;
        case 3:
          view.setContentDescription(getString(R.string.setting));
          break;
        default:
          break;
      }
    }
    // For first icon to set selected
    setTabSelectedView(tabLayout.getTabAt(0));
  }

  /**
   * Sets tab selected image and indicator.
   *
   * @param tab TabLayout.Tab
   */
  private void setTabSelectedView(TabLayout.Tab tab) {
    final View view = tab.getCustomView();
    if (null != view) {
      final ImageView ivTabIcon = view.findViewById(R.id.iv_tab_icon);
      final View vwTabIndicator = view.findViewById(R.id.vw_tab_indicator);
      ivTabIcon.setImageResource(tabIconsSelected.getResourceId(tab.getPosition(), -1));
      vwTabIndicator.setBackgroundColor(
          ContextCompat.getColor(this, R.color.live_recentlylive_suggested_color));
      view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
    }
  }

  /**
   * Sets tab unselected image and indicator.
   *
   * @param tab TabLayout.Tab
   */
  private void setTabUnselectedView(TabLayout.Tab tab) {
    final View view = tab.getCustomView();
    if (null != view) {
      final ImageView ivTabIcon = view.findViewById(R.id.iv_tab_icon);
      final View vwTabIndicator = view.findViewById(R.id.vw_tab_indicator);
      ivTabIcon.setImageResource(tabIcons.getResourceId(tab.getPosition(), -1));
      vwTabIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color
          .live_recentlylive_suggested_color));
      view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
    }
  }

  @Override
  public void onTabSelected(TabLayout.Tab tab) {
    Log.i(TAG, "onTabSelected: ");
    boolean isHome = false;
    Fragment fragment = getSupportFragmentManager().getFragments().get(tab.getPosition());
    if (fragment instanceof HomeFragment) {
      isHome = true;
    } else if (fragment instanceof SettingFragment) {
    } else if (fragment instanceof HomeFragment) {
      //((NetworkFragment) fragment).getAllFeedDetails(EchoMeConstants.EMPTY_STRING);
    } else if (fragment instanceof SettingFragment) {

    }
    setTabSelectedView(tab);

  }

  @Override
  public void onTabUnselected(TabLayout.Tab tab) {
    setTabUnselectedView(tab);
  }

  @Override
  public void onTabReselected(TabLayout.Tab tab) {
    Log.i(TAG, "onTabReselected: ");
  }
}
