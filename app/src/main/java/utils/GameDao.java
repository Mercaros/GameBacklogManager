package utils;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import models.Game;

/**
 * Created by khaled on 10-10-18.
 */
@Dao
public interface GameDao {
    @Query("SELECT * FROM game")
    List<Game> getAllGames();

    @Query("SELECT * FROM game where title LIKE  :title")
    Game findByTitle(String title);

    @Insert
    void insert(Game game);

    @Delete
    void delete(Game game);

    @Update
    void update(Game game);
}
