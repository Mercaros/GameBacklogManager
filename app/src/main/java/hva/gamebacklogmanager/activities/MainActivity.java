package hva.gamebacklogmanager.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.GameAdapter;
import db.GameDatabase;
import hva.gamebacklogmanager.R;
import models.Game;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;
    private FloatingActionButton addGameButton;
    private List<Game> gameList;
    static GameDatabase db;

    //Constants
    public final static int TASK_GET_ALL_GAMES = 0;
    public final static int TASK_DELETE_GAME = 1;
    public final static int TASK_UPDATE_GAME = 2;
    public final static int TASK_INSERT_GAME = 3;
    public static final String TAG = "Game";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addGameButton = findViewById(R.id.btn_add_game);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gameList = new ArrayList<>();
        db = GameDatabase.getInstance(this);
        new GameAsyncTask(TASK_GET_ALL_GAMES).execute();
        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddGameActivity.class);
                startActivity(intent);
            }
        });
        //Swipe to remove item
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                    target) {
                return false;
            }
            //Called when a user swipes left or right
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Get the index corresponding to the selected position
                int position = (viewHolder.getAdapterPosition());
                new GameAsyncTask(TASK_DELETE_GAME).execute(gameList.get(position));
                Toast.makeText(MainActivity.this, R.string.remove_game, Toast.LENGTH_SHORT).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void updateUI() {
        if (gameAdapter == null) {
            gameAdapter = new GameAdapter(gameList, new GameAdapter.OnItemClickListener() {
                //Called when a user clicks on a item
                @Override
                public void onItemClick(Game game) {
                    Intent intent = new Intent(MainActivity.this, EditGameActivity.class);
                    intent.putExtra(TAG, game);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(gameAdapter);
        } else {
            //Refresh list
            gameAdapter.swapList(gameList);
        }
    }

    //Background thread
    public class GameAsyncTask extends AsyncTask<Game, Void, List> {
        private int taskCode;

        public GameAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected List doInBackground(Game... games) {
            switch (taskCode) {
                case TASK_DELETE_GAME:
                    db.gameDao().delete(games[0]);
                    break;

                case TASK_UPDATE_GAME:
                    db.gameDao().update(games[0]);
                    break;

                case TASK_INSERT_GAME:
                    db.gameDao().insert(games[0]);
                    break;
            }
            return db.gameDao().getAllGames();
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onGameDbUpdated(list);
        }

        public void onGameDbUpdated(List list) {
            gameList = list;
            updateUI();
        }
    }

}
