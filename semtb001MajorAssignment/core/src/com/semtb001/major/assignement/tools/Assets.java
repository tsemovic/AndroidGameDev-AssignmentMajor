package com.semtb001.major.assignement.tools;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

// Class used to load in assets
public class Assets {
    public AssetManager manager = new AssetManager();

    public static final AssetDescriptor<Sound> menuClick =
            new AssetDescriptor<Sound>("audio/sounds/menuClick.ogg", Sound.class);

    public static final AssetDescriptor<Music> music =
            new AssetDescriptor<Music>("audio/music/music.ogg", Music.class);

    public static final AssetDescriptor<Sound> hoe =
            new AssetDescriptor<Sound>("audio/sounds/hoe.ogg", Sound.class);

    public static final AssetDescriptor<Sound> seeds =
            new AssetDescriptor<Sound>("audio/sounds/seeds.ogg", Sound.class);

    public static final AssetDescriptor<Sound> waterIn =
            new AssetDescriptor<Sound>("audio/sounds/waterIn.ogg", Sound.class);

    public static final AssetDescriptor<Sound> waterOut =
            new AssetDescriptor<Sound>("audio/sounds/waterOut.ogg", Sound.class);

    public static final AssetDescriptor<Sound> walkFast =
            new AssetDescriptor<Sound>("audio/sounds/walkFast.ogg", Sound.class);

    public static final AssetDescriptor<Sound> walkMedium =
            new AssetDescriptor<Sound>("audio/sounds/walkMedium.ogg", Sound.class);

    public static final AssetDescriptor<Sound> walkSlow =
            new AssetDescriptor<Sound>("audio/sounds/walkSlow.ogg", Sound.class);

    // Provide image assets and their path
    public static final AssetDescriptor<Texture> backgroundTint =
            new AssetDescriptor<Texture>("gui/backgroundTint.png", Texture.class);

    public static final AssetDescriptor<Texture> menuBackground =
            new AssetDescriptor<Texture>("gui/mainMenuBackground.png", Texture.class);

    public static final AssetDescriptor<TextureAtlas> textureAtlas =
            new AssetDescriptor<TextureAtlas>("texturepack/textures.pack", TextureAtlas.class);

    // Provide gui assets and their path
    public static final AssetDescriptor<Texture> hoeActive =
            new AssetDescriptor<Texture>("gui/hoeActive.png", Texture.class);

    public static final AssetDescriptor<Texture> hoeInactive =
            new AssetDescriptor<Texture>("gui/hoeInactive.png", Texture.class);

    public static final AssetDescriptor<Texture> seedsActive =
            new AssetDescriptor<Texture>("gui/seedsActive.png", Texture.class);

    public static final AssetDescriptor<Texture> seedsInactive =
            new AssetDescriptor<Texture>("gui/seedsInactive.png", Texture.class);

    public static final AssetDescriptor<Texture> wateringCanFullActive =
            new AssetDescriptor<Texture>("gui/wateringCanFullActive.png", Texture.class);

    public static final AssetDescriptor<Texture> wateringCanFullInactive =
            new AssetDescriptor<Texture>("gui/wateringCanFullInactive.png", Texture.class);

    public static final AssetDescriptor<Texture> wateringCanEmptyActive =
            new AssetDescriptor<Texture>("gui/wateringCanEmptyActive.png", Texture.class);

    public static final AssetDescriptor<Texture> wateringCanEmptyInactive =
            new AssetDescriptor<Texture>("gui/wateringCanEmptyInactive.png", Texture.class);


    public void load() {

        // Load audio
        manager.load(menuClick);
        manager.load(music);

        manager.load(hoe);
        manager.load(seeds);
        manager.load(waterIn);
        manager.load(waterOut);
        manager.load(walkFast);
        manager.load(walkMedium);
        manager.load(walkSlow);


        // Load texture images
        manager.load(backgroundTint);
        manager.load(menuBackground);

        // Load gui images
        manager.load(hoeActive);
        manager.load(hoeInactive);

        manager.load(seedsActive);
        manager.load(seedsInactive);

        manager.load(wateringCanFullActive);
        manager.load(wateringCanFullInactive);
        manager.load(wateringCanEmptyActive);
        manager.load(wateringCanEmptyInactive);

        // Load texture atlas
        manager.load(textureAtlas);
    }

    public void dispose() {
        manager.dispose();
    }

}
