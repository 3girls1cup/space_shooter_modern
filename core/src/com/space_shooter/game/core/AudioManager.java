package com.space_shooter.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    private static AudioManager instance;
    private float volumeSoundtrack = 0.5f;
    private float volumeSoundEffects = 0.5f;
    private Music soundTrackMusic;

    private AudioManager() {

        soundTrackMusic = Gdx.audio.newMusic(Gdx.files.internal(GameAssets.SOUNDTRACK));

        soundTrackMusic.setLooping(true);
        soundTrackMusic.setVolume(volumeSoundtrack);
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playBackgroundMusic() {
        if (!soundTrackMusic.isPlaying()) {
            soundTrackMusic.play();
        }
    }

    public void stopBackgroundMusic() {
        soundTrackMusic.stop();
    }

    public void dispose() {
        soundTrackMusic.dispose();
    }

    public Sound loadSound(String filePath) {
        return GameAssets.getInstance().getSoundInstance(filePath);
    }

    public void playSoundEffect(String filePath) {
        loadSound(filePath).play(volumeSoundEffects);
    }

    public void setVolumeSoundtrack(float volume) {
        volumeSoundtrack = volume;
        soundTrackMusic.setVolume(volumeSoundtrack);
    }

    public void setVolumeSoundEffects(float volume) {
        volumeSoundEffects = volume;
    }
}
