package cl.jconchav.xilo;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.Random;

public class SoundPlayer {

    private AudioAttributes audioAttributes;
    final int SOUND_POLL_MAX = 2;

    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;


    //xilo
    public int soundX[] = {
            R.raw.a10,R.raw.a11,R.raw.a12,R.raw.a13,R.raw.a14,R.raw.a14,R.raw.a15,R.raw.a16,R.raw.a17,R.raw.a18,R.raw.a19,R.raw.a20
    };
    Random random = new Random();
    int LowX = 0;
    int HighX = 10;

    public SoundPlayer(Context context){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POLL_MAX)
                    .build();
        }else{
            //SoundPool (int, unt streamType, int scrQuality)
            soundPool = new SoundPool(SOUND_POLL_MAX, AudioManager.STREAM_MUSIC,0);
        }

        int rndm = random.nextInt(HighX-LowX) + LowX;
        hitSound = soundPool.load(context, soundX[rndm],1);

        //hitSound = soundPool.load(context, R.raw.a14,1);
        overSound = soundPool.load(context, R.raw.over,1);
    }

    public void playHitSound(){

        soundPool.play(hitSound,1.0f,1.0f,1,0,1.0f);

    }

    public void playOverSound(){
        soundPool.play(overSound,1.0f,1.0f,1,0,1.0f);
    }
}
