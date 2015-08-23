package com.example.android.spotifystreamer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.graphics.Palette;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.spotifystreamer.Utils.MiscUtils;
import com.example.android.spotifystreamer.Utils.MusicController;
import com.example.android.spotifystreamer.Utils.UIUtils;
import com.github.florent37.glidepalette.GlidePalette;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Track;
import android.widget.MediaController.MediaPlayerControl;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrackActivityFragment extends Fragment implements MediaPlayerControl {

    private final SpotifyApi spotifyApi = new SpotifyApi();
    private final SpotifyService spotify = spotifyApi.getService();
    private List<Track> songList = new ArrayList<Track>();
    private String trackListCacheJson;
    Gson gson = new Gson();
    private MusicService musicSrv;
    private Intent playIntent;
    //  flag to keep track of whether the Activity class is bound to the Service class or not
    private boolean musicBound=false;
    private MusicController controller;
    private int trackPos;
    private String artistName = "";
    private boolean paused=false, playbackPaused=false;

    public TrackActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_track, container, false);
        Intent intent = getActivity().getIntent();
        if (savedInstanceState == null && intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            trackPos = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
            artistName = intent.getStringExtra("ArtistName");
            SharedPreferences sharedPreferences = MiscUtils.getFromSharePref(getActivity());
            trackListCacheJson = sharedPreferences.getString("trackListCache", null);
            if (trackListCacheJson.equals(null) ) {
                Log.e("createView", "No tracklistJson");
            }
            songList = gson.fromJson(trackListCacheJson, new TypeToken<List<Track>>() {}.getType());
        } else { // creating from a previously saved instance
            trackPos = savedInstanceState.getInt("trackPos");
            artistName = savedInstanceState.getString("artistName");
            trackListCacheJson = savedInstanceState.getString("songList");
            songList = gson.fromJson(trackListCacheJson, new TypeToken<List<Track>>() {}.getType());
        }
        setRetainInstance(true);
        return rootView;
    }
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated
    // At this point, it is safe to search for activity View objects by their ID, for example.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("songPrepared"));
        if(playIntent==null){
            Log.v("playIntent", "was null");
            playIntent = new Intent (getActivity().getApplicationContext(), MusicService.class);
            getActivity().getApplicationContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().getApplicationContext().startService(playIntent);
        }
        Track track = songList.get(trackPos);
        setUp(track);

    }
    @Override
    public void onStart() {
        super.onStart();
    }
    // Fires when a configuration change occurs and fragment needs to save state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("songList", trackListCacheJson);
        outState.putInt("trackPos", trackPos);
        outState.putString("artistName", artistName);
    }
    @Override
    public void onPause(){
        paused=true;
        if (controller != null) {
            // doesnt resolve window leaked error yet
            controller.hide();
            controller = null;
        }
        super.onPause();
    }
    @Override
    public void onResume(){ //ensure that the controller displays when the user returns to the app.
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }
    @Override
    public void onDestroyView() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("receiver", "Got message: ");
            if (musicSrv != null) {
                uiUpdate();
                setController();
//                controller.show(0);
            }
        }
    };
    //connect to the service
    /*
    The callback methods will inform the class when the Activity instance has successfully connected to the Service instance.
    When that happens, we get a reference to the Service instance so that the Activity can interact with it.
    */
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            musicSrv.setSong(trackPos);
            musicSrv.playSong();
            musicBound = true;
            setController();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void setUp(Track track) {
            AlbumSimple album = track.album;
            String trackName = track.name;
            String albumArtWorkUrl = album.images.get(0).url;
            String albumName = album.name;

            final TextView trackNameTextView = (TextView)  getActivity().findViewById(R.id.track_textview);
            final TextView albumNameTextView = (TextView)  getActivity().findViewById(R.id.album_textview);
            final TextView artistNameTextView = (TextView)  getActivity().findViewById(R.id.artist_textview);
            final ImageView albumArtView = (ImageView) getActivity().findViewById(R.id.albumart_imageView);

            trackNameTextView.setText(trackName);
            albumNameTextView.setText(albumName);
            artistNameTextView.setText(artistName);
            Glide
                    .with(getActivity())
                    .load(albumArtWorkUrl)
                    .listener(GlidePalette.with(albumArtWorkUrl).intoCallBack(
                            new GlidePalette.CallBack() {

                                @Override
                                public void onPaletteLoaded(Palette palette) {
                                    //specific task
                                    Palette.Swatch swatch = UIUtils.getDominantSwatch(palette);
                                    final int backgroundColor = swatch.getRgb();
                                    final int textColor = swatch.getBodyTextColor();
                                    getActivity().getWindow().getDecorView().setBackgroundColor(backgroundColor);
                                    trackNameTextView.setTextColor(textColor);
                                    albumNameTextView.setTextColor(textColor);
                                    artistNameTextView.setTextColor(textColor);
                                }
                            }))
                    .into(albumArtView);
    }
    private void setController(){
        //set the controller up
        Log.d("controller", "SETTING");
            controller = new MusicController(getActivity());
            controller.setPrevNextListeners(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playNext();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playPrev();
                }
            });
            controller.setMediaPlayer(this);
            controller.setAnchorView(getView().findViewById(R.id.albumart_imageView));
            controller.setEnabled(true);

        getView().findViewById(R.id.albumart_imageView).post(new Runnable() {
            public void run() {
                if (musicSrv != null && musicSrv.getPrepared()) { // on config changes
                    controller.show(0);
                }
            }
        });
    }
    @Override
    public void start() {
        Log.v("mediaPlayer", "start");
        musicSrv.go();
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicSrv.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(isPlaying())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(isPlaying())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
    private void playNext() {
        musicSrv.playNext();
        if(playbackPaused){
//            setController();
            playbackPaused=false;
        }
    }

    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
//            setController();
            playbackPaused=false;
        }
    }
    private void uiUpdate(){
        trackPos = musicSrv.getSongPos();
        setUp(songList.get(trackPos));
    }
}
