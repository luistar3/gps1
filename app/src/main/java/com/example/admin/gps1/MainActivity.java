package com.example.admin.gps1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/////
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.Bundle;

import java.io.DataOutputStream;
import java.io.IOException;




public class MainActivity extends AppCompatActivity {


    private Socket client;
    private PrintWriter printwriter;
    private DataOutputStream dataOutputStream;
    private EditText  etIp,etPort,usuario;
    private TextView etMsg;
    private Button button;
    private String messsage;
    int port =0;
    ;
    // numero telefonico

    String numerotel;

    //////////////

    TextView mensaje1;
    TextView mensaje2;
    TextView dato;
    TextView p;
    String user;
    String x;
    String y;
    String msj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // numero telelfono
            TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
            numerotel = tMgr.getLine1Number();




        //
        usuario = (EditText) findViewById(R.id.txt_usuario);
        etIp = (EditText) findViewById(R.id.editText1);
        etPort = (EditText) findViewById(R.id.editText2);
        etMsg = (TextView) findViewById(R.id.txt_dato);
        button = (Button) findViewById(R.id.button1);
        // Asignacion de nombre de usuario
        user=usuario.getText().toString();





        mensaje1 = (TextView) findViewById(R.id.mensaje_id);
        mensaje2 = (TextView) findViewById(R.id.mensaje_id2);
        dato= (TextView) findViewById(R.id.txt_dato);
        //p= (TextView) findViewById(R.id.txt_p);

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new  Localizacion();
        Local.setMainActivity(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            return;

        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,(LocationListener) Local);

        mensaje1.setText("Localizacion agregada");
        mensaje2.setText("");





//paso

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                messsage = etMsg.getText().toString();

               // etMsg.setText("");
                port = Integer.parseInt(etPort.getText().toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            client = new Socket (etIp.getText().toString(),port);
                            dataOutputStream = new DataOutputStream(client.getOutputStream());
                            dataOutputStream.writeUTF(usuario.getText()+messsage);
                            dataOutputStream.flush();
                            dataOutputStream.close();
                            client.close();
                        }catch (UnknownHostException e){
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();
            }




        });







    }
    //obtener numero de telefono
//    private String getNumerochip(){
//
//        TelephonyManager mtelefono;
//        mtelefono = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        return mtelefono.getLine1Number();
//
//    }
    public void setLocation(Location loc){

        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    mensaje2.setText("Mi direccion es: \n"
                            + DirCalle.getAddressLine(0));
                    y=DirCalle.getAddressLine(0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public class Localizacion implements LocationListener{
        MainActivity mainActivity;
        public MainActivity getMainActivity() {
            return mainActivity;
        }
        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();
            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude() + "\n speed "+loc.getSpeed()+"\n precisión"+loc.getAccuracy();
            x= loc.getLatitude()+"%"+loc.getLongitude()+"%"+loc.getSpeed()+"%"+loc.getAccuracy()+"%";
            mensaje1.setText(Text);
           // this.mainActivity.setLocation(loc);
            msj="%"+x+numerotel;
            dato.setText(msj);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            mensaje1.setText("GPS Activado");
        }

        @Override
        public void onProviderDisabled(String s) {
            mensaje1.setText("GPS Desactivado");
        }
    }


}
