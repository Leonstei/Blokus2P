//
// Created by leoney on 08.01.26.
//

#ifndef BLOKUS2P_MCTS_H
#define BLOKUS2P_MCTS_H

#include <stdint.h>

#include <memory>
#include <random>
#include <string>
#include <utility>
#include <vector>

#include "blokus_duo.h"

// A vanilla Monte Carlo Tree Search algorithm.
//
// This algorithm searches the game tree from the given state.
// At the leaf, the evaluator is called if the game state is not terminal.
// A total of max_simulations states are explored.
//
// At every node, the algorithm chooses the action with the highest PUCT value
// defined as: `Q/N + c * prior * sqrt(parent_N) / N`, where Q is the total
// reward after the action, and N is the number of times the action was
// explored in this position. The input parameter c controls the balance
// between exploration and exploitation; higher values of c encourage
// exploration of under-explored nodes. Unseen actions are always explored
// first.
//
// At the end of the search, the chosen action is the action that has been
// explored most often. This is the action that is returned.
//
// This implementation supports sequential n-player games, with or without
// chance nodes. All players maximize their own reward and ignore the other
// players' rewards. This corresponds to max^n for n-player games. It is the
// norm for zero-sum games, but doesn't have any special handling for
// non-zero-sum games. It doesn't have any special handling for imperfect
// information games.
//
// The implementation also supports backing up solved states, i.e. MCTS-Solver.
// The implementation is general in that it is based on a max^n backup (each
// player greedily chooses their maximum among proven children values, or there
// exists one child whose proven value is Game::MaxUtility()), so it will work
// for multiplayer, general-sum, and arbitrary payoff games (not just win/loss/
// draw games). Also chance nodes are considered proven only if all children
// have the same value.
//
// Some references:
// - Sturtevant, An Analysis of UCT in Multi-Player Games,  2008,
//   https://web.cs.du.edu/~sturtevant/papers/multi-player_UCT.pdf
// - Nijssen, Monte-Carlo Tree Search for Multi-Player Games, 2013,
//   https://project.dke.maastrichtuniversity.nl/games/files/phd/Nijssen_thesis.pdf
// - Silver, AlphaGo Zero: Starting from scratch, 2017
//   https://deepmind.com/blog/article/alphago-zero-starting-scratch
// - Winands, Bjornsson, and Saito, Monte-Carlo Tree Search Solver, 2008.
//   https://dke.maastrichtuniversity.nl/m.winands/documents/uctloa.pdf


enum class ChildSelectionPolicy {
    UCT,   // Standard UCT (ohne Prior)
    PUCT   // PUCT (mit Prior, für bessere Exploration)
};

// A simple evaluator that returns the average outcome of playing random actions
// from the given state until the end of the game.
// n_rollouts is the number of random outcomes to be considered.
class RandomRolloutEvaluator  {
public:
    explicit RandomRolloutEvaluator(int n_rollouts, int seed)
            : n_rollouts_(n_rollouts), rng_(seed) {}

    // Runs random games, returning the average returns.
    std::vector<double> Evaluate(const BlokusDuoState& state);

    // Returns equal probability for each action.
    std::vector<std::pair<int, double>> Prior(const BlokusDuoState& state);

private:
    int n_rollouts_;
    std::mt19937 rng_;
};

// A node in the search tree for MCTS
struct SearchNode {
    int action = 0;            // The action taken to get to this node.
    double prior = 0;             // The prior probability of playing this action.
    int player = 0;            // Which player gets to make this action.
    int explore_count = 0;        // Number of times this node was explored.
    double total_reward = 0;      // Total reward passing through this node.
//    std::vector<double> outcome;  // The reward if each players plays perfectly.
    std::vector<SearchNode> children;  // The successors to this state.

    SearchNode() {}

    SearchNode(int action_, int player_, double prior_)
            : action(action_), prior(prior_), player(player_) {}

    // The value as returned by the UCT formula.
    double UCTValue(int parent_explore_count, double uct_c) const;

    // The value as returned by the PUCT formula.
    double PUCTValue(int parent_explore_count, double uct_c) const;

    // The sort order for the BestChild.
    bool CompareFinal(const SearchNode &b) const;

    const SearchNode &BestChild() const;
};

// A SpielBot that uses the MCTS algorithm as its policy.
class MCTSBot {
public:
    // The evaluator is passed as a shared pointer to make it explicit that
    // the same evaluator instance can be passed to multiple bots and to
    // make the MCTSBot Python interface work regardless of the scope of the
    // Python evaluator object.
    //
    // moment, even though it gets assigned to a member of type
    // std::shared_ptr<Evaluator>. This is because using a
    // std::shared_ptr<Evaluator> in the constructor leads to the Julia API test
    // failing. We don't know why right now, but intend to fix this.
    MCTSBot(double uct_c = 1.414,  // Explorationsfaktor
            int max_simulations = 1000,  // Max Iterationen
            int n_rollouts = 1,  // Rollouts pro Leaf
            int random_seed = 42,  // RNG-Seed
            ChildSelectionPolicy policy = ChildSelectionPolicy::UCT);

    ~MCTSBot() = default;

    void Restart()  {}

    void RestartAt(const BlokusDuoState &state) {}

    // Run MCTS for one step, choosing the action, and printing some information.
    Action Step(const BlokusDuoState& state);

    // Run MCTS on a given state, and return the resulting search tree.
    std::unique_ptr<SearchNode> MCTSearch(const BlokusDuoState& state);

private:
    // Applies the UCT policy to play the game until reaching a leaf node.
    //
    // A leaf node is defined as a node that is terminal or has not been evaluated
    // yet. If it reaches a node that has been evaluated before but hasn't been
    // expanded, then expand it's children and continue.
    //
    // Args:
    //   root: The root node in the search tree.
    //   state: The state of the game at the root node.
    //   visit_path: A vector of nodes to be filled in descending from the root
    //     node to a leaf node.
    //
    // Returns: The state of the game at the leaf node.
    std::unique_ptr<BlokusDuoState> ApplyTreePolicy(SearchNode *root, const BlokusDuoState &state,
                                           std::vector<SearchNode *> *visit_path);

    void GarbageCollect(SearchNode *node);

    double uct_c_;
    int max_simulations_;
    int max_nodes_;  // Max nodes allowed in the tree
    int nodes_;  // Nodes used in the tree.
    int gc_limit_;
    double max_utility_;
    bool dont_return_chance_node_;
    std::mt19937 rng_;
    RandomRolloutEvaluator evaluator_;
};

// Returns a vector of noise sampled from a dirichlet distribution. See:
// https://en.wikipedia.org/wiki/Dirichlet_process
std::vector<double> dirichlet_noise(int count, double alpha, std::mt19937 *rng);


#endif //BLOKUS2P_MCTS_H
