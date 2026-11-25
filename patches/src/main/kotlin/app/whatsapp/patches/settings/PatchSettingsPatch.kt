package app.whatsapp.patches.settings

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.getFreeRegisterProvider
import app.whatsapp.patches.shared.Constants.WHATSAPP_COMPATIBILITY

private const val EXTENSION_CLASS = "Lapp/whatsapp/extension/PatchSettingsExtension;"

/**
 * Patch Settings Integration patch.
 *
 * Integrates a "Pengaturan Patch" menu entry directly into WhatsApp's
 * existing Settings screen. The entry opens a dedicated settings screen
 * showing current patch status and information.
 *
 * The settings screen displays:
 * - Section A (Pembersihan Iklan): Current promotional cleanup status.
 * - Section B (Meta AI): Current Meta AI cleanup status.
 * - Section C (Informasi Patch): Patch version and compatibility info.
 *
 * Implementation notes:
 * - Uses an extension (precompiled DEX) to build the settings UI.
 * - The extension uses Android's PreferenceFragmentCompat API.
 * - UI follows WhatsApp's native dark/light theme automatically.
 * - Settings display is informational (read-only) since Morphe patch
 *   options are compile-time only. Changes require repatching.
 *
 * Placement: Between "Bahasa Aplikasi" and "Bantuan dan masukan"
 * in the WhatsApp Settings screen (verified against target version).
 */
@Suppress("unused")
val patchSettingsPatch = patch(
    name = "Patch Settings",
    description = "Adds a native Patch Settings menu into WhatsApp Settings.",
    default = true
) {
    compatibleWith(WHATSAPP_COMPATIBILITY)

    extendWith("extensions/extension.mpe")

    execute {
        // Try to find the settings fragment using available fingerprints.
        // The first matching fingerprint is used.
        val settingsMethod = SettingsFragmentFingerprint.methodOrNull
            ?: SettingsPreferenceFingerprint.methodOrNull

        if (settingsMethod != null) {
            // Insert a call to our extension at the end of the settings setup method,
            // just before the return statement.
            // This adds the "Pengaturan Patch" preference entry to the preference screen.
            val insertIndex = settingsMethod.implementation!!.instructions.size - 1

            val registerProvider = settingsMethod.getFreeRegisterProvider(insertIndex, 1)
            val freeRegister = registerProvider.getFreeRegister4Bit()

            settingsMethod.addInstructions(
                insertIndex,
                """
                    # Get the preference screen from the fragment (p0 = this = PreferenceFragment).
                    invoke-virtual { p0 }, Landroidx/preference/PreferenceFragmentCompat;->getPreferenceScreen()Landroidx/preference/PreferenceScreen;
                    move-result-object v$freeRegister

                    # Call extension to add the "Pengaturan Patch" entry.
                    invoke-static { p0, v$freeRegister }, $EXTENSION_CLASS->addPatchSettings(Landroidx/preference/PreferenceFragmentCompat;Landroidx/preference/PreferenceScreen;)V
                """
            )
        }
        // If neither fingerprint matches, the patch does not modify settings.
        // This is a safe failure — the app works normally without the settings entry.
    }
}
