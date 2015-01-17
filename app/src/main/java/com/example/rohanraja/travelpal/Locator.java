package com.example.rohanraja.travelpal;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import java.util.Random;


public class Locator extends Activity {

    public static String HOST_URL = "10.64.82.255";
    public static String myUSER_NAME = "rohan1020";
    public static String myPASSWORD = "ro991993";

    public DiscussArrayAdapter adapter;
    private ListView lv;
    private String ipsum = "sadasd";
    private EditText editText1;
    private static Random random;
    public XMPP_Listener2 xmp2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);

        xmp2 = new XMPP_Listener2();
        xmp2.execute("");


        random = new Random();

        lv = (ListView) findViewById(R.id.listView1);

        adapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);

        lv.setAdapter(adapter);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText1.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    adapter.add(new OneComment(false, editText1.getText().toString()));

                    //XMPP_Sender xmp = new XMPP_Sender();
                    //xmp.execute(editText1.getText().toString());
                    xmp2.sendMessage(editText1.getText().toString());
                    editText1.setText("");
                    lv.smoothScrollToPosition(adapter.getCount());

                    return true;
                }
                return false;
            }
        });

        addItems();
    }

    private void addItems() {
        adapter.add(new OneComment(true, "Hello bubbles!"));

        for (int i = 0; i < 4; i++) {
            boolean left = getRandomInteger(0, 1) == 0 ? true : false;
            int word = getRandomInteger(1, 10);
            int start = getRandomInteger(1, 40);
            String words = "Random Chat"; //ipsum.getWords(word, start);

            adapter.add(new OneComment(left, words));
        }
    }

    private static int getRandomInteger(int aStart, int aEnd) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        long range = (long) aEnd - (long) aStart + 1;
        long fraction = (long) (range * random.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        return randomNumber;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_locator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class XMPP_Listener2 extends AsyncTask<String, Void, Void>
    {
        String host = "10.64.82.255";
        String port = "5222";
        String service = "XMPP";
        String username = "rohan1020";
        String password = "ro991993";
        XMPPConnection connection;

        public void sendMessage(String chatText)
        {
            Message msg = new Message("admin@rohans-macbook-pro.local", Message.Type.chat);
            msg.setBody(chatText);
            connection.sendPacket(msg);
        }

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
                        final String body = message.getBody();
                        String from = message.getFrom();
                        Log.d("XMPPClientMSG", "[Message] : " + from + ", " + body.toString());

                        Locator.this.runOnUiThread(new Runnable() {
                            public void run() {

                                adapter.add(new OneComment(true, body.toString()));
                                lv.smoothScrollToPosition(adapter.getCount());

                                //adapter.notifyDataSetChanged();
                            }
                        });




                    }
                }, filter);

            }
            catch (Exception e)
            {

            }

        }

    }


}
