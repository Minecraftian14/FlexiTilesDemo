package in.mcxiv.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import in.mcxiv.app.LaGame;
import in.mcxiv.app.LaScreen;
import in.mcxiv.flexitiles.LinearFlexiTile;

import java.util.Random;

public class Demo2 extends LaScreen {

    Texture demoTile;
    TextureRegion adjustmentKnob;
    LinearFlexiTile floorTile;
    //    Array<LinearFlexiTile> clouds = new Array<>();
    World world;
    Box2DDebugRenderer debug;
    Body ball;

    public Demo2(LaGame game) {
        super(game);
        demoTile = game.demoTile;
        adjustmentKnob = new TextureRegion(game.adjustmentKnob);

        world = new World(new Vector2(0, -10), true);
        debug = new Box2DDebugRenderer();

        Random random = new Random();

        {
            Array<Vector2> points = new Array<>();
            for (int i = 0; i < 1 + 3 * 5; ++i)
                points.add(new Vector2(3 * i, random.nextInt(6) - 3));
            floorTile = new LinearFlexiTile(new TextureRegion(demoTile), 0.27f, 0.852f, 0.9302326f);
            floorTile.updateGuidePoints(points.toArray(Vector2.class));
            floorTile.makePathContinuous();

            BodyDef def = new BodyDef();
            def.position.set(0, 0);
            Body body = world.createBody(def);
            Array<FloatArray> shapes = floorTile.createShape(100);
            for (int j = 0; j < shapes.size; j++) {
                PolygonShape s = new PolygonShape();
                s.set(shapes.get(j).toArray());
                body.createFixture(s, 0.0f);
                s.dispose();
            }
        }


//        for (int i = 0; i < 10; ++i) {
//            LinearFlexiTile cloud = new LinearFlexiTile(new TextureRegion(game.cloud), 0.27f, 0.852f, 0.9f);
//            cloud.updateGuidePoints(
//                new Vector2(5 * i - 3, random.nextFloat() * 3),
//                new Vector2(5 * i - 1, random.nextFloat() * 3),
//                new Vector2(5 * i + 1, random.nextFloat() * 3)
//            );
//            clouds.add(cloud);
//        }

        {
            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.DynamicBody;
            def.position.set(-2, 5);
            ball = world.createBody(def);
            CircleShape circle = new CircleShape();
            circle.setRadius(0.25f);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circle;
            fixtureDef.density = 0.5f;
            fixtureDef.friction = 0.4f;
            fixtureDef.restitution = 0.6f;
            ball.createFixture(fixtureDef);
            circle.dispose();
        }
    }

    @Override
    public void renderChild(float delta) {
        doPhysicsStep(Gdx.graphics.getDeltaTime());

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
//          for (int i = 0; i < clouds.size; i++)
//              clouds.get(i).draw(viewport, batch);
        floorTile.draw(viewport, batch);
        Vector2 pos = ball.getPosition();
        batch.draw(adjustmentKnob, pos.x - .25f, pos.y - .25f, 0.25f, 0.25f, .5f, .5f, 1, 1, MathUtils.radiansToDegrees * ball.getAngle());
        batch.end();

//        debug.render(world, camera.combined);
    }

    @Override
    public void processInputs(float delta) {
        Vector2 vel = ball.getLinearVelocity();
        Vector2 pos = ball.getPosition();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && vel.x < 1)
            ball.applyLinearImpulse(0.4f, 0, pos.x, pos.y, true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && vel.x > -1)
            ball.applyLinearImpulse(-0.4f, 0, pos.x, pos.y, true);
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && vel.y < 1)
            ball.applyLinearImpulse(0, 0.4f, pos.x, pos.y, true);

        camera.position.x = MathUtils.lerp(camera.position.x, pos.x, 1.25f * delta);
        camera.position.y = MathUtils.lerp(camera.position.y, pos.y, 1.25f * delta);
        camera.update();
    }

    private float accumulator = 0;

    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= 0.016666668f) {
            world.step(0.016666668f, 6, 2);
            accumulator -= 0.016666668f;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
    }
}
