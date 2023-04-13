package edu.jsu.mcis.cs408.crosswordmagic.model;

import java.util.HashMap;

public class Puzzle {

    private static final char BLOCK_CHAR = '*';
    private static final char BLANK_CHAR = ' ';

    private HashMap<String, Word> words;
    private String name, description;
    private Integer height, width;

    private Character[][] letters;
    private Integer[][] boxes;

    private StringBuilder cluesAcrossBuffer, cluesDownBuffer;

    public Puzzle(HashMap<String, String> params) {

        this.name = params.get("name");
        this.description = params.get("description");
        this.height = Integer.parseInt(params.get("height"));
        this.width = Integer.parseInt(params.get("width"));

        words = new HashMap<>();

        letters = new Character[height][width];
        boxes = new Integer[height][width];

        cluesAcrossBuffer = new StringBuilder();
        cluesDownBuffer = new StringBuilder();

        // fill letter squares with blocks and number squares with zeros (default values)

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                letters[i][j] = BLOCK_CHAR;
                boxes[i][j] = 0;
            }
        }

    }

    public void addWord(Word word) {

        // create composite key (example: "1A" for word at box 1, across)

        String key = (word.getBox() + word.getDirection().toString());

        // add word to collection

        words.put(key, word);

        // get word properties (length and row/column of first letter)

        int row = word.getRow();
        int column = word.getColumn();
        int length = word.getWord().length();

        // add box number to boxes array

        boxes[row][column] = word.getBox();

        // clear letter boxes (remove blocks from letter array to make room for the word)

        for (int i = 0; i < length; ++i) {

            letters[row][column] = BLANK_CHAR;

            if (word.isAcross())
                column++;
            else if (word.isDown())
                row++;

        }

        // append clue (across or down) to corresponding StringBuilder

        if (word.isAcross())
            cluesAcrossBuffer.append(word.getBox()).append(": ").append(word.getClue()).append("\n");

        else if (word.isDown())
            cluesDownBuffer.append(word.getBox()).append(": ").append(word.getClue()).append("\n");

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public String getCluesAcross() {
        return cluesAcrossBuffer.toString();
    }

    public String getCluesDown() {
        return cluesDownBuffer.toString();
    }

    public int getNumWords() {
        return words.size();
    }

    public Character[][] getLetters() {
        return letters;
    }

    public Integer[][] getBoxes() {
        return boxes;
    }

}