package com.example.logintesting.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.logintesting.R;
import com.example.logintesting.databinding.FragmentDashboardBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.resultList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(new VolleyLoginAdapter()); // RandomNumListAdapter(1234)

//        String url = "https://93ee1c56-fb69-43bf-81dc-d02ab97f66ff.mock.pstmn.io/APItest";
//        RequestQueue queue = Volley.newRequestQueue(container.getContext());
//        StringRequest request = new StringRequest(url, new Response.Listener<String>(){
//            @Override
//            public void onResponse(String response){
//                try {
//                    System.out.println(response);
//                    JSONObject resObj = new JSONObject(response);
//                    String title = resObj.getString("text");
//
//                    hw.setText(title);
//                }
//                catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        },new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                volleyError.printStackTrace();
////                System.out.println(String.valueOf(volleyError));
//
//            }
//        });
//        hw.setText("sending request");
//        queue.add(request);


//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}