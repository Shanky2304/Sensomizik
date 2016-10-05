package alrightsolutions.example.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by JohnConnor on 27-Sep-16.
 */

public class Music extends RealmObject {
    @PrimaryKey
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
