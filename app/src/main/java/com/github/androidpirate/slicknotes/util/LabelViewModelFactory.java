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

import android.app.Application;

import com.github.androidpirate.slicknotes.repo.LabelRepository;
import com.github.androidpirate.slicknotes.repo.NoteRepository;
import com.github.androidpirate.slicknotes.viewmodel.LabelListViewModel;
import com.github.androidpirate.slicknotes.viewmodel.NoteLabelViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LabelViewModelFactory implements ViewModelProvider.Factory {
    private static final String EXCEPTION_MESSAGE = "ViewModel class can not be found.";
    private NoteRepository noteRepository;
    private LabelRepository labelRepository;

    public LabelViewModelFactory(Application application) {
        noteRepository = ServiceLocator.provideNoteRepository(application);
        labelRepository = ServiceLocator.provideLabelRepository(application);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(NoteLabelViewModel.class)) {
            return (T) new NoteLabelViewModel(noteRepository, labelRepository);
        } else if(modelClass.isAssignableFrom(LabelListViewModel.class)) {
            return (T) new LabelListViewModel(labelRepository);
        } else {
            throw new IllegalMonitorStateException(EXCEPTION_MESSAGE);
        }
    }
}