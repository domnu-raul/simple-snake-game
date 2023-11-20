package com.myjava.game.Classes;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.myjava.game.Classes.Exceptions.InvalidNameException;
import com.myjava.game.GameFiles;

import java.util.*;

public class Leaderboard {
    public static boolean loaded;
    public enum Sorters {
        BY_NAME,
        BY_LEVEL,
        BY_SCORE;
    }

    private static Sorters current = Sorters.BY_SCORE;
    private static ArrayList<Row> rows;
    private class Row {
        protected String name;
        protected int score;
        protected int level;

        public Row() { };

        public Row(String name, int score, int level) {
            this.name = name;
            this.score = score;
            this.level = level;
        }
    }

    private class NameComparator extends Row implements Comparator<Row> {

        @Override
        public int compare(Row o1, Row o2) {
            return String.CASE_INSENSITIVE_ORDER.compare(o1.name, o2.name);
        }
    }

    private class ScoreComparator extends Row implements Comparator<Row> {

        @Override
        public int compare(Row o1, Row o2) {
            return -1 * Integer.compare(o1.score, o2.score);
        }
    }
    private class LevelComparator extends Row implements Comparator<Row> {

        @Override
        public int compare(Row o1, Row o2) {
            return -1 * Integer.compare(o1.level, o2.level);
        }
    }


    public Leaderboard() {
        this.rows = new ArrayList<Row>();

        FileHandle file = GameFiles.LEADERBOARD.path;
        Iterator<String> lines = Arrays.stream(file.readString().split("\n")).iterator();
        while (lines.hasNext()) {
            String line = lines.next();
            String[] sep = line.split(",");
            String name_str = sep[0].trim();
            String score_str = sep[1].replaceAll("[^0-9]", "");
            String level_str = sep[2].replaceAll("[^0-9]", "");

            this.rows.add(
                    new Row(
                            name_str,
                            Integer.parseInt(score_str),
                            Integer.parseInt(level_str)
                    )
            );
        }

        Collections.sort(rows, new ScoreComparator());
        this.loaded = true;
    }

    public Table get_table(Skin skin) {
        Table T = new Table();
        if (loaded) {
            T.add(
                    new Label(String.format("%1$-15s %2$-15s %3$s", "Name", "Score", "Level"), skin)
            ).width(600).height(50).padBottom(10);
            T.row();

            for (final Row row: this.rows) {
                Label L = new Label(String.format("%1$-15s %2$-15s %3$s", row.name, row.score, row.level), skin);
                T.add( L ).width(600).height(50).padBottom(10);
                T.row();

//                L.addListener(new ClickListener() {
//                    @Override
//                    public void clicked(InputEvent event, float x, float y){
//                        game.setScreen(new SnakeScreen(game, new SaveHandler(row.name, row.level, row.score)));
//                    }
//                });
            }
        }
        else {
            Label.LabelStyle err_skin = new Label.LabelStyle(skin.getFont("Inconsolata-Black"), Color.valueOf("ff9087"));
            Label err_label = new Label(String.format("%s", this.rows.get(0).name), err_skin);
            err_label.setWrap(true);
            T.add(err_label).width(600).height(500);
            T.row();
        }

        return T;
    }
    public void sort(Sorters sorter) {
        Comparator<? super Row> method;
        switch (sorter) {
            case BY_LEVEL:
                method = new LevelComparator();
                break;
            case BY_SCORE:
                method = new ScoreComparator();
                break;
            default:
                method = new NameComparator();
        }

        if (sorter == current) {
            Collections.sort(this.rows, method.reversed());
            current = null;
        }
        else {
            Collections.sort(this.rows, method);
            current = sorter;
        }
    }

    public static void add(String name, int score, int level) throws InvalidNameException {
        if (!name.isEmpty()) {
            if (!name.replaceAll("[0-9a-zA-Z_]", "").isEmpty())
                throw  new InvalidNameException("Name should only contain alphanumerical characters and underscores.");
            FileHandle file = GameFiles.LEADERBOARD.path;
            file.writeString(String.format("%s, %03d, Level %d\n", name, score, level), true);
        }
    }
}
