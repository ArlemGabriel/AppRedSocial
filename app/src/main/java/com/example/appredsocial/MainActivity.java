package com.example.appredsocial;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.synnapps.carouselview.CarouselView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("");


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PerfilFragment()).commit();
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        changeFrag(new PostsFragment());
                        break;
                    case R.id.nav_profile:
                        changeFrag(new PerfilFragment());
                        break;
                    case R.id.nav_friends:
                        changeFrag(new AmigosFragment());
                        break;
                    case R.id.nav_search:
                        changeFrag(new BuscarFragment());
                        break;
                    case R.id.nav_notifications:
                        changeFrag(new NotificacionesFragment());
                        break;
                }

                return true;
            }
        });
    }

    public void changeFrag(Fragment frag){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
    }

    //------- Agregar menu de opciones --------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logOut: {
                cerrarSesion();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cerrarSesion() {
        FirebaseAuth.getInstance()
                .signOut();
                finish();



    }

}
