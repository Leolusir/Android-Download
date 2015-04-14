package library;

import java.io.File;

/**
 * Created by leo on 2015/4/14.
 */
public interface DownLoadListener {

    void OnDownloadStart();
    void OnDownloadComplete(File file);
    void OnDownloadfailed();
    void OnDownLoadProgress(int progress);

}
