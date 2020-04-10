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

package com.github.androidpirate.slicknotes.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.github.androidpirate.slicknotes.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navView;
    private NavController navController;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set default Shared Preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setAppTheme();
        // Setup views
        setupViews();
        // Setup navigation controller and app navigation with drawer
        setupNavigation();
        // Set drawer click listener
        setDrawerClickListener();
    }

    private void setupViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
    }

    private void setupNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = getAppBarConfig();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    private AppBarConfiguration getAppBarConfig() {
        return new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_trash, R.id.nav_settings)
                .setDrawerLayout(drawerLayout)
                .build();
    }

    private void setDrawerClickListener() {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        navController.navigate(R.id.nav_home);
                        break;
                    case R.id.nav_trash:
                        navController.navigate(R.id.nav_trash);
                        break;
                    case R.id.nav_settings:
                        navController.navigate(R.id.nav_settings);
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setAppTheme() {
        boolean darkTheme = sharedPref.getBoolean(getString(R.string.pref_theme_key), false);
        if(darkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        String defaultTextSize = getString(R.string.pref_text_size_default);
        String textSize = sharedPref.getString(
                getString(R.string.pref_text_size_key),
                defaultTextSize);
        if(textSize != null && textSize.equals(defaultTextSize)) {
            setTheme(R.style.AppTheme_Default);
        } else {
            setTheme(R.style.AppTheme_Large);
        }
    }
}
