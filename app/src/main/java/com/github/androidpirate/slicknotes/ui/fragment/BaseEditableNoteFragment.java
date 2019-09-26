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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.github.androidpirate.slicknotes.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public abstract class BaseEditableNoteFragment extends Fragment {

    boolean isKeyboardOn = false;
    EditText title;
    EditText details;
    private Animation fabExpand, fabCollapse, fabRotateLeft,
            fabRotateRight, fabActionShow, fabActionHide;
    private FloatingActionButton fabAction, fabAddLabel, fabChangeColor, fabShare;
    private NavController navController;
    boolean isFabActionOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_note_editable_base,
                        container,
                        false);
        // Set soft keyboard listener
        setSoftKeyboardListener(view);
        title = view.findViewById(R.id.et_title);
        details = view.findViewById(R.id.et_details);
        fabAction = view.findViewById(R.id.fab_actions);
        fabAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });
        fabAddLabel = view.findViewById(R.id.fab_add_label);
        fabChangeColor = view.findViewById(R.id.fab_change_color);
        fabShare = view.findViewById(R.id.fab_share);
        // setup fab animations
        setupFabAnimations();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = Navigation
                .findNavController(
                        Objects.requireNonNull(getActivity()),
                        R.id.nav_host_fragment);
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

    void navigateToList(int deletedNoteId) {
        Bundle args = new Bundle();
        args.putInt("deletedNoteId", deletedNoteId);
        navController.navigate(R.id.nav_details_to_home, args);
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
                        hideFabAction();
                    } else {
                        isKeyboardOn = false;
                        clearFocusFromTextFields();
                        showFabAction();
                    }
                }
            });
    }

    private void clearFocusFromTextFields() {
        title.clearFocus();
        details.clearFocus();
    }

    private void setupFabAnimations() {
        Context context = getContext();
        fabExpand = AnimationUtils.loadAnimation(context, R.anim.fab_expand);
        fabCollapse = AnimationUtils.loadAnimation(context, R.anim.fab_collapse);
        fabRotateLeft = AnimationUtils.loadAnimation(context, R.anim.fab_rotate_left);
        fabRotateRight = AnimationUtils.loadAnimation(context, R.anim.fab_rotate_right);
        fabActionShow = AnimationUtils.loadAnimation(context, R.anim.fab_action_show);
        fabActionHide = AnimationUtils.loadAnimation(context, R.anim.fab_action_hide);
    }

    private void animateFab() {
        if(isFabActionOpen) {
            fabAction.startAnimation(fabRotateRight);
            fabAddLabel.startAnimation(fabCollapse);
            fabAddLabel.setClickable(false);
            fabChangeColor.startAnimation(fabCollapse);
            fabChangeColor.setClickable(false);
            fabShare.startAnimation(fabCollapse);
            fabShare.setClickable(false);
            isFabActionOpen = false;
        } else {
            fabAction.startAnimation(fabRotateLeft);
            fabAddLabel.startAnimation(fabExpand);
            fabAddLabel.setClickable(true);
            fabChangeColor.startAnimation(fabExpand);
            fabChangeColor.setClickable(true);
            fabShare.startAnimation(fabExpand);
            fabShare.setClickable(true);
            isFabActionOpen = true;
        }
    }

    private void showFabAction() {
        fabAction.startAnimation(fabActionShow);
    }

    private void hideFabAction() {
        if(isFabActionOpen) {
            animateFab();
        }
        fabAction.startAnimation(fabActionHide);
    }
}
