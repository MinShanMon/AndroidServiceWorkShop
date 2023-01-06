package com.example.mymusicservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;

public class MyMusicService extends Service {
    private static final String TITLE = "My Music Service";
    private static final String CHANNEL_ID = "MyMusicService_Channel";
    private static final String CHANNEL_DESCRIPTION = "This Channel for music service";
    private static final String CHANNEL_NAME = "Message Notification Channel";
    private static final int FOREGD_NOTIFY_ID = 1;
    private MediaPlayer player = null;
    private int currSongIdx = 0;
    private ArrayList<String> songFnames = new ArrayList<>();

    public MyMusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startInForeground();
    }

    //step 1 create onstartcommand (creating service)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        step 3 handling requests service
        String action = intent.getAction();

        if (action != null) {
            if (action.equalsIgnoreCase("play")) {
                currSongIdx = intent.getIntExtra("song_idx", 0);

                if (player != null) {
                    stopSong();
                }
                playSong();
            }
            else if (action.equalsIgnoreCase("stop")) {
                stopSong();
            }
            else if (action.equalsIgnoreCase("init_songs")) {
                songFnames = (ArrayList<String>) intent.getSerializableExtra("song_fnames");
                int i = 1;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    protected void playSong() {
        if (player != null) {
            stopSong();
        }

        //currsongidx come from main activity
        String currSongFname = songFnames.get(currSongIdx);
        int resId = getResources().getIdentifier(currSongFname, "raw", getPackageName());
        player = MediaPlayer.create(this, resId);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                currSongIdx = (currSongIdx + 1) % songFnames.size();
                playSong();
            }
        });
        player.start();

        onPlay();
    }

    protected void stopSong() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        onStop();
    }

    protected void onPlay()
    {
        //get song name with index from onclick
        String currSongFname = songFnames.get(currSongIdx);

        //call notification channel
        createNotificationChannel();

        //create notification
        Notification notification = createNotification(TITLE,
                "Playing \"" + currSongFname + ".mp3\"");

        //step 3 display notification on tab bar
        NotificationManagerCompat mgr = NotificationManagerCompat.from(this);
        mgr.notify(FOREGD_NOTIFY_ID, notification);
    }

    protected void onStop() {

        //call notification channel
        createNotificationChannel();

        //create notification
        Notification notification = createNotification(TITLE, "");

        //step 3 display notification on tab bar
        NotificationManagerCompat mgr = NotificationManagerCompat.from(this);
        mgr.notify(FOREGD_NOTIFY_ID, notification);
    }

    //step 3 display notification on tab bar(create notification with foreground)
    protected void startInForeground() {
        createNotificationChannel();

        Notification notification = createNotification(TITLE, "");
        startForeground(FOREGD_NOTIFY_ID, notification);
    }

    protected void createNotificationChannel() {
        //step 1 create channel(create notification with foreground)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
        }
    }

    protected Notification createNotification(String title, String text) {
        //step 4 notification tap action(create notification with foreground)
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_IMMUTABLE);

        //step 2 create notification(create notification with foreground)
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
//        Besides the content, other features can be also set, for examples:
//        - Priority: how intrusive the notification is (only for Android <8.0)
//        - Auto cancel: auto removes the notification when the user taps it
                .setContentIntent(pendingIntent);// step 4 notification tap action(create notification with foreground)

        Notification notification = builder.build();
        return notification;    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}