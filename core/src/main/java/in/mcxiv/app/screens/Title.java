package in.mcxiv.app.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import in.mcxiv.app.LaGame;
import in.mcxiv.app.LaScreen;

public class Title extends LaScreen {

    Demo1 demo1;
    Demo2 demo2;

    public Title(LaGame game) {
        super(game);
        demo1 = new Demo1(game);
        demo2 = new Demo2(game);
    }

    @Override
    public void show() {
        super.show();
        VisTable root = new VisTable();
        root.setFillParent(true);
        stage.addActor(root);

        VisTextButton button;

        button = new VisTextButton("Demo 1!");
        button.addListener(action(() -> game.setScreen(demo1)));
        root.add(button).row();

        button = new VisTextButton("Demo 2!");
        button.addListener(action(() -> game.setScreen(demo2)));
        root.add(button).row();
    }

    @Override
    public void renderChild(float delta) {
    }
}
