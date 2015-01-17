package com.example.rohanraja.travelpal;

import android.os.AsyncTask;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

public class XMPP_Register extends AsyncTask<String, Void, Void>
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
            registerUser();
        }
        catch (Exception e){}

        return null;
    }

    public void login()  {



        // Create a connection
        ConnectionConfiguration connConfig =
                new ConnectionConfiguration(host, Integer.parseInt(port));
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
            connection.login(username, password);
            Log.i("XMPPClient", "Logged in as " + connection.getUser());

            // Set the status to available
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);

        } catch (Exception ex) {
            Log.e("XMPPClient", "[SettingsDialog] Failed to log in as " + username);
            //  xmppClient.setConnection(null);
        }



    }

    public void registerUser() throws XMPPException {
        org.jivesoftware.smack.AccountManager accountManager = connection.getAccountManager();
        accountManager.createAccount("adminCreated2", "123456");

    }
}