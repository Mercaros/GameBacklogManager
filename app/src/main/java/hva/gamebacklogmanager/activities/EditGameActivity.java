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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import db.GameDatabase;
import hva.gamebacklogmanager.R;
import models.Game;

/**
 * Created by khaled on 10-10-18.
 */

public class EditGameActivity extends AppCompatActivity {
    private FloatingActionButton editGameButton;
    private EditText title;
    private EditText platform;
    private EditText notes;
    private Spinner statusSpinner;
    private Executor executor;
    private Game updateGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);
        //Init values
        editGameButton = findViewById(R.id.btn_save_edit_game);
        title = findViewById(R.id.titleInputEdit);
        platform = findViewById(R.id.platformInputEdit);
        notes = findViewById(R.id.notesInputEdit);
        statusSpinner = findViewById(R.id.statusInputEdit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        initValues();
        executor = Executors.newSingleThreadExecutor();
        editGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(platform.getText().toString())) {
                updateGame.setTitle(title.getText().toString());
                updateGame.setPlatform(platform.getText().toString());
                updateGame.setNotes(notes.getText().toString());
                updateGame.setStatus(statusSpinner.getSelectedItem().toString());
                executor.execute(() -> {GameDatabase.getInstance(EditGameActivity.this).gameDao().update(updateGame);});
                Intent intent = new Intent(EditGameActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(EditGameActivity.this, R.string.update_game, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditGameActivity.this, R.string.empty_fields, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Init values and set the text
    private void initValues() {
        updateGame = getIntent().getParcelableExtra(MainActivity.TAG);
        title.setText(updateGame.getTitle());
        platform.setText(updateGame.getPlatform());
        notes.setText(updateGame.getNotes());
        statusSpinner.setSelection(getIndex(statusSpinner, updateGame.getStatus()));
    }

    //Get spinner String
    private int getIndex(Spinner spinner, String status) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(status)) {
                return i;
            }
        }
        return -1; //Not found
    }
}

