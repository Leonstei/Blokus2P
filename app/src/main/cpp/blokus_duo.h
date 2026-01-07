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


namespace open_spiel {
    namespace blokus_duo {

// State of an in-play game.
        class BlokusDuoState {
        public:
            explicit BlokusDuoState();  // statt shared_ptr<const Game>
            BlokusDuoState(const BlokusDuoState& other);
            BlokusDuoState& operator=(const BlokusDuoState& other);


            int CurrentPlayer() const { return current_player_; }
            bool IsTerminal() const;
            std::string ActionToString(int player,  Action action_id) const;
            void ApplyAction(int move);  // statt DoApplyAction(Action)
            void UndoAction(int player,int move);   // falls du es brauchst
            int outcome() const { return outcome_; }

            std::vector<double> Returns() const;
            double PlayerReturn(int player) const;
            double EvaluationFunktion(int player) const;
            std::string InformationStateString(int player) const;
            std::string ObservationString(int player) const;
            void ObservationTensor(int player,
                                   absl::Span<float> values) const;
            std::vector<int> LegalActions() const;

            //void ChangePlayer() {current_player_ = current_player_ == 0 ? 1 : 0;}

            void SetCurrentPlayer(int player) { current_player_ = player; }


        protected:
            std::array<uint64_t, kNumBitboardParts> combined_board_;
            std::array<uint64_t, kNumBitboardParts> player_0_board_;
            std::array<uint64_t, kNumBitboardParts> player_1_board_;
            std::array<uint64_t, kNumBitboardParts> bit_border_;
            std::array<uint64_t, kNumBitboardParts> player_0_edges;
            std::array<uint64_t, kNumBitboardParts> player_1_edges;
            uint32_t polyomino_mask_player_0;
            uint32_t polyomino_mask_player_1;



        private:
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
                // Initialisiere hier alles (z. B. bit_border_, starting corners usw.)
                // Genau wie in deinem Originalkonstruktor
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

    }  // namespace blokus_duo
}  // namespace open_spiel

#endif //BLOKUS2P_BLOKUS_DUO_H