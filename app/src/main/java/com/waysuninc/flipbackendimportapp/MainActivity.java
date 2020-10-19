package com.waysuninc.flipbackendimportapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.waysuninc.flipbackendapi.api.PokeApi;
import com.waysuninc.flipbackendapi.entities.User;
import com.waysuninc.flipbackendapi.repo.FlipRepo;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "MainActivity:";
    final MutableLiveData<User> user = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tvName = findViewById(R.id.tvName);
        final TextView tvOrder = findViewById(R.id.tvOrder);

        FlipRepo flipRepo = new FlipRepo(new PokeApi());

        flipRepo.getUser("pikachu", new Function1<User, Unit>() {
            @Override
            public Unit invoke(User result) {
                user.postValue(result);
                return null;
            }
        });

        user.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d(LOG_TAG, "user = " + user.getName() + " " + user.getOrder());

                tvName.setText(user.getName());
                tvOrder.setText("" + user.getOrder());
            }
        });
    }

}



class MyContinuation<T> implements Continuation<T> {

    private MutableLiveData<T> value;

    MyContinuation(MutableLiveData<T> value) {
        this.value = value;
    }

    @NotNull
    @Override
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }

    @Override
    public void resumeWith(@NotNull Object o) {
        value.postValue((T) o);
    }

}