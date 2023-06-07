package com.fk.bantambrowser;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WebViewPagerAdapter extends FragmentStateAdapter {

    private final Map<Integer, WebViewFragment> fragmentMap = new HashMap<>();

    private ArrayList<WebViewFragment> webViewFragments;

    public WebViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<WebViewFragment> webViewFragments) {
        super(fragmentManager, lifecycle);
        this.webViewFragments = webViewFragments;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return webViewFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return webViewFragments.size();
    }

    public WebViewFragment getActiveWebViewFragment(int index) {
        if (index >= 0 && index < webViewFragments.size()) {
            return webViewFragments.get(index);
        }
        return null;
    }

    public void addWebViewFragment(WebViewFragment webViewFragment) {
        webViewFragments.add(webViewFragment);
        notifyItemInserted(webViewFragments.size() - 1);
    }


    public void addTab() {
        webViewFragments.add(new WebViewFragment());
        notifyDataSetChanged();
    }

    public void removeTab(int position) {
        if (position >= 0 && position < webViewFragments.size()) {
            webViewFragments.remove(position);
            notifyDataSetChanged();
        }
    }

}


