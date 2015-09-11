package com.dhammalab.satipatthna;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dhammalab.satipatthna.domain.Template;
import com.dhammalab.satipatthna.repository.Settings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ilya on 18.07.2014.
 */
abstract public class ChartsBaseActivity extends BaseActivity {

    public static final String ASSET_PATH = "file:///android_asset/";

    private WebView webView;

    private ProgressBar loadingBar;
    private TextView uniqueIdTextView;
    private Button sendAnalyticsButton;
    private Spinner templateSpinner;
    private View userSettingsLayout;

    private Settings settings;

    private static byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        settings = new Settings(SatiApplication.getInstance());

        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        sendAnalyticsButton = (Button) findViewById(R.id.btn_send_analytics);
        userSettingsLayout = findViewById(R.id.user_settings_layout);
        uniqueIdTextView = (TextView) findViewById(R.id.unique_id_text_view);
        templateSpinner = (Spinner) findViewById(R.id.template_spinner);

        uniqueIdTextView.setText("Unique ID: " + Utils.getUniqueId());

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, Template.getAllTemplates()) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextColor(getResources().getColor(android.R.color.black));
                view.setPadding(10,10,10,10);
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextColor(getResources().getColor(android.R.color.black));
                view.setPadding(10,10,10,10);
                return view;
            }
        };
        templateSpinner.setAdapter(adapter);

        templateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //6 senses
                        settings.setTemplate(Template.SIX_SENSES);
                        break;
                    case 1:
                        settings.setTemplate(Template.EXTERNAL);
                        break;
                    case 2:
                        settings.setTemplate(Template.INTERNAL);
                        break;
                    case 3:
                        settings.setTemplate(Template.MIND_BODY);
                        break;
                    default:
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Template template = settings.getTemplate();
        if(template == null || template.equals(Template.SIX_SENSES)) {
            templateSpinner.setSelection(0);
        } else if(template.equals(Template.EXTERNAL)) {
            templateSpinner.setSelection(1);
        } else if(template.equals(Template.INTERNAL)) {
            templateSpinner.setSelection(2);
        } else if(template.equals(Template.MIND_BODY)) {
            templateSpinner.setSelection(3);
        }

        if(settings.getLastSession() != null) {
            loadingBar.setVisibility(View.GONE);
            webView = (WebView) findViewById(R.id.webview);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    loadingBar.setVisibility(View.GONE);
                }

                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                    try {
                        if (url.contains("format")) {
                            return new WebResourceResponse("text/js", "UTF-8", getAssets().open("format.js"));
                        } else if (url.contains("ui+en.css")) {
                            return new WebResourceResponse("text/css", "UTF-8", getAssets().open("ui+en.css"));
                        } else if (url.contains("visualization")) {
                            return new WebResourceResponse("text/js", "UTF-8", getAssets().open("visualization.js"));
                        } else if (url.contains("tooltip")) {
                            return new WebResourceResponse("text/css", "UTF-8", getAssets().open("tooltip.css"));
                        } else {
                            return super.shouldInterceptRequest(view, url);
                        }
                    } catch (IOException error) {
                        Log.e("AnalysisActivity", "An error occurred.", error);
                    }
                    return null;
                }
            });

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            //webSettings.setBuiltInZoomControls(true);
            String content = null;
            try {
                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open(getAssetsFileName());
                byte[] bytes = readFully(in);
                content = new String(bytes, "UTF-8");
            } catch (IOException e) {
                Log.e("AnalysisActivity", "An error occurred.", e);
            }

            String formattedContent = String.format(content, getData());
            webView.loadDataWithBaseURL(ASSET_PATH, formattedContent, "text/html", "utf-8", null);
            webView.requestFocusFromTouch();
        } else {
            loadingBar.setVisibility(View.GONE);
        }
    }

    abstract public String getAssetsFileName();

    abstract public Object[] getData();

    protected void showSendAnalyticsButton(boolean show) {
        sendAnalyticsButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected void showUserSettingsViews(boolean show) {
        userSettingsLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected void showUniqueId(boolean show) {
        uniqueIdTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
