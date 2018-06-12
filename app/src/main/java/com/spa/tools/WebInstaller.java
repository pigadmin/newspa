/**
 *
 */
package com.spa.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import com.spa.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class WebInstaller {

    private Context context;
    private String packageName; // 包名 com.xx.xx
    private String apkurl; // 下载链接
    private String savePath; // 项目路径

    private ProgressDialog pBar;
    private DownLoadFileThread downLoadFileThread;
    private Handler handler;
    private String filename;

    public WebInstaller(Context context, String apkurl) {
        this.context = context;

        File dir = context.getDir("spa", Context.MODE_PRIVATE
                | Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        dir.mkdirs();
        this.packageName = context.getPackageName();
        this.apkurl = apkurl;
        this.savePath = dir.getAbsolutePath() + "/";
        handler = new Handler();

//         filename = Math.abs(this.apkurl.hashCode()) + ".apk";
        filename = apkurl.split("/")[apkurl.split("/").length - 1];
    }

    String content;

    public void downloadAndInstall(String content) {
        try {
            this.content = content;
            pBar = new ProgressDialog(context);
            pBar.setTitle(context.getString(R.string.Download));
            pBar.setMessage(content);
            // pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pBar.setCancelable(false);
            pBar.setButton(ProgressDialog.BUTTON_NEGATIVE,
                    context.getString(R.string.Cancle), new OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            pBar.cancel();
                            if (downLoadFileThread != null) {
                                downLoadFileThread.interrupt();
                            }
                        }
                    });
            pBar.setButton(ProgressDialog.BUTTON_NEUTRAL,
                    context.getString(R.string.Background),
                    new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            pBar.cancel();
                        }
                    });
            pBar.setProgress(100);
            pBar.show();
            downLoadFileThread = new DownLoadFileThread(apkurl, savePath
                    + this.filename, new DownloadCallback() {

                @Override
                public void onSuccess() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                pBar.cancel();
//                                AppTool.toast(context, context.getString(R.string.Complete), 0, Gravity.CENTER, 0, 0);
                                install(savePath + filename);
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    });
                }

                @Override
                public void onPrecessing(int written, long total) {
                    int c = (int) ((long) written * 100 / total);
                    pBar.setProgress(c);
                }

                @Override
                public void onFail(Throwable e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            AppTool.toast(context, context.getString(R.string.DownFailed), 0, Gravity.CENTER, 0, 0);
                        }
                    });
                    pBar.cancel();
                }

                @Override
                public void onCancel() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            AppTool.toast(context, context.getString(R.string.Cancle), 0, Gravity.CENTER, 0, 0);

                        }
                    });
                }
            });
            downLoadFileThread.start();
//            if (!new File(savePath + filename).exists()) {
//                downLoadFileThread.start();
//            } else {
//                pBar.cancel();
//                install(savePath + filename);
//            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void install(String fullfilepath) {
        getFilePermission(fullfilepath);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + fullfilepath),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private void restart() {
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void getFilePermission(String file) {
        ShellExecute ex = new ShellExecute();
        String[] cmd = {"chmod", "607", file};
        try {
            ex.execute(cmd, "/");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class DownLoadFileThread extends Thread {

        private String url;
        // 文件保存路径
        private String fullFilename;
        private DownloadCallback callback;

        public DownLoadFileThread(String url, String fullFilename,
                                  DownloadCallback callback) {
            this.url = url;
            this.fullFilename = fullFilename;
            this.callback = callback;
        }

        public void run() {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(this.url);
            HttpResponse response;

            FileOutputStream outStream = null;
            try {
                response = client.execute(get);

                HttpEntity entity = response.getEntity();
                long length = entity.getContentLength();
                InputStream is = entity.getContent();
                File f = new File(fullFilename);
                f.getParentFile().mkdirs();
                if (is != null) {
                    outStream = new FileOutputStream(new File(fullFilename));

                    byte[] buf = new byte[1024];
                    int ch = -1;
                    int count = 0;
                    while ((ch = is.read(buf)) != -1) {
                        if (this.isInterrupted()) {
                            callback.onCancel();
                            return;
                        }
                        outStream.write(buf, 0, ch);
                        count += ch;
                        if (count != 0) {
                            callback.onPrecessing(count, length);
                        }
                    }
                }
                outStream.flush();
                callback.onSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                callback.onFail(e);
            } finally {
                if (outStream != null) {
                    try {
                        outStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    interface DownloadCallback {

        public void onSuccess();

        public void onPrecessing(int written, long total);

        public void onFail(Throwable e);

        public void onCancel();
    }

    private class ShellExecute {
        public String execute(String[] cmmand, String directory)
                throws IOException {
            String result = "";
            try {
                ProcessBuilder builder = new ProcessBuilder(cmmand);
                if (directory != null)
                    builder.directory(new File(directory));
                builder.redirectErrorStream(true);
                Process process = builder.start();
                InputStream is = process.getInputStream();
                byte[] buffer = new byte[1024];
                while (is.read(buffer) != -1) {
                    result = result + new String(buffer);
                }
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
