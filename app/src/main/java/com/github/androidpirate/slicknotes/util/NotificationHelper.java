/*
 * <!--
 *  Copyright (C) 2016 The Android Open Source Project
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 * -->
 */

package com.github.androidpirate.slicknotes.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.ui.activity.MainActivity;
import com.github.androidpirate.slicknotes.ui.fragment.BaseEditableNoteFragment;

public class NotificationHelper extends ContextWrapper {
    private static final String NOTIFICATION_CHANNEL_ID = "alarm_channel";
    private static final String NOTIFICATION_NAME = "alarm_notification";
    private static final String GROUP_KEY_REMINDER = "com.github.androidpirate.slicknotes.REMINDER";
    private NotificationManager notificationManager;
    private int noteId;
    private String noteTitle;
    private String noteDetails;

    public NotificationHelper(Context context, int noteId, String noteTitle, String noteDetails) {
        super(context);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteDetails = noteDetails;
    }

    public NotificationCompat.Builder getNotificationBuilder() {
        // Create a pending intent that will navigate to NoteDetails
        Bundle args = new Bundle();
        args.putInt(BaseEditableNoteFragment.EXTRA_NOTE_ID, noteId);
        PendingIntent pendingIntent = new NavDeepLinkBuilder(getApplicationContext())
                .setComponentName(MainActivity.class)
                .setGraph(R.navigation.navigation_graph)
                .setDestination(R.id.nav_details)
                .setArguments(args)
                .createPendingIntent();
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(getBaseContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_slick_notes)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(noteTitle)
                .setContentText(noteDetails)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setLights(Color.YELLOW, 1000, 1000)
                .setSound(notificationSound)
                .setVibrate(new long[0])
                .setGroup(GROUP_KEY_REMINDER);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        notificationManager.createNotificationChannel(channel);
    }
}
