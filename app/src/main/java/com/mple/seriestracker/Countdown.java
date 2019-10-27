package com.mple.seriestracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.mple.seriestracker.activity.HomeScreenActivity;
import com.mple.seriestracker.util.CountdownUtil;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZonedDateTime;

public class Countdown extends AppCompatActivity {

    private int season;
    private int episode;
    private String name;
    private ZonedDateTime airDate;
    private Context context;

    public Countdown(String name, int episode,int season,ZonedDateTime airDate){
        this.airDate = airDate;
        this.name = name;
        this.episode = episode;
        this.season = season;
    }

    public Countdown(com.mple.seriestracker.api.episodate.entities.show.Countdown countdown){
        this.airDate = CountdownUtil.parseToLocal(countdown.air_date);
        this.name = countdown.name;
        this.episode = countdown.episode;
        this.season = countdown.season;
    }
 public void sendNotification(String name, int season, int episode)
    {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "M_CH_ID");

        //Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(context, HomeScreenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel =
                new NotificationChannel("M_CH_ID", "M_CH_ID", NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription("airing");
        nm.createNotificationChannel(notificationChannel);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("Hearty365")
                .setContentTitle("Series Tracker")
                .setContentText(name + " Season " + season + " episode " + episode + " is now airing!")
                .setContentInfo("Info");

        nm.notify(1, notificationBuilder.build());
    }

    public String getCountdownFormat(Context context){
        this.context = context;
        Duration duration = Duration.between(LocalDateTime.now(),airDate);
        long secondsRemaining = 0;
        long days = duration.toDays();
        //No idea why this returns an absurd number, possibly something wrong with the time conversion
        //So the simple fix is to convert the days into hours, subtract the total hours with the days.
        //This returns the real value, and makes it accurate.
        long hours = duration.toHours()-(days*24);
        int minutes = (int) ((duration.getSeconds() % (60 * 60)) / 60);
        int seconds = (int) (duration.getSeconds() % 60);
        String timeString = "";
        if(days > 0){
            timeString+=formatDay(days);
            secondsRemaining += days * 24 * 60 *60;
        }
        if(hours > 0){
            timeString+=formatHour(hours);
            secondsRemaining += hours * 60 *60;
        }
        if(minutes > 0){
            timeString+= formatMinutes(minutes);
            secondsRemaining += minutes *60;
        }
        if(seconds > 0){
            timeString += formatSeconds(seconds);
            secondsRemaining +=  seconds;
        }
        if (secondsRemaining == 323200){
            sendNotification(getName(), this.season, this.episode);
        }
        return timeString;

    }


    public String getName() {
        return name;
    }

    public int getEpisode() {
        return episode;
    }

    public int getSeason() {
        return season;
    }

    public ZonedDateTime getAirDate() {
        return airDate;
    }

    private String formatDay(long days){
        return format(days,"day");
    }

    private String formatHour(long hours){
        return format(hours,"hour");
    }

    private String formatMinutes(long minutes){
        return format(minutes,"minute");
    }

    private String formatSeconds(long seconds){
        return format(seconds,"second");
    }

    private String format(long x,String nonPlural){
        //Checks whether or not a plural should be added
        String string = nonPlural;
        if(x > 1)
            string+="s";

        return String.format("%s %s ",x,string);
    }
}
