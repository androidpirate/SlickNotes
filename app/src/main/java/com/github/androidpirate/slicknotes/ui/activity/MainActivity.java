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
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.github.androidpirate.slicknotes.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get navController
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        // Get AppBarConfig
        AppBarConfiguration appBarConfiguration = getAppBarConfig();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        navView = findViewById(R.id.nav_view);
        setDrawerClickListener();
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
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_trash:
                        navController.navigate(R.id.nav_trash);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_settings:
                        break;
                }
                return true;
            }
        });
    }
}
