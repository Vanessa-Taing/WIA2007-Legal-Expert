package com.example.mad_login;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_login.Model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class terminateSession extends AppCompatActivity {

    private Dialog terminateDialog;
    private RadioGroup rgReason;
    private Button btnConfirm;
    private FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminate_session);    //just use the original activity(layout that contain "terminate case" btn),no need to use this
        authProfile = FirebaseAuth.getInstance();
        DatabaseReference reasonsRef = FirebaseDatabase.getInstance().getReference("termination_reasons");
        // Initialize dialog and UI elements for termination
        terminateDialog = new Dialog(this);
        terminateDialog.setContentView(R.layout.activity_terminate_session);

        rgReason = terminateDialog.findViewById(R.id.rgReason);
        btnConfirm = terminateDialog.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleConfirmButtonClick();
            }
        });

        // Show the dialog when the terminate button is clicked (this implement at the onCreate method of activity that have "Terminate Case" Button)
//        findViewById(R.id.btnTerminate).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                terminateDialog.show();
//            }
//        });
    }

    private void handleConfirmButtonClick() {
        // Get selected radio button(s)
        int selectedId = rgReason.getCheckedRadioButtonId();

        if (selectedId != -1) {
            RadioButton radioButton = terminateDialog.findViewById(selectedId);

            // Get the selected reason
            String selectedReason = radioButton.getText().toString();

            // Save the data to Firebase
            saveTerminationReason(selectedReason);

            // Dismiss the dialog
            terminateDialog.dismiss();

        } else {
            // Handle the case where no radio button is selected
            Toast.makeText(this, "Please select a reason", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTerminationReason(String reason) {
        // Save the data to Firebase
        // Replace "user_lawyer_relationships" with your actual Firebase node
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_lawyer_relationships");

        // Replace "user_id" and "lawyer_id" with the actual user and lawyer IDs
        // This is a simplified example, and you should replace it with your actual data structure
        String userId = authProfile.getCurrentUser().getUid();
        String lawyerId = "lawyer_id";

        // Save the termination reason
        databaseReference.child(userId).child(lawyerId).child("termination_reason").setValue(reason);
        Toast.makeText(this, "Your terminate reason has been saved", Toast.LENGTH_SHORT).show();
    }

//    terminate relationship, save reason, terminate chat function
//    private void terminateRelationship(String userId, String lawyerId,String reason) {
//        // Update the 'terminated' flag for the chats between the user and lawyer
//        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("Chats");
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_lawyer_relationships");
//
//        // Save the termination reason
//        databaseReference.child(userId).child(lawyerId).child("termination_reason").setValue(reason);
//        Toast.makeText(this, "Your terminate reason has been saved", Toast.LENGTH_SHORT).show();
//        chatReference.orderByChild("sender_receiver").equalTo(userId + "_" + lawyerId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Chat chat = snapshot.getValue(Chat.class);
//                    if (chat != null) {
//                        chat.setTerminated(true);
//                        snapshot.getRef().setValue(chat);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("MessageActivity", "Error terminating relationship", error.toException());
//            }
//        });
//    }
}
