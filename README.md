# 🧩 WhatsApp Clean Experience Patches

Enhanced WhatsApp experience through Morphe patches.

> **Independent community project.** Not affiliated with WhatsApp, Meta, or Morphe.

## ❓ About

WhatsApp Clean Experience provides patches for WhatsApp that:

- **Promotional UI Cleanup** — Reduces supported promotional and advertising-related interface elements.
- **Meta AI UI Cleanup** — Hides supported Meta AI-related interface entry points.
- **Patch Settings** — Adds a native "Pengaturan Patch" menu into WhatsApp Settings.

### How to use these patches

Click the button below, or manually add this repo URL to Morphe Manager:

<a href="https://morphe.software/add-source?github=mridwanfirdaus2603/whatsapp">
  <img src=".github/assets/add-to-morphe.svg" alt="Add to Morphe" height="48">
</a>

## 🩹 Patches

<!-- PATCHES_START -->

| Patch | Description |
|---|---|
| **Promotional UI Cleanup** | Reduces supported promotional and advertising-related interface elements. |
| **Meta AI UI Cleanup** | Hides supported Meta AI-related interface entry points. |
| **Patch Settings** | Adds a native Patch Settings menu into WhatsApp Settings. |

<!-- PATCHES_END -->

## 📱 Supported WhatsApp Versions

| Version | Status |
|---|---|
| **2.26.46.76** | ✅ Target version (APKM) |
| Other versions | ⚠️ Experimental — may work but untested |

## 📦 Installation

1. Install [Morphe Manager](https://morphe.software).
2. Add this patch source to Morphe Manager (use the button above or add manually).
3. Download WhatsApp **2.26.46.76** APKM from APKMirror.
4. Select patches to apply in Morphe Manager.
5. Patch and install.

## ⚙️ Configuration

Patch options are configured in **Morphe Manager** before patching:

| Option | Default | Description |
|---|---|---|
| Hide promotional elements | ✅ Enabled | Reduces promotional/advertising UI elements |
| Hide Meta AI | ✅ Enabled | Hides Meta AI interface entry points |
| Patch Settings | ✅ Enabled | Adds "Pengaturan Patch" to WhatsApp Settings |

> **Note:** Configuration changes require repatching through Morphe Manager. 
> Runtime toggle switches are not supported by the Morphe patch architecture.

## 🔧 Patch Settings Menu

When the Patch Settings patch is applied, a new "**Pengaturan Patch**" entry appears 
in WhatsApp's Settings screen. This menu shows:

- **Pembersihan Iklan** — Current promotional cleanup status.
- **Meta AI** — Current Meta AI cleanup status.
- **Informasi Patch** — Patch version, WhatsApp version, compatibility status.

The settings display is informational and shows the current compiled configuration.

## ⚠️ Known Limitations

- **Compile-time configuration only.** Settings cannot be toggled at runtime. Changes require repatching.
- **Fingerprint-based detection.** Patches rely on fingerprinting methods by string constants and resource IDs. Untested WhatsApp versions may not match.
- **No complete ad removal guarantee.** Only identifiable promotional elements are targeted.
- **UI hiding only for Meta AI.** Backend services and infrastructure are not modified.
- **NOT RUNTIME VERIFIED** unless explicitly tested on a device with the target WhatsApp version.

## 🛠️ Building

### Remote (Recommended)

Push changes to the `dev` branch. GitHub Actions will automatically build and generate the `.mpp` artifact.

### Locally

```bash
./gradlew buildAndroid
```

The built patches `.mpp` file is found in `patches/build/libs/patches-*.mpp`.

## 🔒 Disclaimer

- This is an **independent community project**.
- Not affiliated with WhatsApp, Meta, Morphe, or any other entity.
- Does not claim to remove all advertisements or disable Meta AI services.
- Use at your own risk.
- Modifying WhatsApp may violate its Terms of Service.

## 📜 License

WhatsApp Clean Experience Patches are licensed under the [GNU General Public License v3.0](LICENSE).
