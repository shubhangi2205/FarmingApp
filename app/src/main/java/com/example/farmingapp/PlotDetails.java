package com.example.farmingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.farmingapp.models.CropModel;
import com.example.farmingapp.models.PlotModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class PlotDetails extends AppCompatActivity {
    FirebaseFirestore db;
    final String TAG = "firestore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        PlotModel plotModel = new PlotModel(
                "Brahampuri",
                1500,
                "Meerut"

        );
        savePlotDetails(plotModel);
        getPlotDetails(plotModel);
    }

    void savePlotDetails(PlotModel plotModel){
        Map<String, Object> plotDetailMap = plotModel.mapAllTheData();
        db.collection("farmer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String farmerDocId = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String phoneNum = (String) document.get("Phone Number");
                                if(Objects.equals(phoneNum, "123568898")) {
                                    farmerDocId = document.getId();
                                    break;
                                }
                            }
                            db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("plot_details")
                                    .add(plotDetailMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            //Log.d(TAG, "Data Saved at crop_details successfully");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {

                                            Log.d(TAG, "Failed to Save data at plot_details");


                                        }
                                    });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    void getPlotDetails(PlotModel plotModel){
        Map<String, Object> plotDetailMap = plotModel.mapAllTheData();
        db.collection("farmer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String farmerDocId = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String phoneNum = (String) document.get("Phone Number");
                                if (Objects.equals(phoneNum, "123568898")) {
                                    farmerDocId = document.getId();
                                    break;
                                }
                            }
                            //db.collection("crop_details")
                            db.collection("farmer")
                                    .document(farmerDocId)
                                    .collection("plot_details")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            ArrayList<String> plotNameList = new ArrayList<>();
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " => " + document.get("plotName"));
                                                    //String listOfCrops = (String) document.get("cropName");
                                                    //Log.d(TAG, "Data at crop_details retrieved successfully");

                                                    plotNameList.add((String) document.get("plotName"));
                                                    //Log.d("List of crops", String.valueOf(cropNameList));


                                                }
                                                Log.d(
                                                        TAG, String.valueOf(plotNameList)
                                                );
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    }

                });
    };

    void updatePlotDetails(){

    }
}