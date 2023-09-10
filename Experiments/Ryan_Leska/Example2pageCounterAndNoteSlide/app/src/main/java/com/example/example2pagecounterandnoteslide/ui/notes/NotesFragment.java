package com.example.example2pagecounterandnoteslide.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.example2pagecounterandnoteslide.R;
import com.example.example2pagecounterandnoteslide.databinding.FragmentNotesBinding;

import org.w3c.dom.Text;

public class NotesFragment extends Fragment {

    TextView countText;
    int count;
    private FragmentNotesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        count = 0;
        NotesViewModel notesViewModel =
                new ViewModelProvider(this).get(NotesViewModel.class);

        binding = FragmentNotesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        countText = (TextView) root.findViewById(R.id.noteText);

        countText.setText(Integer.toString(count));



//        final TextView textView = binding.text;
//        notesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    void setCount(int i){
        count = i;
    }
    void incrementCount(){
        count++;
    }
    int getCount(){
        return count;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}