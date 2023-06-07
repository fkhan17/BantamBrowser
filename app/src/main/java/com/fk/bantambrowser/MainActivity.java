package com.fk.bantambrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.viewpager2.widget.ViewPager2;


import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    EditText urlInput;
    ImageView cleanUrl;
    ViewPager2 viewPager;


    WebViewPagerAdapter webViewPagerAdapter;
    ProgressBar progressBar;
    ImageView browser_back, browser_forward, browser_refresh, browser_share, tab_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlInput=findViewById(R.id.txt_input);
        cleanUrl=findViewById(R.id.clear_icon);
        progressBar = findViewById(R.id.progress_bar);
        viewPager = findViewById(R.id.viewPager);



        browser_back=findViewById(R.id.browser_back);
        browser_refresh=findViewById(R.id.browser_refresh);
        browser_forward=findViewById(R.id.browser_forward);
        browser_share=findViewById(R.id.browser_share);
        ImageView tabIcon = findViewById(R.id.tab_icon);
        ImageView settingsIcon = findViewById(R.id.settings_icon);
        settingsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        viewPager = findViewById(R.id.viewPager);

        ImageView browser_close_tab = findViewById(R.id.browser_close_tab);

        browser_close_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCurrentTab();
            }
        });

        tabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTab();
            }
        });


        webViewPagerAdapter = new WebViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), webViewFragments);
        viewPager.setAdapter(webViewPagerAdapter);


        urlInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(urlInput.getWindowToken(), 0);
                WebView activeWebView = getActiveWebView();
                if (activeWebView != null) {
                    loadMyUrl(activeWebView, urlInput.getText().toString());
                }
                return true;
            }
            return false;
        });

        cleanUrl.setOnClickListener(view -> urlInput.setText(""));

        browser_back.setOnClickListener(view -> {
            WebView activeWebView = getActiveWebView();
            if (activeWebView != null && activeWebView.canGoBack()) {
                activeWebView.goBack();
            }
        });

        browser_forward.setOnClickListener(view -> {
            WebView activeWebView = getActiveWebView();
            if (activeWebView != null && activeWebView.canGoForward()) {
                activeWebView.goForward();
            }
        });

        browser_refresh.setOnClickListener(view -> {
            WebView activeWebView = getActiveWebView();
            if (activeWebView != null) {
                activeWebView.reload();
            }
        });

        browser_share.setOnClickListener(view -> {
            WebView activeWebView = getActiveWebView();
            if (activeWebView != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, activeWebView.getUrl());
                startActivity(Intent.createChooser(intent, "Share via"));
            }
        });
    }

    private WebView getActiveWebView() {
        WebViewFragment activeFragment = webViewPagerAdapter.getActiveWebViewFragment(viewPager.getCurrentItem());
        if (activeFragment != null) {
            return activeFragment.getWebView();
        }
        return null;
    }
    private ArrayList<WebViewFragment> webViewFragments = new ArrayList<>(Collections.singletonList(new WebViewFragment()));

    private void addNewTab() {
        WebViewFragment webViewFragment = new WebViewFragment();
        webViewFragments.add(webViewFragment);
        webViewPagerAdapter.notifyItemInserted(webViewFragments.size() - 1);
        viewPager.setCurrentItem(webViewFragments.size() - 1);
    }


    private void closeCurrentTab() {
        int currentPosition = viewPager.getCurrentItem();
        webViewPagerAdapter.removeTab(currentPosition);

        if (webViewPagerAdapter.getItemCount() == 0) {
            finish();
        } else {
            viewPager.setCurrentItem(Math.max(0, currentPosition - 1));
        }
    }

    void loadMyUrl(WebView webView, String url) {
        boolean matchUrl = Patterns.WEB_URL.matcher(url).matches();
        if (matchUrl) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl("https://duckduckgo.com/?q=" + url);
        }
    }

    @Override
    public void onBackPressed() {
        WebView activeWebView = getActiveWebView();
        if (activeWebView != null && activeWebView.canGoBack()) {
            activeWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}