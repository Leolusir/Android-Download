package sample.downlload.leo.android_download;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import library.DownLoadListener;
import library.DownLoadTask;

public class SampleActivity extends ActionBarActivity {
    private final static String url = "http://img.yingyonghui.com/apk/16457/com.rovio.angrybirdsspace.ads.1332528395706.apk";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        textView = (TextView)findViewById(R.id.textview_progress);

        //the task can execute only once
        final DownLoadTask task = new DownLoadTask(url, "sample.apk", this, new DownLoadListener() {
            @Override
            public void OnDownloadStart() {
                Log.i("download", "start");
            }

            @Override
            public void OnDownloadComplete(File file) {
                Log.i("download", "complete");
                textView.setText("complete");
                openFile(file);
            }

            @Override
            public void OnDownloadfailed() {
                Log.i("download", "failed");
                textView.setText("failed");
            }

            @Override
            public void OnDownLoadProgress(int progress) {
                Log.i("download", progress + "%");
                textView.setText(progress + "%");
            }
        });

        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    task.execute();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    task.setIsdownload(false);
                    task.cancel(true);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void openFile(File file) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
