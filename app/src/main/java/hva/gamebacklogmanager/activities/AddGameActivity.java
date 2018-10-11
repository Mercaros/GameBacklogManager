package hva.gamebacklogmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import db.GameDatabase;
import hva.gamebacklogmanager.R;
import models.Game;


/**
 * Created by khaled on 10-10-18.
 */

public class AddGameActivity extends AppCompatActivity {
    private FloatingActionButton saveGameButton;
    private EditText title;
    private EditText platform;
    private EditText notes;
    private Spinner statusSpinner;
    private Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        //Init values
        title = findViewById(R.id.titleInput);
        platform = findViewById(R.id.platformInput);
        notes = findViewById(R.id.notesInput);
        saveGameButton = findViewById(R.id.btn_save_game);
        executor = Executors.newSingleThreadExecutor();
        //Init spinner
        statusSpinner = findViewById(R.id.statusInput);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        saveGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(platform.getText().toString())) {
                    Game game = new Game(title.getText().toString(), platform.getText().toString(), notes.getText().toString(), statusSpinner.getSelectedItem().toString(), currentDate());
                    executor.execute(() -> {GameDatabase.getInstance(AddGameActivity.this).gameDao().insert(game);});
                    Intent intent = new Intent(AddGameActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(AddGameActivity.this, R.string.add_game, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AddGameActivity.this, R.string.empty_fields, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private String currentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy ");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }
}
