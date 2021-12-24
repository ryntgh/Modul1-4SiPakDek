package id.ryan.modul4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String NIK_WARGA = "NIK Warga";
    public static final String NAMA_WARGA = "Nama Warga";
    public static final String USIA_WARGA = "Usia Warga";
    public static final String JK_WARGA = "Jenis Kelamin Warga";
    public static final String PEKERJAAN_WARGA = "Pekerjaan Warga";
    public static final String SEEKBAR = "SEEKBAR";


    boolean isAllFieldsChecked = false;
    int seekBarValue;
    TextView Persentase;
    EditText nikWarga, namaWarga, usiaWarga;
    CheckBox pekerjaan1, pekerjaan2, pekerjaan3;
    RadioGroup jenisKelamin;
    RadioButton pria, wanita, radioButton;
    Button btnSubmit, btnReset;
    SeekBar seekBar;
    DBPenduduk dbPenduduk;
//    String SQLiteQuery;
//    SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbPenduduk = new DBPenduduk(getBaseContext());

        nikWarga = (EditText) findViewById(R.id.etNIK);
        namaWarga = (EditText) findViewById(R.id.etNamaWarga);
        usiaWarga = (EditText) findViewById(R.id.etUsiaWarga);

        pekerjaan1 = (CheckBox) findViewById(R.id.pekerjaan1);
        pekerjaan2 = (CheckBox) findViewById(R.id.pekerjaan2);
        pekerjaan3 = (CheckBox) findViewById(R.id.pekerjaan3);

        jenisKelamin = (RadioGroup) findViewById(R.id.radioJK);

        pria = (RadioButton) findViewById(R.id.jkPria);
        wanita = (RadioButton) findViewById(R.id.jkWanita);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnReset = (Button) findViewById(R.id.btnReset);

        seekBar = (SeekBar) findViewById(R.id.parameterKemudahan);
        Persentase = (TextView) findViewById(R.id.tvNilaiSeekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                seekBarValue = progressValue;
                String value = String.valueOf(progressValue);
                if(value.equals("0")){
                    Persentase.setText("Sulit");
                }else if (value.equals("1")){
                    Persentase.setText("Mudah");
                }else if (value.equals("2")){
                    Persentase.setText("Sangat Mudah");
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked){
                    launchSecondActivity();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nikWarga.setText("");
                namaWarga.setText("");
                usiaWarga.setText("");
                pekerjaan1.setChecked(false);
                pekerjaan2.setChecked(false);
                pekerjaan3.setChecked(false);
                pria.setChecked(false);
                wanita.setChecked(false);
                seekBar.setProgress(0);
                Persentase.setText("");
            }
        });

        Button viewData = findViewById(R.id.readData);
        viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewData.class));
            }
        });
        
    }
    private boolean CheckAllFields(){
        if (nikWarga.length() == 0) {
            nikWarga.setError("Data tidak boleh kosong");
            return false;
        }
        if (namaWarga.length() == 0) {
            namaWarga.setError("Data tidak boleh kosong");
            return false;
        }
        if (usiaWarga.length() == 0) {
            usiaWarga.setError("Data tidak boleh kosong");
            return false;
        }
        if (jenisKelamin.getCheckedRadioButtonId() == -1) {
            Toast.makeText(MainActivity.this,"Harap pilih jenis kelamin",Toast.LENGTH_SHORT).show();
//            wanita.setError("Harap pilih jenis kelamin");
            return false;
        }
        if (!pekerjaan1.isChecked() && !pekerjaan2.isChecked() && !pekerjaan3.isChecked()) {
            Toast.makeText(MainActivity.this,"Harap pilih pekerjaan",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void launchSecondActivity() {

        SQLiteDatabase create = dbPenduduk.getWritableDatabase();

        Intent intentData = new Intent(this, SecondActivity.class);
        int selectedId = jenisKelamin.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        String messageNIK = nikWarga.getText().toString();
        String messageNama = namaWarga.getText().toString();
        String messageUsia = usiaWarga.getText().toString();
        String messageRadio = radioButton.getText().toString();
        String messageSeekbar = String.valueOf(seekBarValue);
        String messagePekerjaan = "";
        if(pekerjaan1.isChecked()){
            messagePekerjaan = messagePekerjaan + "Siswa/Mahasiswa; ";
        }
        if(pekerjaan2.isChecked()){
            messagePekerjaan = messagePekerjaan + "Wirausaha; ";
        }
        if(pekerjaan3.isChecked()){
            messagePekerjaan = messagePekerjaan + "ASN/PNS; ";
        }

        intentData.putExtra(NIK_WARGA, messageNIK);
        intentData.putExtra(NAMA_WARGA, messageNama);
        intentData.putExtra(USIA_WARGA, messageUsia);
        intentData.putExtra(JK_WARGA, messageRadio);
        intentData.putExtra(SEEKBAR, messageSeekbar);
        intentData.putExtra(PEKERJAAN_WARGA, messagePekerjaan);

        //Membuat Map Baru, Yang Berisi Nama Kolom dan Data Yang Ingin Dimasukan
        ContentValues values = new ContentValues();
        values.put(DBPenduduk.MyColumns.NIK, messageNIK);
        values.put(DBPenduduk.MyColumns.Nama, messageNama);
        values.put(DBPenduduk.MyColumns.Usia, messageUsia);
        values.put(DBPenduduk.MyColumns.JenisKelamin, messageRadio);
        values.put(DBPenduduk.MyColumns.Pekerjaan, messagePekerjaan);
        values.put(DBPenduduk.MyColumns.Penilaian, messageSeekbar);

        //Menambahkan Baris Baru, Berupa Data Yang Sudah Diinputkan pada Kolom didalam Database
        create.insert(DBPenduduk.MyColumns.NamaTabel, null, values);

        startActivity(intentData);
    }
}