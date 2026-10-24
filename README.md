# Smart Pantry Manager

A Java Android application designed to track leftover ingredients, reduce household food waste, and suggest recipes based strictly on available pantry items.

---

## Project Overview & Concept
The **Smart Pantry Manager** helps users cut food waste by tracking ingredients currently available in their pantry and recommending recipes they can cook without requiring a grocery trip or buying additional items.

### Core Features
**Pantry Management (Full CRUD):** Add, view, edit, and delete ingredients with parameters including Name, Quantity, Unit (`items`, `litres`, `grams`), and an optional Expiry Date.
**Strict-Matching Recipe Engine:** Only suggests recipes when **100% of required ingredients** are present in the pantry in equal or greater quantities.
**Recipe Collection & Detail View:** Seeded database of pre-loaded recipes showing full ingredient lists and step-by-step preparation methods.
**Settings & User Preferences:**
**Expiry Notices Toggle:** Turn on/off alerts for items approaching their expiration date.
**Dark / Light Mode:** Customizable app theme using `AppCompatDelegate`.
**Zero-Match Feedback:** Clear visual guidance when no recipes strictly match the current pantry contents.

---

## Architecture & Technical Stack

- **Language:** Java (Android SDK)
- **IDE:** Android Studio
- **Architecture/Components:** Activities, Custom RecyclerView Adapters, Layout Inflaters, System Intents, EdgeToEdge UI.
- **Database Choice & Justification:** **SQLite / SharedPreferences**
  - *Justification:* On-device local persistence ensures complete offline availability, zero latency for instant strict-matching calculations, lightweight memory overhead, and true data persistence across application restarts without relying on external network conditions or third-party APIs.

---

## App Structure & Screen Navigation

1. **Pantry List Activity:** Displays current ingredients using a dynamic `RecyclerView` bound to local storage.
2. **Add / Edit Ingredient Activity:** Form input with validation for adding or modifying pantry items.
3. **Suggested Recipes Activity:** Evaluates pantry contents using the strict-matching logic to filter eligible recipes.
4. **Recipe Detail Activity:** Displays full preparation steps and complete ingredient breakdown for selected recipes.
5. **Settings Activity:** User preferences screen for Expiry Reminders and Dark/Light Mode appearance.

---

## Strict-Matching Business Logic Rule

The application strictly enforces a **100% match requirement**:
- A recipe is **only** recommended if *every single required ingredient* exists in the user's pantry in sufficient quantity.
- If a recipe requires 5 ingredients and the user only has 4, the recipe is strictly excluded from suggestions.
- Ingredient matching normalizes string inputs (case-insensitive and unit handling) to ensure robust comparisons across entry variants.

---

## Out of Scope Declarations
Per section 3.3 of the assignment brief:
- No Google Maps, location SDKs, or GPS services are utilized.
- No payment processing or financial transactions.

---

## Setup & Execution Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Razarplayz1/SmartPantryManager
   ```
2. Open the `SmartPantryManager` folder in **Android Studio** and let Gradle sync.
3. Create/start an emulator (API 24+; API 34+ recommended) or plug in a device.
4. Press **Run** on the `app` configuration.

To run the unit tests (strict-matching engine): `./gradlew test`
