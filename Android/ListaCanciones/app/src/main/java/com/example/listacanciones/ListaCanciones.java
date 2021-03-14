package com.example.listacanciones;



import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class ListaCanciones extends AppCompatActivity {
    MediaPlayer mp = new MediaPlayer();
    int position;

    int canciones[] = {
            R.raw.believer,
            R.raw.sonido_1,
            R.raw.sonido_2,
            R.raw.sonido_3,
            R.raw.sonido_4,
            R.raw.sonido_5,
            R.raw.sonido_6,

    };
    String[] title = new String[]{
            "ImagineDragons(canción buena)",
            "Sonido1",
            "Sonido2",
            "Sonido3",
            "Sonido4",
            "Sonido5",
            "Sonido6",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView list30 = (ListView) findViewById(R.id.lista_canciones);

        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, title);

        list30.setAdapter(adaptador);

        list30.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                method(position);
            }
        });

    }


    public void method(int position){
        if((mp.isPlaying()==true) && (this.position == position))
            mp.stop();
        else{
            this.position = position;
            try{
                mp.reset();
                mp.release();
                mp= MediaPlayer.create(this, canciones[position]);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.seekTo(0);
                mp.start();
            }
            catch (Exception e)
            {
                Toast.makeText(getApplication(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

