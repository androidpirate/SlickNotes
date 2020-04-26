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

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.util.AlarmReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public abstract class BaseEditableNoteFragment extends Fragment
    implements View.OnClickListener {
    public static final String EXTRA_NOTE_ID = "note_id";
    public static final String EXTRA_NOTE_TITLE = "note_title";
    public static final String EXTRA_NOTE_DETAILS = "note_details";
    static final String DELETED_NOTE_ID = "deletedNoteId";
    static final String NOTE_PIN_STATUS = "note_pin_status";
    static final String NAVIGATION_BASE = "navigation_base";

    private static final String PICKER_DIALOG_TITLE = "Pick Card Background";
    private static final String PICKER_DIALOG_CANCEL = "Cancel";

    boolean isKeyboardOn = false;
    EditText title;
    EditText details;
    TextView dateCreated;
    TextView dateEdited;
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
     * @param color Color string resource
     */
    abstract void onColorPickerFabClick(String color);

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
        switch (v.getId()) {
            case R.id.fab_actions: animateFab();
                break;
            case R.id.fab_add_label:
                break;
            case R.id.fab_change_color: displayColorPickerDialog();
                break;
            case R.id.fab_share:
                shareNote();
                break;
            case R.id.fab_default:
                onColorPickerFabClick(getString(R.string.color_default));
                break;
            case R.id.fab_pink:
                onColorPickerFabClick(getString(R.string.color_pink));
                break;
            case R.id.fab_yellow:
                onColorPickerFabClick(getString(R.string.color_yellow));
                break;
            case R.id.fab_blue:
                onColorPickerFabClick(getString(R.string.color_blue));
                break;
            case R.id.fab_orange:
                onColorPickerFabClick(getString(R.string.color_orange));
                break;
            case R.id.fab_green:
                onColorPickerFabClick(getString(R.string.color_green));
                break;
            case R.id.fab_purple:
                onColorPickerFabClick(getString(R.string.color_purple));
                break;
            case R.id.fab_gray:
                onColorPickerFabClick(getString(R.string.color_gray));
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

    void setBackgroundColor(String color) {
        switch (color) {
            case "blue":
                rootView.setBackgroundColor(
                        ContextCompat.getColor(
                                Objects.requireNonNull(getContext()), R.color.colorCardBgBlue));
                break;
            case "gray":
                rootView.setBackgroundColor(
                        ContextCompat.getColor(
                                Objects.requireNonNull(getContext()), R.color.colorCardBgGray));
                break;
            case "green":
                rootView.setBackgroundColor(
                        ContextCompat.getColor(
                                Objects.requireNonNull(getContext()), R.color.colorCardBgGreen));
                break;
            case "orange":
                rootView.setBackgroundColor(
                        ContextCompat.getColor(
                                Objects.requireNonNull(getContext()), R.color.colorCardBgOrange));
                break;
            case "pink":
                rootView.setBackgroundColor(
                        ContextCompat.getColor(
                                Objects.requireNonNull(getContext()), R.color.colorCardBgPink));
                break;
            case "purple":
                rootView.setBackgroundColor(
                        ContextCompat.getColor(
                                Objects.requireNonNull(getContext()), R.color.colorCardBgPurple));
                break;
            case "yellow":
                rootView.setBackgroundColor(
                        ContextCompat.getColor(
                                Objects.requireNonNull(getContext()), R.color.colorCardBgYellow));
                break;
            default:
                rootView.setBackgroundColor(
                        ContextCompat.getColor(
                                Objects.requireNonNull(getContext()), R.color.colorCardBgDefault));
        }
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
                    R.string.note_pinned_toast,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(),
                    R.string.note_unpinned_toast,
                    Toast.LENGTH_SHORT).show();
        }
    }

    void displayNoteDiscardToast() {
        Toast.makeText(getContext(), R.string.discard_note_toast, Toast.LENGTH_SHORT).show();
    }

    boolean checkFieldsAreEmpty() {
        return title.getText().toString().trim().length() == 0 &&
                details.getText().toString().trim().length() == 0;
    }

    void displayDateAndTimePicker(final int noteId, final String noteTitle, final String noteDetails) {
        Calendar currentDateAndTime = Calendar.getInstance();
        final int startYear = currentDateAndTime.get(Calendar.YEAR);
        final int startMonth = currentDateAndTime.get(Calendar.MONTH);
        final int startDay = currentDateAndTime.get(Calendar.DAY_OF_MONTH);
        final int startHour = currentDateAndTime.get(Calendar.HOUR_OF_DAY);
        final int startMinute = currentDateAndTime.get(Calendar.MINUTE);

        new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar pickedDateAndTime = Calendar.getInstance();
                        pickedDateAndTime.set(year, month, dayOfMonth, hourOfDay, minute);
                        setReminder(pickedDateAndTime, noteId, noteTitle, noteDetails);
                    }
                }, startHour, startMinute, true).show();
            }
        }, startYear, startMonth, startDay).show();
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
        dateCreated = rootView.findViewById(R.id.tv_date_created);
        dateEdited = rootView.findViewById(R.id.tv_date_update);
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
        FloatingActionButton fabWhite = dialogView.findViewById(R.id.fab_default);
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

    private void shareNote() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, title.getText().toString());
        sendIntent.putExtra(Intent.EXTRA_TEXT, details.getText().toString());
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void setReminder(Calendar reminderDateAndTime, int noteId,
                             String noteTitle, String noteDetails) {
        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity())
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        intent.putExtra(EXTRA_NOTE_TITLE, noteTitle);
        intent.putExtra(EXTRA_NOTE_DETAILS, noteDetails);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(),
                getUniqueAlarmCode(),
                intent,
                PendingIntent.FLAG_ONE_SHOT);
        Objects.requireNonNull(alarmManager).setExact(
                AlarmManager.RTC_WAKEUP,
                reminderDateAndTime.getTimeInMillis(),
                pendingIntent);
    }

    private int getUniqueAlarmCode() {
        long currentTime = Long.parseLong(
                new SimpleDateFormat("ddHHmmssSS", Locale.US).format(new Date()));
        return (int) (currentTime % Integer.MAX_VALUE);
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
