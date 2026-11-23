# Quadrax (Android – DOS Edice)

Kooperativní 2D logická plošinovka pro Android inspirovaná kultovní československou hrou **Quadrax** (1994 ZX Spectrum / 1996 MS-DOS, studio Cauldron). Ovládáš dva archeology – **Ricka** a **Bena** – a pomocí přesné spolupráce, tlačení kamenných kvádrů a ovládání starověkých chrámových mechanismů je musíš oba bezpečně dostat do zlatého portálu.

📥 **Stáhnout APK pro Android:** [Poslední Release (v2.0.0)](https://github.com/PatrikKM/Quadrax/releases/latest)

Aplikace obsahuje **20 ručně navržených a matematicky ověřených úrovní**, autentické herní mechaniky z DOS verze, retro syntetizované 8-bitové ozvučení, volitelný CRT filtr, neomezené `Undo` (až 250 kroků) a přehledné dotykové ovládání pro režim na výšku i na šířku.

---

## Herní mechaniky

### Základní pravidla
* **Dva spolupracující hrdinové:** Libovolně přepínej mezi Rickem a Benem (`🔀 PŘEPNOUT`). Oba musí na konci stát ve stejném východovém portálu.
* **Kamenné kvádry (balvany):** Lze je tlačit po rovině, shazovat do propastí, zasypávat jimi smrtící bodáky (`^`) nebo je využít jako schod.
* **Výška pádu:** Hrdina přežije seskok nejvýše ze **2 bloků** – pád ze 3 a více bloků je smrtelný (lze jej ale ztlumit přistavením balvanu!).
* **Živá zarážka (Living Stopper):** Pokud za balvanem stojí druhý hrdina, kámen se neposune a první hrdina na něj může vylézt jako na schod.
* **Páky, nášlapné desky, brány a propadla:** Páky přepínají mechanismy trvale (lze je přepnout tam i zpět), nášlapné desky vyžadují stálé zatížení hrdinou nebo kamenem.

### Rozšířené mechaniky z DOS Quadraxu (1996)
* **Výtahy (`ElevatorConfig`):** Vertikální pohyblivé plošiny ovládané pákami nebo deskami. Bezpečně vyvážejí i svážejí **hrdiny i těžké balvany** a v horní poloze fungují jako pohyblivý most.
* **Rozpadávající se bloky (`~`):** Křehké pískovcové dlaždice, které se po opuštění hrdinou nebo balvanem nadobro zbortí — umožňují jednosměrné mosty i otevírání nových šachet.
* **Inverzní (XOR) a dvojité (AND) mechanismy:**
  * *Inverzní brány/propadla* jsou ve výchozím stavu otevřené a aktivací páky se **zavírají** (jedna páka tak současně otevírá jednu cestu a uzavírá druhou).
  * *Dvojité AND brány* mají barevné indikátory na překladu a otevřou se pouze při současné aktivaci všech požadovaných kanálů.
* **Teleporty pro hrdiny i kameny:** Obousměrné magické portály přenášejí jak postavy, tak protlačené či padající balvany (s ochranou proti teleportaci do obsazeného pole).

---

## Přehled úrovní (20 levelů)

Všechny úrovně jsou rozděleny do dvou částí – původních 10 výukových úrovní a 10 pokročilých DOS úrovní s plným využitím nových mechanik:

| # | Název | Podtitul / Hlavní mechanika |
| :-: | :--- | :--- |
| **1** | Chrám zapomenutých bohů | Základní trénink (balvany, páky, desky, brány) |
| **2** | Klamná podlaha | Představení propadel |
| **3** | Kvantový skok | Teleporty a prostorové anomálie |
| **4** | Zrcadlové sály | Křížová závislost hrdinů |
| **5** | Motýlí efekt | Řetězová reakce propadel |
| **6** | Štafeta | Jeden kámen, dvoje ruce |
| **7** | Ocelové čelisti | Základy bodáků a jejich zasypávání |
| **8** | Zrádná propast | Propadlo nad bodákem |
| **9** | Pochybná zkratka | Kombinatorika mechanismů |
| **10** | Tichá oběť | Dvojitý pád kamenů |
| **11** | **Živá zarážka** | Hrdina jako opora pro výstup přes balvan |
| **12** | **Most na heslo** | Otevření a opětovné zavření propadla |
| **13** | **Měkké přistání** | Vzdušný teleport a tlumení smrtelného pádu |
| **14** | **Dvojsečná páka** | Inverzní XOR brány a dvě přechodové komory |
| **15** | **Křehká stezka** | Rozpadávající se blok (nejprve most, poté šachta) |
| **16** | **Zdvihák faraonů** | Výtah vyvážející kameny i hrdinu do horního patra |
| **17** | **Kvantový kvádr** | Teleportace kamenů a správa volného výstupu portálu |
| **18** | **Váhy Osirise** | Dvojitý zámek (AND brána) a výtah jako most |
| **19** | **Kaskáda zkázy** | Křehký most, propadlo, dva teleporty a inverzní brána |
| **20** | **Hrobka Cauldronu** | Velké finále kombinující všechny DOS mechaniky |

> **Tip:** V dialogu výběru úrovně (`LVL X/20 ▼`) lze zapnout **`GOD: ON`** a okamžitě si vyzkoušet kterýkoli z 20 levelů. Každý level má navíc vlastní krokovou nápovědu pod tlačítkem **`❓`**.

---

## Ovládání a rozhraní

* **Režim na výšku (Portrait):** Kompaktní jednořádková horní lišta bez zalamování textu, jednořádkový klikatelný proužek s popisem (po klepnutí zobrazí plnou nápovědu), automaticky přizpůsobená `16:11` hrací plocha uprostřed a pohodlný D-Pad s tlačítky `🔀 PŘEPNOUT` a `⚙️ PÁKA` dole.
* **Režim na šířku (Landscape – handheld styl):**
  * **Levý panel:** výběr levelu, zkrácený název, počítadlo kroků a **D-Pad**.
  * **Střed:** maximálně velká centrovaná `16:11` hrací plocha přes celou výšku obrazovky bez překrývajících popisů.
  * **Pravý panel:** 2×2 ikonová tlačítka (`↩`, `🔄`, `🔊`/`🔇`, `❓`), přepínače `Rick`/`Ben` a tlačítka `🔀 PŘEPNOUT` a `⚙️ PÁKA`.

---

## Architektura projektu

```text
app/src/
├── main/java/com/example/quadrax/
│   ├── MainActivity.kt                      # Vstupní aktivita (Jetpack Compose)
│   ├── audio/
│   │   └── SoundManager.kt                  # Syntetizované 8-bitové zvuky (AudioTrack) + rozhraní SoundEffects
│   ├── data/
│   │   ├── model/
│   │   │   ├── Entities.kt                  # Datové modely (hrdinové, balvany, páky, desky, brány, propadla, výtahy, teleporty)
│   │   │   └── LevelDefinition.kt           # Definice úrovně na mřížce 16×11
│   │   └── manager/
│   │       ├── LevelRepository.kt           # Definice všech 20 úrovní a českých návodů
│   │       └── LevelProgressRepository.kt   # Ukládání postupu, nejlepších kroků a nastavení (SharedPreferences)
│   ├── engine/
│   │   └── QuadraxEngine.kt                 # Deterministický herní engine, fyzika, Undo a animace
│   └── ui/game/
│       ├── GameScreen.kt                    # Responsivní Portrait / Handheld Landscape rozvržení (FittedGameCanvasBox)
│       ├── GameCanvas.kt                    # Vykreslování chrámu, animací, výtahů, praskajících bloků a CRT filtru
│       └── TouchControls.kt                 # HUD, D-Pad, boční panely a dialogy (výhra, smrt, nápověda, výběr levelů)
└── test/java/com/example/quadrax/
    └── LevelSolvabilityTest.kt              # Automatický Macro-A* solver ověřující 100% dohratelnost všech 20 levelů
```

---

## Sestavení a ověření dohratelnosti

### Požadavky
* Android Studio / JDK 17+ (testováno na Gradle 9.7.1, AGP `9.6.0-alpha03`, Kotlin `2.2.10`, Jetpack Compose BOM `2026.02.01`)
* `minSdk = 29`, `targetSdk = 37`

### Příkazy

```powershell
# Spuštění automatického solveru a ověření dohratelnosti všech 20 levelů (+ test Undo)
.\gradlew.bat testDebugUnitTest --tests com.example.quadrax.LevelSolvabilityTest --info

# Sestavení debug APK
.\gradlew.bat assembleDebug

# Sestavení podepsaného release APK
.\gradlew.bat assembleRelease
```

### Poznámka k release buildu (AGP 9.6 alpha)
V `app/build.gradle.kts` je v `buildTypes.release` vypnuta experimentální částečná R8 optimalizace (`optimization { enable = false }`), protože v `AGP 9.6.0-alpha03` způsobovala při spuštění aplikace pád `IllegalAccessError` v `kotlin.collections`. Release APK je pro přímou instalaci do zařízení podepsáno pomocí lokálního `debug` keystore, čímž se při aktualizaci zachová uložený herní postup.
