package app.whatsapp.extension;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Extension code for the Patch Settings integration.
 *
 * This class provides the UI implementation for the "Pengaturan Patch"
 * settings screen. It is merged into the WhatsApp APK as a precompiled
 * DEX file and called from the patched settings fragment.
 *
 * Design principles:
 * - Uses WhatsApp's own theme resources for native appearance.
 * - Follows WhatsApp's layout patterns for consistency.
 * - Automatically supports dark and light mode via theme attributes.
 * - Settings display is informational since Morphe options are compile-time.
 *
 * WhatsApp Clean Experience - Independent community project.
 * Not affiliated with WhatsApp, Meta, or Morphe.
 */
@SuppressWarnings("unused")
public class PatchSettingsExtension {

    // Patch version
    private static final String PATCH_VERSION = "1.0.0";
    private static final String PROJECT_NAME = "WhatsApp Clean Experience";
    private static final String PROJECT_DESC = "Kustomisasi antarmuka WhatsApp melalui Morphe patches.";

    /**
     * Called from the patched WhatsApp settings fragment to add the
     * "Pengaturan Patch" preference entry.
     *
     * This method attempts to add a preference using the PreferenceFragmentCompat API.
     * If the API is not available, it fails silently to preserve app stability.
     */
    public static void addPatchSettings(Object fragment, Object preferenceScreen) {
        try {
            Context context = null;
            if (fragment instanceof android.app.Fragment) {
                context = ((android.app.Fragment) fragment).getActivity();
            }
            if (context == null) {
                try {
                    context = (Context) fragment.getClass().getMethod("getContext").invoke(fragment);
                } catch (Throwable ignored) {
                }
            }
            if (context == null) {
                try {
                    context = (Context) fragment.getClass().getMethod("getActivity").invoke(fragment);
                } catch (Throwable ignored) {
                }
            }
            if (context == null) return;

            addPatchSettingsPreference(context, preferenceScreen);
        } catch (Throwable t) {
            // Fail silently to avoid crashing WhatsApp settings.
            // This can happen on unsupported WhatsApp versions where
            // the settings infrastructure differs from expectations.
        }
    }

    /**
     * Adds the "Pengaturan Patch" preference entry to the preference screen.
     * Uses reflection to maintain compatibility with different WhatsApp builds.
     */
    private static void addPatchSettingsPreference(Context context, Object preferenceScreen) {
        try {
            // Create a new Preference
            Class<?> prefClass = Class.forName("androidx.preference.Preference");
            Object pref = prefClass.getConstructor(Context.class).newInstance(context);

            // Set title: "Pengaturan Patch"
            prefClass.getMethod("setTitle", CharSequence.class).invoke(pref, "Pengaturan Patch");

            // Set summary: "Kelola fitur dan kustomisasi patch"
            prefClass.getMethod("setSummary", CharSequence.class).invoke(pref, "Kelola fitur dan kustomisasi patch");

            // Set icon using a system icon (tune/settings style)
            prefClass.getMethod("setIconSpaceReserved", boolean.class).invoke(pref, true);

            // Set click listener to open patch settings screen
            final Context ctx = context;
            Class<?> listenerClass = Class.forName("androidx.preference.Preference$OnPreferenceClickListener");
            Object listener = java.lang.reflect.Proxy.newProxyInstance(
                listenerClass.getClassLoader(),
                new Class<?>[]{listenerClass},
                (proxy, method, args) -> {
                    if ("onPreferenceClick".equals(method.getName())) {
                        showPatchSettingsDialog(ctx);
                        return true;
                    }
                    return false;
                }
            );
            prefClass.getMethod("setOnPreferenceClickListener", listenerClass).invoke(pref, listener);

            // Set a unique key
            prefClass.getMethod("setKey", String.class).invoke(pref, "patch_settings");

            // Set ordering to place after language settings
            prefClass.getMethod("setOrder", int.class).invoke(pref, 999);

            // Add to preference screen
            Class<?> prefGroupClass = Class.forName("androidx.preference.PreferenceGroup");
            prefGroupClass.getMethod("addPreference", prefClass).invoke(preferenceScreen, pref);
        } catch (Throwable t) {
            // Silently fail if preference API is not available.
        }
    }

    /**
     * Shows the Patch Settings dialog with native-looking WhatsApp styling.
     *
     * Uses a programmatically built UI that follows WhatsApp's visual language.
     * This approach works without requiring custom XML resources and
     * automatically adapts to dark/light theme via theme attributes.
     */
    public static void showPatchSettingsDialog(Context context) {
        try {
            // Build settings content view
            ScrollView scrollView = new ScrollView(context);
            LinearLayout container = new LinearLayout(context);
            container.setOrientation(LinearLayout.VERTICAL);

            int padding = dpToPx(context, 16);
            container.setPadding(padding, padding, padding, padding);

            // Section A: Pembersihan Iklan
            addSectionHeader(context, container, "PEMBERSIHAN IKLAN");
            addSettingsRow(context, container,
                    "Sembunyikan Elemen Promosi",
                    "Mengurangi tampilan elemen promosi dan iklan yang didukung.",
                    true); // Shows as enabled (patch is applied)

            addDivider(context, container);

            // Section B: Meta AI
            addSectionHeader(context, container, "META AI");
            addSettingsRow(context, container,
                    "Sembunyikan Meta AI",
                    "Menyembunyikan elemen antarmuka Meta AI yang didukung.",
                    true); // Shows as enabled (patch is applied)

            addDivider(context, container);

            // Section C: Informasi Patch
            addSectionHeader(context, container, "INFORMASI PATCH");
            addInfoRow(context, container, "Proyek", PROJECT_NAME);
            addInfoRow(context, container, "Deskripsi", PROJECT_DESC);
            addInfoRow(context, container, "Versi patch", PATCH_VERSION);
            addInfoRow(context, container, "Versi WhatsApp", getWhatsAppVersion(context));
            addInfoRow(context, container, "Status", "Patch diterapkan");
            addInfoRow(context, container, "Fitur didukung",
                    "Pembersihan UI promosi, Pembersihan UI Meta AI, Pengaturan Patch");

            addDivider(context, container);

            // Note about configuration
            addNoteRow(context, container,
                    "Catatan: Pengaturan fitur diatur saat patching melalui Morphe Manager. " +
                    "Untuk mengubah konfigurasi, lakukan repatching.");

            scrollView.addView(container);

            // Show as dialog with WhatsApp-style appearance
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Pengaturan Patch");
            builder.setView(scrollView);
            builder.setPositiveButton("Tutup", null);
            builder.show();
        } catch (Throwable t) {
            // Fail silently.
        }
    }

    /**
     * Adds a section header with WhatsApp-style typography.
     */
    private static void addSectionHeader(Context context, LinearLayout container, String title) {
        TextView header = new TextView(context);
        header.setText(title);
        header.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        header.setAllCaps(true);
        header.setLetterSpacing(0.05f);

        // Use theme color for accent
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true)) {
            header.setTextColor(typedValue.data);
        } else {
            header.setTextColor(0xFF25D366); // WhatsApp green fallback
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = dpToPx(context, 16);
        params.bottomMargin = dpToPx(context, 8);
        header.setLayoutParams(params);

        container.addView(header);
    }

    /**
     * Adds a settings row with title, description, and a visual switch indicator.
     * The switch is display-only and shows the current compiled state.
     */
    private static void addSettingsRow(Context context, LinearLayout container,
                                        String title, String description, boolean enabled) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        rowParams.bottomMargin = dpToPx(context, 12);
        row.setLayoutParams(rowParams);

        // Text container
        LinearLayout textContainer = new LinearLayout(context);
        textContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
        );
        textContainer.setLayoutParams(textParams);

        // Title
        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        // Use primary text color from theme
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)) {
            titleView.setTextColor(typedValue.data);
        }
        textContainer.addView(titleView);

        // Description
        TextView descView = new TextView(context);
        descView.setText(description);
        descView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

        TypedValue secondaryColor = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.textColorSecondary, secondaryColor, true)) {
            descView.setTextColor(secondaryColor.data);
        }
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        descParams.topMargin = dpToPx(context, 2);
        descView.setLayoutParams(descParams);
        textContainer.addView(descView);

        row.addView(textContainer);

        // Switch indicator (display-only)
        Switch switchView = new Switch(context);
        switchView.setChecked(enabled);
        switchView.setClickable(false); // Read-only display
        switchView.setFocusable(false);
        row.addView(switchView);

        container.addView(row);
    }

    /**
     * Adds an information row with label and value.
     */
    private static void addInfoRow(Context context, LinearLayout container,
                                    String label, String value) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        rowParams.bottomMargin = dpToPx(context, 8);
        row.setLayoutParams(rowParams);

        // Label
        TextView labelView = new TextView(context);
        labelView.setText(label);
        labelView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        TypedValue secondaryColor = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.textColorSecondary, secondaryColor, true)) {
            labelView.setTextColor(secondaryColor.data);
        }
        row.addView(labelView);

        // Value
        TextView valueView = new TextView(context);
        valueView.setText(value);
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        TypedValue primaryColor = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.textColorPrimary, primaryColor, true)) {
            valueView.setTextColor(primaryColor.data);
        }
        row.addView(valueView);

        container.addView(row);
    }

    /**
     * Adds a note/disclaimer row.
     */
    private static void addNoteRow(Context context, LinearLayout container, String note) {
        TextView noteView = new TextView(context);
        noteView.setText(note);
        noteView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        TypedValue secondaryColor = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.textColorSecondary, secondaryColor, true)) {
            noteView.setTextColor(secondaryColor.data);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = dpToPx(context, 8);
        params.bottomMargin = dpToPx(context, 8);
        noteView.setLayoutParams(params);

        container.addView(noteView);
    }

    /**
     * Adds a divider line.
     */
    private static void addDivider(Context context, LinearLayout container) {
        View divider = new View(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(context, 1)
        );
        params.topMargin = dpToPx(context, 8);
        params.bottomMargin = dpToPx(context, 8);
        divider.setLayoutParams(params);

        // Use divider color from theme
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.listDivider, typedValue, true)) {
            divider.setBackgroundResource(typedValue.resourceId);
        } else {
            divider.setBackgroundColor(0x1FFFFFFF); // Subtle divider fallback
        }

        container.addView(divider);
    }

    /**
     * Gets the installed WhatsApp version name.
     */
    private static String getWhatsAppVersion(Context context) {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (Throwable t) {
            return "Unknown";
        }
    }

    /**
     * Converts dp to pixels.
     */
    private static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }
}
