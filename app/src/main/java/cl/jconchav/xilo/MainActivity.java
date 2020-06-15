package cl.jconchav.xilo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView pink;
    private ImageView black;

    //size
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    //position
    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;

    //Score
    private int score = 0;

    //Initialize class
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer sound;

    //status check
    private boolean action_flag = false;
    private boolean start_flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sound = new SoundPlayer(this);

        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        box = findViewById(R.id.box);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);
        black = findViewById(R.id.black);

        //get Screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //move to out screen
        orange.setX(-80);
        orange.setY(-80);
        pink.setX(-80);
        pink.setY(-80);
        black.setX(-80);
        black.setY(-80);

        scoreLabel.setText("Score : 0");

    }

    public void changePos(){

        hitCheck();

        // Orange
        orangeX -= 12;
        if(orangeX < 0){
            orangeX = screenWidth +20;
            orangeY = (int)Math.floor(Math.random() * (frameHeight - orange.getHeight()));

        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //Black
        blackX -=16;
        if(blackX < 0){
            blackX = screenWidth +10;
            blackY = (int)Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        //Pink
        pinkX -=20;
        if(pinkX < 0){
            pinkX = screenWidth + 5000;
            pinkY = (int)Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        //move the box
        if(action_flag == true){
            //touching
            boxY -= 20;
        }else{
            //releasing
            boxY += 20;
        }

        //check box position
        if(boxY <0) boxY = 0;

        if(boxY > frameHeight - boxSize)boxY = frameHeight - boxSize;
        box.setY(boxY);

        scoreLabel.setText("Score : " + score);
    }

    public void hitCheck(){
        // if the center of the ball is in the box, it count as a hit

        //Orange
        int orangeCenterX = orangeX + orange.getWidth()/2;
        int orangeCenterY = orangeY + orange.getHeight()/2;

        //0 <= orangeCenterX <= boxWidth
        //boxY <= orangeCenterY <= boxY  boxHeigth

        if(0 <= orangeCenterX && orangeCenterX <= boxSize &&
                boxY <= orangeCenterY && orangeCenterY <= boxY+boxSize){

            score += 10;
            orangeX =-10;
            sound.playHitSound();
        }

        //Pink
        int pinkCenterX = pinkX + pink.getWidth()/2;
        int pinkCenterY = pinkY + pink.getHeight()/2;
        if(0 <= pinkCenterX && pinkCenterX <= boxSize &&
                boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize){

            score += 30;
            pinkX =-10;
            sound.playHitSound();
        }

        //Black
        int blackCenterX = blackX + black.getWidth()/2;
        int blackCenterY = blackY + black.getHeight()/2;
        if(0 <= blackCenterX && blackCenterX <= boxSize &&
                boxY <= blackCenterY && blackCenterY <= boxY + boxSize){

            score -= 50;
            blackX =-10;
            sound.playOverSound();
            //Stop Timer!
           // timer.cancel();
           // timer = null;
            //Show result
        }

    }

    public boolean onTouchEvent(MotionEvent me){

        //set audio
        SoundPlayer sound = new SoundPlayer(getApplicationContext());
        sound.random.nextInt();

        if(start_flag == false){
            start_flag =true;

            //Why get height and box height here?
            //because the UI has not been set on he screen in OnCreate

            FrameLayout frame = findViewById(R.id.frame);
            frameHeight = frame.getHeight();
            boxY = (int)box.getY();

            //the box is a square.(height and width are the same)
            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            },0,20);

        }else{
            if(me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flag = true;
            }else if(me.getAction() == MotionEvent.ACTION_UP){
                action_flag = false;
            }
        }

        return true;
    }

}
