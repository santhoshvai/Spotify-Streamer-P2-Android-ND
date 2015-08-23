package com.example.android.spotifystreamer.Utils;

import android.content.Context;
import android.widget.MediaController;

public class MusicController extends MediaController {

    public MusicController(Context c){
        super(c, false); // ( ,false) doesnt show rewind and forward
    }

//    public void hide(){} // cannot hide now

}
