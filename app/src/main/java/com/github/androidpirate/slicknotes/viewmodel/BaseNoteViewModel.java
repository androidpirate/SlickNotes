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

package com.github.androidpirate.slicknotes.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.repo.NoteRepository;

public class BaseNoteViewModel extends ViewModel {
    NoteRepository repo;
    LiveData<Note> uiModel;
    Note databaseModel;

    public void updateNoteColor(String color) {
        databaseModel.setColor(color);
    }

    public boolean updateNotePinStatus() {
        databaseModel.setPinned(!databaseModel.isPinned());
        return databaseModel.isPinned();
    }
}
