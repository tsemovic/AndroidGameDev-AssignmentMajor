package com.semtb001.individual.assignement.tools;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Assets {
    public AssetManager manager = new AssetManager();

    public static final AssetDescriptor<Music> music =
            new AssetDescriptor<Music>("audio/music/Airship Serenity.mp3", Music.class);

    public static final AssetDescriptor<Sound> jump =
            new AssetDescriptor<Sound>("audio/sounds/jump.ogg", Sound.class);

    public static final AssetDescriptor<Sound> slide =
            new AssetDescriptor<Sound>("audio/sounds/slide.ogg", Sound.class);

    public static final AssetDescriptor<Sound> runGrass =
            new AssetDescriptor<Sound>("audio/sounds/runGrass.ogg", Sound.class);


    public static final AssetDescriptor<Sound> runStone =
            new AssetDescriptor<Sound>("audio/sounds/runStone.ogg", Sound.class);

    public static final AssetDescriptor<Sound> fail =
            new AssetDescriptor<Sound>("audio/sounds/fail.ogg", Sound.class);


    public void load() {
        manager.load(music);
        manager.load(jump);
        manager.load(slide);
        manager.load(runGrass);
        manager.load(runStone);

        manager.load(fail);

    }

    public void dispose() {
        manager.dispose();
    }

}
