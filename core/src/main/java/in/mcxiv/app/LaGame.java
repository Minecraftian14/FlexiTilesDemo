package in.mcxiv.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.kotcrab.vis.ui.VisUI;
import in.mcxiv.app.screens.Title;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class LaGame extends Game {

    public Texture demoTile;
    public Texture adjustmentKnob;
    public Texture cloud;

    Title title;

    @Override
    public void create() {
        VisUI.load(VisUI.SkinScale.X2);
        demoTile = new Texture(Gdx.files.internal("demoTile2.png"), Pixmap.Format.RGBA8888, false);
        adjustmentKnob = new Texture("adjustmentKnob.png");
        cloud = new Texture("cloud.png");
        setScreen(title = new Title(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        VisUI.dispose();
        demoTile.dispose();
        adjustmentKnob.dispose();
        cloud.dispose();
        title.dispose();
    }
}
