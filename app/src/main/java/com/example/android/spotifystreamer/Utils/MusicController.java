package com.example.android.spotifystreamer.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.MediaController;

public class MusicController extends MediaController {

    public MusicController(Context c){
        super(c, false); // ( ,false) doesnt show rewind and forward
    }
//    @Override
    public void hide(){} // cannot hide now
// finish the activity on back press to avoid two back presses
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            ((Activity) getContext()).finish();

        return super.dispatchKeyEvent(event);
    }

}
