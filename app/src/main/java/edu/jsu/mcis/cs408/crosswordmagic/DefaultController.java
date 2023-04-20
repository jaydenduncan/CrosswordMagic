package edu.jsu.mcis.cs408.crosswordmagic;

import java.util.HashMap;

public class DefaultController extends AbstractController {

    public static final String CLUES_ACROSS_PROPERTY = "CluesAcross";
    public static final String CLUES_DOWN_PROPERTY = "CluesDown";

    public static final String GRID_LETTERS_PROPERTY = "GridLetters";
    public static final String GRID_NUMBERS_PROPERTY = "GridNumbers";

    public static final String GUESS_PROPERTY = "Guess";

    public static final String SOLVED_PROPERTY = "Solved";

    public void getCluesAcross() {
        getModelProperty(CLUES_ACROSS_PROPERTY);
    }

    public void getCluesDown() {
        getModelProperty(CLUES_DOWN_PROPERTY);
    }

    public void getGridLetters() {
        getModelProperty(GRID_LETTERS_PROPERTY);
    }

    public void getGridNumbers() {
        getModelProperty(GRID_NUMBERS_PROPERTY);
    }

    public void setGuess(HashMap<String, String> params) {
        setModelProperty(GUESS_PROPERTY, params);
    }

}