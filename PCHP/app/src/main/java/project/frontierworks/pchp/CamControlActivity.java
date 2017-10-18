package project.frontierworks.pchp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class CamControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_control);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        WebView myWebView = (WebView) this.findViewById(R.id.web_view);
        myWebView.loadUrl("http://192.168.1.109:8081");
    }

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
