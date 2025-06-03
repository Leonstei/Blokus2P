
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Mindestversion von CMake
cmake_minimum_required(VERSION 3.22.1)

# Name der nativen Bibliothek. Muss mit System.loadLibrary("blokus_mcts") in Kotlin übereinstimmen.
#add_library(
        #blokus_mcts
        #SHARED
        #blokus_mcts.cpp
        #blokus_game_state.cpp
        #blokus_board.cpp
        #blokus_pieces.cpp
#) # Alle deine .cpp Dateien hier auflisten

# Wenn deine Header in Unterverzeichnissen sind, musst du sie hier hinzufügen
# include_directories(${CMAKE_SOURCE_DIR}/path/to/your/headers)

find_library( log-lib log )

#target_link_libraries( blokus_mcts ${log-lib} )

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED TRUE)
