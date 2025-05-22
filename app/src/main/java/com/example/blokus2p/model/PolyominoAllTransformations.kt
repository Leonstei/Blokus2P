package com.example.blokus2p.model

import com.example.blokus2p.game.PolyominoVariant

val polyominoVariants: Map<PolyominoNames,List<PolyominoVariant>> =
    mapOf(
        PolyominoNames.FÜNF to listOf(
            PolyominoVariant(cells = listOf(0, 14, 28, 42, 56), isFlipped = false, rotation = 0),
            PolyominoVariant(cells = listOf(0, 14, 28, 42, 56), isFlipped = true, rotation = 0),
            PolyominoVariant(cells = listOf(0, 1, 2, 3, 4), isFlipped = false, rotation = 90),
            PolyominoVariant(cells = listOf(0, 1, 2, 3, 4), isFlipped = true, rotation = 90),
            PolyominoVariant(cells = listOf(0, 14, 28, 42, 56), isFlipped = false, rotation = 180),
            PolyominoVariant(cells = listOf(0, 14, 28, 42, 56), isFlipped = true, rotation = 180),
            PolyominoVariant(cells = listOf(0, 1, 2, 3, 4), isFlipped = false, rotation = 270),
            PolyominoVariant(cells = listOf(0, 1, 2, 3, 4), isFlipped = true, rotation = 270)
        ),
        PolyominoNames.FÜNF_ZL to listOf(
            PolyominoVariant(cells=listOf(0, 14, 15, 16, 29), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(0, 14, 15, 16, 29), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(1, 2, 14, 15, 29), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 15, 16, 29), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(1, 14, 15, 16, 30), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(1, 14, 15, 16, 28), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(1, 15, 16, 28, 29), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(1, 14, 15, 29, 30), isFlipped=true, rotation=270)
        ),
        PolyominoNames.FÜNF_7 to listOf(
            PolyominoVariant(cells=listOf(0, 14, 28, 42, 43), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(1, 15, 29, 42, 43), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 2, 3, 14), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 2, 3, 17), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 15, 29, 43), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0, 1, 14, 28, 42), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(3, 14, 15, 16, 17), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 14, 15, 16, 17), isFlipped=true, rotation=270)
        ),
        PolyominoNames.FÜNF_L to listOf(
            PolyominoVariant(cells=listOf(0, 14, 28, 42, 43), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(1, 15, 29, 42, 43), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 2, 3, 14), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 2, 3, 17), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 15, 29, 43), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0, 1, 14, 28, 42), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(3, 14, 15, 16, 17), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 14, 15, 16, 17), isFlipped=true, rotation=270)
        ),
        PolyominoNames.FÜNF_SMAL_T to listOf(
            PolyominoVariant(cells=listOf(0, 14, 15, 28, 42), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(1, 14, 15, 29, 43), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 2, 3, 16), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 2, 3, 15), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(1, 15, 28, 29, 43), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0, 14, 28, 29, 42), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(1, 14, 15, 16, 17), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(2, 14, 15, 16, 17), isFlipped=true, rotation=270)
        ),
        PolyominoNames.FÜNF_W to listOf(
            PolyominoVariant(cells=listOf(0, 14, 15, 29, 30), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(2, 15, 16, 28, 29), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(1, 2, 14, 15, 28), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 15, 16, 30), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 15, 16, 30), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(1, 2, 14, 15, 28), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(2, 15, 16, 28, 29), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 14, 15, 29, 30), isFlipped=true, rotation=270)
        ),
        PolyominoNames.FÜNF_Z to listOf(
            PolyominoVariant(cells=listOf(0, 14, 15, 16, 30), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(2, 14, 15, 16, 28), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(1, 2, 15, 28, 29), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 15, 29, 30), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 14, 15, 16, 30), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(2, 14, 15, 16, 28), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(1, 2, 15, 28, 29), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 1, 15, 29, 30), isFlipped=true, rotation=270)
        ),
        PolyominoNames.FÜNF_LANG_L to listOf(
            PolyominoVariant(cells=listOf(0, 14, 28, 29, 30), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(2, 16, 28, 29, 30), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 2, 14, 28), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 2, 16, 30), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 2, 16, 30), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0, 1, 2, 14, 28), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(2, 16, 28, 29, 30), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 14, 28, 29, 30), isFlipped=true, rotation=270)
        ),
        PolyominoNames.FÜNF_C to listOf(
            PolyominoVariant(cells=listOf(0, 14, 1, 2, 16), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(0, 14, 1, 2, 16), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 15, 28, 29), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 14, 28, 29), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 2, 14, 15, 16), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0, 2, 14, 15, 16), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(0, 1, 14, 28, 29), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 1, 15, 28, 29), isFlipped=true, rotation=270)
        ),
        PolyominoNames.FÜNF_BLOCK to listOf(
            PolyominoVariant(cells=listOf(0, 1, 14, 15, 29), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 14, 15, 28), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(1, 2, 14, 15, 16), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 14, 15, 16), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 14, 15, 28, 29), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(1, 14, 15, 28, 29), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(0, 1, 2, 14, 15), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 1, 2, 15, 16), isFlipped=true, rotation=270)
        ),
        PolyominoNames.FÜNF_T to listOf(
            PolyominoVariant(cells=listOf(0, 1, 2, 15, 29), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 2, 15, 29), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(2, 14, 15, 16, 30), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 14, 15, 16, 28), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(1, 15, 28, 29, 30), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(1, 15, 28, 29, 30), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(0, 14, 15, 16, 28), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(2, 14, 15, 16, 30), isFlipped=true, rotation=270)
        ),
        PolyominoNames.FÜNF_PLUS to listOf(
            PolyominoVariant(cells=listOf(1, 14, 15, 29, 16), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(1, 14, 15, 29, 16), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(1, 14, 15, 29, 16), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(1, 14, 15, 29, 16), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(1, 14, 15, 29, 16), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(1, 14, 15, 29, 16), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(1, 14, 15, 29, 16), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(1, 14, 15, 29, 16), isFlipped=true, rotation=270)
        ),
        PolyominoNames.VIER to listOf(
            PolyominoVariant(cells=listOf(0, 14, 28, 42), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(0, 14, 28, 42), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 2, 3), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 2, 3), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 14, 28, 42), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0, 14, 28, 42), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(0, 1, 2, 3), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 1, 2, 3), isFlipped=true, rotation=270)
        ),
        PolyominoNames.VIER_L to listOf(
            PolyominoVariant(cells=listOf(0, 14, 28, 29), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(0, 14, 28, 29), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 2, 14), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 2, 14), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 14, 28, 29), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0, 14, 28, 29), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(0, 1, 2, 14), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 1, 2, 14), isFlipped=true, rotation=270)
        ),
        PolyominoNames.VIER_T to listOf(
            PolyominoVariant(cells=listOf(0, 14, 28, 15), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(0, 14, 28, 15), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 2, 14), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 2, 14), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 14, 28, 15), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0, 14, 28, 15), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(0, 1, 2, 14), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 1, 2, 14), isFlipped=true, rotation=270)
        ),
        PolyominoNames.VIER_Z to listOf(
            PolyominoVariant(cells=listOf(0, 14, 15, 29), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(1, 14, 15, 28), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(1, 2, 14, 15), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 15, 16), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 14, 15, 29), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(1, 14, 15, 28), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(1, 2, 14, 15), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 1, 15, 16), isFlipped=true, rotation=270)
        ),
        PolyominoNames.VIER_BLOCK to listOf(
            PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 1, 14, 15), isFlipped=true, rotation=270)
        ),
        PolyominoNames.DREI to listOf(
            PolyominoVariant(cells=listOf(0, 14, 28), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(0, 14, 28), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 2), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 2), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 14, 28), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0, 14, 28), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(0, 1, 2), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 1, 2), isFlipped=true, rotation=270)
        ),
        PolyominoNames.DREI_L to listOf(
            PolyominoVariant(cells=listOf(0, 1, 14), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 15), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0, 1, 15), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1, 14), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(1, 14, 15), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0, 14, 15), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(0, 14, 15), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(1, 14, 15), isFlipped=true, rotation=270)
        ),
        PolyominoNames.ZWEI to listOf(
            PolyominoVariant(cells=listOf(0, 14), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(0, 14), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0, 1), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0, 1), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0, 14), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0, 14), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(0, 1), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0, 1), isFlipped=true, rotation=270)
        ),
        PolyominoNames.EINS to listOf(
            PolyominoVariant(cells=listOf(0), isFlipped=false, rotation=0),
            PolyominoVariant(cells=listOf(0), isFlipped=true, rotation=0),
            PolyominoVariant(cells=listOf(0), isFlipped=false, rotation=90),
            PolyominoVariant(cells=listOf(0), isFlipped=true, rotation=90),
            PolyominoVariant(cells=listOf(0), isFlipped=false, rotation=180),
            PolyominoVariant(cells=listOf(0), isFlipped=true, rotation=180),
            PolyominoVariant(cells=listOf(0), isFlipped=false, rotation=270),
            PolyominoVariant(cells=listOf(0), isFlipped=true, rotation=270)
        ))
val polyominoVariantsDistinct: Map<PolyominoNames,List<List<Int>>> =
    mapOf(
        PolyominoNames.FÜNF to listOf(
            listOf(0, 14, 28, 42, 56), listOf(0, 1, 2, 3, 4)
        ), PolyominoNames.FÜNF_ZL to listOf(
            listOf(0, 14, 28, 29, 43), listOf(1, 15, 28, 29, 42),
            listOf(1, 2, 3, 14, 15), listOf(0, 1, 2, 16, 17),
            listOf(0, 14, 15, 29, 43), listOf(1, 14, 15, 28, 42),
            listOf(2, 3, 14, 15, 16), listOf(0, 1, 15, 16, 17)
        ),
        PolyominoNames.FÜNF_7 to listOf(
            listOf(0, 14, 15, 16, 29), listOf(2, 14, 15, 16, 29),
            listOf(1, 2, 14, 15, 29), listOf(0, 1, 15, 16, 29),
            listOf(1, 14, 15, 16, 30), listOf(1, 14, 15, 16, 28),
            listOf(1, 15, 16, 28, 29), listOf(1, 14, 15, 29, 30)
        ),
        PolyominoNames.FÜNF_L to listOf(
            listOf(0, 14, 28, 42, 43), listOf(1, 15, 29, 42, 43),
            listOf(0, 1, 2, 3, 14), listOf(0, 1, 2, 3, 17),
            listOf(0, 1, 15, 29, 43), listOf(0, 1, 14, 28, 42),
            listOf(3, 14, 15, 16, 17), listOf(0, 14, 15, 16, 17)
        ),
        PolyominoNames.FÜNF_SMAL_T to listOf(
            listOf(0, 14, 15, 28, 42), listOf(1, 14, 15, 29, 43),
            listOf(0, 1, 2, 3, 16), listOf(0, 1, 2, 3, 15),
            listOf(1, 15, 28, 29, 43), listOf(0, 14, 28, 29, 42),
            listOf(1, 14, 15, 16, 17), listOf(2, 14, 15, 16, 17)
        ),
        PolyominoNames.FÜNF_W to listOf(
            listOf(0, 14, 15, 29, 30), listOf(2, 15, 16, 28, 29),
            listOf(1, 2, 14, 15, 28), listOf(0, 1, 15, 16, 30)
        ),
        PolyominoNames.FÜNF_Z to listOf(
            listOf(0, 14, 15, 16, 30), listOf(2, 14, 15, 16, 28),
            listOf(1, 2, 15, 28, 29), listOf(0, 1, 15, 29, 30)
        ),
        PolyominoNames.FÜNF_LANG_L to listOf(
            listOf(0, 14, 28, 29, 30), listOf(2, 16, 28, 29, 30),
            listOf(0, 1, 2, 14, 28), listOf(0, 1, 2, 16, 30)
        ),
        PolyominoNames.FÜNF_C to listOf(
            listOf(0, 1, 2, 14, 16), listOf(0, 1, 15, 28, 29),
            listOf(0, 1, 14, 28, 29), listOf(0, 2, 14, 15, 16)
        ),
        PolyominoNames.FÜNF_BLOCK to listOf(
            listOf(0, 1, 14, 15, 29), listOf(0, 1, 14, 15, 28),
            listOf(1, 2, 14, 15, 16), listOf(0, 1, 14, 15, 16),
            listOf(0, 14, 15, 28, 29), listOf(1, 14, 15, 28, 29),
            listOf(0, 1, 2, 14, 15), listOf(0, 1, 2, 15, 16)
        ),
        PolyominoNames.FÜNF_T to listOf(
            listOf(0, 1, 2, 15, 29),
            listOf(2, 14, 15, 16, 30),
            listOf(0, 14, 15, 16, 28),
            listOf(1, 15, 28, 29, 30)
        ),
        PolyominoNames.FÜNF_PLUS to listOf(
            listOf(1, 14, 15, 16, 29)
        ),
        PolyominoNames.VIER to listOf(
            listOf(0, 14, 28, 42), listOf(0, 1, 2, 3)
        ),
        PolyominoNames.VIER_L to listOf(
            listOf(0, 14, 28, 29), listOf(1, 15, 28, 29), listOf(0, 1, 2, 14), listOf(0, 1, 2, 16),
            listOf(0, 1, 15, 29), listOf(0, 1, 14, 28), listOf(2, 14, 15, 16), listOf(0, 14, 15, 16)
        ),
        PolyominoNames.VIER_T to listOf(
            listOf(0, 14, 15, 28),
            listOf(1, 14, 15, 29),
            listOf(0, 1, 2, 15),
            listOf(1, 14, 15, 16)
        ),
        PolyominoNames.VIER_Z to listOf(
            listOf(0, 14, 15, 29),
            listOf(1, 14, 15, 28),
            listOf(1, 2, 14, 15),
            listOf(0, 1, 15, 16)
        ),
        PolyominoNames.VIER_BLOCK to listOf(
            listOf(0, 1, 14, 15)
        ),
        PolyominoNames.DREI to listOf(
            listOf(0, 14, 28), listOf(0, 1, 2)
        ),
        PolyominoNames.DREI_L to listOf(
            listOf(0, 1, 14), listOf(0, 1, 15), listOf(1, 14, 15), listOf(0, 14, 15)
        ),
        PolyominoNames.ZWEI to listOf(
            listOf(0, 14), listOf(0, 1)
        ),
        PolyominoNames.EINS to listOf(
            listOf(0)
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