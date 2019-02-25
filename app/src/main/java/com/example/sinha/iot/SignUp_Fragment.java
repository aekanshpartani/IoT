package com.example.sinha.iot;

import android.app.LauncherActivity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.sinha.iot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp_Fragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText fullName, emailId, vehicleNo,
            password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public SignUp_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_signup, container, false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        fullName = (EditText) view.findViewById(R.id.fullName);
        emailId = (EditText) view.findViewById(R.id.userEmailId);
        vehicleNo = (EditText) view.findViewById(R.id.vehicleno);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn);
        login = (TextView) view.findViewById(R.id.already_user);

    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                if(checkValidation())
                {
                    firebaseAuth.createUserWithEmailAndPassword(emailId.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {

                                            new CustomToast().Show_Toast(getActivity(), view,
                                                    "Email Sent.");


                                            Map<String, Object> user = new HashMap<>();
                                            user.put("Name", fullName.getText().toString());
                                            user.put("Enail", emailId.getText().toString());
                                            user.put("VehicleNo", vehicleNo.getText().toString());
                                            user.put("Date Created", new Timestamp(new Date().getTime()));
                                            user.put("Dues",false);

                                             db.collection("Users")
                                                .add(user)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    }
                                                })
                                                     .addOnFailureListener(new OnFailureListener() {
                                                         @Override
                                                         public void onFailure(@NonNull Exception e) {
                                                             Log.w("Error", "Error adding document", e);
                                                         }
                                                     });

                                        }
                                        else
                                        {

                                            new CustomToast().Show_Toast(getActivity(), view,
                                                    "Email Not Sent.");
                                        }
                                    }
                                });
                            }
                            else if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {

                                Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }
                break;

            case R.id.already_user:


                // Replace login fragment
                new Laucher().replaceLoginFragment();
                break;
        }

    }

    // Check Validation Method
    private boolean checkValidation() {

        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getVehicleNo = vehicleNo.getText().toString();

        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getVehicleNo.equals("") || getVehicleNo.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0) {

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");
            return false;
        }
            // Check if email id valid or not
        else if (!m.find()){
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
            return false;
        }
        else if(getPassword.length() <= 6 && getConfirmPassword.length() <=6 )
        {
            new CustomToast().Show_Toast(getActivity(), view,
                "Password Must Be Atleast of length Six Digits");
            return false;
        }

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword)){
            new CustomToast().Show_Toast(getActivity(), view,
                    "Both password doesn't match.");
        return false;
    }
            // Else do signup or do your stuff
        else
                return true;

    }
}