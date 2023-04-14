package in.mcxiv.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import in.mcxiv.app.LaGame;
import in.mcxiv.app.LaScreen;
import in.mcxiv.flexitiles.LinearFlexiTile;

public class Demo1 extends LaScreen {

    private Texture demoTile;
    private Texture adjustmentKnob;
    private LinearFlexiTile flexiTile;

    private VisTable root = new VisTable();

    Array<Vector2> points = new Array<>();
    Vector2 selectedPoint = null;
    Vector2 point = new Vector2();
    Vector2 cache = new Vector2();

    public Demo1(LaGame game) {
        super(game);
        demoTile = game.demoTile;
        adjustmentKnob = game.adjustmentKnob;
        flexiTile = new LinearFlexiTile(new TextureRegion(demoTile), 0.27f, 0.852f, 0.9302326f);

        points.addAll(
            new Vector2(-3, -1),
            new Vector2(-3, 0),
            new Vector2(-1, -1),
            new Vector2(0, -.1f),
            new Vector2(1, 1),
            new Vector2(2, -1),
            new Vector2(3, 1)
        );

        root.setFillParent(true);
        stage.addActor(root);
        VisTextButton button = new VisTextButton("Add New Point!");
        root.add(button).bottom().left().expand();
        button.addListener(new DragListener() {
            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                viewport.unproject(cache.set(x, y));
                points.add(new Vector2(cache));
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                viewport.unproject(cache.set(Gdx.input.getX(), Gdx.input.getY()));
                points.peek().set(cache);
            }
        });
    }

    @Override
    public void renderChild(float delta) {
        flexiTile.updateGuidePoints(points.toArray(Vector2.class));

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        flexiTile.draw(viewport, batch);
        for (int i = 0; i < points.size; i++) {
            batch.setColor(Color.RED);
            Vector2 p = points.get(i);
            batch.draw(adjustmentKnob, p.x - 0.16f, p.y - 0.16f, 0.32f, 0.32f);
        }
        batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        viewport.unproject(point.set(screenX, screenY));
        float d = cache.set(point).sub(points.get(0)).len2();
        Vector2 sp = points.get(0);
        for (int i = 1; i < points.size; i++) {
            float dc = cache.set(point).sub(points.get(i)).len2();
            if (dc < d) {
                d = dc;
                sp = points.get(i);
            }
        }
        selectedPoint = sp;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (selectedPoint == null) return false;
//        viewport.unproject(point.set(Gdx.input.getX(), Gdx.input.getY()));
        viewport.unproject(point.set(screenX, screenY));
        selectedPoint.set(point);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        selectedPoint = null;
        return true;
    }
}
