//
// Created by leoney on 08.01.26.
//
#include "mcts.h"

#include <algorithm>
#include <cmath>
#include <limits>
#include <memory>
#include <random>
#include <vector>


int MIN_GC_LIMIT = 5;

int MemoryUsedMb(int nodes) {
    return nodes * sizeof(SearchNode) / (1 << 20);
}

std::vector<double> RandomRolloutEvaluator::Evaluate(const BlokusDuoState &state) {
    std::vector<double> total_returns(2, 0.0);  // Für Spieler 0 und 1

    for (int i = 0; i < n_rollouts_; ++i) {
        BlokusDuoState sim = state;  // Kopie per Wert (dein State ist klein)

        while (!sim.IsTerminal()) {
            std::vector<int> actions = sim.LegalActions();
            if (actions.empty()) break;  // Pass oder Ende

            int random_idx = std::uniform_int_distribution<int>(0, actions.size() - 1)(rng_);
            sim.ApplyAction(actions[random_idx]);
        }

        std::vector<double> returns = sim.Returns();

        total_returns[0] += returns[0];
        total_returns[1] += returns[1];
    }

    total_returns[0] /= n_rollouts_;
    total_returns[1] /= n_rollouts_;

    return total_returns;
}

std::vector<std::pair<int, double>>  RandomRolloutEvaluator::Prior(const BlokusDuoState &state) {
    // Returns equal probability for all actions.
    std::vector<int> actions = state.LegalActions();
    std::vector<std::pair<int, double>> prior;
    prior.reserve(actions.size());

    double prob = actions.empty() ? 0.0 : 1.0 / actions.size();
    for (int action : actions) {
        prior.emplace_back(action, prob);
    }
    return prior;
}

// UCT value of given child
double SearchNode::UCTValue(int parent_explore_count, double uct_c) const {
    if (explore_count == 0) return std::numeric_limits<double>::infinity();

    // The "greedy-value" of choosing a given child is always with respect to
    // the current player for this node.
    return total_reward / explore_count +
           uct_c * std::sqrt(std::log(parent_explore_count) / explore_count);
}

double SearchNode::PUCTValue(int parent_explore_count, double uct_c) const {
    // Returns the PUCT value of this node.
    return ((explore_count != 0 ? total_reward / explore_count : 0) +
            uct_c * prior * std::sqrt(parent_explore_count) /
            (explore_count + 1));
}

bool SearchNode::CompareFinal(const SearchNode &b) const {
    double out = (player >= 0 && player < outcome.size() ? outcome[player] : 0);
    double out_b =
            (b.player >= 0 && b.player < b.outcome.size() ? b.outcome[b.player] : 0);
    if (out != out_b) {
        return out < out_b;
    }
    if (explore_count != b.explore_count) {
        return explore_count < b.explore_count;
    }
    return total_reward < b.total_reward;
}

const SearchNode &SearchNode::BestChild() const {
    // Returns the best action from this node, either proven or most visited.
    //
    // This ordering leads to choosing:
    // - Highest proven score > 0 over anything else, including a promising but
    //   unproven action.
    // - A proven draw only if it has higher exploration than others that are
    //   uncertain, or the others are losses.
    // - Uncertain action with most exploration over loss of any difficulty
    // - Hardest loss if everything is a loss
    // - Highest expected reward if explore counts are equal (unlikely).
    // - Longest win, if multiple are proven (unlikely due to early stopping).
    return *std::max_element(children.begin(), children.end(),
                             [](const SearchNode &a, const SearchNode &b) {
                                 return a.CompareFinal(b);
                             });
}



MCTSBot::MCTSBot(double uct_c, int max_simulations,
                 int n_rollouts_, int random_seed, ChildSelectionPolicy policy)
        : uct_c_(uct_c),
          max_simulations_(max_simulations),
          n_rollouts_(n_rollouts_),
          rng_(random_seed),
          policy_(policy),
          evaluator_(n_rollouts, random_seed) {}

int MCTSBot::Step(const BlokusDuoState& root_state) {
    SearchNode root;
    root.player = root_state.CurrentPlayer();

    // Prior für Root setzen (uniform)
    auto prior = evaluator_.Prior(root_state);
    root.children.reserve(prior.size());
    for (const auto& p : prior) {
        root.children.emplace_back();
        root.children.back().action = p.first;
        root.children.back().prior = p.second;
        root.children.back().player = root_state.CurrentPlayer();
    }

    for (int sim = 0; sim < max_simulations_; ++sim) {
        BlokusDuoState state = root_state;
        std::vector<SearchNode*> path = {&root};

        SearchNode* node = &root;
        while (!node->children.empty() && !state.IsTerminal()) {
            // Wähle bestes Kind
            SearchNode* best_child = nullptr;
            double best_value = -std::numeric_limits<double>::infinity();

            for (SearchNode& child : node->children) {
                double value = (policy_ == ChildSelectionPolicy::PUCT)
                               ? child.PUCTValue(node->explore_count, uct_c_)
                               : child.UCTValue(node->explore_count, uct_c_);
                if (value > best_value) {
                    best_value = value;
                    best_child = &child;
                }
            }

            state.ApplyAction(best_child->action);
            path.push_back(best_child);
            node = best_child;
        }

        // Leaf bewerten
        std::vector<double> returns = evaluator_.Evaluate(state);

        // Backup: Wert hochpropagieren (zero-sum: Gegner bekommt -reward)
        double value = returns[root.player];
        for (auto it = path.rbegin(); it != path.rend(); ++it) {
            SearchNode* n = *it;
            n->explore_count++;
            n->total_reward += value;
            value = -value;  // Für nächsten Spieler
        }
    }

    // Beste Action = meistbesuchtes Kind
    if (root.children.empty()) return -1;  // Sollte nicht passieren
    const SearchNode& best = root.BestChild();
    return best.action;
}

std::unique_ptr<BlokusDuoState> MCTSBot::ApplyTreePolicy(
        SearchNode *root, const State &state,
        std::vector<SearchNode *> *visit_path) {
    visit_path->push_back(root);
    std::unique_ptr<State> working_state = state.Clone();
    SearchNode *current_node = root;
    while ((!working_state->IsTerminal() && current_node->explore_count > 0) ||
           (working_state->IsChanceNode() && dont_return_chance_node_)) {
        if (current_node->children.empty()) {
            // For a new node, initialize its state, then choose a child as normal.
            ActionsAndProbs legal_actions = evaluator_->Prior(*working_state);
            if (current_node == root && dirichlet_alpha_ > 0) {
                std::vector<double> noise =
                        dirichlet_noise(legal_actions.size(), dirichlet_alpha_, &rng_);
                for (int i = 0; i < legal_actions.size(); i++) {
                    legal_actions[i].second =
                            (1 - dirichlet_epsilon_) * legal_actions[i].second +
                            dirichlet_epsilon_ * noise[i];
                }
            }
            // Reduce bias from move generation order.
            std::shuffle(legal_actions.begin(), legal_actions.end(), rng_);
            Player player = working_state->CurrentPlayer();
            current_node->children.reserve(legal_actions.size());
            for (auto [action, prior]: legal_actions) {
                current_node->children.emplace_back(action, player, prior);
            }
            nodes_ += current_node->children.capacity();
        }

        Action selected_action;
        if (current_node->children.empty()) {
            // no children, sample from prior
            selected_action = current_node->SampleFromPrior(state, evaluator_.get(),
                                                            &rng_);
        } else {
            // look at children
            SearchNode *chosen_child = nullptr;
            if (working_state->IsChanceNode()) {
                // For chance nodes, rollout according to chance node's probability
                // distribution
                Action chosen_action =
                        SampleAction(working_state->ChanceOutcomes(), rng_).first;

                for (SearchNode &child: current_node->children) {
                    if (child.action == chosen_action) {
                        chosen_child = &child;
                        break;
                    }
                }
            } else {
                // Otherwise choose node with largest UCT value.
                double max_value = -std::numeric_limits<double>::infinity();
                for (SearchNode &child: current_node->children) {
                    double val;
                    switch (child_selection_policy_) {
                        case ChildSelectionPolicy::UCT:
                            val = child.UCTValue(current_node->explore_count, uct_c_);
                            break;
                        case ChildSelectionPolicy::PUCT:
                            val = child.PUCTValue(current_node->explore_count, uct_c_);
                            break;
                    }
                    if (val > max_value) {
                        max_value = val;
                        chosen_child = &child;
                    }
                }
            }
            selected_action = chosen_child->action;
            current_node = chosen_child;
        }

        working_state->ApplyAction(selected_action);
        visit_path->push_back(current_node);
    }

    return working_state;
}

std::unique_ptr<SearchNode> MCTSBot::MCTSearch(const BlokusDuoState &state) {
    nodes_ = 1;
    gc_limit_ = MIN_GC_LIMIT;
    auto root = std::make_unique<SearchNode>(kInvalidAction,
                                             state.CurrentPlayer(), 1);
    std::vector<SearchNode *> visit_path;
    std::vector<double> returns;
    visit_path.reserve(64);
    for (int i = 0; i < max_simulations_; ++i) {
        visit_path.clear();
        returns.clear();

        std::unique_ptr<State> working_state =
                ApplyTreePolicy(root.get(), state, &visit_path);

        bool solved;
        if (working_state->IsTerminal()) {
            returns = working_state->Returns();
            visit_path[visit_path.size() - 1]->outcome = returns;
            solved = solve_;
        } else {
            returns = evaluator_->Evaluate(*working_state);
            solved = false;
        }

        // Propagate values back.
        while (!visit_path.empty()) {
            int decision_node_idx = visit_path.size() - 1;
            SearchNode *node = visit_path[decision_node_idx];

            // If it's a chance node, find the parent player id.
            while (visit_path[decision_node_idx]->player == kChancePlayerId) {
                decision_node_idx--;
            }

            node->total_reward += returns[visit_path[decision_node_idx]->player];
            node->explore_count += 1;
            visit_path.pop_back();

            // Back up solved results as well.
            if (solved && !node->children.empty()) {
                Player player = node->children[0].player;
                if (player == kChancePlayerId) {
                    // Only back up chance nodes if all have the same outcome.
                    // An alternative would be to back up the weighted average of
                    // outcomes if all children are solved, but that is less clear.
                    const std::vector<double> &outcome = node->children[0].outcome;
                    if (!outcome.empty() &&
                        std::all_of(node->children.begin() + 1, node->children.end(),
                                    [&outcome](const SearchNode &c) {
                                        return c.outcome == outcome;
                                    })) {
                        node->outcome = outcome;
                    } else {
                        solved = false;
                    }
                } else {
                    // If any have max utility (won?), or all children are solved,
                    // choose the one best for the player choosing.
                    const SearchNode *best = nullptr;
                    bool all_solved = true;
                    for (const SearchNode &child: node->children) {
                        if (child.outcome.empty()) {
                            all_solved = false;
                        } else if (best == nullptr ||
                                   child.outcome[player] > best->outcome[player]) {
                            best = &child;
                        }
                    }
                    if (best != nullptr &&
                        (all_solved || best->outcome[player] == max_utility_)) {
                        node->outcome = best->outcome;
                    } else {
                        solved = false;
                    }
                }
            }
        }

        if (!root->outcome.empty() ||  // Full game tree is solved.
            root->children.size() == 1) {
            break;
        }
        if (max_nodes_ > 1 && nodes_ >= max_nodes_) {
            // Note that actual memory used as counted by ps/top might exceed the
            // counted value here, possibly by a significant margin (1.5x even!). Part
            // of that is not counting the outcome array, but most of that is due to
            // memory fragmentation and is out of our control without writing our own
            // memory manager.
            if (verbose_) {
                std::cerr << absl::StrFormat(
                        ("Approx %d mb in %d nodes after %d sims, garbage collecting with "
                         "limit %d ... "),
                        MemoryUsedMb(nodes_), nodes_, i, gc_limit_);
            }
            GarbageCollect(root.get());

            // Slowly increase or decrease to target releasing half the memory.
            gc_limit_ *= (nodes_ > max_nodes_ / 2 ? 1.25 : 0.9);
            gc_limit_ = std::max(MIN_GC_LIMIT, gc_limit_);
            if (verbose_) {
                std::cerr << absl::StrFormat(
                        "%d mb in %d nodes remaining\n",
                        MemoryUsedMb(nodes_), nodes_);
            }
        }
    }

    return root;
}

void MCTSBot::GarbageCollect(SearchNode *node) {
    if (node->children.empty()) {
        return;
    }
    bool clear_children = node->explore_count < gc_limit_;
    for (SearchNode &child: node->children) {
        GarbageCollect(&child);
    }
    if (clear_children) {
        nodes_ -= node->children.capacity();
        node->children.clear();
        node->children.shrink_to_fit();  // release the memory
    }
}