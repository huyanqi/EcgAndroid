package dev.frankie.ecgwave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import dev.frankie.view.EcgView;

public class MainActivity extends AppCompatActivity {

    private List<Integer> datas = new ArrayList<Integer>();
    private List<Integer> data1Datas = new ArrayList<Integer>();

    private Queue<Integer> data0Q = new LinkedList<Integer>();
    private Queue<Integer> data1Q = new LinkedList<Integer>();

    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadDatas();
        simulator();
    }

    /**
     * 模拟心电发送，心电数据是一秒500个包，所以
     */
    private void simulator(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(EcgView.isRunning){
                    if(data0Q.size() > 0){
                        EcgView.addEcgData0(data0Q.poll());
                        EcgView.addEcgData1(data1Q.poll());
                    }
                }
            }
        }, 0, 2);
    }

    private void loadDatas(){
        try{
            String data0 = "";
            InputStream in = getResources().openRawResource(R.raw.ecgdata);
            int length = in.available();
            byte [] buffer = new byte[length];
            in.read(buffer);
            data0 = new String(buffer);
            in.close();
            String[] data0s = data0.split(",");
            for(String str : data0s){
                datas.add(Integer.parseInt(str));
            }

            data0Q.addAll(datas);
            data1Q.addAll(datas);
        }catch (Exception e){}

    }

}
