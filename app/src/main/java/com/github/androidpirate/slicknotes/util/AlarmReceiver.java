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

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.androidpirate.slicknotes.ui.fragment.BaseEditableNoteFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent pendingIntent) {
        int noteId = pendingIntent
                .getIntExtra(BaseEditableNoteFragment.EXTRA_NOTE_ID, 0);
        String noteTitle = pendingIntent
                .getStringExtra(BaseEditableNoteFragment.EXTRA_NOTE_TITLE);
        String noteDetails = pendingIntent
                .getStringExtra(BaseEditableNoteFragment.EXTRA_NOTE_DETAILS);
        NotificationHelper notificationHelper =
                new NotificationHelper(context, noteId, noteTitle, noteDetails);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = createUniqueNotificationId();
        notificationManager.notify(notificationId,
                notificationHelper.getNotificationBuilder().build());
    }

    private int createUniqueNotificationId() {
        long currentTime = Long.parseLong(
                new SimpleDateFormat("ddHHmmssSS", Locale.US).format(new Date()));
        return (int) (currentTime % Integer.MAX_VALUE);
    }
}
