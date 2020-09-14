package com.TUBEDELIVERIES.Fragments.DrawerLayout;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {

    private View view;
    @BindView(R.id.webview)
    WebView webview;
    private String title="";
    private String Url="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web_view, container, false);
        ButterKnife.bind(this,view);
        getIntentData();

//        ////set webpages for webview
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.loadUrl(Url);
//
//        webview.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                CommonUtilities.dismissLoadingDialog();
//                // setBackResult(url);
//
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                CommonUtilities.showLoadingDialog(getActivity());
//            }
//
//        });

        return view;
    }

    private void getIntentData(){

        Bundle bundle=this.getArguments();
        if(bundle!=null){

            Url=  bundle.getString(ParamEnum.WEB_URL.theValue());
            title= bundle.getString("TITLE");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbar(title,View.GONE);
    }

}
