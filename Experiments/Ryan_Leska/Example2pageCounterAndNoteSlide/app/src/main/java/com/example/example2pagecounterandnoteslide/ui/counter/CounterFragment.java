package com.example.example2pagecounterandnoteslide.ui.counter;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.example2pagecounterandnoteslide.R;
import com.example.example2pagecounterandnoteslide.databinding.FragmentCounterBinding;
import com.example.example2pagecounterandnoteslide.ui.notes.NotesFragment;

import java.net.URISyntaxException;

public class CounterFragment extends Fragment {

    private int count = 0;
    TextView counterText;
    Button counterButton;
    private FragmentCounterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CounterViewModel counterViewModel =
                new ViewModelProvider(this).get(CounterViewModel.class);

//        count = savedInstanceState.getInt("testing");

        binding = FragmentCounterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        try {
            Bundle extras = getIntent("intentTest").getExtras();
            count = extras.getInt("test");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        counterButton = (Button) root.findViewById(R.id.butCount);
        counterText = (TextView) root.findViewById(R.id.txtcount);
        counterText.setText(Integer.toString(count));
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                counterText.setText(Integer.toString(count));
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


        binding = null;
    }
}