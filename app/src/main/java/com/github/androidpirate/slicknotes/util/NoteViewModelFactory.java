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

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.androidpirate.slicknotes.repo.NoteRepository;
import com.github.androidpirate.slicknotes.viewmodel.NoteCreateViewModel;
import com.github.androidpirate.slicknotes.viewmodel.NoteDetailViewModel;
import com.github.androidpirate.slicknotes.viewmodel.NoteListViewModel;
import com.github.androidpirate.slicknotes.viewmodel.NoteTrashViewModel;

public class NoteViewModelFactory implements ViewModelProvider.Factory {
    private static final String EXCEPTION_MESSAGE = "ViewModel class can not be found.";
    public NoteRepository repo;

    public NoteViewModelFactory(Application application) {
        repo = NoteRepository.getInstance(application.getApplicationContext());
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(NoteCreateViewModel.class)) {
            return (T) new NoteCreateViewModel(repo);
        } else if(modelClass.isAssignableFrom(NoteDetailViewModel.class)) {
            return (T) new NoteDetailViewModel(repo);
        } else if(modelClass.isAssignableFrom(NoteListViewModel.class)) {
            return (T) new NoteListViewModel(repo);
        } else if(modelClass.isAssignableFrom(NoteTrashViewModel.class)) {
            return (T) new NoteTrashViewModel(repo);
        } else
            throw new IllegalStateException(EXCEPTION_MESSAGE);
    }
}
