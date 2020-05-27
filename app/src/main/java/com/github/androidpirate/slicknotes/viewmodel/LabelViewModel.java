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

import com.github.androidpirate.slicknotes.data.Label;
import com.github.androidpirate.slicknotes.data.NoteWithLabels;
import com.github.androidpirate.slicknotes.repo.LabelRepository;
import com.github.androidpirate.slicknotes.repo.NoteRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class LabelViewModel extends ViewModel {
    private NoteRepository noteRepository;
    private LabelRepository labelRepository;

    public LabelViewModel(NoteRepository noteRepository, LabelRepository labelRepository) {
        this.noteRepository = noteRepository;
        this.labelRepository = labelRepository;
    }

    public LiveData<List<Label>> getDatabaseLabels() {
        return labelRepository.getDatabaseLabels();
    }

    public LiveData<NoteWithLabels> getDatabaseNote(int noteId) {
        return noteRepository.getDatabaseNote(noteId);
    }
}
