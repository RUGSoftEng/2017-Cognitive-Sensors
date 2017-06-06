package com.teamwan.wander.db;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import static com.google.android.gms.gcm.Task.NETWORK_STATE_CONNECTED;

/**
 * Created by Thomas on 6-6-2017.
 */
public class UploadService extends GcmTaskService {

    public static final String TAG = "upload_service";

    private static final int UPLOAD_PERIOD_SECONDS = 60 * 60 * 12;

    @Override
    public void onCreate() {
        super.onCreate();
        Task task = new PeriodicTask.Builder()
                .setService(UploadService.class)
                .setPeriod(UPLOAD_PERIOD_SECONDS)
                .setTag(UploadService.TAG)
                .setRequiredNetwork(NETWORK_STATE_CONNECTED)
                .setRequiresCharging(false)
                .setUpdateCurrent(false)
                .setPersisted(true)
                .build();
        GcmNetworkManager.getInstance(this).schedule(task);
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        new DBDownload().execute(new DBpars(this)); //download questions from server
        new DBUpload().execute(new DBpars(this)); //send the stored local data to server
        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
