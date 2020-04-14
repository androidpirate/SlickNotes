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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.github.androidpirate.slicknotes.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    private SharedPreferences sharedPref;
    private SwitchPreference themePref;
    private SwitchPreference itemOrderPref;
    private ListPreference textSizePref;
    private String themePrefKey;
    private String itemOrderPrefKey;
    private String textSizePrefKey;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager
                .getDefaultSharedPreferences(Objects.requireNonNull(getContext()));
        // Get preference keys
        getPreferenceKeys();
        // Get references to preferences
        getPreferences();
        // Set change listeners
        setChangeListeners();
    }

    private void getPreferences() {
        themePref = findPreference(themePrefKey);
        itemOrderPref = findPreference(itemOrderPrefKey);
        textSizePref = findPreference(textSizePrefKey);
    }

    private void getPreferenceKeys() {
        themePrefKey = getString(R.string.pref_theme_key);
        itemOrderPrefKey = getString(R.string.pref_item_order_key);
        textSizePrefKey = getString(R.string.pref_text_size_key);
    }

    private void setChangeListeners() {
        themePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                sharedPref
                        .edit()
                        .putBoolean(themePrefKey, (Boolean) newValue)
                        .apply();
                Objects.requireNonNull(getActivity()).recreate();
                return true;
            }
        });
        itemOrderPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                sharedPref
                        .edit()
                        .putBoolean(itemOrderPrefKey, (Boolean) newValue)
                        .apply();
                return true;
            }
        });
        // Set text size preference summary
        setTextSizeSummary();
        Objects.requireNonNull(textSizePref)
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        sharedPref
                                .edit()
                                .putString(textSizePrefKey, newValue.toString())
                                .apply();
                        preference.setSummary(newValue.toString());
                        return true;
                    }
                });
    }

    private void setTextSizeSummary() {
        String textSizeValue = sharedPref.getString(textSizePrefKey, "Default");
        textSizePref.setSummary(textSizeValue);
    }
}
