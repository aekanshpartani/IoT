package com.example.sinha.iot;

import com.google.firebase.firestore.DocumentReference;

public interface AsyncResponse {
    void processFinish(String output,DocumentReference dr);
}
