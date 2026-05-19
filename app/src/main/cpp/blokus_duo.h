//
// Created by leons on 07.01.2026.
//

#ifndef BLOKUS2P_BLOKUS_DUO_H
#define BLOKUS2P_BLOKUS_DUO_H


#include <array>
#include <memory>
#include <string>
#include <vector>

#include "blokus_duo_logic.h"


// State of an in-play game.
class BlokusDuoState {
public:
    BlokusDuoState(){
        combined_board_.fill(0ULL);
        player_0_board_.fill(0ULL);
        player_1_board_.fill(0ULL);
        bit_border_.fill(0ULL);
        player_0_edges = {0ULL, 67108864, 0ULL, 0ULL};
        player_1_edges = {0ULL, 0ULL, 137438953472ULL, 0ULL};
        polyomino_mask_player_0 = 0x1FFFFF; // maske für ob alle 21 Steine da sind
        polyomino_mask_player_1 = 0x1FFFFF; // maske für ob alle 21 Steine da sind


        // 2. Rand auf dem combined_board_ setzen.
        //    Der Rand repräsentiert die ungültigen Positionen.
        for (int r = 0; r < kBoardSize; ++r) {
            for (int c = 0; c < kBoardSize; ++c) {
                // Prüfen, ob die Koordinate am Rand (Zeile 0, Zeile 15, Spalte 0, Spalte 15) liegt.
                if (r == 0 || r == kBoardSize - 1 || c == 0 || c == kBoardSize - 1) {
                    int index = r * kBoardSize + c;

                    int part = index / 64;
                    int bit = index % 64;

                    if (index == 165) {
                        player_0_edges[part] |= (1ULL << bit);
                    }
                    if (index == 90) {
                        player_1_edges[part] |= (1ULL << bit);
                    }

                    bit_border_[part] |= (1ULL << bit);
                }
            }
        }
    }



    int CurrentPlayer() const { return current_player_; }

    bool IsTerminal() const;

    std::string ActionToString(int player, Action action_id) const;

    void ApplyAction(int move);  // statt DoApplyAction(Action)
    void UndoAction(int player, int move);   // falls du es brauchst
    int outcome() const { return outcome_; }

    std::vector<double> Returns() const;

    double PlayerReturn(int player) const;

    double EvaluationFunktion(int player) const;

    std::string InformationStateString(int player) const;

    std::string ObservationString(int player) const;

//            void ObservationTensor(int player,
//                                   absl::Span<float> values) const;
    std::vector<int> LegalActions() const;

    //void ChangePlayer() {current_player_ = current_player_ == 0 ? 1 : 0;}

    void SetCurrentPlayer(int player) { current_player_ = player; }

    std::array<uint64_t, kNumBitboardParts> combined_board_{};
    std::array<uint64_t, kNumBitboardParts> player_0_board_{};
    std::array<uint64_t, kNumBitboardParts> player_1_board_{};
    std::array<uint64_t, kNumBitboardParts> bit_border_{};
    std::array<uint64_t, kNumBitboardParts> player_0_edges{};
    std::array<uint64_t, kNumBitboardParts> player_1_edges{};
    uint32_t polyomino_mask_player_0;
    uint32_t polyomino_mask_player_1;


    int current_player_ = 0;
    int outcome_ = -1;
    int num_moves_ = 0;
    bool player0_pass = false;
    bool player1_pass = false;
};

// Game object.
class BlokusDuoGame {
public:
    BlokusDuoGame() = default;

    //explicit BlokusDuoGame(const GameParameters& params);
    int NumDistinctActions() const { return kNumDistinctActions; }

    BlokusDuoState NewInitialState() const {
        BlokusDuoState state;
        return state;
    }

    int NumPlayers() const { return kNumPlayers; }

    double MinUtility() const { return -1; }

    //absl::optional<double> UtilitySum() const override { return 0; }
    double MaxUtility() const { return 1; }

    std::vector<int> ObservationTensorShape() const {
        return {kTotalChannels, kBoardSizeWithoutBorder, kBoardSizeWithoutBorder};
        // Ergebnis: {46, 14, 14}
    }

    int MaxGameLength() const { return max_game_length; }

    std::string ActionToString(int player, int action_id) const;
};


#endif //BLOKUS2P_BLOKUS_DUO_H