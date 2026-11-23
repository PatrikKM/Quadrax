package com.example.quadrax.data.manager

import com.example.quadrax.data.model.*

object LevelRepository {

    const val TOTAL_LEVELS = 20

    fun getLevel(id: Int): LevelDefinition {
        return when (id) {
            1 -> level1
            2 -> level2
            3 -> level3
            4 -> level4
            5 -> level5
            6 -> level6
            7 -> level7
            8 -> level8
            9 -> level9
            10 -> level10
            11 -> level11
            12 -> level12
            13 -> level13
            14 -> level14
            15 -> level15
            16 -> level16
            17 -> level17
            18 -> level18
            19 -> level19
            20 -> level20
            else -> level20
        }
    }

    val level1 = LevelDefinition(
        id = 1,
        title = "Chrám zapomenutých bohů",
        subtitle = "Základní trénink",
        grid = listOf(
            "################",
            "#..............#",
            "#..............#",
            "#H.......#E...H#",
            "#H####...#####H#",
            "#H............H#",
            "#H............H#",
            "#H............H#",
            "#H.....####.####",
            "#H.....#########",
            "################"
        ),
        torches = listOf(TorchConfig(3, 1), TorchConfig(12, 1), TorchConfig(4, 6)),
        rickStart = GridPosition(2, 3),
        benStart = GridPosition(2, 9),
        boulders = listOf(
            BoulderState(0, 4, 3),
            BoulderState(1, 9, 7)
        ),
        levers = mapOf(
            "yellow" to LeverConfig(3, 3, "#facc15", "Žlutá páka"),
            "cyan" to LeverConfig(8, 7, "#22d3ee", "Azurová páka"),
            "green" to LeverConfig(11, 3, "#34d399", "Zelená páka")
        ),
        plates = mapOf(
            "magenta" to PlateConfig(11, 8, "#e879f9", "Purpurová deska"),
            "green" to PlateConfig(13, 7, "#34d399", "Zelená deska")
        ),
        gates = mapOf(
            "yellow" to GateConfig(5, 9, "#facc15"),
            "cyan" to GateConfig(1, 5, "#22d3ee"),
            "magenta" to GateConfig(12, 7, "#e879f9"),
            "green" to GateConfig(12, 3, "#34d399")
        ),
        exitPos = GridPosition(10, 3),
        log = "Dostaň oba hrdiny do zlatého portálu.",
        walkthrough = listOf(
            "S Rickem (nahoře vlevo) dojdi na Žlutou páku [3,3] a přepni ji (Páka) – otevře se Žlutá brána dole.",
            "S Rickem zatlač horní balvan doprava přes okraj římsy – balvan spadne dolů na [6,9].",
            "Přepni na Bena (Ben), projdi Žlutou bránou, vylez přes shozený balvan na střední plošinu a přepni Azurovou páku [8,7].",
            "S Benem zatlač druhý balvan doprava do prohlubně [11,8] (sepne Purpurovou desku a vytvoří most).",
            "S Benem přejdi přes balvan doprava a stoupni si na Zelenou desku [13,7].",
            "Přepni na Ricka, slez po levém žebříku dolů, přejdi přes oba balvany k pravému žebříku, vylez nahoru a přepni Zelenou páku [11,3].",
            "S oběma hrdiny dojdi do zlatého portálu [10,3]."
        )
    )

    val level2 = LevelDefinition(
        id = 2,
        title = "Klamná podlaha",
        subtitle = "Představení propadel",
        grid = listOf(
            "################",
            "#..............#",
            "#H............H#",
            "#H###......###H#",
            "#H............H#",
            "#H............H#",
            "#H............H#",
            "#H.....E......H#",
            "#H..#.####.#..H#",
            "################",
            "################"
        ),
        torches = listOf(TorchConfig(2, 1), TorchConfig(13, 1)),
        rickStart = GridPosition(2, 8),
        benStart = GridPosition(13, 8),
        boulders = listOf(
            BoulderState(0, 5, 2),
            BoulderState(1, 10, 2)
        ),
        levers = mapOf(
            "yellow" to LeverConfig(3, 2, "#facc15", "Žlutá páka"),
            "green" to LeverConfig(12, 2, "#34d399", "Zelená páka")
        ),
        plates = mapOf(
            "yellow" to PlateConfig(5, 8, "#facc15", "Žlutá deska"),
            "green" to PlateConfig(10, 8, "#34d399", "Zelená deska")
        ),
        trapdoors = mapOf(
            "yellow" to TrapdoorConfig(5, 3, "#facc15"),
            "green" to TrapdoorConfig(10, 3, "#34d399")
        ),
        gates = mapOf(
            "yellow" to GateConfig(6, 7, "#facc15"),
            "green" to GateConfig(9, 7, "#34d399")
        ),
        exitPos = GridPosition(7, 7),
        log = "Podlaha nemusí být vždy pevná. Nech spadnout balvany skrz propadla do prohlubní!",
        walkthrough = listOf(
            "S Rickem vylez po levém žebříku nahoru k Žluté páce [3,2] a přepni ji.",
            "Žluté propadlo [5,3] se otevře, levý balvan propadne dolů do prohlubně [5,8], sepne Žlutou desku a vytvoří most.",
            "S Benem vylez po pravém žebříku nahoru k Zelené páce [12,2] a přepni ji.",
            "Zelené propadlo [10,3] se otevře, pravý balvan propadne do prohlubně [10,8], sepne Zelenou desku a vytvoří most.",
            "S oběma hrdiny slez po žebřících dolů, vystup na vyvýšenou střední plošinu a přes shozené balvany dojdi do portálu [7,7]."
        )
    )

    val level3 = LevelDefinition(
        id = 3,
        title = "Kvantový skok",
        subtitle = "Teleporty a prostorové anomálie",
        grid = listOf(
            "################",
            "#H.............#",
            "#H...E.........#",
            "#H#######......#",
            "#H.............#",
            "#H.......#######",
            "#H.............#",
            "#H.............#",
            "#H.............#",
            "################",
            "################"
        ),
        torches = listOf(TorchConfig(4, 1), TorchConfig(12, 4)),
        rickStart = GridPosition(2, 8),
        benStart = GridPosition(3, 8),
        boulders = listOf(
            BoulderState(0, 10, 4)
        ),
        teleports = listOf(
            TeleportPair(5, 8, 13, 4, "#a855f7")
        ),
        plates = mapOf(
            "yellow" to PlateConfig(12, 8, "#facc15", "Žlutá deska")
        ),
        gates = mapOf(
            "yellow" to GateConfig(3, 2, "#facc15")
        ),
        exitPos = GridPosition(5, 2),
        log = "Magické víry propojí dvě vzdálená místa.",
        walkthrough = listOf(
            "S Benem dojdi doprava do fialového teleportu [5,8] – přenese tě na pravou plošinu [13,4].",
            "S Benem zatlač balvan [10,4] doleva přes okraj plošiny – balvan spadne do přízemí na [8,8].",
            "S Benem se vrať doprava do teleportu [13,4] (neskáč dolů z plošiny, pád ze 4 bloků je smrtelný!).",
            "Dole dotlač shozený balvan doprava na Žlutou desku [12,8] – otevře se Žlutá brána [3,2] nahoře.",
            "S oběma hrdiny vylez po levém žebříku úplně nahoru, projdi otevřenou bránou a vejdi do portálu [5,2]."
        )
    )

    val level4 = LevelDefinition(
        id = 4,
        title = "Zrcadlové sály",
        subtitle = "Křížová závislost",
        grid = listOf(
            "################",
            "#H...#....#...H#",
            "#H...#....#...H#",
            "#H####....####H#",
            "#H............H#",
            "#H............H#",
            "#H######.#####H#",
            "#H.....#......H#",
            "#H.....#......H#",
            "################",
            "################"
        ),
        torches = listOf(TorchConfig(2, 1), TorchConfig(13, 1), TorchConfig(4, 7)),
        rickStart = GridPosition(2, 2),
        benStart = GridPosition(13, 2),
        boulders = listOf(
            BoulderState(0, 10, 5)
        ),
        levers = mapOf(
            "yellow" to LeverConfig(3, 2, "#facc15", "Žlutá páka"),
            "green" to LeverConfig(13, 5, "#34d399", "Zelená páka")
        ),
        plates = mapOf(
            "magenta" to PlateConfig(8, 8, "#e879f9", "Purpurová deska")
        ),
        gates = mapOf(
            "yellow" to GateConfig(14, 4, "#facc15"),
            "green" to GateConfig(1, 4, "#34d399"),
            "magenta" to GateConfig(4, 8, "#e879f9")
        ),
        teleports = listOf(
            TeleportPair(12, 8, 2, 8, "#a855f7")
        ),
        exitPos = GridPosition(5, 8),
        log = "Spolupráce je klíč. Jeden bez druhého neudělá ani krok.",
        walkthrough = listOf(
            "S Rickem (nahoře vlevo) přepni Žlutou páku [3,2], čímž otevřeš Žlutou bránu [14,4] na pravém žebříku.",
            "S Benem (vpravo) slez po žebříku na střední patro a přepni Zelenou páku [13,5] – uvolníš levý žebřík Rickovi.",
            "S Benem zatlač balvan [10,5] o dvě pole doleva do šachty [8,5] – balvan spadne dolů na Purpurovou desku [8,8].",
            "S Benem slez po pravém žebříku úplně dolů a vejdi do fialového teleportu [12,8] (přenese tě doleva na [2,8]).",
            "S Rickem slez po levém žebříku úplně dolů a oba projděte otevřenou Purpurovou bránou [4,8] do portálu [5,8]."
        )
    )

    val level5 = LevelDefinition(
        id = 5,
        title = "Motýlí efekt",
        subtitle = "Řetězová reakce propadel",
        grid = listOf(
            "################",
            "#H............H#",
            "#H............H#",
            "#H####.#######H#",
            "#H............H#",
            "#H............H#",
            "#H#######.####H#",
            "#H............H#",
            "#H############H#",
            "################",
            "################"
        ),
        torches = listOf(TorchConfig(4, 1), TorchConfig(11, 1), TorchConfig(4, 7)),
        rickStart = GridPosition(2, 2),
        benStart = GridPosition(3, 2),
        boulders = listOf(
            BoulderState(0, 6, 2),
            BoulderState(1, 8, 5)
        ),
        levers = mapOf(
            "yellow" to LeverConfig(4, 2, "#facc15", "Žlutá páka")
        ),
        plates = mapOf(
            "cyan" to PlateConfig(6, 5, "#22d3ee", "Azurová deska"),
            "green" to PlateConfig(9, 7, "#34d399", "Zelená deska")
        ),
        trapdoors = mapOf(
            "yellow" to TrapdoorConfig(6, 3, "#facc15"),
            "cyan" to TrapdoorConfig(9, 6, "#22d3ee")
        ),
        gates = mapOf(
            "green" to GateConfig(11, 7, "#34d399")
        ),
        teleports = listOf(
            TeleportPair(2, 7, 10, 7, "#a855f7")
        ),
        exitPos = GridPosition(12, 7),
        log = "Připrav spodní kámen na propadlo a spusť řetězovou reakci!",
        walkthrough = listOf(
            "S Benem slez po levém žebříku do prostředního patra [1,5] a zatlač prostřední balvan [8,5] o jedno pole doprava na zavřené Azurové propadlo [9,5].",
            "S Rickem nahoře dojdi na Žlutou páku [4,2] a přepni ji.",
            "Spustí se řetězová reakce: horní balvan propadne Žlutým propadlem [6,3] na Azurovou desku [6,5], ta otevře Azurové propadlo [9,6] a druhý balvan dopadne na Zelenou desku [9,7]!",
            "Zelená brána [11,7] dole u východu se otevřela.",
            "S oběma hrdiny slez po levém žebříku dolů do fialového teleportu [2,7] a z [10,7] dojdi doprava do portálu [12,7]."
        )
    )

    val level6 = LevelDefinition(
        id = 6,
        title = "Štafeta",
        subtitle = "Jeden kámen, dvoje ruce",
        grid = listOf(
            "################",
            "#H............H#",
            "#H............H#",
            "#H#####....###H#",
            "#H............H#",
            "#H....######..H#",
            "#H............H#",
            "#H..######....H#",
            "#H............H#",
            "################",
            "################"
        ),
        torches = listOf(TorchConfig(2, 1), TorchConfig(13, 1), TorchConfig(8, 4)),
        rickStart = GridPosition(2, 2),
        benStart = GridPosition(13, 8),
        boulders = listOf(
            BoulderState(0, 5, 2)
        ),
        teleports = listOf(
            TeleportPair(12, 2, 10, 4, "#22d3ee")
        ),
        plates = mapOf(
            "yellow" to PlateConfig(5, 6, "#facc15", "Žlutá deska")
        ),
        gates = mapOf(
            "yellow" to GateConfig(8, 8, "#facc15")
        ),
        exitPos = GridPosition(10, 8),
        log = "Kámen musí urazit dlouhou cestu. Každý mu musí v jednom patře pomoci.",
        walkthrough = listOf(
            "S Rickem nahoře zatlač balvan [5,2] doprava přes okraj římsy na [7,2] – balvan spadne na prostřední plošinu [7,4].",
            "S Benem vylez po pravém žebříku úplně nahoru a vejdi do modrého teleportu [12,2] (přenese tě na prostřední plošinu [10,4]).",
            "S Benem zatlač balvan [7,4] doleva přes okraj na [5,4] – balvan spadne přímo na Žlutou desku [5,6] a otevře Žlutou bránu [8,8] dole.",
            "S Benem seskoč doleva přes plošinu s balvanem až do přízemí (každý seskok je jen o 1–2 bloky).",
            "S Rickem slez po levém žebříku dolů a oba projděte otevřenou bránou [8,8] do portálu [10,8]."
        )
    )

    val level7 = LevelDefinition(
        id = 7,
        title = "Ocelové čelisti",
        subtitle = "Základy bodáků",
        grid = listOf(
            "################",
            "#..............#",
            "#H............H#",
            "#H######......H#",
            "#H............H#",
            "#H.......#####H#",
            "#H............H#",
            "#H...........E.#",
            "#H######^^######",
            "################",
            "################"
        ),
        torches = listOf(TorchConfig(2, 1), TorchConfig(12, 1)),
        rickStart = GridPosition(2, 2),
        benStart = GridPosition(13, 4),
        boulders = listOf(
            BoulderState(0, 6, 2),
            BoulderState(1, 10, 4)
        ),
        exitPos = GridPosition(13, 7),
        log = "Kamenný kvádr shozený do bodáků vytvoří bezpečný most.",
        walkthrough = listOf(
            "S Rickem (nahoře vlevo) zatlač balvan [6,2] doprava přes okraj římsy na [8,2] – spadne přímo do levých bodáků [8,8].",
            "S Benem (vpravo uprostřed) zatlač balvan [10,4] doleva přes okraj na [9,4] – spadne do pravých bodáků [9,8].",
            "Oba smrtící bodáky dole jsou nyní vyplněné kameny a tvoří bezpečný most.",
            "S Rickem slez po levém žebříku dolů na [1,7] a přejdi doprava přes oba balvany až do portálu [13,7].",
            "S Benem slez po pravém žebříku dolů na [14,7] a připoj se k Rickovi v portálu [13,7]."
        )
    )

    val level8 = LevelDefinition(
        id = 8,
        title = "Zrádná propast",
        subtitle = "Propadlo nad bodákem",
        grid = listOf(
            "################",
            "#..............#",
            "##............H#",
            "##.......#####H#",
            "##............H#",
            "##..####.#####H#",
            "##............H#",
            "##..E.........H#",
            "########^#####H#",
            "################",
            "################"
        ),
        torches = listOf(TorchConfig(12, 1), TorchConfig(4, 4)),
        rickStart = GridPosition(13, 2),
        benStart = GridPosition(13, 7),
        boulders = listOf(
            BoulderState(0, 10, 2)
        ),
        levers = mapOf(
            "yellow" to LeverConfig(12, 7, "#facc15", "Žlutá páka")
        ),
        trapdoors = mapOf(
            "yellow" to TrapdoorConfig(8, 5, "#facc15")
        ),
        exitPos = GridPosition(4, 7),
        log = "Otevři propadlo uprostřed, aby shozený balvan zneškodnil bodáky v hlubině.",
        walkthrough = listOf(
            "S Benem (dole vpravo) přepni Žlutou páku [12,7] – otevře se Žluté propadlo [8,5] v prostředním patře.",
            "S Rickem (nahoře vpravo) zatlač balvan [10,2] doleva přes okraj na [8,2].",
            "Balvan proletí otevřeným propadlem [8,5] až dolů a vyplní smrtící bodák na [8,8]!",
            "S Rickem slez po pravém žebříku úplně dolů na [14,7].",
            "Oba hrdinové nyní bezpečně přejdou doleva přes zakrytý bodák do portálu [4,7]."
        )
    )

    val level9 = LevelDefinition(
        id = 9,
        title = "Pochybná zkratka",
        subtitle = "Kombinatorika mechanismů",
        grid = listOf(
            "################",
            "#H.......#....H#",
            "#H.......#.E..H#",
            "#H####...#####H#",
            "#H............H#",
            "#H............H#",
            "#H####^#######H#",
            "#H############H#",
            "#H............H#",
            "################",
            "################"
        ),
        torches = listOf(TorchConfig(4, 1), TorchConfig(7, 4)),
        rickStart = GridPosition(2, 2),
        benStart = GridPosition(11, 5),
        boulders = listOf(
            BoulderState(0, 5, 2),
            BoulderState(1, 8, 5)
        ),
        levers = mapOf(
            "cyan" to LeverConfig(3, 2, "#22d3ee", "Azurová páka")
        ),
        plates = mapOf(
            "green" to PlateConfig(4, 5, "#34d399", "Zelená deska")
        ),
        gates = mapOf(
            "cyan" to GateConfig(13, 2, "#22d3ee"),
            "green" to GateConfig(12, 2, "#34d399")
        ),
        exitPos = GridPosition(11, 2),
        log = "První kámen vyplní bodák a vytvoří most, po kterém přetlačíš druhý kámen na spínač.",
        walkthrough = listOf(
            "S Rickem (nahoře vlevo) přepni Azurovou páku [3,2] – otevře se Azurová brána [13,2] u východu.",
            "S Rickem zatlač horní balvan [5,2] doprava na [6,2] – spadne přímo do bodáku [6,6] a vyrovná podlahu v prostředním patře.",
            "S Benem v prostředním patře zatlač druhý balvan [8,5] doleva přes zakrytý bodák až na Zelenou desku [4,5] – otevře se Zelená brána [12,2].",
            "S Rickem slez po levém žebříku na [1,5], přelez přes balvan na Zelené desce [4,5] doprava k pravému žebříku [14,5].",
            "S oběma hrdiny vylez po pravém žebříku nahoru na [14,2] a projdi otevřenými branami do portálu [11,2]."
        )
    )

    val level10 = LevelDefinition(
        id = 10,
        title = "Tichá oběť",
        subtitle = "Královská logická past",
        grid = listOf(
            "################",
            "#H............H#",
            "#H.....#######H#",
            "#H............H#",
            "#H####.#######H#",
            "#H............H#",
            "#H............H#",
            "#H####^#######H#",
            "#H....#...#.E.H#",
            "################",
            "################"
        ),
        torches = listOf(TorchConfig(3, 1), TorchConfig(11, 5)),
        rickStart = GridPosition(12, 1),
        benStart = GridPosition(4, 8),
        boulders = listOf(
            BoulderState(0, 8, 1),
            BoulderState(1, 8, 3)
        ),
        levers = mapOf(
            "yellow" to LeverConfig(3, 8, "#facc15", "Žlutá páka")
        ),
        plates = mapOf(
            "magenta" to PlateConfig(5, 6, "#e879f9", "Purpurová deska")
        ),
        trapdoors = mapOf(
            "yellow" to TrapdoorConfig(6, 4, "#facc15")
        ),
        gates = mapOf(
            "magenta" to GateConfig(13, 8, "#e879f9")
        ),
        exitPos = GridPosition(12, 8),
        log = "Jeden kámen musí obětovat svůj pád do bodáku, aby druhý mohl sepnout spínač.",
        walkthrough = listOf(
            "S Benem (dole vlevo) dojdi na Žlutou páku [3,8] a přepni ji – otevře se Žluté propadlo [6,4].",
            "S Rickem (v nejvyšším patře) zatlač horní balvan [8,1] doleva přes okraj na [6,1] – balvan proletí otevřeným propadlem [6,4] a zakryje bodák [6,7].",
            "S Rickem slez po pravém žebříku o patro níž na [14,3] a zatlač druhý balvan [8,3] doleva na [6,3] – spadne skrz propadlo na první balvan a zastaví se na [6,6].",
            "S Rickem slez po pravém žebříku na [14,6], dojdi doleva k balvanu [6,6] a zatlač ho o jedno pole doleva na Purpurová deska [5,6] – otevře se Purpurová brána [13,8] dole.",
            "S Rickem slez po pravém žebříku dolů na [14,8] a vejdi doleva do portálu [12,8].",
            "S Benem vylez po levém žebříku na [1,6], přelez přes balvan na Purpurové desce [5,6] doprava k pravému žebříku [14,6], slez dolů na [14,8] a vejdi do portálu [12,8]."
        )
    )

    val level11 = LevelDefinition(
        id = 11,
        title = "Živá zarážka",
        subtitle = "Hrdina jako opora pro výstup",
        grid = listOf(
            "################",
            "#..............#",
            "#..............#",
            "#..............#",
            "#.............H#",
            "#.........####H#",
            "#.............H#",
            "#.......######H#",
            "#.............H#",
            "################",
            "################"
        ),
        torches = listOf(TorchConfig(3, 2), TorchConfig(12, 2), TorchConfig(4, 6)),
        rickStart = GridPosition(2, 8),
        benStart = GridPosition(9, 8),
        boulders = listOf(
            BoulderState(0, 5, 8),
            BoulderState(1, 12, 6)
        ),
        levers = mapOf(
            "yellow" to LeverConfig(9, 6, "#facc15", "Žlutá páka"),
            "cyan" to LeverConfig(12, 4, "#22d3ee", "Azurová páka")
        ),
        gates = mapOf(
            "yellow" to GateConfig(13, 8, "#facc15"),
            "cyan" to GateConfig(14, 5, "#22d3ee")
        ),
        exitPos = GridPosition(13, 4),
        log = "Když za balvanem stojí druhý hrdina, kámen neuhne a můžeš na něj vylézt!",
        walkthrough = listOf(
            "S Rickem zatlač spodní balvan [5,8] doprava na [7,8]; s Benem si stoupni pod římsu na [8,8] jako živá zarážka.",
            "S Rickem jdi doprava proti balvanu [7,8] – díky Benovi se kámen neposune a Rick vyleze na [7,7] a pak na střední patro [8,6].",
            "S Rickem přepni Žlutou páku [9,6] (otevře Žlutou bránu [13,8] dole) a ustup doleva na [8,6].",
            "S Benem projdi Žlutou bránou [13,8], vylez po pravém žebříku na [14,6] a zatlač horní balvan [12,6] doleva na [9,6].",
            "Ben teď stojí na [10,6] pod horní římsou jako druhá živá zarážka.",
            "S Rickem (z [8,6]) vylez přes zablokovaný balvan [9,6] až na nejvyšší patro [10,4] a přepni Azurovou páku [12,4].",
            "Otevře se Azurová brána [14,5] na žebříku – Ben vyleze nahoru a oba vstoupí do portálu [13,4]."
        )
    )

    val level12 = LevelDefinition(
        id = 12,
        title = "Most na heslo",
        subtitle = "Otevřít a znovu zavřít",
        grid = listOf(
            "################",
            "#H.............#",
            "#H###..........#",
            "#H............H#",
            "#######.###.##H#",
            "#..........#..H#",
            "#.............H#",
            "#..........#..H#",
            "#.............H#",
            "#######^########",
            "################"
        ),
        torches = listOf(TorchConfig(7, 1), TorchConfig(12, 1), TorchConfig(3, 6)),
        rickStart = GridPosition(2, 3),
        benStart = GridPosition(2, 8),
        boulders = listOf(
            BoulderState(0, 3, 1),
            BoulderState(1, 5, 3)
        ),
        levers = mapOf(
            "yellow" to LeverConfig(4, 8, "#facc15", "Žlutá páka")
        ),
        plates = mapOf(
            "green" to PlateConfig(11, 4, "#34d399", "Zelená deska")
        ),
        trapdoors = mapOf(
            "yellow" to TrapdoorConfig(7, 4, "#facc15")
        ),
        gates = mapOf(
            "green" to GateConfig(11, 8, "#34d399")
        ),
        exitPos = GridPosition(13, 8),
        log = "Páku lze přepnout i zpět! Jedním kamenem zasyp bodák, pak propadlo zavři pro druhý kámen.",
        walkthrough = listOf(
            "S Benem dole přepni Žlutou páku [4,8] – otevře se Žluté propadlo [7,4] nad bodákem.",
            "S Rickem nahoře zatlač spodní balvan [5,3] do otevřeného propadla [7,4] – spadne do bodáku [7,9].",
            "S Benem přepni Žlutou páku [4,8] ZPĚT – propadlo [7,4] se zavře a vytvoří pevný most.",
            "S Rickem vylez po krátkém levém žebříku na římsu [1,1], shoď horní balvan [3,1] dolů na [5,3] a přetlač ho přes zavřené propadlo až na Zelenou desku [11,4].",
            "Otevře se Zelená brána [11,8] – Rick sleze po pravém žebříku na [14,8] a s Benem se potkejte v portálu [13,8]."
        )
    )

    val level13 = LevelDefinition(
        id = 13,
        title = "Měkké přistání",
        subtitle = "Vzdušný teleport a tlumení pádu",
        grid = listOf(
            "################",
            "#......#.......#",
            "#......#.......#",
            "#......#.......#",
            "#......#....E.H#",
            "#......#...###H#",
            "#......#......H#",
            "#......###....H#",
            "#......#......H#",
            "################",
            "################"
        ),
        torches = listOf(TorchConfig(3, 5), TorchConfig(10, 2), TorchConfig(12, 7)),
        rickStart = GridPosition(2, 8),
        benStart = GridPosition(13, 8),
        boulders = listOf(
            BoulderState(0, 12, 8)
        ),
        teleports = listOf(
            TeleportPair(5, 8, 10, 5, "#a855f7", allowBoulders = false)
        ),
        levers = mapOf(
            "cyan" to LeverConfig(8, 6, "#22d3ee", "Azurová páka")
        ),
        plates = mapOf(
            "yellow" to PlateConfig(8, 8, "#facc15", "Žlutá deska")
        ),
        gates = mapOf(
            "cyan" to GateConfig(14, 7, "#22d3ee"),
            "yellow" to GateConfig(14, 6, "#facc15")
        ),
        exitPos = GridPosition(12, 4),
        log = "Výstup z teleportu je 3 bloky nad zemí! Přistav pod něj balvan, aby Rick přežil pád a dostal se na římsu.",
        walkthrough = listOf(
            "S Benem zatlač balvan [12,8] o dvě pole doleva na [10,8] – přesně pod fialový teleport [10,5].",
            "S Rickem (vlevo) vejdi do teleportu [5,8] – spadne jen o 2 bloky bezpečně na balvan [10,7]!",
            "S Rickem vylez z balvanu doleva na vyvýšenou římsu [9,6] a přepni Azurovou páku [8,6] (otevře Azurovou bránu [14,7]).",
            "S Rickem seskoč zpět doprava na zem a zatlač balvan z [10,8] doleva pod římsu na Žlutou desku [8,8] (otevře Žlutou bránu [14,6]).",
            "S oběma hrdiny vylez po pravém žebříku nahoru a dojdi do portálu [12,4]."
        )
    )

    val level14 = LevelDefinition(
        id = 14,
        title = "Dvojsečná páka",
        subtitle = "Inverzní brány a přechodová komora",
        grid = listOf(
            "################",
            "#.....#...#....#",
            "#..........H...#",
            "###########H.#.#",
            "#..........H...#",
            "#..........H...#",
            "#..........H...#",
            "#.....#...#H...#",
            "#..........H.E.#",
            "############^###",
            "################"
        ),
        torches = listOf(TorchConfig(3, 1), TorchConfig(13, 1), TorchConfig(3, 6)),
        rickStart = GridPosition(2, 2),
        benStart = GridPosition(2, 8),
        boulders = listOf(
            BoulderState(0, 4, 2)
        ),
        levers = mapOf(
            "yellow" to LeverConfig(3, 8, "#facc15", "Žlutá páka"),
            "cyan" to LeverConfig(7, 2, "#22d3ee", "Azurová páka")
        ),
        gates = mapOf(
            "yellow_in" to GateConfig(6, 2, "#facc15", channel = "yellow", inverted = false),
            "yellow_out" to GateConfig(10, 2, "#facc15", channel = "yellow", inverted = true),
            "cyan_in" to GateConfig(6, 8, "#22d3ee", channel = "cyan", inverted = false),
            "cyan_out" to GateConfig(10, 8, "#22d3ee", channel = "cyan", inverted = true)
        ),
        exitPos = GridPosition(13, 8),
        log = "Jedna páka první bránu otevírá, ale druhou (inverzní) zavírá! Využij střední komory jako propusti.",
        walkthrough = listOf(
            "S Benem (dole) přepni Žlutou páku [3,8] – otevře se levá Žlutá brána [6,2] nahoře (pravá [10,2] se zavře).",
            "S Rickem zatlač balvan [4,2] do horní střední komory na [9,2] a stoupni si na Azurovou páku [7,2].",
            "S Rickem přepni Azurovou páku [7,2] – otevře se levá Azurová brána [6,8] dole (pravá [10,8] se zavře).",
            "S Benem přepni Žlutou páku [3,8] ZPĚT (otevře pravou Žlutou bránu [10,2] nahoře) a vejdi do spodní komory [8,8].",
            "S Rickem přepni Azurovou páku [7,2] ZPĚT – otevře se pravá Azurová brána [10,8] pro Bena.",
            "S Rickem dotlač balvan [9,2] doprava přes žebřík do šachty [12,2] – zasype bodáky [12,9] dole.",
            "Rick sleze po žebříku [11,2] dolů a oba hrdinové přejdou přes zasypaný bodák do portálu [13,8]."
        )
    )

    val level15 = LevelDefinition(
        id = 15,
        title = "Křehká stezka",
        subtitle = "Rozpadávající se bloky",
        grid = listOf(
            "################",
            "#H.............#",
            "#H###..........#",
            "#H............H#",
            "#H#####~###.##H#",
            "#H............H#",
            "#H............H#",
            "#H..........#.H#",
            "#H............H#",
            "#######^###.####",
            "################"
        ),
        torches = listOf(TorchConfig(7, 1), TorchConfig(12, 1), TorchConfig(5, 6)),
        rickStart = GridPosition(2, 3),
        benStart = GridPosition(2, 8),
        boulders = listOf(
            BoulderState(0, 3, 1),
            BoulderState(1, 5, 3)
        ),
        plates = mapOf(
            "green" to PlateConfig(11, 9, "#34d399", "Zelená deska")
        ),
        gates = mapOf(
            "green" to GateConfig(12, 8, "#34d399")
        ),
        exitPos = GridPosition(13, 8),
        log = "Křehký blok se rozpadne, jakmile z něj odejdeš. Nejdřív po něm přetlač kámen, pak dírou shoď druhý!",
        walkthrough = listOf(
            "S Rickem zatlač spodní balvan [5,3] doprava přes křehký blok [7,4] až do šachty [11,3] – spadne do prohlubně na Zelenou desku [11,9].",
            "Jakmile Rick přejde přes [7,3], křehký blok [7,4] se za ním rozpadne a otevře novou šachtu nad bodákem!",
            "S Benem vylez po levém žebříku úplně nahoru na [1,1], shoď horní balvan [3,1] dolů na [5,3] a zatlač ho doprava do nově vzniklé díry [7,3] – zakryje bodák [7,9].",
            "S Benem slez po levém žebříku dolů, přejdi přes zasypaný bodák i balvan na Zelené desce [11,9] otevřenou bránou [12,8] do portálu [13,8].",
            "S Rickem slez po pravém žebříku dolů na [14,8] a připoj se k Benovi v portálu [13,8]."
        )
    )

    val level16 = LevelDefinition(
        id = 16,
        title = "Zdvihák faraonů",
        subtitle = "Výtahy pro hrdiny i kameny",
        grid = listOf(
            "################",
            "#..........#...#",
            "#..............#",
            "#######.#^.#####",
            "#........##....#",
            "#..............#",
            "#H.............#",
            "#H###..........#",
            "#H.............#",
            "#######.########",
            "################"
        ),
        torches = listOf(TorchConfig(3, 1), TorchConfig(13, 1), TorchConfig(12, 6)),
        rickStart = GridPosition(2, 2),
        benStart = GridPosition(2, 8),
        boulders = listOf(
            BoulderState(0, 3, 6),
            BoulderState(1, 5, 8)
        ),
        levers = mapOf(
            "yellow" to LeverConfig(4, 2, "#facc15", "Žlutá páka")
        ),
        elevators = listOf(
            ElevatorConfig(
                id = "el_yellow",
                x = 7,
                bottomY = 9,
                topY = 3,
                channel = "yellow",
                colorHex = "#facc15"
            )
        ),
        plates = mapOf(
            "green" to PlateConfig(10, 3, "#34d399", "Zelená deska")
        ),
        gates = mapOf(
            "green" to GateConfig(11, 2, "#34d399")
        ),
        exitPos = GridPosition(13, 2),
        log = "Výtah vyveze nahoru jak hrdiny, tak těžké balvany! Nalož oba kameny a nakonec vyvez Bena.",
        walkthrough = listOf(
            "S Benem dole zatlač spodní balvan [5,8] na spuštěný Žlutý výtah [7,8].",
            "S Rickem nahoře přepni Žlutou páku [4,2] – výtah vyveze balvan na [7,2].",
            "S Rickem zatlač vyvezený balvan doprava do bodáku [9,3], pak se vrať a přepni Žlutou páku [4,2] zpět dolů.",
            "S Benem vylez po krátkém žebříku na [1,6], shoď druhý balvan [3,6] na [5,8] a nalož ho na výtah [7,8]; Rick ho vyveze nahoru a dotlačí do prohlubně se Zelenou deskou [10,3].",
            "Rick znovu sjede výtahem dolů, Ben nastoupí na [7,8] a Rick ho pákou vyveze nahoru.",
            "Oba hrdinové přejdou přes usazené balvany a otevřenou Zelenou bránu [11,2] do portálu [13,2]."
        )
    )

    val level17 = LevelDefinition(
        id = 17,
        title = "Kvantový kvádr",
        subtitle = "Teleportace kamenů a volný výstup",
        grid = listOf(
            "################",
            "#..............#",
            "#..............#",
            "###########.####",
            "#.......#......#",
            "#.......#...E.H#",
            "#H......#...##H#",
            "#H###...#.....H#",
            "#H......#.....H#",
            "###########^#.##",
            "################"
        ),
        torches = listOf(TorchConfig(3, 1), TorchConfig(10, 1), TorchConfig(5, 5)),
        rickStart = GridPosition(2, 2),
        benStart = GridPosition(2, 8),
        boulders = listOf(
            BoulderState(0, 3, 6),
            BoulderState(1, 5, 8)
        ),
        teleports = listOf(
            TeleportPair(7, 8, 3, 1, "#a855f7", allowBoulders = true),
            TeleportPair(13, 2, 10, 8, "#22d3ee", allowBoulders = true)
        ),
        levers = mapOf(
            "yellow" to LeverConfig(5, 2, "#facc15", "Žlutá páka")
        ),
        trapdoors = mapOf(
            "yellow" to TrapdoorConfig(11, 3, "#facc15")
        ),
        plates = mapOf(
            "green" to PlateConfig(13, 9, "#34d399", "Zelená deska")
        ),
        gates = mapOf(
            "green" to GateConfig(14, 6, "#34d399")
        ),
        exitPos = GridPosition(12, 5),
        log = "Teleporty přenášejí i balvany! Dej ale pozor, abys kamenem nezablokoval cílový portál dřív, než projde první hrdina.",
        walkthrough = listOf(
            "S Benem zatlač spodní balvan [5,8] do fialového teleportu [7,8] – objeví se nahoře na [3,1] a spadne na [3,2].",
            "S Rickem nahoře přepni Žlutou páku [5,2] (otevře propadlo [11,3]) a zatlač balvan do propadla – zasype bodák [11,9].",
            "S Rickem zavři Žlutou páku [5,2] (propadlo se zavře) a projdi azurovým teleportem [13,2] dolů na [10,8] – pak ustup DOLEVA na [9,8]!",
            "S Benem shoď horní balvan [3,6] z římsy na [5,8] a pošli ho fialovým teleportem [7,8] nahoru (spadne na [3,2], výstup [3,1] zůstane volný!).",
            "S Benem projdi fialovým teleportem [7,8] na [3,1] (přistane na balvanu), seskoč doleva na [2,2] a zatlač balvan doprava do azurového teleportu [13,2].",
            "Balvan se objeví přímo před Rickem na [10,8] – Rick ho zatlačí doprava přes zakrytý bodák do prohlubně se Zelenou deskou [13,9].",
            "Otevře se Zelená brána [14,6] a uvolní se azurový teleport – Ben projde teleportem dolů a oba vylezou po pravém žebříku do portálu [12,5]."
        )
    )

    val level18 = LevelDefinition(
        id = 18,
        title = "Váhy Osirise",
        subtitle = "Dvojitý zámek (AND) a výtah jako most",
        grid = listOf(
            "################",
            "#..............#",
            "#..............#",
            "#...........#..#",
            "#H............E#",
            "#H######.##.####",
            "#H.........#...#",
            "#H###..........#",
            "#H.............#",
            "########.#######",
            "################"
        ),
        torches = listOf(TorchConfig(4, 2), TorchConfig(10, 2), TorchConfig(12, 6)),
        rickStart = GridPosition(2, 8),
        benStart = GridPosition(4, 8),
        boulders = listOf(
            BoulderState(0, 3, 6),
            BoulderState(1, 6, 8)
        ),
        plates = mapOf(
            "magenta" to PlateConfig(3, 8, "#e879f9", "Purpurová deska"),
            "yellow" to PlateConfig(11, 5, "#facc15", "Žlutá deska"),
            "cyan" to PlateConfig(12, 8, "#22d3ee", "Azurová deska")
        ),
        levers = mapOf(
            "magenta" to LeverConfig(10, 4, "#e879f9", "Purpurová páka")
        ),
        elevators = listOf(
            ElevatorConfig(
                id = "el_magenta",
                x = 8,
                bottomY = 9,
                topY = 5,
                channel = "magenta",
                colorHex = "#e879f9"
            )
        ),
        gates = mapOf(
            "exit_and" to GateConfig(
                x = 12,
                y = 4,
                colorHex = "#facc15",
                requiredChannels = listOf("yellow", "cyan"),
                requiredColors = listOf("#facc15", "#22d3ee")
            )
        ),
        exitPos = GridPosition(14, 4),
        log = "Dvojitá brána se otevře jen při současném sepnutí Žluté I Azurové desky. Výtah zároveň slouží jako most!",
        walkthrough = listOf(
            "Zatlač spodní balvan [6,8] na spuštěný Purpurový výtah [8,8].",
            "S Benem si stoupni na Purpurovou desku [3,8] – výtah vyveze balvan na [8,4] a přemostí propast v prostředním patře.",
            "S Rickem vylez po levém žebříku na [1,4] a zatlač vyvezený balvan doprava do prohlubně se Žlutou deskou [11,5].",
            "S Benem sejdi z Purpurové desky [3,8] (výtah sjede dolů), vylez po levém žebříku na římsu [1,6], shoď druhý balvan [3,6] na [5,8] a dotlač ho přes výtah na Azurovou desku [12,8]!",
            "Dvojitá brána [12,4] se otevře – Ben se vrátí na spuštěný výtah [8,8].",
            "S Rickem nahoře přepni Purpurovou páku [10,4] – výtah vyveze Bena nahoru a oba projdou do portálu [14,4]."
        )
    )

    val level19 = LevelDefinition(
        id = 19,
        title = "Kaskáda zkázy",
        subtitle = "Křehký most, propadlo a inverzní brána",
        grid = listOf(
            "################",
            "#H.............#",
            "#H###..........#",
            "#H............H#",
            "########~#.###H#",
            "#H............H#",
            "#H########.#####",
            "#H.........#.#.#",
            "#H.............#",
            "##########^#####",
            "################"
        ),
        torches = listOf(TorchConfig(7, 1), TorchConfig(12, 1), TorchConfig(5, 7)),
        rickStart = GridPosition(4, 3),
        benStart = GridPosition(2, 8),
        boulders = listOf(
            BoulderState(0, 3, 1),
            BoulderState(1, 6, 3)
        ),
        levers = mapOf(
            "yellow" to LeverConfig(4, 8, "#facc15", "Žlutá páka")
        ),
        plates = mapOf(
            "cyan" to PlateConfig(8, 5, "#22d3ee", "Azurová deska")
        ),
        trapdoors = mapOf(
            "yellow" to TrapdoorConfig(10, 4, "#facc15")
        ),
        gates = mapOf(
            "yellow_ladder" to GateConfig(1, 6, "#facc15", channel = "yellow", inverted = false),
            "yellow_exit" to GateConfig(13, 8, "#facc15", channel = "yellow", inverted = true),
            "cyan" to GateConfig(11, 8, "#22d3ee")
        ),
        teleports = listOf(
            TeleportPair(2, 5, 2, 1, "#38bdf8", allowBoulders = false),
            TeleportPair(12, 5, 7, 8, "#a855f7", allowBoulders = false)
        ),
        exitPos = GridPosition(14, 8),
        log = "Žlutá páka otevírá propadlo a žebřík, ale zavírá východ! Zkombinuj křehký blok, propadlo a teleporty.",
        walkthrough = listOf(
            "S Benem dole přepni Žlutou páku [4,8] – otevře Žluté propadlo [10,4] i levý žebřík [1,6] (ale zavře inverzní bránu [13,8]).",
            "S Rickem nahoře přetlač pravý balvan [6,3] přes křehký blok [8,4] do otevřeného propadla [10,3] – zasype bodák [10,9] a křehký blok se za Rickem rozpadne.",
            "S Rickem slez po pravém žebříku na [14,5] a fialovým teleportem [12,5] se přenes dolů na [7,8].",
            "S Benem vylez po levém žebříku na [1,5], modrým teleportem [2,5] se přenes na horní římsu [2,1], shoď levý balvan [3,1] na [5,3], slez po krátkém žebříku [1,1] dolů a zatlač balvan do rozpadlé díry [8,3] na Azurovou desku [8,5].",
            "S Benem vylez po krátkém žebříku zpět na [2,1], modrým teleportem a levým žebříkem se vrať dolů a přepni Žlutou páku [4,8] ZPĚT – otevře se inverzní brána [13,8].",
            "Oba hrdinové přejdou přes zasypaný bodák a obě otevřené brány do portálu [14,8]."
        )
    )

    val level20 = LevelDefinition(
        id = 20,
        title = "Hrobka Cauldronu",
        subtitle = "Velké finále všech DOS mechanik",
        grid = listOf(
            "################",
            "#..........##..#",
            "#........#.....#",
            "######~#########",
            "#H............H#",
            "#H#####.######H#",
            "#H............H#",
            "#H###.........H#",
            "#H............H#",
            "#######.########",
            "################"
        ),
        torches = listOf(TorchConfig(3, 1), TorchConfig(13, 1), TorchConfig(11, 6)),
        rickStart = GridPosition(2, 8),
        benStart = GridPosition(4, 8),
        boulders = listOf(
            BoulderState(0, 3, 6),
            BoulderState(1, 6, 8)
        ),
        levers = mapOf(
            "green" to LeverConfig(3, 8, "#34d399", "Zelená páka")
        ),
        plates = mapOf(
            "yellow" to PlateConfig(8, 2, "#facc15", "Žlutá deska"),
            "cyan" to PlateConfig(6, 4, "#22d3ee", "Azurová deska")
        ),
        elevators = listOf(
            ElevatorConfig(
                id = "el_green",
                x = 7,
                bottomY = 9,
                topY = 5,
                channel = "green",
                colorHex = "#34d399"
            )
        ),
        gates = mapOf(
            "green_inv" to GateConfig(11, 2, "#34d399", channel = "green", inverted = true),
            "cyan_ladder" to GateConfig(14, 6, "#22d3ee", channel = "cyan", inverted = false),
            "exit_and" to GateConfig(
                x = 12,
                y = 2,
                colorHex = "#facc15",
                requiredChannels = listOf("yellow", "cyan"),
                requiredColors = listOf("#facc15", "#22d3ee")
            )
        ),
        teleports = listOf(
            TeleportPair(11, 4, 3, 1, "#a855f7", allowBoulders = true)
        ),
        exitPos = GridPosition(14, 2),
        log = "Velké finále! Zkombinuj výtah, teleport kamenů, křehký most, inverzní páku a dvojitý zámek.",
        walkthrough = listOf(
            "S Benem zatlač spodní balvan [6,8] na spuštěný Zelený výtah [7,8] a přepni Zelenou páku [3,8] – výtah vyveze balvan na [7,4].",
            "S Rickem vylez po levém žebříku na [1,4] a zatlač balvan do fialového teleportu [11,4] – objeví se na [3,1] a spadne na [3,2].",
            "S Rickem projdi fialovým teleportem [11,4] na [3,1] (přistane na balvanu), seskoč doleva na [2,2] a zatlač balvan přes křehký blok [6,3] až ke zdi na Žlutou desku [8,2].",
            "Křehký blok [6,3] se za Rickem rozpadne – Rick seskočí (bezpečné 2 bloky) vzniklou dírou na [6,4] a sleze po levém žebříku dolů k Zelené páce [3,8].",
            "S Benem dole přepni Zelenou páku [3,8] ZPĚT (výtah sjede), shoď druhý balvan z římsy [3,6] na [5,8], nalož ho na výtah [7,8] a vyvez nahoru (Zelená páka ON).",
            "S Benem vylez na [1,4], pošli druhý balvan fialovým teleportem [11,4] nahoru, sám projdi za ním na [3,1], seskoč na [2,2] a zatlač balvan do díry [6,2] na Azurovou desku [6,4]!",
            "Druhý balvan odemkne pravý žebřík [14,6], aktivuje druhý zámek brány A opraví přechod přes díru [6,3].",
            "Rick dole přepne Zelenou páku [3,8] ZPĚT (otevře inverzní Zelenou bránu [11,2]), přejde vpravo na odemčený žebřík [14,6], fialovým teleportem [11,4] nahoru a oba projdou přes opravenou díru i schod [8,2]–[9,2] do portálu [14,2]!"
        )
    )
}
