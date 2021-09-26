package org.chromium.chrome.browser.settings;

import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.appcompat.app.AlertDialog;


import org.chromium.chrome.R;
import org.chromium.chrome.browser.preferences.ChromePreferenceKeys;
import org.chromium.chrome.browser.preferences.Pref;
import org.chromium.chrome.browser.preferences.SharedPreferencesManager;
import org.chromium.chrome.browser.profiles.Profile;
import org.chromium.chrome.browser.util.ChromeAccessibilityUtil;
import org.chromium.components.browser_ui.settings.ChromeSwitchPreference;
import org.chromium.components.browser_ui.settings.SettingsUtils;
import org.chromium.components.user_prefs.UserPrefs;
import org.chromium.content_public.browser.ContentFeatureList;

import android.app.Activity;
import org.chromium.base.ContextUtils;
import android.content.DialogInterface;

import org.chromium.base.SysUtils;
import org.chromium.base.Log;
import org.chromium.ui.base.DeviceFormFactor;
import android.content.SharedPreferences;


/**
 * Fragment to keep track of all the toolbar related preferences.
 */
public class ToolbarSettings
        extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
   

    private ChromeSwitchPreference mKeepToolbar;
    private ChromeSwitchPreference mSideSwipePref;
    private ChromeSwitchPreference mTabSwitcherButtonPref;
    private boolean mRecordFontSizeChangeOnStop;
    private Activity mActivity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getActivity().setTitle(R.string.prefs_accessibility);
        getActivity().setTitle("Toolbar Options");
        setDivider(null);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        SettingsUtils.addPreferencesFromResource(this, R.xml.toolbar_preferences);

        mTabSwitcherButtonPref = (ChromeSwitchPreference) findPreference("tabswitcher_opens_contextual_menu");
        mTabSwitcherButtonPref.setOnPreferenceChangeListener(this);

        mSideSwipePref = (ChromeSwitchPreference) findPreference("side_swipe_mode_enabled");
        mSideSwipePref.setOnPreferenceChangeListener(this);
        mSideSwipePref.setChecked(ContextUtils.getAppSharedPreferences().getBoolean("side_swipe_mode_enabled", true));

        ChromeSwitchPreference mEnableBottomToolbar = (ChromeSwitchPreference) findPreference("enable_bottom_toolbar");
        mEnableBottomToolbar.setOnPreferenceChangeListener(this);
        if (DeviceFormFactor.isTablet()) {
             this.getPreferenceScreen().removePreference(mEnableBottomToolbar);
        }
        ChromeSwitchPreference mOverscrollButton = (ChromeSwitchPreference) findPreference("enable_overscroll_button");
        mOverscrollButton.setOnPreferenceChangeListener(this);
        mOverscrollButton.setChecked(ContextUtils.getAppSharedPreferences().getBoolean("enable_overscroll_button", true));
        if (DeviceFormFactor.isTablet()) {
             this.getPreferenceScreen().removePreference(mOverscrollButton);
        }

        mKeepToolbar = (ChromeSwitchPreference) findPreference("keep_toolbar_visible");
        mKeepToolbar.setOnPreferenceChangeListener(this);
        String KeepToolbarSetting = ContextUtils.getAppSharedPreferences().getString("keep_toolbar_visible_configuration", "unknown");
        if (KeepToolbarSetting.equals("unknown")) {
          if (ChromeAccessibilityUtil.get().isAccessibilityEnabled())
            mKeepToolbar.setChecked(true);
          else
            mKeepToolbar.setChecked(false);
        } else if (KeepToolbarSetting.equals("on")) {
            mKeepToolbar.setChecked(true);
        } else {
            mKeepToolbar.setChecked(false);
        }
        if (DeviceFormFactor.isTablet()) {
             this.getPreferenceScreen().removePreference(mKeepToolbar);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if ("side_swipe_mode_enabled".equals(preference.getKey())) {
            AskForRelaunch(getActivity());
        } else if ("enable_overscroll_button".equals(preference.getKey())) {
            AskForRelaunch(getActivity());
        } else if ("tabswitcher_opens_contextual_menu".equals(preference.getKey())) {
            AskForRelaunch(getActivity());
        } else if ("keep_toolbar_visible".equals(preference.getKey())) {
            SharedPreferences.Editor sharedPreferencesEditor = ContextUtils.getAppSharedPreferences().edit();
            if ((boolean)newValue)
              sharedPreferencesEditor.putString("keep_toolbar_visible_configuration", "on");
            else
              sharedPreferencesEditor.putString("keep_toolbar_visible_configuration", "off");
            sharedPreferencesEditor.apply();
        } else if ("enable_bottom_toolbar".equals(preference.getKey())) {
            AskForRelaunch(getActivity());
        }

        return true;
    }

    public static void AskForRelaunch(Activity activity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
         alertDialogBuilder
            .setMessage(R.string.preferences_restart_is_needed)
            .setCancelable(true)
            .setPositiveButton(R.string.preferences_restart_now, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {
                  ApplicationLifetime.terminate(true);
                  dialog.cancel();
              }
            })
            .setNegativeButton(R.string.preferences_restart_later, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog,int id) {
                  dialog.cancel();
              }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
    }
}
