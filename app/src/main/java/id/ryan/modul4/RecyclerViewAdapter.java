package id.ryan.modul4;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList nikList;
    private ArrayList namaList;
    private ArrayList usiaList;
    private ArrayList jkList;
    private ArrayList pekerjaanList;
    private Context context;

    //Membuat Konstruktor pada Class RecyclerViewAdapter
    RecyclerViewAdapter(ArrayList nikList, ArrayList namaList, ArrayList usiaList, ArrayList jkList, ArrayList pekerjaanList){
        this.nikList = nikList;
        this.namaList = namaList;
        this.usiaList = usiaList;
        this.jkList = jkList;
        this.pekerjaanList = pekerjaanList;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView NIK, Nama, Usia, jenisKelamin, Pekerjaan;
        private ImageButton Overflow;

        ViewHolder(View itemView) {
            super(itemView);

            //Mendapatkan Context dari itemView yang terhubung dengan Activity ViewData
            context = itemView.getContext();

            //Menginisialisasi View-View untuk digunakan pada RecyclerView
            NIK = itemView.findViewById(R.id.nikView);
            Nama = itemView.findViewById(R.id.namaView);
            Usia = itemView.findViewById(R.id.usiaView);
            jenisKelamin = itemView.findViewById(R.id.jkView);
            Pekerjaan = itemView.findViewById(R.id.pekerjaanView);
            Overflow = itemView.findViewById(R.id.overflow);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //Memanggil Nilai/Value Pada View-View Yang Telah Dibuat pada Posisi Tertentu
        final String NIK = nikList.get(position).toString();
        final String Nama = namaList.get(position).toString();
        final String Usia = usiaList.get(position).toString();
        final String JenisKelamin = jkList.get(position).toString();
        final String Pekerjaan = pekerjaanList.get(position).toString();

        holder.NIK.setText(NIK);
        holder.Nama.setText(Nama);
        holder.Usia.setText(Usia + " Tahun");
        holder.jenisKelamin.setText(JenisKelamin);
        holder.Pekerjaan.setText(Pekerjaan);

        //Mengimplementasikan Menu Popup pada Overflow (ImageButton)
        holder.Overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Membuat Instance/Objek dari PopupMenu
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete:
                                //Menghapus Data Dari Database
                                DBPenduduk getDatabase = new DBPenduduk(view.getContext());
                                SQLiteDatabase DeleteData = getDatabase.getWritableDatabase();
                                //Menentukan di mana bagian query yang akan dipilih
                                String selection = DBPenduduk.MyColumns.NIK + " LIKE ?";
                                //Menentukan Nama Dari Data Yang Ingin Dihapus
                                String[] selectionArgs = {holder.NIK.getText().toString()};
                                DeleteData.delete(DBPenduduk.MyColumns.NamaTabel, selection, selectionArgs);

                                //Menghapus Data pada List dari Posisi Tertentu
                                int position = nikList.indexOf(NIK);
                                nikList.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(view.getContext(),"Data Dihapus",Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.update:
                                Intent dataForm = new Intent(view.getContext(), UpdateActivity.class);
                                dataForm.putExtra("SendNIK", holder.NIK.getText().toString());
                                dataForm.putExtra("SendNama", holder.Nama.getText().toString());
                                dataForm.putExtra("SendUsia", holder.Usia.getText().toString());
                                dataForm.putExtra("SendJK", holder.jenisKelamin.getText().toString());
                                dataForm.putExtra("SendPekerjaan", holder.Pekerjaan.getText().toString());

                                context.startActivity(dataForm);
                                ((Activity)context).finish();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return nikList.size();
    }

}