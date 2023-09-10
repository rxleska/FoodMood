package com.example.mainapplication.ui.lists;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainapplication.R;
import com.example.mainapplication.datainterface.DataClasses.Profile;
import com.example.mainapplication.datainterface.DataClasses.Review;
import com.example.mainapplication.datainterface.MiddleMan;
import com.example.mainapplication.datainterface.volleySimplification.VolleyRespInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * The class ReviewCourseAdapter is used to create a recycler view for the reviews.
 *
 * @author Ryan Leska
 * */
public class ReviewCourseAdapter extends RecyclerView.Adapter<ReviewCourseAdapter.ViewHolder>{

    /**
     * reviewList is a list of type Review that holds the reviews.
     * */
    private List<Review> reviewList;
    /**
     * profile holds the information of the current user.
     */
    private static Profile profile;
    /**
     * context is the context of the activity containing the review list.
     * @see com.example.mainapplication.ui.lists.ReviewList
     * */
    private Context context;
    /**
     * middleMan is the middleman used to communicate with the server and cache.
     * @see com.example.mainapplication.datainterface.MiddleMan
     * */
    private MiddleMan middleMan;



    /**
     * The constructor ReviewCourseAdapter creates a new ReviewCourseAdapter instance.
     * @param reviewList list of reviews to be displayed
     * @param context context of the activity containing the review list
     */
    public ReviewCourseAdapter(List<Review> reviewList, Context context) {
        middleMan = new MiddleMan(context);
        middleMan.getCurrentPublicUserData(new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("error")){
                    Log.e("ERROR", "NO CONNECTION");
                }
                else if(result.equals("false")){
                    Log.e("ERROR", "NOT LOGGED IN");
                }
                else{
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        profile = new Profile(jsonObject);
                        Log.e("SUCCCESS", "APPROVAL");
                        Log.e("UserID", String.valueOf(profile.getId()));
                    } catch (JSONException e) {
                        Log.e("ERROR", "NOT LOGIN");
                    }
                }
            }
        });
        this.reviewList = reviewList;
        this.context = context;
    }


    /**
     * onCreateViewHolder is used to create a view holder for the recycler view.
     * @param parent parent view group
     * @param viewType view type
     * */
    @NonNull
    @Override
    public ReviewCourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new ViewHolder(view);
    }

    /**
     * onBindViewHolder is used to bind the data to our views of recycler view.
     * @param holder view holder
     * @param position position of the item in recycler view
     * */
    @Override
    public void onBindViewHolder(@NonNull ReviewCourseAdapter.ViewHolder holder, int position) {

        // setting data to our views of recycler view.
        Review curr = reviewList.get(position);

        holder.username.setText(curr.getUserName());
        holder.reviewRating.setText(curr.getRating() + " stars");
        holder.reviewBody.setText(curr.getReviewBody());
        if(profile != null){
            if(curr.getAssociatedProfileId() == profile.getId() || profile.getAdmin() == true)
            {
                Log.e("RevID", String.valueOf(curr.getAssociatedProfileId()));
                Log.e("Admin? ", String.valueOf(profile.getAdmin()));
                holder.delete.setVisibility(View.VISIBLE);
            }
            if(profile.getAdmin() == true )
            {
            holder.ban.setVisibility(View.VISIBLE);
            }
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are creating a dialog click listener
                // variable and initializing it.
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            // on below line we are setting a click listener
                            // for our positive button
                            case DialogInterface.BUTTON_POSITIVE:
                                // on below line we are displaying a toast message.
                                middleMan.deleteReview(curr.getId(), new VolleyRespInterface() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Toast.makeText(context, "Review Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            // on below line we are setting click listener
                            // for our negative button.
                            case DialogInterface.BUTTON_NEGATIVE:
                                // on below line we are dismissing our dialog box.
                                dialog.dismiss();

                        }
                    }
                };
                // on below line we are creating a builder variable for our alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // on below line we are setting message for our dialog box.
                builder.setMessage("Would you like to permanently remove this review? (This can't be undone!)")
                        // on below line we are setting positive button
                        // and setting text to it.
                        .setPositiveButton("Yes", dialogClickListener)
                        // on below line we are setting negative button
                        // and setting text to it.
                        .setNegativeButton("No", dialogClickListener)
                        // on below line we are calling
                        // show to display our dialog.
                        .show();

            }
        });
        holder.ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are creating a dialog click listener
                // variable and initializing it.
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            // on below line we are setting a click listener
                            // for our positive button
                            case DialogInterface.BUTTON_POSITIVE:
                                // on below line we are displaying a toast message.
                                middleMan.userBlacklist(curr.getAssociatedProfileId(), new VolleyRespInterface() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Toast.makeText(context, "User has been banned", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            // on below line we are setting click listener
                            // for our negative button.
                            case DialogInterface.BUTTON_NEGATIVE:
                                // on below line we are dismissing our dialog box.
                                dialog.dismiss();

                        }
                    }
                };
                // on below line we are creating a builder variable for our alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // on below line we are setting message for our dialog box.
                builder.setMessage("A banned user will not be able to leave reviews. Please use with caution this cannot be undone. Would you like to ban this user?")
                        // on below line we are setting positive button
                        // and setting text to it.
                        .setPositiveButton("Yes", dialogClickListener)
                        // on below line we are setting negative button
                        // and setting text to it.
                        .setNegativeButton("No", dialogClickListener)
                        // on below line we are calling
                        // show to display our dialog.
                        .show();

            }
        });
    }


    /**
     * getItemCount is used to get the size of the review list.
     * @return size of the review list: returns 0 if the list is null
     * */
    @Override
    public int getItemCount() {
        if(this.reviewList == null){
            return 0;
        }
        return this.reviewList.size();
    }

    /**
     * The class ViewHolder is used to hold the views of the recycler view.
     * */
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username, reviewRating, reviewBody;
        public ImageButton delete, ban;


        public ViewHolder(@NonNull View itemView){
            super(itemView);

            //initializing our views with their ids.
            username = itemView.findViewById(R.id.UserName);
            reviewRating = itemView.findViewById(R.id.Rating);
            reviewBody = itemView.findViewById(R.id.RBody);
            delete = itemView.findViewById(R.id.deleteReview);
            ban = itemView.findViewById(R.id.BanUser);
        }
    }



}
