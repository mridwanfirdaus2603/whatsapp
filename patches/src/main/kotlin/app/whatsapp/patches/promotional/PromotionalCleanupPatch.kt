package app.whatsapp.patches.promotional

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.returnEarly
import app.whatsapp.patches.shared.Constants.WHATSAPP_COMPATIBILITY

/**
 * Promotional UI Cleanup patch.
 *
 * Reduces supported promotional and advertising-related interface elements in WhatsApp.
 * Only modifies identifiable presentation-layer elements. Does not interfere with
 * normal messaging, Status, Channels, or other unrelated application features.
 *
 * This patch targets methods that handle promotional content display by overriding
 * their return values to prevent rendering of promotional UI elements.
 *
 * Limitations:
 * - Only targets elements identifiable via string-based fingerprints.
 * - Does not modify advertising infrastructure or backend services.
 * - Does not guarantee complete advertisement removal.
 * - Fingerprints may not resolve on untested WhatsApp versions.
 */
@Suppress("unused")
val promotionalCleanupPatch = patch(
    name = "Promotional UI Cleanup",
    description = "Reduces supported promotional and advertising-related interface elements.",
    default = true
) {
    compatibleWith(WHATSAPP_COMPATIBILITY)

    execute {
        // Attempt to find and disable promotional content display methods.
        // Each fingerprint targets a specific promotional UI component.
        // If a fingerprint cannot match (the string doesn't exist in this version),
        // the patch fails gracefully with a descriptive error.

        // Disable sponsored content visibility check.
        // When this method returns false, the promotional content is not displayed.
        PromotionalContentFingerprint.methodOrNull?.let { method ->
            method.returnEarly(false)
        }

        // Disable ad banner setup.
        // Returning early from void methods prevents the banner from being inflated.
        AdBannerSetupFingerprint.methodOrNull?.let { method ->
            method.returnEarly()
        }

        // Disable promotional card rendering.
        PromotionalCardFingerprint.methodOrNull?.let { method ->
            method.returnEarly()
        }
    }
}
