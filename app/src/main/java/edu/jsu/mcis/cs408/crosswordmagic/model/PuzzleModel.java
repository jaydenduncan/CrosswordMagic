package edu.jsu.mcis.cs408.crosswordmagic.model;

import edu.jsu.mcis.cs408.crosswordmagic.DefaultController;

public class PuzzleModel extends AbstractModel {

    private int DEFAULT_PUZZLE_ID = 1;

    public static final String TAG = "PuzzleModel";

    private PuzzleDatabaseModel db;

    private Puzzle puzzle;

    public PuzzleModel(PuzzleDatabaseModel db) {
        this.db = db;
    }

    public void initDefault() {

        // get default puzzle data (with a puzzle ID of 1)

        puzzle = db.getPuzzle(DEFAULT_PUZZLE_ID);

        // fire a property change to display list of clues in Activity

        firePropertyChange(DefaultController.CLUES_DOWN_PROPERTY, "", puzzle.getCluesAcross());

    }

}