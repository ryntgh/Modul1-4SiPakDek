package id.ryan.modul4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    TextView nik, nama, usia, jenisKelamin, pekerjaan, seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intentWarga = getIntent();
        String messageNIK = intentWarga.getStringExtra(MainActivity.NIK_WARGA);
        String messageNama = intentWarga.getStringExtra(MainActivity.NAMA_WARGA);
        String messageUsia = intentWarga.getStringExtra(MainActivity.USIA_WARGA);
        String messageRadio = intentWarga.getStringExtra(MainActivity.JK_WARGA);
        String messagePekerjaan = intentWarga.getStringExtra(MainActivity.PEKERJAAN_WARGA);
        String messageSeekbar = intentWarga.getStringExtra(MainActivity.SEEKBAR);

        nik = (TextView) findViewById(R.id.scndNIKView);
        nama = (TextView) findViewById(R.id.scndNamaView);
        usia = (TextView) findViewById(R.id.scndUsiaView);
        jenisKelamin = (TextView) findViewById(R.id.scndJKView);
        pekerjaan = (TextView) findViewById(R.id.scndPekerjaanView);
        seekbar = (TextView) findViewById(R.id.scndSeekbarView);

        nik.setText(messageNIK);
        nama.setText(messageNama);
        usia.setText(messageUsia + " Tahun");
        jenisKelamin.setText(messageRadio);
        pekerjaan.setText(messagePekerjaan);
        switch (messageSeekbar) {
            case "0":
                seekbar.setText("Sulit");
                break;
            case "1":
                seekbar.setText("Mudah");
                break;
            case "2":
                seekbar.setText("Sangat Mudah");
                break;
        }

    }

    public void launchMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        Toast messageText = Toast.makeText(getApplicationContext(), "Selamat Tinggal !", Toast.LENGTH_SHORT);
        messageText.show();
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast messageText = Toast.makeText(getApplicationContext(), "Data ditambahkan !", Toast.LENGTH_SHORT);
        messageText.show();
    }

    @Override
    protected void onStop() {
        Toast messageText = Toast.makeText(getApplicationContext(), "Menutup Activity..", Toast.LENGTH_SHORT);
        messageText.show();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Toast messageText = Toast.makeText(getApplicationContext(), "Selamat Tinggal !", Toast.LENGTH_SHORT);
        messageText.show();
        super.onDestroy();
    }

    protected void onRestart() {
        Toast messageText = Toast.makeText(getApplicationContext(), "Selamat Datang Kembali !", Toast.LENGTH_SHORT);
        messageText.show();
        super.onRestart();
    }
}