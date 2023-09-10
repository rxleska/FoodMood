package com.example.scrollingreviews;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView courseRV = findViewById(R.id.idRVCourse);

        // Here, we have created new array list and added data to it
        ArrayList<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();
        courseModelArrayList.add(new CourseModel("DSA in Java", 4, android.R.drawable.btn_star_big_on));
        courseModelArrayList.add(new CourseModel("Java Course", 3, android.R.drawable.btn_star_big_on));
        courseModelArrayList.add(new CourseModel("C++ Course", 4, android.R.drawable.btn_star_big_on));
        courseModelArrayList.add(new CourseModel("DSA in C++", 4, android.R.drawable.btn_star_big_on));
        courseModelArrayList.add(new CourseModel("Kotlin for Android", 4, android.R.drawable.btn_star_big_on));
        courseModelArrayList.add(new CourseModel("Java for Android", 4, android.R.drawable.btn_star_big_on));
        courseModelArrayList.add(new CourseModel("HTML and CSS", 4, android.R.drawable.btn_star_big_on));

        // we are initializing our adapter class and passing our arraylist to it.
        CourseAdapter courseAdapter = new CourseAdapter(this, courseModelArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);
    }
}
