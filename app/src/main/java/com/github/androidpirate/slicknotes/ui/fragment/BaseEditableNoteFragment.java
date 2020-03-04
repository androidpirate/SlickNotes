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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.github.androidpirate.slicknotes.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public abstract class BaseEditableNoteFragment extends Fragment
    implements View.OnClickListener {
    static final String EXTRA_NOTE_ID = "note_id";
    static final String DELETED_NOTE_ID = "deletedNoteId";
    static final String NOTE_PIN_STATUS = "note_pin_status";
    static final String NAVIGATION_BASE = "navigation_base";
    private static final String PICKER_DIALOG_TITLE = "Pick Card Background";
    private static final String PICKER_DIALOG_CANCEL = "Cancel";

    boolean isKeyboardOn = false;
    EditText title;
    EditText details;
    private Animation fabExpand, fabCollapse, fabRotateLeft,
            fabRotateRight, fabActionShow, fabActionHide;
    private View rootView;
    private FloatingActionButton fabAction;
    private FloatingActionButton fabAddLabel;
    private FloatingActionButton fabChangeColor;
    private FloatingActionButton fabShare;
    private NavController navController;
    private AlertDialog colorPickerDialog;
    private boolean isFabActionOpen = false;
    private boolean isFabOn = true;
    boolean pinStatus;

    /**
     * Abstract methods for color picker dialog
     * @param colorId Color resource id
     */
    abstract void onColorPickerFabClick(int colorId);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_note_editable_base,
                container,
                false);
        // Set soft keyboard listener
        setSoftKeyboardListener(rootView);
        // Setup views
        setupViews(rootView);
        // Setup fab action
        setupFabAction(rootView);
        // setup fab animations
        setupFabAnimations();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Find navigation controller
        navController = Navigation.findNavController(
                Objects.requireNonNull(getActivity()),
                R.id.nav_host_fragment);
    }

    @Override
    public void onClick(View v) {
        @NonNull
        Context context = Objects.requireNonNull(getContext());
        switch (v.getId()) {
            case R.id.fab_actions: animateFab();
                break;
            case R.id.fab_add_label:
                break;
            case R.id.fab_change_color: displayColorPickerDialog();
                break;
            case R.id.fab_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, title.getText().toString());
                sendIntent.putExtra(Intent.EXTRA_TEXT, details.getText().toString());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                break;
            case R.id.fab_white:
                onColorPickerFabClick(ContextCompat.getColor(context, R.color.colorFabWhite));
                break;
            case R.id.fab_pink:
                onColorPickerFabClick(ContextCompat.getColor(context, R.color.colorFabPink));
                break;
            case R.id.fab_yellow:
                onColorPickerFabClick(ContextCompat.getColor(context, R.color.colorFabYellow));
                break;
            case R.id.fab_blue:
                onColorPickerFabClick(ContextCompat.getColor(context, R.color.colorFabBlue));
                break;
            case R.id.fab_orange:
                onColorPickerFabClick(ContextCompat.getColor(context, R.color.colorFabOrange));
                break;
            case R.id.fab_green:
                onColorPickerFabClick(ContextCompat.getColor(context, R.color.colorFabGreen));
                break;
            case R.id.fab_purple:
                onColorPickerFabClick(ContextCompat.getColor(context, R.color.colorFabPurple));
                break;
            case R.id.fab_gray:
                onColorPickerFabClick(ContextCompat.getColor(context, R.color.colorFabGray));
                break;
        }
    }

    void navigateToList(int deletedNoteId) {
        Bundle args = new Bundle();
        args.putInt(DELETED_NOTE_ID, deletedNoteId);
        navController.navigate(R.id.nav_details_to_home, args);
    }

    void navigateToTrash() {
        navController.navigate(R.id.nav_details_to_trash);
    }

    void setBackgroundColor(int colorId) {
        rootView.setBackgroundColor(colorId);
    }

    void hideColorPickerDialog() {
        colorPickerDialog.cancel();
    }

    void setFabActionVisibility() {
        hideFabAction();
    }

    void setPinIcon(boolean isPinned, MenuItem item) {
        if(isPinned) {
            item.setIcon(R.drawable.ic_pin_selected);
        } else {
            item.setIcon(R.drawable.ic_pin);
        }
    }

    void displayPinToast(boolean pinStatus) {
        if(pinStatus) {
            Toast.makeText(getContext(),
                    getResources().getString(R.string.note_pinned_toast),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(),
                    getResources().getString(R.string.note_unpinned_toast),
                    Toast.LENGTH_SHORT).show();
        }
    }

    void hideSoftKeyboard() {
        @NonNull
        InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(
                Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE));
        View currentFocusedView = getActivity().getCurrentFocus();
        if(currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(
                    currentFocusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void setupViews(View rootView) {
        title = rootView.findViewById(R.id.et_title);
        details = rootView.findViewById(R.id.et_details);
    }

    private void setupFabAction(View view) {
        fabAction = view.findViewById(R.id.fab_actions);
        fabAction.setOnClickListener(this);
        fabAddLabel = view.findViewById(R.id.fab_add_label);
        fabAddLabel.setOnClickListener(this);
        fabChangeColor = view.findViewById(R.id.fab_change_color);
        fabChangeColor.setOnClickListener(this);
        fabShare = view.findViewById(R.id.fab_share);
        fabShare.setOnClickListener(this);
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

    private void showFabAction() {
        fabAction.startAnimation(fabActionShow);
    }

    private void hideFabAction() {
        if(isFabActionOpen) {
            animateFab();
        }
        fabAction.startAnimation(fabActionHide);
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

    private void displayColorPickerDialog() {
        // Inflate dialog view
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_color_picker, null);
        // Create dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(PICKER_DIALOG_TITLE)
                .setView(dialogView)
                .setNegativeButton(PICKER_DIALOG_CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideColorPickerDialog();
                    }
                });
        colorPickerDialog = builder.create();
        // Setup color picker FABs
        setupColorPickerFABs(dialogView);
        colorPickerDialog.show();
    }

    private void setupColorPickerFABs(final View dialogView) {
        FloatingActionButton fabWhite = dialogView.findViewById(R.id.fab_white);
        fabWhite.setOnClickListener(this);
        FloatingActionButton fabPink = dialogView.findViewById(R.id.fab_pink);
        fabPink.setOnClickListener(this);
        FloatingActionButton fabYellow = dialogView.findViewById(R.id.fab_yellow);
        fabYellow.setOnClickListener(this);
        FloatingActionButton fabBlue = dialogView.findViewById(R.id.fab_blue);
        fabBlue.setOnClickListener(this);
        FloatingActionButton fabOrange = dialogView.findViewById(R.id.fab_orange);
        fabOrange.setOnClickListener(this);
        FloatingActionButton fabGreen = dialogView.findViewById(R.id.fab_green);
        fabGreen.setOnClickListener(this);
        FloatingActionButton fabPurple = dialogView.findViewById(R.id.fab_purple);
        fabPurple.setOnClickListener(this);
        FloatingActionButton fabGray = dialogView.findViewById(R.id.fab_gray);
        fabGray.setOnClickListener(this);
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
                        if (heightDiff > 500 && isFabOn) {
                            // if more than 100 pixels, its probably a keyboard...
                            isKeyboardOn = true;
                            isFabOn = false;
                            hideFabAction();
                        } else if (heightDiff < 500 && !isFabOn){
                            isKeyboardOn = false;
                            isFabOn = true;
                            clearFocusFromTextFields();
                            showFabAction();
                        }
                    }
                });
    }
}
