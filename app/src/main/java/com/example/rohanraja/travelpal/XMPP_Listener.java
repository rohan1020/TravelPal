package com.example.rohanraja.travelpal;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

public class XMPP_Listener extends AsyncTask<String, Void, Void>
{
    String host = "10.64.82.255";
    String port = "5222";
    String service = "XMPP";
    String username = "rohan1020";
    String password = "ro991993";
    XMPPConnection connection;

    @Override
    protected Void doInBackground(String... strings) {
        login();
        try {
            //registerUser();
        }
        catch (Exception e){}

        return null;
    }

    public void login()  {



        // Create a connection
        ConnectionConfiguration connConfig =
                new ConnectionConfiguration(Locator.HOST_URL, Integer.parseInt(port));
        connection = new XMPPConnection(connConfig);

        try {
            //connConfig.setSASLAuthenticationEnabled(true);
            connection.connect();
            Log.i("XMPPClient", "[SettingsDialog] Connected to " + connection.getHost());
        } catch (Exception ex) {
            Log.e("XMPPClient", "[SettingsDialog] Failed to connect to " + connection.getHost());
            Log.e("XMPPClient", "[SettingsDialog] " + ex.toString());
            // xmppClient.setConnection(null);
        }
        try {
            connection.login(Locator.myUSER_NAME, Locator.myPASSWORD);
            Log.i("XMPPClient", "Logged in as " + connection.getUser());

            // Set the status to available
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);

            //connection.wait(100000);

//            Message msg = new Message("admin@rohans-macbook-pro.local", Message.Type.chat);
//            msg.setBody("Testing Testing one two three");
//            connection.sendPacket(msg);
        } catch (Exception ex) {
            Log.e("XMPPClient", "[SettingsDialog] Failed to log in as " + Locator.myUSER_NAME);
            //  xmppClient.setConnection(null);
        }



        try
        {
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);

            Log.d("DG", "Listening**************");
            connection.addPacketListener(new PacketListener()
            {
                public void processPacket(Packet packet)
                {
                    Message message = (Message) packet;
                    String body = message.getBody();
                    String from = message.getFrom();


                    //Locator.adapter.add(new OneComment(true, body));


                    Log.d("XMPPClientMSG", "[Message] : " + from + ", " + body);
                }
            }, filter);

        }
        catch (Exception e)
        {

        }

    }

}