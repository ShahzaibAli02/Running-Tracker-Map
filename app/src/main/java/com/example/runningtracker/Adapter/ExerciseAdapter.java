package com.example.runningtracker.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.runningtracker.Model.Exercise;
import com.example.runningtracker.R;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {


     /*

    this is ExerciseAdapter class which take
    list of exercise objects
    and populate a list for recyclerview


     */

    private List<Exercise>listData; //our list  containing exercise data
    Context context; //context of calling class
    public ExerciseAdapter(List<Exercise> listData, Context context)
    {
        // constructor
        this.listData = listData;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {

        // inflate lyt_exercise for list
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lyt_exercise,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {

        // get data from list
        Exercise exercise=listData.get(i);

        // set data on views
        holder.txtDate.setText(exercise.getDate());
        holder.txtDistance.setText(exercise.getDistance());
        holder.txtExperience.setText(exercise.getExperience());
        holder.txtNote.setText(exercise.getNote());
        holder.txtTime.setText(exercise.getTime());


    }

    @Override // get item count
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtDate,txtTime,txtDistance,txtNote,txtExperience;
        public ViewHolder(View itemView) {
            super(itemView);

            // initializng views
            txtDate=(TextView)itemView.findViewById(R.id.txtDate);
            txtTime=(TextView)itemView.findViewById(R.id.txtTime);
            txtDistance=(TextView)itemView.findViewById(R.id.txtDistanceTraveled);
            txtNote=(TextView)itemView.findViewById(R.id.txtnote);
            txtExperience=(TextView)itemView.findViewById(R.id.txtExperience);



        }
    }

}
