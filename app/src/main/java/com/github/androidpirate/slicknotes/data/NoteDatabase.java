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

package com.github.androidpirate.slicknotes.data;

import android.content.Context;

import com.github.androidpirate.slicknotes.util.FakeData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class, Label.class, NoteLabelCrossRef.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase INSTANCE;
    public abstract NoteDao noteDao();
    public abstract LabelDao labelDao();

    public static NoteDatabase getInstance(final Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDatabase.class,
                    "notes-database")
//                    .addCallback(new Callback() {
////                        @Override
////                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
////                            super.onCreate(db);
////                            Executor executor = Executors.newSingleThreadExecutor();
////                            executor.execute(new Runnable() {
////                                @Override
////                                public void run() {
////                                    insertFakeData(context);
////                                }
////                            });
////                        }
////                    })
                    .build();
        }
        return INSTANCE;
    }

    // TODO : Remove this method from the release build
    private static void insertFakeData(Context context) {
        List<NoteWithLabels> fakeNotes = FakeData.getNotes();
        NoteDao noteDao = getInstance(context).noteDao();
        LabelDao labelDao = getInstance(context).labelDao();
        for(NoteWithLabels noteWtLabels: fakeNotes) {
            int noteId = (int) noteDao.insertDatabaseNote(noteWtLabels.getNote());
            for(Label label: noteWtLabels.getLabels()) {
                labelDao.insertLabel(label);
                noteDao.insertNoteLabelCrossRef(new NoteLabelCrossRef(noteId, label.getLabelTitle()));
            }
        }
    }
}
