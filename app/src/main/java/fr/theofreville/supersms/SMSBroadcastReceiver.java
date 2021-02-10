package fr.theofreville.supersms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    boolean dataAdded = MainActivity.databaseHelper.addData(phoneNumber, message);
                    if(dataAdded){
                        Toast.makeText(context, "Nouveau message recu.", Toast.LENGTH_LONG).show();
                        MainActivity.populateListView();
                    } else {
                        Toast.makeText(context, "Erreur lors de la reception du nouveau message", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception SMSBroadcastReceiver" + e);
        }
    }
}