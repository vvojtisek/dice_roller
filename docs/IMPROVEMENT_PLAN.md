# Improvement plan

Goal: turn Dice Roller from a Czech-only prototype into a free app that anyone can install, understand and use within seconds.

Each item lists *why* it matters, with evidence from the current code, and *what done looks like*. Phases are ordered: each one unblocks the next. Effort estimates assume one developer: **S** is under a day, **M** is one to three days, **L** is about a week.

---

## Phase 0: Before the first public release

These decisions are expensive or impossible to change after users have installed the app.

| # | Item | Why | Done when | Effort |
| --- | --- | --- | --- | --- |
| 0.1 | **Choose a license** | Without one, the code is "all rights reserved", nobody may legally fork or contribute, and F-Droid will not list the app. Common choices are MIT or Apache-2.0 (permissive) and GPL-3.0 (forks must stay open). | `LICENSE` file added, and the README updated | S |
| 0.2 | **Set the permanent application ID** | `applicationId` is the AI Studio placeholder `com.aistudio.diceroller.tkgvyp` and `namespace` is `com.example`. Android and the stores treat a different ID as a different app, so it cannot change after release. | For example `io.github.vvojtisek.diceroller`; namespace and packages renamed to match; `ExampleInstrumentedTest` updated | S |
| 0.3 | **Remove unused dependencies** | Firebase AI, Firebase App Check, Retrofit, OkHttp 4.10, Moshi, Room, the google-services plugin and the secrets plugin (with `GEMINI_API_KEY`) are declared but not used by any code. They make the APK bigger, probably add network permissions through manifest merging (check with `./gradlew :app:processDebugManifest`), cause dependency alerts that don't apply, and keep the app out of F-Droid, which rejects proprietary Firebase libraries. | Removed from `app/build.gradle.kts` and `libs.versions.toml`; `.env.example` and `metadata.json` deleted; merged manifest has no `INTERNET` permission | S |
| 0.4 | **Create and back up the release signing key** | Every update must be signed with the same key. If the key is lost, users have to uninstall and reinstall. | Keystore stored in a password manager or offline backup; three repository secrets set (README, "Repository setup", step 5) | S |
| 0.5 | **Apply the repository settings** | Required checks, release pull requests and security alerts depend on GitHub settings that cannot live in the repository. | All five steps in README, "Repository setup" done, and private vulnerability reporting enabled (Settings → Code security) | S |
| 0.6 | **Enable R8 (code shrinking)** | `isMinifyEnabled = false` ships unused code. Expect a noticeably smaller APK, especially after 0.3. kotlinx.serialization includes its own keep rules. | Release build minified, and E2E tests pass against a minified build | S |

## Phase 1: Make it understandable outside the Czech Republic (internationalization)

The interface currently has 107 hard-coded string literals with Czech diacritics, 54 `text = "…"` literals and 17 `contentDescription` literals in `MainActivity.kt`. Some of them are English ("Toggle Mute", "Looks Fair", "Looks Biased"), so the app is currently mixed-language for everyone.

| # | Item | Why / how | Done when | Effort |
| --- | --- | --- | --- | --- |
| 1.1 | **Move every user-visible string into resources** | Use `stringResource(R.string.…)`. `res/values/strings.xml` holds English, the default for every unknown locale. `res/values-cs/strings.xml` holds the current Czech text. | No user-visible literal strings remain in Kotlin. A Lint check (`HardcodedText`, plus a custom grep in CI) fails on new ones | M |
| 1.2 | **Use Android plurals** | Czech has three plural forms (1 kostka, 2–4 kostky, 5+ kostek), which are hand-coded in `when` blocks today. `<plurals>` handles every language's rules. | Every count-dependent text uses `pluralStringResource` | S |
| 1.3 | **Store stable IDs, not Czech names** | `dice_colors` in DataStore stores theme *names* such as "Modrá skalice", and test tags are built from them (`theme_option_modrá_skalice`). Translated names would break saved settings. `getChemistryThemeByName` already accepts IDs, so a migration is simple. | Persisted values and test tags use `ChemistryDieTheme.id`; old saved names are migrated on load and covered by a unit test | S |
| 1.4 | **Localize the default profile and presets** | "Základní", "Body (D6)" and "Barevná" are saved as data on first launch. | Default presets store a resource key or a "default" flag, and are shown in the device language unless the user renamed them | S |
| 1.5 | **Per-app language picker** | Android 13+ lets users pick a language per app. Add `locales_config.xml` and an in-app picker for older versions through `AppCompatDelegate.setApplicationLocales`. | Language can be changed in system settings and in the app | S |
| 1.6 | **Community translations** | Use [Weblate](https://hosted.weblate.org/hosting/), which is free for libre projects (requires 0.1), or Crowdin, which is free for open source. Either syncs `strings.xml` through pull requests. | Project registered and linked from the README; at least English and Czech complete, others welcome | S |
| 1.7 | **Localized store listing and screenshots** | Users decide from the listing. Fastlane `metadata/android/<locale>/` folders work for both F-Droid and Google Play. | English and Czech descriptions and screenshots in the repository | S |

## Phase 2: Make it easy to use

| # | Item | Evidence in current code | Done when | Effort |
| --- | --- | --- | --- | --- |
| 2.1 | **An obvious "Roll" button** | The only way to roll is to tap the dice area, explained by a small caption ("KLEPNI NA KOSTKY PRO HOD"). | A large primary button below the dice; tapping the dice still works | S |
| 2.2 | **Screen reader support** | `dice_throw_area` is a bare `clickable` with no role or label, and the result is not announced, so TalkBack users cannot use the core feature. The code base has no `semantics` modifiers. | Roll control has `Role.Button` and a label, the result is announced through a live region, and an accessibility E2E test checks both | S |
| 2.3 | **Remember settings between launches** | Dark theme, mute, shake, dice count and die type use `remember`, so they reset on every launch and every screen rotation. `isDarkTheme` always starts as `true`. | Settings saved to DataStore; the theme follows the system by default, with a manual override | M |
| 2.4 | **Keep history and statistics on rotation** | History and statistics live in `remember` inside the composable. Rotating the phone wipes them. | State moved to a `ViewModel` with unit tests; optionally persisted | M |
| 2.5 | **Consistent dice limits** | Settings offer 1–3 dice, while presets (`DicePreset.count`) and the playground support 4. | One limit everywhere, from `MAX_DICE` in `DiceLogic.kt` | S |
| 2.6 | **Readable selected states** | In settings, the die-type button label uses the muted text color even when the button is selected (blue background). | All text meets WCAG AA contrast (4.5:1) in both themes | S |
| 2.7 | **Remove the "Exit" button** | It calls `finish()`. Android apps do not have exit buttons; Back and Home already do this, and the button takes space in a crowded top bar. | Removed; top bar actions reduced to the essentials, with the rest moved to an overflow menu | S |
| 2.8 | **Haptics and reduced motion** | Rolling has sound but no vibration, and the animation ignores the system "Remove animations" setting. | Short haptic tick on landing; animation shortened when animations are disabled | S |
| 2.9 | **First-run hint** | New users do not discover presets, profiles or shake to roll. | A dismissible one-time hint, stored in DataStore | S |
| 2.10 | **Tablet, landscape and foldable layouts** | The layout is a single column with a 500 dp maximum width. | Two-pane layout on wide screens (dice and history side by side) | M |

## Phase 3: Features people ask for in dice apps

Do these after phases 1 and 2. They broaden the audience from board games to tabletop RPGs.

- **Mixed rolls and modifiers:** `2d6 + 1d20 + 3` in one roll, with roll notation typed or built with buttons.
- **d100 (percentile)** and custom dice, such as a d6 with your own labels for party games or classroom use.
- **Keep or reroll individual dice**, as needed for games like Yahtzee.
- **Share a result** as text.
- **Home-screen widget** and quick settings tile for a one-tap roll.

## Phase 4: Engineering health

| # | Item | Why | Effort |
| --- | --- | --- | --- |
| 4.1 | **Split `MainActivity.kt`** | It is about 4,300 lines and 190 KB in one file. Split it by feature: audio, themes, profiles, settings, statistics and history UI. The pure logic has already moved to `DiceLogic.kt`; continue that pattern. | M |
| 4.2 | **Screenshot regression tests in CI** | Roborazzi is configured, but `GreetingScreenshotTest` only records. Re-record the baselines on CI's Linux renderer, then run `verifyRoborazziDebug` in `ci.yml`. | S |
| 4.3 | **Fix the fairness check's statistics** | The chi-square approximation is only valid when every face expects at least about 5 rolls, so a d20 needs 100 or more rolls. Below that, the app still shows "Looks Biased" or "Looks Fair". Also, re-testing after every roll at p < 0.01 means a fair die will eventually be flagged in a long session (multiple comparisons). Show "not enough rolls yet" below 5 × sides, and word the verdict as a hint rather than a finding. | S |
| 4.4 | **Update dependencies** | Compose BOM 2024.09.00, Lifecycle 2.8.7 and OkHttp 4.10.0 are old. Dependabot will now propose updates weekly; merge them while CI is green. | Ongoing |
| 4.5 | **Make Lint stricter** | After the string extraction, enable `warningsAsErrors` for a curated set of checks (accessibility, `HardcodedText`, security). | S |
| 4.6 | **Measure startup and roll performance** | Add a Macrobenchmark module if users report jank on low-end devices. | M |

## Phase 5: Distribution

| Channel | Cost | Requirements | Notes |
| --- | --- | --- | --- |
| **GitHub Releases** | Free | Done (release workflow) | Works with Obtainium for automatic updates |
| **F-Droid** | Free | 0.1 (FOSS license) and 0.3 (no proprietary libraries); reproducible build recommended | The best fit for a free, ad-free app; F-Droid builds and signs it |
| **IzzyOnDroid** | Free | APK on GitHub Releases | Faster to get listed than F-Droid |
| **Google Play** | US$25 one-time | Developer account, privacy policy ("no data collected"), Data Safety form, AAB (already built by the release workflow), closed testing period for new personal accounts | Largest reach; add a `play-publish` job to `release.yml` later |

---

## Suggested order

1. **Week 1:** Phase 0, then publish v1.x on GitHub Releases.
2. **Weeks 2–3:** Phase 1 (1.1–1.5), then the first English release, and submit to F-Droid.
3. **Weeks 4–5:** Phase 2 (2.1–2.5 first, because they matter most to users).
4. **After that:** Phases 3 and 4, based on user feedback in Issues.
