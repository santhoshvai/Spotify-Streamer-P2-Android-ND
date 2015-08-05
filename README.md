# Spotify-Streamer-P2-Android-ND

**Evaluation**

Stage 2 is evaluated against [this rubric.](https://docs.google.com/document/d/1WAcuzWociiTXFBcV3Rx216ALL6TYUXwtfXOZzcd0TSU/pub?embedded=true)

## User Experience Design
Tablet Interaction Flow
(using a Master Detail Flow)
  1. Search for an artist. ![Spotify_Streamer_stage2_1.png](https://lh6.googleusercontent.com/KQOB_6B6k8p3DKnDZpcJfHg0DDFlGy0dxFCzpXP57a5uQFA3rGzf3xnxQlNKejF7_9NSxdVpFAQkvIQ_sZkuUPJmsiZuEiQcdevjKI5pz1D2rWK505nBiwTQJMN94ZVA8PyUAaY)
  2. Fetch and display the top tracks for the selected artist.![Spotify_Streamer_stage2_2.png](https://lh6.googleusercontent.com/1_VfckqlgdIEJozjXFY6PWt-Qhnh7d_A4LCbxuRUsSIH-yezq9DVNA1TssTZNgMKynwXhkYUIQ9Yf-VJcvV26XsVXP2qbW8dkx9pZSrFDE4A0ODuqU1UE4j3KrmmXh-EJHoWM-4)
  3. Play the selected track preview.
  
![Spotify_Streamer_stage2_3.png](https://lh5.googleusercontent.com/5X4E2Uprvd2ul-RvYlgiOyS6pwBKHXEnRn7CZ8QTPmAXyYefeSYlXj5z4dujGgO7mp1qFlNQo6_lQt1ASIArXCylXRFq3P2dP0mMDflSWkTPi5ksv-_vZpde7kRgJITsjDPpjUY)![Spotify_Streamer_stage2_portrait.png](https://lh3.googleusercontent.com/Em_jjruTP-gf2jDI-EPJ5TI6nWm7f15d5bR6dnvQJXdCqhyCcmL4tEtQ6V3kcIF6H8XQvpBFGnhckVLG3veVNGa1hxo6_0HT2lwzihbCCFQD2IxAZs-7XNrszuuGhWC-V2gCNYw)

## Build a Track Player and Optimize for Tablet 

**Task 1: Build a Simple Player UI**

Use the layout fundamental skills you learned in Developing Android Apps to build a simple track player. You will launch this view via Intent when a user selects a specific track from the Artist Top Tracks view.

![P1_Spotify_screen3_play_track_preview_300x533.png](https://lh6.googleusercontent.com/2LKwh1Z_kNrc3Jb86o6tmx3i9xzm-_UNZ-sM7SIk4XUKbZy00EjlUXj0O7OWG_FHEq-RYiC3m4P0iUZsHevMY1UfjYXsRUbv2u7mFOv_egUi035iJKjdGDL6DbtU-nuQGJKi88w)

**This player UI should display the following information:**
  * artist name
  * album name
  * album artwork
  * track name
  * track duration

**This player UI should display the following playback controls:**
  *   Play/Pause Button - (Displays “Pause” when a track is currently playing & displays “Play” when a playback is stopped)
  *   Next Track - advances forward to the next track in the top track list.
  *   Previous Track - advances to backward to the previous track in the top track list.
  *   (Scrub Bar) - This shows the current playback position in time and is scrubbable.
  
**Icons**

To get the icons for the playback controls, you can use the ones that are built-in on Android as drawables. Check out [http://androiddrawables.com/](http://www.google.com/url?q=http%3A%2F%2Fandroiddrawables.com%2F&sa=D&sntz=1&usg=AFQjCNG6rIlxrhrgVUVFWC9KYwCCc34syw) to see all the built-in drawables. The ones used in the mockup are found in the Other section of the site:

- ic_media_play
- ic_media_pause
- ic_media_next
- ic_media_previous

Referencing built-in Android drawables involve using the syntax `@android:drawable/{drawable_id}`. For example, `@android:drawable/ic_media_play` refers to the play button drawable.

### Task 2: Implement playback for a selected track

You will use Android’s [MediaPlayer API](http://developer.android.com/guide/topics/media/mediaplayer.html) to stream the track preview of a currently selected track.

Please consult the [guide on using MediaPlayer](http://developer.android.com/guide/topics/media/mediaplayer.html#mediaplayer) on developer.android.com

- Remember that you will be streaming tracks, not playing local audio files.
- Don’t forget to add the [necessary permissions](http://developer.android.com/guide/topics/media/mediaplayer.html#manifest) to your app manifest.

### Task 3: Optimize the entire end to end experience for a tablet

Migrate the existing UI flow to use a Master-Detail Structure for tablet. If you haven’t done so already, you will want to implementthree Fragments for your tablet UI: one for artist search, one for top track results, and another for Playback.

- If you need a review of how to build for tablet, please refer back to [Lesson 5 of Developing Android Apps, where the instructors discuss a Master-Detail layout.](https://www.google.com/url?q=https%3A%2F%2Fwww.udacity.com%2Fcourse%2Fviewer%23!%2Fc-ud853%2Fl-1623168625%2Fe-1603010052%2Fm-1673948920&sa=D&sntz=1&usg=AFQjCNFe2T2WKgD1Td1O_7kUBsA6D3MgKg)
- To display the Now Playing screen in a dialog on the tablet and in a normal activity on the phone, you can use a DialogFragment, which can act as a normal fragment (for the phone) or show in a dialog (for the tablet). See the [documentation on Dialogs](http://developer.android.com/guide/topics/ui/dialogs.html) for more information—the section called “Showing a Dialog Fullscreen or as an Embedded Fragment” is particularly helpful.

### Project Submission (Spotify Streamer App, Stage 2)

In order to submit P2: Spotify Streamer App, Stage 2, please [use this link](https://www.google.com/url?q=https%3A%2F%2Freview.udacity.com%2F%23!%2Fprojects%2F60&sa=D&sntz=1&usg=AFQjCNEsXInMOSl3VGQCPdtzqwf-DMpytQ) and follow instructions.

If you have any problems submitting your project, please email us at [android-project@udacity.com](mailto:android-project@udacity.com).
