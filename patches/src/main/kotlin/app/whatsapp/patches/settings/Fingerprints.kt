package app.whatsapp.patches.settings

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.string

/**
 * Fingerprints for locating WhatsApp's Settings infrastructure.
 *
 * These fingerprints identify the settings activity/fragment where the
 * "Pengaturan Patch" entry will be inserted. WhatsApp uses a preference-based
 * settings UI, and these fingerprints target the preference setup methods.
 */

/**
 * Matches the method in WhatsApp's settings fragment that sets up
 * the preference screen. This is where we inject the new
 * "Pengaturan Patch" preference entry.
 *
 * We look for the settings preferences XML resource to locate
 * the method that inflates the settings screen.
 */
object SettingsFragmentFingerprint : Fingerprint(
    returnType = "V",
    isStatic = false,
    filters = listOf(
        // WhatsApp settings typically reference a preferences resource.
        // The addPreferencesFromResource or setPreferencesFromResource call
        // loads the main settings XML.
        methodCall(
            name = "addPreferencesFromResource",
        ),
    )
)

/**
 * Alternative fingerprint that searches for the settings fragment
 * using string constants commonly found in WhatsApp settings code.
 */
object SettingsPreferenceFingerprint : Fingerprint(
    returnType = "V",
    isStatic = false,
    filters = listOf(
        string("pref_screen"),
    )
)
