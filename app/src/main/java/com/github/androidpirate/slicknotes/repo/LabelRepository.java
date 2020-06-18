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

package com.github.androidpirate.slicknotes.repo;

import android.content.Context;

import com.github.androidpirate.slicknotes.data.Label;
import com.github.androidpirate.slicknotes.data.LabelDao;
import com.github.androidpirate.slicknotes.data.NoteDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class LabelRepository {
    private LabelDao labelDao;
    private Executor executor;

    public LabelRepository(Context context) {
        labelDao = NoteDatabase.getInstance(context).labelDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Label>> getDatabaseLabels() {
        return labelDao.getAllLabels();
    }

    public void insertLabel(final Label label) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                labelDao.insertLabel(label);
            }
        });
    }

    public void updateLabel(final String originalLabelTitle, final String newTitle) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                labelDao.updateLabel(originalLabelTitle, newTitle);
                labelDao.updateNoteLabelCrossRef(originalLabelTitle, newTitle);
            }
        });
    }

    public void deleteLabel(final Label label) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                labelDao.deleteLabel(label);
            }
        });
    }
}