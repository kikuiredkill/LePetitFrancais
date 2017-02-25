package com.example.kikui.lepetitfranais.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kikui on 25/02/2017.
 */

public class ScoreBDD {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "maBDD.db";

    private static final String TABLE_JEUX = "table_jeux";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_JEUX = "Jeux";
    private static final int NUM_COL_JEUX = 1;
    private static final String COL_SCORES = "Scores";
    private static final int NUM_COL_SCORES = 2;

    private SQLiteDatabase bdd;

    private DataBase maBaseSQLite;

    public ScoreBDD(Context context){
        //On créer la BDD et sa table
        maBaseSQLite = new DataBase(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertJeu(Jeu jeu){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_JEUX, jeu.getJeu());
        values.put(COL_SCORES, jeu.getScore());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_JEUX, null, values);
    }

    public int updateJeu(int id, Jeu jeu){
        //La mise à jour d'un jeu dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simple préciser quelle jeu on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_JEUX, jeu.getJeu());
        values.put(COL_SCORES, jeu.getScore());
        return bdd.update(TABLE_JEUX, values, COL_ID + " = " +id, null);
    }

    public int removeJeuWithID(int id){
        //Suppression d'un jeu de la BDD grâce à l'ID
        return bdd.delete(TABLE_JEUX, COL_ID + " = " +id, null);
    }

    public int getScoreWithJeu(String jeu){
        //Récupère dans un Cursor la valeur correspondant au score d'un jeu contenu dans la BDD (ici on sélectionne le score grâce à son jeu)
        Cursor c = bdd.query(TABLE_JEUX, new String[] {COL_SCORES}, COL_JEUX + " LIKE \"" + jeu +"\"", null, null, null, null);
        if (c.getCount() == 0)
            return 0;
        c.moveToFirst();
        int score;
        score = c.getInt(NUM_COL_SCORES);
        c.close();
        return score;
    }
}