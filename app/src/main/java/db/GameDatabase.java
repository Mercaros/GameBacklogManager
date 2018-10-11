package db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import models.Game;
import utils.GameDao;

/**
 * Created by khaled on 10-10-18.
 */

@Database(entities = {Game.class}, version = 1)
public abstract class GameDatabase extends RoomDatabase {
    private static GameDatabase INSTANCE;

    public abstract GameDao gameDao();

    private final static String DB_NAME = "game_database";

    public static GameDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, GameDatabase.class, DB_NAME).build();
        }
        return INSTANCE;
    }
}
