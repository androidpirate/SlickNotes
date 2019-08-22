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

package com.github.androidpirate.slicknotes.ui.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.viewmodel.NoteViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public abstract class BaseEditableNoteFragment extends Fragment {
    boolean isKeyboardOn = false;
    EditText title;
    EditText details;
    NoteViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_editable, container, false);
        // Set soft keyboard listener
        setSoftKeyboardListener(view);
        title = view.findViewById(R.id.et_title);
        details = view.findViewById(R.id.et_details);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
    }

    void hideSoftKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                Objects.requireNonNull(getActivity())
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocusedView = getActivity().getCurrentFocus();
        if(currentFocusedView != null) {
            Objects.requireNonNull(inputManager)
                    .hideSoftInputFromWindow(currentFocusedView.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void setSoftKeyboardListener(final View view) {
        view.getViewTreeObserver()
            .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Rect r = new Rect();
                    //r will be populated with the coordinates of
                    // your view that area still visible.
                    view.getWindowVisibleDisplayFrame(r);
                    int heightDiff = view.getRootView().getHeight() - (r.bottom - r.top);
                    if (heightDiff > 500) {
                        // if more than 100 pixels, its probably a keyboard...
                        isKeyboardOn = true;
                    } else {
                        isKeyboardOn = false;
                    }
                }
            });
    }
}
