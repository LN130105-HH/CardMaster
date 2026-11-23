package com.cardmaster.app.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cardmaster.app.R;
import com.cardmaster.app.databinding.ActivityMainBinding;
import com.cardmaster.app.ui.main.aichat.AiChatFragment;
import com.cardmaster.app.ui.main.home.HomeFragment;
import com.cardmaster.app.ui.main.profile.ProfileFragment;
import com.cardmaster.app.ui.main.statistics.StatisticsFragment;
import com.cardmaster.app.ui.main.storage.StorageFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment statisticsFragment = new StatisticsFragment();
    private final Fragment aiChatFragment = new AiChatFragment();
    private final Fragment storageFragment = new StorageFragment();
    private final Fragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Default screen is Home
        if (savedInstanceState == null) {
            loadFragment(homeFragment);
        }

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                loadFragment(homeFragment);
                return true;
            } else if (itemId == R.id.navigation_statistics) {
                loadFragment(statisticsFragment);
                return true;
            } else if (itemId == R.id.navigation_chat_ai) {
                loadFragment(aiChatFragment);
                return true;
            } else if (itemId == R.id.navigation_storage) {
                loadFragment(storageFragment);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                loadFragment(profileFragment);
                return true;
            }
            return false;
        });
    }

    public void navigateToStorage() {
        binding.bottomNavigation.setSelectedItemId(R.id.navigation_storage);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_container, fragment)
                .commit();
    }
}
