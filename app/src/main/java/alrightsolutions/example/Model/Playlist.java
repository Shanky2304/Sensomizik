package alrightsolutions.example.Model;

import android.widget.ImageView;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Shanky23 on 2/19/2017.
 */

public class Playlist extends RealmObject {

    @PrimaryKey
    String playlistName;
    int id;
    String musicName;
    String musicArtist;
    String musicURI;
    String albumId;
    String albumArt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistname(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicArtist() {
        return musicArtist;
    }

    public void setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
    }

    public String getMusicURI() {
        return musicURI;
    }

    public void setMusicURI(String musicURI) {
        this.musicURI = musicURI;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }
}

