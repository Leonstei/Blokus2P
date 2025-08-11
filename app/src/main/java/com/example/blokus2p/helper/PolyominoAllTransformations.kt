package com.example.blokus2p.helper

import com.example.blokus2p.model.PolyominoVariant

val polyominoVariants: Map<PolyominoNames,List<PolyominoVariant>> =
    mapOf(
        PolyominoNames.FÜNF to listOf(
            PolyominoVariant(cells = listOf(17, 33, 49, 65, 81), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(17, 33, 49, 65, 81), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 19, 20, 21), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 19, 20, 21), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 33, 49, 65, 81), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17, 33, 49, 65, 81), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(17, 18, 19, 20, 21), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 18, 19, 20, 21), isFlipped = true, rotation = 270)
        ),
        PolyominoNames.FÜNF_ZL to listOf(
            PolyominoVariant(cells = listOf(17, 33, 34, 50, 66), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(18, 33, 34, 49, 65), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(19, 20, 33, 34, 35), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 34, 35, 36), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 33, 49, 50, 66), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(18, 34, 49, 50, 65), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(18, 19, 20, 33, 34), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 18, 19, 35, 36), isFlipped = true, rotation = 270)
        ),
        PolyominoNames.FÜNF_7 to listOf(
            PolyominoVariant(cells = listOf(17, 33, 34, 35, 50), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(19, 33, 34, 35, 50), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(18, 19, 33, 34, 50), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 34, 35, 50), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(18, 33, 34, 35, 51), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(18, 33, 34, 35, 49), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(18, 34, 35, 49, 50), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(18, 33, 34, 50, 51), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.FÜNF_L to listOf(
            PolyominoVariant(cells = listOf(17, 33, 49, 65, 66), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(18, 34, 50, 65, 66), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 19, 20, 33), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 19, 20, 36), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 34, 50, 66), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17, 18, 33, 49, 65), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(20, 33, 34, 35, 36), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 33, 34, 35, 36), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.FÜNF_SMAL_T to listOf(
            PolyominoVariant(cells = listOf(17, 33, 34, 49, 65), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(18, 33, 34, 50, 66), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 19, 20, 35), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 19, 20, 34), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(18, 34, 49, 50, 66), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17, 33, 49, 50, 65), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(18, 33, 34, 35, 36), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(19, 33, 34, 35, 36), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.FÜNF_W to listOf(
            PolyominoVariant(cells = listOf(17, 33, 34, 50, 51), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(19, 34, 35, 49, 50), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(18, 19, 33, 34, 49), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 34, 35, 51), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 34, 35, 51), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(18, 19, 33, 34, 49), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(19, 34, 35, 49, 50), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 33, 34, 50, 51), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.FÜNF_Z to listOf(
            PolyominoVariant(cells = listOf(17, 33, 34, 35, 51), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(19, 33, 34, 35, 49), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(18, 19, 34, 49, 50), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 34, 50, 51), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 33, 34, 35, 51), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(19, 33, 34, 35, 49), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(18, 19, 34, 49, 50), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 18, 34, 50, 51), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.FÜNF_LANG_L to listOf(
            PolyominoVariant(cells = listOf(17, 33, 49, 50, 51), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(19, 35, 49, 50, 51), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 19, 33, 49), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 19, 35, 51), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 19, 35, 51), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17, 18, 19, 33, 49), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(19, 35, 49, 50, 51), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 33, 49, 50, 51), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.FÜNF_C to listOf(
            PolyominoVariant(cells = listOf(17, 33, 18, 19, 35), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(17, 33, 18, 19, 35), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 34, 49, 50), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 33, 49, 50), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 19, 33, 34, 35), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17, 19, 33, 34, 35), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(17, 18, 33, 49, 50), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 18, 34, 49, 50), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.FÜNF_BLOCK to listOf(
            PolyominoVariant(cells = listOf(17, 18, 33, 34, 50), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 33, 34, 49), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(18, 19, 33, 34, 35), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 33, 34, 35), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 33, 34, 49, 50), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(18, 33, 34, 49, 50), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(17, 18, 19, 33, 34), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 18, 19, 34, 35), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.FÜNF_T to listOf(
            PolyominoVariant(cells = listOf(17, 18, 19, 34, 50), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 19, 34, 50), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(19, 33, 34, 35, 51), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 33, 34, 35, 49), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(18, 34, 49, 50, 51), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(18, 34, 49, 50, 51), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(17, 33, 34, 35, 49), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(19, 33, 34, 35, 51), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.FÜNF_PLUS to listOf(
            PolyominoVariant(cells = listOf(18, 33, 34, 50, 35), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(18, 33, 34, 50, 35), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(18, 33, 34, 50, 35), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(18, 33, 34, 50, 35), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(18, 33, 34, 50, 35), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(18, 33, 34, 50, 35), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(18, 33, 34, 50, 35), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(18, 33, 34, 50, 35), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.VIER to listOf(
            PolyominoVariant(cells = listOf(17, 33, 49, 65), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(17, 33, 49, 65), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 19, 20), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 19, 20), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 33, 49, 65), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17, 33, 49, 65), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(17, 18, 19, 20), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 18, 19, 20), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.VIER_L to listOf(
            PolyominoVariant(cells = listOf(17, 33, 49, 50), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(18, 34, 49, 50), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 19, 33), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 19, 35), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 34, 50), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17, 18, 33, 49), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(19, 33, 34, 35), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 33, 34, 35), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.VIER_T to listOf(
            PolyominoVariant(cells = listOf(17, 33, 49, 34), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(18, 33, 34, 50), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 19, 34), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 19, 34), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(18, 33, 34, 50), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17, 33, 49, 34), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(18, 33, 34, 35), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(18, 33, 34, 35), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.VIER_Z to listOf(
            PolyominoVariant(cells = listOf(17, 33, 34, 50), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(18, 33, 34, 49), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(18, 19, 33, 34), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 34, 35), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 33, 34, 50), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(18, 33, 34, 49), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(18, 19, 33, 34), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 18, 34, 35), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.VIER_BLOCK to listOf(
            PolyominoVariant(cells = listOf(17, 18, 33, 34), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 33, 34), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 33, 34), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 33, 34), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 33, 34), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17, 18, 33, 34), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(17, 18, 33, 34), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 18, 33, 34), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.DREI to listOf(
            PolyominoVariant(cells = listOf(17, 33, 49), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(17, 33, 49), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 19), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 19), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 33, 49), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17, 33, 49), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(17, 18, 19), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 18, 19), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.DREI_L to listOf(
            PolyominoVariant(cells = listOf(17, 18, 33), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 34), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18, 34), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18, 33), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(18, 33, 34), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17, 33, 34), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(17, 33, 34), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(18, 33, 34), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.ZWEI to listOf(
            PolyominoVariant(cells = listOf(17, 33), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(17, 33), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17, 18), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17, 18), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17, 33), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17, 33), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(17, 18), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17, 18), isFlipped = true, rotation = 270),
        ),
        PolyominoNames.EINS to listOf(
            PolyominoVariant(cells = listOf(17), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(17), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(17), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(17), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(17), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(17), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(17), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(17), isFlipped = true, rotation = 270),
        ))
val polyominoVariantsDistinct: Map<PolyominoNames,List<List<Int>>> =
    mapOf(
        PolyominoNames.FÜNF to listOf(
            listOf(17, 33, 49, 65, 81), listOf(17, 18, 19, 20, 21)
        ), PolyominoNames.FÜNF_ZL to listOf(
            listOf(17, 33, 49, 50, 66), listOf(18, 34, 49, 50, 65),
            listOf(18, 19, 20, 33, 34), listOf(17, 18, 19, 35, 36),
            listOf(17, 33, 34, 50, 66), listOf(18, 33, 34, 49, 65),
            listOf(19, 20, 33, 34, 35), listOf(17, 18, 34, 35, 36)
        ),
        PolyominoNames.FÜNF_7 to listOf(
            listOf(17, 33, 34, 35, 50),
            listOf(19, 33, 34, 35, 50),
            listOf(18, 19, 33, 34, 50),
            listOf(17, 18, 34, 35, 50),
            listOf(18, 33, 34, 35, 51),
            listOf(18, 33, 34, 35, 49),
            listOf(18, 34, 35, 49, 50),
            listOf(18, 33, 34, 50, 51),
        ),
        PolyominoNames.FÜNF_L to listOf(
            listOf(17, 33, 49, 65, 66),
            listOf(18, 34, 50, 65, 66),
            listOf(17, 18, 19, 20, 33),
            listOf(17, 18, 19, 20, 36),
            listOf(17, 18, 34, 50, 66),
            listOf(17, 18, 33, 49, 65),
            listOf(20, 33, 34, 35, 36),
            listOf(17, 33, 34, 35, 36),
        ),
        PolyominoNames.FÜNF_SMAL_T to listOf(
            listOf(17, 33, 34, 49, 65),
            listOf(18, 33, 34, 50, 66),
            listOf(17, 18, 19, 20, 35),
            listOf(17, 18, 19, 20, 34),
            listOf(18, 34, 49, 50, 66),
            listOf(17, 33, 49, 50, 65),
            listOf(18, 33, 34, 35, 36),
            listOf(19, 33, 34, 35, 36),
        ),
        PolyominoNames.FÜNF_W to listOf(
            listOf(17, 33, 34, 50, 51),
            listOf(19, 34, 35, 49, 50),
            listOf(18, 19, 33, 34, 49),
            listOf(17, 18, 34, 35, 51),
        ),
        PolyominoNames.FÜNF_Z to listOf(
            listOf(17, 33, 34, 35, 51),
            listOf(19, 33, 34, 35, 49),
            listOf(18, 19, 34, 49, 50),
            listOf(17, 18, 34, 50, 51),
        ),
        PolyominoNames.FÜNF_LANG_L to listOf(
            listOf(17, 33, 49, 50, 51),
            listOf(19, 35, 49, 50, 51),
            listOf(17, 18, 19, 33, 49),
            listOf(17, 18, 19, 35, 51),
        ),
        PolyominoNames.FÜNF_C to listOf(
            listOf(17, 18, 19, 33, 35),
            listOf(17, 18, 34, 49, 50),
            listOf(17, 18, 33, 49, 50),
            listOf(17, 19, 33, 34, 35),
        ),
        PolyominoNames.FÜNF_BLOCK to listOf(
            listOf(17, 18, 33, 34, 50),
            listOf(17, 18, 33, 34, 49),
            listOf(18, 19, 33, 34, 35),
            listOf(17, 18, 33, 34, 35),
            listOf(17, 33, 34, 49, 50),
            listOf(18, 33, 34, 49, 50),
            listOf(17, 18, 19, 33, 34),
            listOf(17, 18, 19, 34, 35),
        ),
        PolyominoNames.FÜNF_T to listOf(
            listOf(17, 18, 19, 34, 50),
            listOf(19, 33, 34, 35, 51),
            listOf(17, 33, 34, 35, 49),
            listOf(18, 34, 49, 50, 51),
        ),
        PolyominoNames.FÜNF_PLUS to listOf(
            listOf(18, 33, 34, 35, 50),
        ),
        PolyominoNames.VIER to listOf(
            listOf(17, 33, 49, 65),
            listOf(17, 18, 19, 20),
        ),
        PolyominoNames.VIER_L to listOf(
            listOf(17, 33, 49, 50),
            listOf(18, 34, 49, 50),
            listOf(17, 18, 19, 33),
            listOf(17, 18, 19, 35),
            listOf(17, 18, 34, 50),
            listOf(17, 18, 33, 49),
            listOf(19, 33, 34, 35),
            listOf(17, 33, 34, 35),
        ),
        PolyominoNames.VIER_T to listOf(
            listOf(17, 33, 34, 49),
            listOf(18, 33, 34, 50),
            listOf(17, 18, 19, 34),
            listOf(18, 33, 34, 35),
        ),
        PolyominoNames.VIER_Z to listOf(
            listOf(17, 33, 34, 50),
            listOf(18, 33, 34, 49),
            listOf(18, 19, 33, 34),
            listOf(17, 18, 34, 35),
        ),
        PolyominoNames.VIER_BLOCK to listOf(
            listOf(17, 18, 33, 34),
        ),
        PolyominoNames.DREI to listOf(
            listOf(17, 33, 49),
            listOf(17, 18, 19),
        ),
        PolyominoNames.DREI_L to listOf(
            listOf(17, 18, 33),
            listOf(17, 18, 34),
            listOf(18, 33, 34),
            listOf(17, 33, 34),
        ),
        PolyominoNames.ZWEI to listOf(
            listOf(17, 33),
            listOf(17, 18),
        ),
        PolyominoNames.EINS to listOf(
            listOf(17),
        )
    )







//generateAllTrangenerateAllTransformations: (
//PolyominoVariant(cells=listOf(0, 14, 15, 28, 42), isFlipped=false, rotation=0),
//PolyominoVariant(cells=listOf(1, 14, 15, 29, 43), isFlipped=true, rotation=0),
//PolyominoVariant(cells=listOf(0, 1, 2, 3, 16), isFlipped=false, rotation=90),
//PolyominoVariant(cells=listOf(0, 1, 2, 3, 15), isFlipped=true, rotation=90),
//PolyominoVariant(cells=listOf(1, 15, 28, 29, 43), isFlipped=false, rotation=180),
//PolyominoVariant(cells=listOf(0, 14, 28, 29, 42), isFlipped=true, rotation=180),
//PolyominoVariant(cells=listOf(1, 14, 15, 16, 17), isFlipped=false, rotation=270),
//PolyominoVariant(cells=listOf(2, 14, 15, 16, 17), isFlipped=true, rotation=270))sformations: (PolyominoVariant(cells=listOf(0, 14, 15, 29, 30), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(2, 15, 16, 28, 29), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(1, 2, 14, 15, 28), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 15, 16, 30), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0, 1, 15, 16, 30), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(1, 2, 14, 15, 28), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(2, 15, 16, 28, 29), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0, 14, 15, 29, 30), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 14, 15, 16, 30), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(2, 14, 15, 16, 28), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(1, 2, 15, 28, 29), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 15, 29, 30), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0, 14, 15, 16, 30), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(2, 14, 15, 16, 28), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(1, 2, 15, 28, 29), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0, 1, 15, 29, 30), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 14, 28, 29, 30), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(2, 16, 28, 29, 30), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(0, 1, 2, 14, 28), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 2, 16, 30), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0, 1, 2, 16, 30), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(0, 1, 2, 14, 28), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(2, 16, 28, 29, 30), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0, 14, 28, 29, 30), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 1, 2, 14, 16), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(0, 1, 2, 14, 16), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(0, 1, 15, 28, 29), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 14, 28, 29), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0, 2, 14, 15, 16), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(0, 2, 14, 15, 16), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(0, 1, 14, 28, 29), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0, 1, 15, 28, 29), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 1, 14, 15, 29), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(0, 1, 14, 15, 28), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(1, 2, 14, 15, 16), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 14, 15, 16), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0, 14, 15, 28, 29), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(1, 14, 15, 28, 29), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(0, 1, 2, 14, 15), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0, 1, 2, 15, 16), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 1, 2, 15, 29), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(0, 1, 2, 15, 29), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(2, 14, 15, 16, 30), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 14, 15, 16, 28), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(1, 15, 28, 29, 30), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(1, 15, 28, 29, 30), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(0, 14, 15, 16, 28), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(2, 14, 15, 16, 30), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(1, 14, 15, 16, 29), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(1, 14, 15, 16, 29), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(1, 14, 15, 16, 29), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(1, 14, 15, 16, 29), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(1, 14, 15, 16, 29), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(1, 14, 15, 16, 29), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(1, 14, 15, 16, 29), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(1, 14, 15, 16, 29), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 14, 28, 42), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(0, 14, 28, 42), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(0, 1, 2, 3), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 2, 3), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0, 14, 28, 42), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(0, 14, 28, 42), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(0, 1, 2, 3), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0, 1, 2, 3), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 14, 28, 29), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(1, 15, 28, 29), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(0, 1, 2, 14), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 2, 16), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0, 1, 15, 29), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(0, 1, 14, 28), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(2, 14, 15, 16), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0, 14, 15, 16), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 14, 15, 28), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(1, 14, 15, 29), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(0, 1, 2, 15), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 2, 15), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(1, 14, 15, 29), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(0, 14, 15, 28), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(1, 14, 15, 16), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(1, 14, 15, 16), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 14, 15, 29), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(1, 14, 15, 28), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(1, 2, 14, 15), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 15, 16), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0, 14, 15, 29), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(1, 14, 15, 28), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(1, 2, 14, 15), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0, 1, 15, 16), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 14, 28), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(0, 14, 28), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(0, 1, 2), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 2), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0, 14, 28), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(0, 14, 28), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(0, 1, 2), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0, 1, 2), isFlipped=true, rotation=270))
//generateAllTransformations: ( PolyominoVariant(cells=listOf(0, 1, 14), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(0, 1, 15), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(0, 1, 15), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 14), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(1, 14, 15), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(0, 14, 15), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(0, 14, 15), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(1, 14, 15), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 14), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(0, 14), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(0, 1), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0, 14), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(0, 14), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(0, 1), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0, 1), isFlipped=true, rotation=270))
//generateAllTransformations: (PolyominoVariant(cells=listOf(0), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(0), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(0), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(0), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(0), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0), isFlipped=true, rotation=270))


//generateAllTransformations: (PolyominoVariant(cells=listOf(0, 14, 28, 29, 43), isFlipped=false, rotation=0), PolyominoVariant(cells=listOf(1, 15, 28, 29, 42), isFlipped=true, rotation=0), PolyominoVariant(cells=listOf(1, 2, 3, 14, 15), isFlipped=false, rotation=90), PolyominoVariant(cells=listOf(0, 1, 2, 16, 17), isFlipped=true, rotation=90), PolyominoVariant(cells=listOf(0, 14, 15, 29, 43), isFlipped=false, rotation=180), PolyominoVariant(cells=listOf(1, 14, 15, 28, 42), isFlipped=true, rotation=180), PolyominoVariant(cells=listOf(2, 3, 14, 15, 16), isFlipped=false, rotation=270), PolyominoVariant(cells=listOf(0, 1, 15, 16, 17), isFlipped=true, rotation=270))
//generateAllTransformations: (
//            PolyominoVariant(cells=listOf(0, 14, 15, 16, 29), isFlipped=false, rotation=0),
//            PolyominoVariant(cells=listOf(2, 14, 15, 16, 29), isFlipped=true, rotation=0),
//            PolyominoVariant(cells=listOf(1, 2, 14, 15, 29), isFlipped=false, rotation=90),
//            PolyominoVariant(cells=listOf(0, 1, 15, 16, 29), isFlipped=true, rotation=90),
//            PolyominoVariant(cells=listOf(1, 14, 15, 16, 30), isFlipped=false, rotation=180),
//            PolyominoVariant(cells=listOf(1, 14, 15, 16, 28), isFlipped=true, rotation=180),
//            PolyominoVariant(cells=listOf(1, 15, 16, 28, 29), isFlipped=false, rotation=270),
//            PolyominoVariant(cells=listOf(1, 14, 15, 29, 30), isFlipped=true, rotation=270))
//generateAllTransformations: (
//        PolyominoVariant(cells=listOf(0, 14, 28, 42, 43), isFlipped=false, rotation=0),
//        PolyominoVariant(cells=listOf(1, 15, 29, 42, 43), isFlipped=true, rotation=0),
//        PolyominoVariant(cells=listOf(0, 1, 2, 3, 14), isFlipped=false, rotation=90),
//        PolyominoVariant(cells=listOf(0, 1, 2, 3, 17), isFlipped=true, rotation=90),
//        PolyominoVariant(cells=listOf(0, 1, 15, 29, 43), isFlipped=false, rotation=180),
//        PolyominoVariant(cells=listOf(0, 1, 14, 28, 42), isFlipped=true, rotation=180),
//        PolyominoVariant(cells=listOf(3, 14, 15, 16, 17), isFlipped=false, rotation=270),
//        PolyominoVariant(cells=listOf(0, 14, 15, 16, 17), isFlipped=true, rotation=270))