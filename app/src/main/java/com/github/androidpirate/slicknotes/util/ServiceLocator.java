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

import android.content.Context;

import com.github.androidpirate.slicknotes.repo.LabelRepository;
import com.github.androidpirate.slicknotes.repo.NoteRepository;

public class ServiceLocator {
    private static NoteRepository noteRepository;
    private static LabelRepository labelRepository;

    public static NoteRepository provideNoteRepository(Context context) {
        if(noteRepository == null) {
            return createNoteRepository(context);
        }
        return noteRepository;
    }

    public static LabelRepository provideLabelRepository(Context context) {
        if(labelRepository == null) {
            return createLabelRepository(context);
        }
        return labelRepository;
    }

    private static NoteRepository createNoteRepository(Context context) {
        NoteRepository newRepo = new NoteRepository(context);
        noteRepository = newRepo;
        return newRepo;
    }

    private static LabelRepository createLabelRepository(Context context) {
        LabelRepository newRepo = new LabelRepository(context);
        labelRepository = newRepo;
        return newRepo;
    }
}
