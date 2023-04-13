package edu.jsu.mcis.cs408.crosswordmagic.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.opencsv.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import edu.jsu.mcis.cs408.crosswordmagic.R;

public class PuzzleDatabaseModel extends SQLiteOpenHelper {

    private Context context;

    private static final int DATABASE_VERSION = 1;
    private static final int CSV_HEADER_FIELDS = 4;
    private static final int CSV_DATA_FIELDS = 6;

    public PuzzleDatabaseModel(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {

        super(context, context.getString(R.string.database_file_name), factory, DATABASE_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create new database for new instances

        db.execSQL(context.getString(R.string.sql_create_puzzles_table));
        db.execSQL(context.getString(R.string.sql_create_words_table));
        db.execSQL(context.getString(R.string.sql_create_guesses_table));

        // add initial puzzle data from CSV data file (a raw resource)

        addPuzzleFromCSV(db, R.raw.puzzle);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // delete and re-create database for upgrades

        db.execSQL(context.getString(R.string.sql_drop_guesses_table));
        db.execSQL(context.getString(R.string.sql_drop_words_table));
        db.execSQL(context.getString(R.string.sql_drop_puzzles_table));

        onCreate(db);

    }

    public void addPuzzleFromCSV(SQLiteDatabase db, int id) {

        try {

            // use OpenCSV to read CSV file data to a list of String arrays

            BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(id)));
            CSVParser parser = (new CSVParserBuilder()).withSeparator('\t').withIgnoreQuotations(true).build();
            CSVReader reader = (new CSVReaderBuilder(br)).withCSVParser(parser).build();
            List<String[]> csv = reader.readAll();

            HashMap<String, String> params;

            // get header row contents

            String[] fields = csv.get(0);

            // if header row is valid, get header data

            if (fields.length == CSV_HEADER_FIELDS) {

                // place header data in puzzle parameter map

                params = new HashMap<>();

                params.put(context.getString(R.string.sql_field_name), fields[0]);
                params.put(context.getString(R.string.sql_field_description), fields[1]);

                params.put(context.getString(R.string.sql_field_height), fields[2]);
                params.put(context.getString(R.string.sql_field_width), fields[3]);

                // call "addPuzzle" to add puzzle entry to database (the "puzzles" table)

                int puzzleid = addPuzzle(db, params);

                // loop through remaining rows to get the words

                for (int i = 1; i < csv.size(); ++i) {

                    fields = csv.get(i);

                    if (fields.length == CSV_DATA_FIELDS) {

                        params = new HashMap<>();

                        /*


                            INSERT YOUR CODE HERE


                         */

                        // call "addWord" to add word to database (the "words" table)

                        addWord(db, params);

                    }

                }

            }

            // close input buffer

            br.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int addPuzzle(SQLiteDatabase db, HashMap<String, String> params) {

        String name = context.getString(R.string.sql_field_name);
        String description = context.getString(R.string.sql_field_description);
        String height = context.getString(R.string.sql_field_height);
        String width = context.getString(R.string.sql_field_width);

        ContentValues values = new ContentValues();
        values.put(name, params.get(name));
        values.put(description, params.get(description));
        values.put(height, Integer.parseInt(params.get(height)));
        values.put(width, Integer.parseInt(params.get(width)));

        int key = (int)db.insert(context.getString(R.string.sql_table_puzzles), null, values);

        return key;

    }

    public int addWord(SQLiteDatabase db, HashMap<String, String> params) {

        ContentValues values = new ContentValues();

        /*


            INSERT YOUR CODE HERE


         */

        int key = (int)db.insert(context.getString(R.string.sql_table_words), null, values);

        return key;

    }

    public Puzzle getPuzzle(int id) {

        Puzzle puzzle = null;

        SQLiteDatabase db = this.getWritableDatabase();

        // get puzzle data from database (the "puzzles" table)

        String query = context.getString(R.string.sql_get_puzzle);
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {

            // add data from database to parameter map

            cursor.moveToFirst();

            HashMap<String, String> params = new HashMap<>();
            params.put(context.getString(R.string.sql_field_name), cursor.getString(1));
            params.put(context.getString(R.string.sql_field_description), cursor.getString(2));
            params.put(context.getString(R.string.sql_field_height), String.valueOf(cursor.getInt(3)));
            params.put(context.getString(R.string.sql_field_width), String.valueOf(cursor.getInt(4)));

            cursor.close();

            // create new Puzzle object

            puzzle = new Puzzle(params);

            // get word list from database (the "words" table)

            query = context.getString(R.string.sql_get_words);
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

            if (cursor.moveToFirst()) {

                cursor.moveToFirst();

                // loop through word list in results; add each word to Puzzle as a new Word object

                /*


                    INSERT YOUR CODE HERE


                 */

                cursor.close();

            }

        }

        db.close();

        // return fully initialized Puzzle object

        return puzzle;

    }

}