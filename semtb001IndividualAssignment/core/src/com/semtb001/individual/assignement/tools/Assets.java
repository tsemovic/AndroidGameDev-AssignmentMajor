package com.semtb001.individual.assignement.tools;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

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

    public static final AssetDescriptor<Texture> backgroundTint =
            new AssetDescriptor<Texture>("gui/backgroundTint.png", Texture.class);

    public static final AssetDescriptor<Texture> menuBackground =
            new AssetDescriptor<Texture>("gui/mainMenuBackground.png", Texture.class);

    public void load() {
        //load music and sounds
        manager.load(music);
        manager.load(jump);
        manager.load(slide);
        manager.load(runGrass);
        manager.load(runStone);
        manager.load(fail);

        //load texture images
        manager.load(backgroundTint);
        manager.load(menuBackground);
    }

    public void dispose() {
        manager.dispose();
    }

}
