package com.dygame.myonetooneclientsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity
{
    protected Socket socket = null ;
    protected EditText etText ;
    protected EditText etTextIP ;
    protected Button testButton ;
    protected Button sendButton ;
    protected MyLogListViewAdapter pListViewAdapter ;
    protected ListView pListView ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //find resource
        etText = (EditText)findViewById(R.id.editText) ;
        etTextIP = (EditText)findViewById(R.id.editText2) ;
        testButton = (Button)findViewById(R.id.button) ;
        sendButton = (Button)findViewById(R.id.button2) ;
        pListView = (ListView)findViewById(R.id.listView) ;
        //
        testButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (socket == null)
                {
                    new Thread(new ClientThread()).start();
                }
                else
                {
                    try
                    {
                        String str = "socket close" ;
                        Log.i("TAG", str);
                        pListViewAdapter.getInstance().addLog(str);
                        // Close server socket
                        socket.close();
                    }
                    catch (IOException e)
                    {
                        String str = "socket close = IOException" ;
                        Log.i("TAG", str);
                        pListViewAdapter.getInstance().addLog(str);

                        e.printStackTrace();
                    }
                }
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (socket == null)
                    {
                        String str = "socket = null";
                        Log.i("TAG", str);
                        pListViewAdapter.getInstance().addLog(str);
                        return ;
                    }
                    String str = etText.getText().toString();
                    //log
                    String str0 = "socket send = "+str ;
                    Log.i("TAG", str0);
                    pListViewAdapter.getInstance().addLog(str0);

                    // 創造網路輸出串流
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    // 寫入訊息到串流
                    bw.write(str + "\n");
                    // 立即發送
                    bw.flush();
                    /* another sample
                              // 將信息通過這個對象來發送給Server
                              PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream())),true) ;
                              // 把用戶輸入的內容發送給server
                              String str = etText.getText().toString();
                              pw.println(str);
                              pw.flush();
                              */

                }
                catch (UnknownHostException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        //set adapter
        pListViewAdapter.getInstance().create(this);
        //set listView
        pListView.setAdapter(pListViewAdapter.getInstance());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ClientThread implements Runnable
    {
        static final int SERVERPORT = 5000;
        static final String SERVER_IP = "10.0.2.2";//模擬器訪問10.0.2.2相當於pc上的瀏覽器訪問127.0.0.1或者localhost
        //"10.15.21.57" ;//指定Server的IP地址，此地址为局域网地址，如果是使用WIFI上网，则为PC机的WIFI IP地址，在ipconfig查看到的IP地址如下：
        Socket socket1 = null;
        @Override
        public void run()
        {
            try
            {
                //log
                final String str = "socket connect.." ;
                Log.i("TAG", str);
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        pListViewAdapter.getInstance().addLog(str);
                    }
                });

                // 以內定(本機電腦端)IP為Server端
                String strIP = etTextIP.getText().toString();
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                // 應用Server的IP和端口建立Socket對像
                socket = new Socket(serverAddr, SERVERPORT);

                //log
                final String str1 = "socket = "+strIP+":"+SERVERPORT ;
                Log.i("TAG", str1);
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        pListViewAdapter.getInstance().addLog(str1);
                    }
                });

                // 取得網路輸入串流 // 接收服務器信息
                BufferedReader br = new BufferedReader(new InputStreamReader( socket.getInputStream()));
                // 當連線後
                while (socket.isConnected())
                {
                    // 取得網路訊息
                    String tmp = br.readLine();
                    // 如果不是空訊息則顯示新的訊息
                    if(tmp!=null)
                        Log.i("TAG", "socket is Connected..."+tmp);
                }
            }
/*          //another sample,立即連線,可以立即timeout
             Socket client = new Socket();
             InetSocketAddress isa = new InetSocketAddress(SERVER_IP , SERVERPORT);
             try
             {
                client.connect(isa, 10000);
                BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());
                // 送出字串
                out.write("Send From Client ".getBytes());
                out.flush();
                out.close();
                out = null;
             }
*/          catch (UnknownHostException e1)
            {
                String str1 = "socket UnknownHostException..";
                Log.i("TAG", str1);
//                      pListViewAdapter.getInstance().addLog(str1);
                e1.printStackTrace();
            }
            catch (IOException e1)
            {
                String str2 = "socket IOException..";
                Log.i("TAG", str2);
//                      pListViewAdapter.getInstance().addLog(str2);

                e1.printStackTrace();
            }
            finally
            {
                final String str3 = "try-finally..";
                Log.i("TAG", str3);
                //pListViewAdapter.getInstance().addLog(str3);
                if(socket != null)
                {
                    try
                    {
                        String str4 = "socket close";
                        Log.i("TAG", str4);
//                                  pListViewAdapter.getInstance().addLog(str4);
                        socket.close();
                    }
                    catch (IOException e)
                    {
                        Log.i("TAG", "try-finally = IOException");
                        e.printStackTrace();
                    }
                }
                else
                {
                    String str5 = "try-finally = socket is null" ;
                    Log.i("TAG", str5);
//                            pListViewAdapter.getInstance().addLog(str5);
                }
            }
            String str6 = "ClientThread done" ;
            Log.i("TAG", str6);
            pListViewAdapter.getInstance().addLog(str6);
        }
    }
}
