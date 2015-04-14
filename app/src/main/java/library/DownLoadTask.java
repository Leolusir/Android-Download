package library;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;

/**
 * Created by leo on 2015/4/14.
 */
public class DownLoadTask extends AsyncTask<Void, Integer, File> {
    private Context context;
    private DownLoadListener listener;

    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    private static final String FILE_ROOT   = SDCARD_ROOT + "downloadsample/";

    private int      length;
    private File     tmpFile;
    private File     file;
    private String   apkurl;
    private String   filename = "sample";
    private int      progress = 0;
    private boolean  isdownload = false;

    public boolean isdownload() {
        return isdownload;
    }

    public void setIsdownload(boolean isdownload) {
        this.isdownload = isdownload;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public DownLoadTask(String apkurl, String filename, Context context, DownLoadListener listener){
        this.apkurl     = apkurl;
        this.context    = context;
        this.filename   = filename;
        this.listener   = listener;
        tmpFile         = new File(FILE_ROOT);
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        file = new File(FILE_ROOT + filename);
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        listener.OnDownloadStart();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);
        listener.OnDownLoadProgress(values[0]);
    }

    @Override
    protected void onCancelled() {
        // TODO Auto-generated method stub
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(File result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if(result != null){
            listener.OnDownloadComplete(result);
        }else{
            listener.OnDownloadfailed();
        }
    }

    @SuppressWarnings("resource")
    @Override
    protected File doInBackground(Void... params) {
        // TODO Auto-generated method stub
        try {
            URL url = new URL(apkurl);
            try {
                isdownload = true;
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[256];
                length = length / 256 + 1;
                conn.connect();
                double count = 0;
                if (conn.getResponseCode() >= 400) {
                    return null;
                } else {
                    while (count <= 100) {
                        if (is != null) {
                            if(isdownload){
                                int numRead = is.read(buf);
                                if (numRead <= 0) {
                                    break;
                                } else {
                                    progress ++;
                                    if(progress < length){
                                        double p = (progress * 1.00) / (length * 1.00);
                                        String p1 = NumberFormat.getPercentInstance().format(p);
                                        p1 = p1.replace("%", "");
                                        publishProgress(Integer.parseInt(p1));
                                    }else{
                                        publishProgress(100);
                                    }
                                    fos.write(buf, 0, numRead);
                                }
                            }

                        } else {
                            break;
                        }

                    }
                }
                conn.disconnect();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return file;
    }

}
