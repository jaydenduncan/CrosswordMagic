package edu.jsu.mcis.cs408.crosswordmagic.model;

import java.util.HashMap;

import edu.jsu.mcis.cs408.crosswordmagic.DefaultController;

public class PuzzleModel extends AbstractModel {

    private final int DEFAULT_PUZZLE_ID = 1;

    public static final String TAG = "PuzzleModel";

    private PuzzleDatabaseModel db;

    private Puzzle puzzle;

    public PuzzleModel(PuzzleDatabaseModel db) { this.db = db; }

    public void init() {

        puzzle = db.getPuzzle(DEFAULT_PUZZLE_ID);

    }

    public void getCluesAcross() {
        firePropertyChange(DefaultController.CLUES_ACROSS_PROPERTY, null, puzzle.getCluesAcross());
    }

    public void getCluesDown() {
        firePropertyChange(DefaultController.CLUES_DOWN_PROPERTY, null, puzzle.getCluesDown());
    }

    public void getGridLetters() {
        firePropertyChange(DefaultController.GRID_LETTERS_PROPERTY, null, puzzle.getLetters());
    }

    public void getGridNumbers() {
        firePropertyChange(DefaultController.GRID_NUMBERS_PROPERTY, null, puzzle.getNumbers());
    }

    public void setGuess(HashMap<String, String> params) {

        Integer num = Integer.parseInt(params.get("num"));
        String guess = params.get("guess").toUpperCase().trim();

        Word result = puzzle.guess(num, guess);

        /* if guess was correct, add word to database */

        if (result != null) {
            db.addGuess(result.getPuzzleid(), result.getId());
            firePropertyChange(DefaultController.GRID_LETTERS_PROPERTY, null, puzzle.getLetters());
        }

        /* if puzzle is solved, notify View; otherwise, notify View if guess was correct or not */

        if (puzzle.isSolved()) {
            firePropertyChange(DefaultController.SOLVED_PROPERTY, null, true);
        }
        else {
            firePropertyChange(DefaultController.GUESS_PROPERTY, null, (result != null));
        }

    }

}
