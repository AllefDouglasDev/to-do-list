package com.example.allef.tad.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.allef.tad.UpdateTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Allef on 22/05/2018.
 */

public class Util
{
    /**
     *
     * @return yyyy-MM-dd HH:mm:ss timestamp formatado como string
     */
    public String getCurrentTimeStamp()
    {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     *
     * @return yyyy-MM-dd data formatada como string
     */
    public String getCurrentDate()
    {
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        return formataData.format(new Date());
    }

    /**
     * Notifica o usuário
     *
     * @param context
     * @param id
     * @param hour
     * @param min
     */
    public void notifyUser(Context context, int id, int hour, int min)
    {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, UpdateTask.class);
        intent.putExtra("taskId", Integer.toString(id));

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);

        long inicio = calendar.getTimeInMillis();

        alarmMgr.set(AlarmManager.RTC_WAKEUP, inicio, alarmIntent);
    }
}
