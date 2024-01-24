package com.example.moto_jen;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String url;

    WebView mywebView;

    ValueCallback<Uri[]>upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url = "https://mobile.moto-jen.online/";
        mywebView=findViewById(R.id.webview);
        mywebView.getSettings().setJavaScriptEnabled(true);
        mywebView.loadUrl(url);
        mywebView.setWebViewClient(new MyWeb());
        mywebView.setWebChromeClient(new WebChromeClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
                Intent intent;
                intent=fileChooserParams.createIntent();
                upload=filePathCallback;
                startActivityForResult(intent, 101);
                return true;
            }
        });
    }

    private class MyWeb extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView mywebView, WebResourceRequest request) {
            mywebView.loadUrl(request.getUrl().toString());
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101){
            if (upload==null){
                return;
            }

            upload.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode,data));
            upload=null;
        }

    }

    @Override
    public void onBackPressed() {
        if (mywebView.canGoBack()) {
            mywebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
