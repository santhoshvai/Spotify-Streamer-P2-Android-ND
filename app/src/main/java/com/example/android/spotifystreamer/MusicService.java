package com.example.android.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.MediaController;

import com.example.android.spotifystreamer.Utils.MusicController;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //media player
    private MediaPlayer player;
    private boolean prepared;
    //song list
    private ArrayList<Track> songs;
    //current position
    private int songPosn;
    private final IBinder musicBind = new MusicBinder();

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //  Return the communication channel to the service.
        return musicBind;
    }
    // release resources when the Service instance is unbound
    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }
    @Override
    public void onCreate(){
        //create the service
        super.onCreate();
        //initialize position
        songPosn=0;
        //create player
        player = new MediaPlayer();
        initMusicPlayer();
    }
    //set player properties
    public void initMusicPlayer(){
        // The wake lock will let playback continue when the device becomes idle
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }
    // to pass the list of songs from the TrackActivity
    public void setList(List<Track> theSongs){
        songs = new ArrayList<Track>(theSongs);
    }
    // call when the user selects a song from the list
    public void setSong(int songIndex){
        songPosn=songIndex;
    }
    // set the app up to play a track
    public void playSong(){
        player.reset(); // when the user is playing subsequent songs
        // get track
        Track playSong = songs.get(songPosn);
        try{
            player.setDataSource(playSong.preview_url);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    // need to access this from the Activity class.
    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
        sendMessage();
        prepared = true;
    }
    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }
    public void seek(int posn){
        player.seekTo(posn);
    }
    public int getSongPos(){
        return songPosn;
    }
    public boolean getPrepared(){
        return prepared;
    }
    public void go(){
        player.start();
    }
    public void playPrev(){
        prepared = false;
        songPosn--;
        if(songPosn < 0) songPosn=songs.size()-1;
        playSong();
    }
    //skip to next
    public void playNext(){
        prepared = false;
        songPosn++;
        if(songPosn >= songs.size()) songPosn=0;
        playSong();
    }
    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("songPrepared");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
