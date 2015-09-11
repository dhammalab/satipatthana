package com.dhammalab.satipatthna;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by anthony.lipscomb on 10/9/2014.
 */
public class ReadActivity extends BaseActivity {
    public static final String URL_EXTRA = "url";
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        getActionBar().hide();

        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        String url = getIntent().getExtras().getString(URL_EXTRA);
        String doc = "<iframe src=\"" + url + "\" width='100%' height='100%' style='border: none;'\"></iframe>";
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                showProgressBar(false);
            }
        });
        webView.loadData(doc, "text/html",  "UTF-8");

    }

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
