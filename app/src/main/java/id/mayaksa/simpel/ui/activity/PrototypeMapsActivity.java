package id.mayaksa.simpel.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.databinding.ActivityMapsPrototypeBinding;

public class PrototypeMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsPrototypeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsPrototypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        binding.focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(-6.490910,106.856221))
                                .title("Kalimantan Ancur")
                                .snippet("-6.490910, 106.856221"));

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
                        marker.showInfoWindow();

                    }
                });
            }
        });

        binding.focus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(-6.489216, 106.856346))
                                .title("Gatau ini dimana")
                                .snippet("-6.489216, 106.856346"));

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
                        marker.showInfoWindow();

                    }
                });
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        try {
            // Inisialisasi KmlLayer
            // Parameters: (GoogleMap, resourceId, Context)
            KmlLayer layer = new KmlLayer(googleMap, R.raw.vak_new, getApplicationContext());

            // Menampilkan data KMZ ke peta
            layer.addLayerToMap();

            // Opsional: Memberi log jika berhasil
            System.out.println("KMZ Layer berhasil dimuat.");

        } catch (IOException e) {
            // Terjadi kesalahan saat membaca file
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // Terjadi kesalahan saat parsing XML di dalam KMZ
            e.printStackTrace();
        }

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-6.489797, 106.854330))
                .title("Sumatera Tumbang")
                .snippet("-6.489797, 106.854330")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-6.490910,106.856221))
                .title("Kalimantan Ancur")
                .snippet("-6.490910, 106.856221"));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-6.489216, 106.856346))
                .title("Gatau ini dimana")
                .snippet("-6.489216, 106.856346"));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-6.489797, 106.854330), 15));

        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

//        mMap = googleMap;
//
//        List<LokasiWisata> daftarLokasi = new ArrayList<>();
//        daftarLokasi.add(new LokasiWisata("Monas", -6.175392, 106.827153));
//        daftarLokasi.add(new LokasiWisata("Bundaran HI", -6.194968, 106.823036));
//        daftarLokasi.add(new LokasiWisata("Gelora Bung Karno", -6.218335, 106.802216));
//        // Variable untuk menyimpan marker terakhir (agar bisa kita buka infonya)
//        Marker markerTerakhir = null;
//
//        for (LokasiWisata lokasi : daftarLokasi) {
//            LatLng posisi = new LatLng(lokasi.lat, lokasi.lng);
//
//            // Simpan hasil addMarker ke variabel
//            Marker marker = mMap.addMarker(new MarkerOptions()
//                    .position(posisi)
//                    .title(lokasi.nama)
//                    .snippet("Ketuk untuk detail")); // Tambahan snippet
//
//            markerTerakhir = marker; // Update marker terakhir
//        }
//
//        // Jika ada data, gerakkan kamera ke lokasi terakhir DAN buka infonya
//        if (markerTerakhir != null) {
//            // Gerakkan kamera
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerTerakhir.getPosition(), 15));
//
//            // Paksa info window terbuka otomatis
//            markerTerakhir.showInfoWindow();
//        }

    }

    // Class sederhana untuk menyimpan data lokasi
//    static class LokasiWisata {
//        String nama;
//        double lat;
//        double lng;
//
//        public LokasiWisata(String nama, double lat, double lng) {
//            this.nama = nama;
//            this.lat = lat;
//            this.lng = lng;
//        }
//    }
}