package in.mcxiv.app;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class LaScreen implements Screen, InputProcessor {

    public LaGame game;
    public OrthographicCamera camera;
    public ExtendViewport viewport;
    public SpriteBatch batch;
    public Stage stage;

    public LaScreen(LaGame game) {
        this.game = game;
        camera = new OrthographicCamera(8, 6);
        viewport = new ExtendViewport(camera.viewportWidth, camera.viewportHeight, camera);
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
    }

    @Override
    public final void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(227 / 255f, 196 / 255f, 138 / 255f, 1.0f);
        stage.act(delta);
        processInputs(delta);
        renderChild(delta);
        stage.draw();
    }

    public void processInputs(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            moveCamera(0, 3);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            moveCamera(0, -3);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            moveCamera(3, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            moveCamera(-3, 0);
    }

    public abstract void renderChild(float delta);

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        if (width * height != 0)
            stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public ChangeListener action(Runnable r) {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                r.run();
            }
        };
    }

    protected boolean moveCamera(int keycode) {
        if (keycode == Input.Keys.UP)
            return moveCamera(0, 1);
        if (keycode == Input.Keys.DOWN)
            return moveCamera(0, -1);
        if (keycode == Input.Keys.RIGHT)
            return moveCamera(1, 0);
        if (keycode == Input.Keys.LEFT)
            return moveCamera(-1, 0);
        return false;
    }

    public boolean moveCamera(float x, float y) {
        camera.position.add(x * Gdx.graphics.getDeltaTime(), y * Gdx.graphics.getDeltaTime(), 0);
        camera.update();
        return true;
    }
}
