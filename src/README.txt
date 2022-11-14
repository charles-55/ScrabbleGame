SYSC3110 Project, Group 3 Milestone 1 README
Scrabble Game

Roadmap:
Implemented GUI based version of the Scrabble game

Deliverables:
	Bag.java  // @author Meyiwa Temile
	Board.java  // @author Osamudiamen Nwoko
	Main.java  // @author Group 3
	Player.java  // @author Ejeh Leslie
    Rack.java  // @author Ejeh Leslie
    Square.java  // @author Longjohn Dagogo
    Tile.java  // @author Meyiwa Temile
    ScrabbleUML.png  // @author Longjohn Dagogo
	GameMaster.java  // @author Osamudiamen Nwoko, Meyiwa Temile
	GameMasterTest.java // @author Ejeh Leslie, Longjohn Dagogo
	BoardController.java // @author Ejeh Leslie, Osamudiamen Nwoko
	CommandController.java // @author Ejeh Leslie, Osamudiamen Nwoko
	ScrabbleView.java // Osamudiamen Nwoko, Meyiywa Temile
	ScrabbleFrame.java // Osamudiamen Nwoko, Meyiwa Temile
	WordList.txt  //@author https://www.mit.edu/~ecprice/wordlist.10000
	README.txt  // @author Group 3


Game Rules:
The rules of the game is taken from https://en.wikipedia.org/wiki/Scrabble
We will follow the official game rules.


Game Play:
The first player combines words from his/her rack to form a word and places it on the board to read either across or down with one letter on the center square.
Diagonal words are not allowed. Then draw as many new letters as you played; always keep seven letters on your rack, as long as there are enough tiles left in the bag.
The play order is in order of Player creation. The second player, and then each in turn, adds one or more letters to those already played to form new words.
All letters played on a turn must be placed in one row across or down the board, to form at least one complete word.
If, at the same time, they touch others letters in adjacent rows, those must also form complete words. The player gets full credit for all words formed or modified on his or her turn.
Players may use a turn to exchange all, some, or none of the letters, this ends their turn.
The two blank tiles may be used as any letters. When playing a blank, you must state which letter it represents. It remains that letter for the rest of the game.
The game ends when all letters have been drawn and one player uses his or her last letter; or when all possible plays have been made.

New words may be formed by:
Adding one or more letters to a word or letters already on the board.
Placing a word at right angles to a word already on the board. The new word must use one of the letters already on the board or must add a letter to it.
Placing a complete word parallel to a word already played so that adjacent letters also form complete words.
No tile may be shifted or replaced after it has been played and scored.


How to Play:
For making any move, select the position on the board you intend to play, then enter the word and direction to play.
Every play must be connected to at least one previous tile, if it's not the first move of the game.
If you can't come up with a word, select "pass" to skip your turn or "exchange" to get new tiles and skip your turn.
If you make an illegal move or a wrong word, you will be notified and required to enter a new word and direction of play.


Known Issues:
save/saveAs unable to save game
load unable to load a previous game
Incorrect score from play