package com.nennig.vtglibrary.managers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.vtglibrary.custobjs.MatrixID;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class VideoManager extends AsyncTask<Void, Void, Boolean>{
	private static final String TAG = AppConfig.APP_TITLE_SHORT + ".VideoManager";
    private String videosDir = "";
    private Activity activity;
    Class destClass;
    private ProgressDialog pd;
    private static String videoExt = "mp4";
    private static String downloadSite;
    private String videoFile;
    boolean destroyActivity;
    private AppConstants.Set _set;

    /**
     * Constructor that gets the video, but destroys the current activity because the same activity
     * will be rerun.
     * @param a - current activity
     * @param set - current set
     * @param id - current matrix ID
     * @param propID - id for the current prop
     */
    public VideoManager(Activity a, AppConstants.Set set, MatrixID id, AppConstants.PropType propID){
        this(a, a.getClass(), set, id, propID);
        destroyActivity = true;
    }

    /**
     *
     * @param a - current activity
     * @param destC - activity that will be called with the new video file
     * @param set - current set
     * @param id - current matrix ID
     * @param propID - id for the current prop
     */
    public VideoManager(Activity a, Class destC, AppConstants.Set set, MatrixID id, AppConstants.PropType propID){
        destroyActivity = false;
        activity = a;
        destClass = destC;
        _set = set;
        videosDir = Environment.getExternalStorageDirectory() +
                "/Android/data/" + activity.getPackageName() + "/files/";
        videoFile = "v" + set.toSetID() + "_" + id.toString() + "_" + propID.toPropID() + "." + videoExt;

        downloadSite = "http://noelyee.com/vtg%20videos/" + videoFile;
        Log.d(TAG, "Site: " + downloadSite);

    }

    /**
     * Returns the path where the current video can be found on the SD card
     * @return current video path
     */
    public String getVideoPath(){
        return videosDir + videoFile;
    }

    /**
     * This checks to see if the root path for the videos is created. If not, it creates it.
     * @return directory of the root for the videos
     */
    private File getDownloadRootDir() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File mDownloadRootDir;
            // http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
            mDownloadRootDir = new File(videosDir);
            if (!mDownloadRootDir.isDirectory()) {
                // this must be the first time we've attempted to download files, so create the proper external directories
                mDownloadRootDir.mkdirs();
            }
            return mDownloadRootDir;
        }
        Toast.makeText(activity, "Please make sure your SD card is connected", Toast.LENGTH_LONG).show();
        return null;
    }

    /**
     * This calls the download for the video if it does not exist on the SD card
     * @param strings
     * @return true if the video is now available on the SD card, false otherwise
     */
    @Override
    protected Boolean doInBackground(Void... strings) {
        File f = getDownloadRootDir();
        boolean response = true;
        if(!new File(f.getPath(), videoFile).exists()){
            response = downloadVideo();
            pd.dismiss();
        }
        return response;
    }

    /**
     * Sets up the thread with a wait screen.
     */
    @Override
    protected void onPreExecute() {
        File f = getDownloadRootDir();
        if(!new File(f.getPath(), videoFile).exists())
            pd = ProgressDialog.show(activity, "Fetching clip...", "Loading....");
    }

    /**
     * Once the video is downloaded to the SD card, then the destination activity is called
     * @param result boolean to determine if it is safe to step into the destination activity
     */
    @Override
    protected void onPostExecute(Boolean result) {
//        Toast.makeText(activity, Boolean.toString(result), Toast.LENGTH_LONG).show();
        if(result)
        {
            Intent i = new Intent(activity, destClass);
            i.putExtra(AppConstants.CUR_SET, _set);
            activity.startActivity(i);
            if(destroyActivity)
                activity.finish();
        }
        else
            Toast.makeText(activity, "Could not retrieve from server", Toast.LENGTH_LONG).show();
    }

    /**
     * This is a helper method to download and save the video file from the internet.
     */
    private final int TIMEOUT_CONNECTION = 5000;//5sec
    private final int TIMEOUT_SOCKET = 30000;//30sec
    private boolean downloadVideo(){
        try{
            URL url = new URL(downloadSite);
            long startTime = System.currentTimeMillis();
            Log.d(TAG, "image download beginning: "+videoFile);

            //Open a connection to that URL.
            URLConnection ucon = url.openConnection();

            //this timeout affects how long it takes for the app to realize there's a connection problem
            ucon.setReadTimeout(TIMEOUT_CONNECTION);
            ucon.setConnectTimeout(TIMEOUT_SOCKET);


            //Define InputStreams to read from the URLConnection.
            // uses 3KB download buffer
            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
            FileOutputStream outStream = new FileOutputStream(videosDir + videoFile);
            byte[] buff = new byte[5 * 1024];

            //Read bytes (and store them) until there is nothing more to read(-1)
            int len;
            while ((len = inStream.read(buff)) != -1)
            {
                outStream.write(buff,0,len);
            }

            //clean up
            outStream.flush();
            outStream.close();
            inStream.close();

            Log.d(TAG, "download completed in "
                    + ((System.currentTimeMillis() - startTime) / 1000)
                    + " sec");
        }catch (Exception e){
            Log.d(TAG, "Failed to download: " + e.getMessage());
            return false;
        }
        return true;

    }
}
