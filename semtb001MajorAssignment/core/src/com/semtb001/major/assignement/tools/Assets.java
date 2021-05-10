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

    // Provide image assets and their path
    public static final AssetDescriptor<Texture> backgroundTint =
            new AssetDescriptor<Texture>("gui/backgroundTint.png", Texture.class);

    public static final AssetDescriptor<Texture> menuBackground =
            new AssetDescriptor<Texture>("gui/mainMenuBackground.png", Texture.class);

    public static final AssetDescriptor<TextureAtlas> textureAtlas =
            new AssetDescriptor<TextureAtlas>("texturepack/textures.pack", TextureAtlas.class);

    public void load() {

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
