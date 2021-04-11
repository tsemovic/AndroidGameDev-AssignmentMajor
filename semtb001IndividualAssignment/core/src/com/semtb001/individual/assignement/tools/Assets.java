package com.semtb001.individual.assignement.tools;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

// Class used to load in assets
public class Assets {
    public AssetManager manager = new AssetManager();

    // Provide audio assets and thier path
    public static final AssetDescriptor<Music> music =
            new AssetDescriptor<Music>("audio/music/music.ogg", Music.class);

    public static final AssetDescriptor<Sound> jump1 =
            new AssetDescriptor<Sound>("audio/sounds/jump1.ogg", Sound.class);

    public static final AssetDescriptor<Sound> jump2 =
            new AssetDescriptor<Sound>("audio/sounds/jump2.ogg", Sound.class);

    public static final AssetDescriptor<Sound> jump3 =
            new AssetDescriptor<Sound>("audio/sounds/jump3.ogg", Sound.class);

    public static final AssetDescriptor<Sound> slide1 =
            new AssetDescriptor<Sound>("audio/sounds/slide1.ogg", Sound.class);

    public static final AssetDescriptor<Sound> slide2 =
            new AssetDescriptor<Sound>("audio/sounds/slide2.ogg", Sound.class);

    public static final AssetDescriptor<Sound> slide3 =
            new AssetDescriptor<Sound>("audio/sounds/slide3.ogg", Sound.class);

    public static final AssetDescriptor<Sound> fail =
            new AssetDescriptor<Sound>("audio/sounds/fail.ogg", Sound.class);

    public static final AssetDescriptor<Music> slime =
            new AssetDescriptor<Music>("audio/sounds/slime.ogg", Music.class);

    public static final AssetDescriptor<Music> bee =
            new AssetDescriptor<Music>("audio/sounds/bee.ogg", Music.class);

    public static final AssetDescriptor<Sound> coin =
            new AssetDescriptor<Sound>("audio/sounds/coin.ogg", Sound.class);

    public static final AssetDescriptor<Sound> menuClick =
            new AssetDescriptor<Sound>("audio/sounds/menuClick.ogg", Sound.class);

    // Provide image assets and their path
    public static final AssetDescriptor<Texture> backgroundTint =
            new AssetDescriptor<Texture>("gui/backgroundTint.png", Texture.class);

    public static final AssetDescriptor<Texture> menuBackground =
            new AssetDescriptor<Texture>("gui/mainMenuBackground.png", Texture.class);

    public static final AssetDescriptor<TextureAtlas> textureAtlas =
            new AssetDescriptor<TextureAtlas>("texturepack/playerAndEnemy.pack", TextureAtlas.class);

    public void load() {

        // Load player sounds
        manager.load(jump1);
        manager.load(jump2);
        manager.load(jump3);
        manager.load(slide1);
        manager.load(slide2);
        manager.load(slide3);
        manager.load(fail);

        // Load looping game music
        manager.load(music);

        // Load enemy sounds
        manager.load(slime);
        manager.load(bee);

        // Load coin sound
        manager.load(coin);

        // Load menu click sound
        manager.load(menuClick);

        // Load texture images
        manager.load(backgroundTint);
        manager.load(menuBackground);

        // Load texture atlas
        manager.load(textureAtlas);
    }

    public void dispose() {
        manager.dispose();
    }

}
