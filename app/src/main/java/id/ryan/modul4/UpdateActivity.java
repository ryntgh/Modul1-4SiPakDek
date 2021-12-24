package id.ryan.modul4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    int seekBarValue;
    private DBPenduduk MyDatabase;
    private EditText NewNama, NewUsia, NewNIK;
    private TextView NewPersentase;
    private RadioGroup NewJK;
    private RadioButton NewJKselected;
    private CheckBox pekerjaan1Update, pekerjaan2Update, pekerjaan3Update;
    private SeekBar NewSeekBar;
    private String getNewNIK, getNewNama, getNewUsia, getNewJK, getNewPekerjaan, getNewSeekBar;
    private Button Update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        MyDatabase = new DBPenduduk(getBaseContext());

        NewNIK = findViewById(R.id.nikUpdateEdit);
        NewNama = findViewById(R.id.namaUpdateEdit);
        NewUsia = findViewById(R.id.usiaUpdateEdit);
        NewJK = findViewById(R.id.radioJKUpdate);
        NewSeekBar = findViewById(R.id.parameterKemudahanUpdate);
        NewPersentase = findViewById(R.id.tvNilaiSeekBarUpdate);

        NewSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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
                    NewPersentase.setText("Sulit");
                }else if (value.equals("1")){
                    NewPersentase.setText("Mudah");
                }else if (value.equals("2")){
                    NewPersentase.setText("Sangat Mudah");
                }
            }
        });

        pekerjaan1Update = (CheckBox) findViewById(R.id.pekerjaan1Update);
        pekerjaan2Update = (CheckBox) findViewById(R.id.pekerjaan2Update);
        pekerjaan3Update = (CheckBox) findViewById(R.id.pekerjaan3Update);

        //Menerima Data Nama dan NIK yang telah dipilih Oleh User untuk diposes
        NewNama.setText(getIntent().getExtras().getString("SendNama"));
        NewNIK.setText(getIntent().getExtras().getString("SendNIK"));
        NewUsia.setText(getIntent().getExtras().getString("SendUsia"));
        String oldPekerjaan = getIntent().getExtras().getString("SendPekerjaan");
        String oldJK = getIntent().getExtras().getString("SendJK");
        Log.d("aaaa", oldPekerjaan);

        if (oldPekerjaan.equals("Siswa/Mahasiswa;")){
            pekerjaan1Update.setChecked(true);
        }else if (oldPekerjaan.equals("Wirausaha;")){
            pekerjaan2Update.setChecked(true);
        }else if (oldPekerjaan.equals("ASN/PNS;")){
            pekerjaan3Update.setChecked(true);
        }


        if (oldJK.equals("Laki-laki")){
            NewJK.check(R.id.jkPriaUpdate);
        }else if (oldJK.equals("Perempuan")){
            NewJK.check(R.id.jkWanitaUpdate);
        }

        Update = findViewById(R.id.btnUpdate);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpdateData();
                startActivity(new Intent(UpdateActivity.this, ViewData.class));
                finish();
            }
        });
    }

    private void setUpdateData(){
        int selectedId = NewJK.getCheckedRadioButtonId();
        NewJKselected = (RadioButton) findViewById(selectedId);

        getNewNIK = NewNIK.getText().toString();
        getNewNama = NewNama.getText().toString();
        getNewUsia = NewUsia.getText().toString();
        getNewJK = NewJKselected.getText().toString();
        getNewSeekBar = String.valueOf(seekBarValue);
        getNewPekerjaan = "";
        if(pekerjaan1Update.isChecked()){
            getNewPekerjaan = getNewPekerjaan + "Siswa/Mahasiswa; ";
        }
        if(pekerjaan2Update.isChecked()){
            getNewPekerjaan = getNewPekerjaan + "Wirausaha; ";
        }
        if(pekerjaan3Update.isChecked()){
            getNewPekerjaan = getNewPekerjaan + "ASN/PNS; ";
        }

        Intent intentPenduduk = getIntent();
        String NIK = intentPenduduk.getExtras().getString("SendNIK");

        SQLiteDatabase database = MyDatabase.getReadableDatabase();

        if (getNewNIK.length() == 0 || getNewNama.length() == 0 || getNewUsia.length() == 0 || !pekerjaan1Update.isChecked() && !pekerjaan2Update.isChecked() && !pekerjaan3Update.isChecked()){
            Toast.makeText(UpdateActivity.this,"Update Data Gagal",Toast.LENGTH_SHORT).show();
            Toast.makeText(UpdateActivity.this,"Data Tidak Boleh Kosong!",Toast.LENGTH_SHORT).show();
        }else{
            //Memasukan Data baru pada 2 kolom
            ContentValues values = new ContentValues();
            values.put(DBPenduduk.MyColumns.NIK, getNewNIK);
            values.put(DBPenduduk.MyColumns.Nama, getNewNama);
            values.put(DBPenduduk.MyColumns.Usia, getNewUsia);
            values.put(DBPenduduk.MyColumns.JenisKelamin, getNewJK);
            values.put(DBPenduduk.MyColumns.Pekerjaan, getNewPekerjaan);
            values.put(DBPenduduk.MyColumns.Penilaian, getNewSeekBar);

            //Untuk Menentukan Data/Item yang ingin diubah berdasarkan NIK
            String selection = DBPenduduk.MyColumns.NIK + " LIKE ?";
            String[] selectionArgs = {NIK};
            database.update(DBPenduduk.MyColumns.NamaTabel, values, selection, selectionArgs);
            Toast.makeText(getApplicationContext(), "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
        }

    }
}