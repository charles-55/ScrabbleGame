# Scrabble Game
A simplified version of the classic board game Scrabble.

## Authors
- Osamudiamen Nwoko
- Leslie Ejeh
- Meyiwa Temile
- LongJohn Dagogo

## Game Rules
The rules of the game is taken from https://en.wikipedia.org/wiki/Scrabble.

## Game Play
In this game you can choose to play with real players or AI players programmed to play the game with you.
The first player combines words from his/her rack to form a word and places it on the board to read either across or down with one letter on the center square. Diagonal words are not allowed. The game automatically draws new letters to replace played ones, as long as there are enough tiles left in the bag.
The play order is in order of Player creation. The second player, and then each in turn, adds one or more letters to those already played to form new words.
All letters played on a turn must be placed in one row across or down the board, to form at least one complete word.
If, at the same time, they touch others letters in adjacent rows, those must also form complete words. The player gets full credit for all words formed or modified on his or her turn.
Players may use a turn to exchange all, some, or none (pass) of the letters, this ends their turn.
The two blank tiles may be used as any letters. When playing a blank, you must state which letter it represents. It remains that letter for the rest of the game.
The game ends when all letters have been drawn and one player uses his or her last letter; or when all possible plays have been made.
You can UNDO and REDO a play, play with a customizable board, and save and load games.

## How to Play
For making any move, select the position on the board you intend to play, then enter the word and direction to play.
Every play must be connected to at least one previous tile, if it's not the first move of the game.
If you can't come up with a word, select "pass" to skip your turn or "exchange" to get new tiles and skip your turn.
If you make an illegal move or a wrong word, you will be notified and required to enter a new word and direction of play.

## Running the Jar File
1. Extract file content from the jar file.
2. Create a new folder called "src".
3. Move the "Audio" and "Graphics" folder in to the "src" folder.
4. Open your terminal and navigate to the folder containing the extracted files.
5. Enter "java Main.java" in the terminal.

## Known Issues
1. Gameplay takes long to load.
2. AI doesn't work correctly.

## Files
AIPlayer.java //@author Osamudiamen Nwoko
Bag.java  // @author Meyiwa Temile
Board.java  // @author Osamudiamen Nwoko, Ejeh Leslie
BoardController.java // @author Ejeh Leslie, Osamudiamen Nwoko
CommandController.java // @author Ejeh Leslie, Osamudiamen Nwoko
CustomizableController// @author Osamudiamen Nwoko
GameMaster.java  // @author Osamudiamen Nwoko, Meyiwa Temile
GameMasterTest.java // @author Ejeh Leslie, Longjohn Dagogo
Main.java  // @author Group 3
Milestone 4 UML.png  // @author Longjohn Dagogo
PlayEvent.java  // @author Osamudiamen Nwoko
Player.java  // @author Ejeh Leslie
README.txt  // @author Group 3
Rack.java  // @author Ejeh Leslie
ScrabbleFrame.java // @author Osamudiamen Nwoko, Meyiwa Temile
ScrabbleView.java // @author Osamudiamen Nwoko, Meyiywa Temile
Square.java  // @author Longjohn Dagogo
Tile.java  // @author Meyiwa Temile
WordList.txt  //@author https://www.mit.edu/~ecprice/wordlist.10000
