/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minecraftredone;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 *
 * @author Dasty
 */
public class MinecraftRedone {

    private boolean fullScreen = false;
    private boolean running = false;
    
    private TextureManager tm;
    
    public final int maxLookUp = 85;
    public final int maxLookDown = -85;
    public int fov = 68;
    public Vector3f position = new Vector3f(0, 0, 0);
    public Vector3f rotation = new Vector3f(0, 0, 0);
    public float zFar = 20f;
    public float zNear = 0.3f;
    public int walkingSpeed = 35;
    public int mouseSpeed = 2;
    
    private int fps;
    private long lastFPS;
    private long lastFrame;
    
    private Chunk c;
    
    public MinecraftRedone()
    {
        
        getDelta();
        lastFPS = getTime();
        initGL();
        tm = new TextureManager();
        c = new Chunk(tm);
        start();
    }
    
    public void initGL()
    {
        try 
        {
            if (fullScreen) 
            {
                Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
            } 
            else 
            {
                Display.setResizable(true);
                Display.setDisplayMode(new DisplayMode(800, 600));
            }
            Display.setTitle("Minecraft Redone");
            Display.create();
            
            
            glViewport(0, 0, Display.getWidth(), Display.getHeight());
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            gluPerspective(45.0f, (float)Display.getWidth()/(float)Display.getHeight(), 1.0f, 100.0f);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glShadeModel(GL_SMOOTH);              // Enable Smooth Shading
            glClearColor(0.0f, 0.0f, 0.5f, 0.5f);    // Black Background
            glClearDepth(1.0f);                      // Depth Buffer Setup
            glEnable(GL_DEPTH_TEST);              // Enables Depth Testing
            glDepthFunc(GL_LEQUAL);               // The Type Of Depth Testing To Do
            glEnable(GL_TEXTURE_2D);   
            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);  
            glEnable(GL_FOG);
            {
                FloatBuffer fogColours = BufferUtils.createFloatBuffer(
                        4);
                fogColours.put(new float[]{185f/255f,211f/255f, 238f/255f, .5f});
                glClearColor(185f/255f,211f/255f, 238f/255f, .5f);
                fogColours.flip();
                glFog(GL_FOG_COLOR, fogColours);
                glFogi(GL_FOG_MODE, GL_LINEAR);
                glHint(GL_FOG_HINT, GL_NICEST);
                glFogf(GL_FOG_START, 32f);
                glFogf(GL_FOG_END, 36f);
                glFogf(GL_FOG_DENSITY, 0.005f);
            }
        } 
        catch (LWJGLException ex) 
        {
            System.err.println("Display initialization failed.");
            System.exit(1);
        }
    }
    
    public void start()
    {
        if(!running)
            running = true;

        while(running)
        {
            int delta = getDelta();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
           
            glRotatef(rotation.x, 1, 0, 0);
            glRotatef(rotation.y, 0, 1, 0);
            glRotatef(rotation.z, 0, 0, 1);
            glTranslatef(position.x, position.y, position.z);

            c.drawBlocks(position);
            
            if (Mouse.isGrabbed()) {
                float mouseDX = Mouse.getDX() * mouseSpeed * 0.16f;
                float mouseDY = Mouse.getDY() * mouseSpeed * 0.16f;
                if (rotation.y + mouseDX >= 360) {
                    rotation.y = rotation.y + mouseDX - 360;
                } else if (rotation.y + mouseDX < 0) {
                    rotation.y = 360 - rotation.y + mouseDX;
                } else {
                    rotation.y += mouseDX;
                }
                if (rotation.x - mouseDY >= maxLookDown && rotation.x - mouseDY <= maxLookUp) {
                    rotation.x += -mouseDY;
                } else if (rotation.x - mouseDY < maxLookDown) {
                    rotation.x = maxLookDown;
                } else if (rotation.x - mouseDY > maxLookUp) {
                    rotation.x = maxLookUp;
                }
            }

            boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W);
            boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
            boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
            boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);
            boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
            boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
            boolean moveFaster = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
            boolean moveSlower = Keyboard.isKeyDown(Keyboard.KEY_TAB);

            if (moveFaster && !moveSlower) {
                walkingSpeed *= 4f;
            }
            if (moveSlower && !moveFaster) {
                walkingSpeed /= 10f;
            }

            if (keyUp && keyRight && !keyLeft && !keyDown) {
                float angle = rotation.y + 45;
                Vector3f newPosition = new Vector3f(position);
                float schuine = (walkingSpeed * 0.0002f) * delta;
                float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
                float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
                newPosition.z += aanliggende;
                newPosition.x -= overstaande;
                position.z = newPosition.z;
                position.x = newPosition.x;
            }
            if (keyUp && keyLeft && !keyRight && !keyDown) {
                float angle = rotation.y - 45;
                Vector3f newPosition = new Vector3f(position);
                float schuine = (walkingSpeed * 0.0002f) * delta;
                float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
                float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
                newPosition.z += aanliggende;
                newPosition.x -= overstaande;
                position.z = newPosition.z;
                position.x = newPosition.x;
            }
            if (keyUp && !keyLeft && !keyRight && !keyDown) {
                float angle = rotation.y;
                Vector3f newPosition = new Vector3f(position);
                float schuine = (walkingSpeed * 0.0002f) * delta;
                float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
                float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
                newPosition.z += aanliggende;
                newPosition.x -= overstaande;
                position.z = newPosition.z;
                position.x = newPosition.x;
            }
            if (keyDown && keyLeft && !keyRight && !keyUp) {
                float angle = rotation.y - 135;
                Vector3f newPosition = new Vector3f(position);
                float schuine = (walkingSpeed * 0.0002f) * delta;
                float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
                float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
                newPosition.z += aanliggende;
                newPosition.x -= overstaande;
                position.z = newPosition.z;
                position.x = newPosition.x;
            }
            if (keyDown && keyRight && !keyLeft && !keyUp) {
                float angle = rotation.y + 135;
                Vector3f newPosition = new Vector3f(position);
                float schuine = (walkingSpeed * 0.0002f) * delta;
                float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
                float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
                newPosition.z += aanliggende;
                newPosition.x -= overstaande;
                position.z = newPosition.z;
                position.x = newPosition.x;
            }
            if (keyDown && !keyUp && !keyLeft && !keyRight) {
                float angle = rotation.y;
                Vector3f newPosition = new Vector3f(position);
                float schuine = -(walkingSpeed * 0.0002f) * delta;
                float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
                float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
                newPosition.z += aanliggende;
                newPosition.x -= overstaande;
                position.z = newPosition.z;
                position.x = newPosition.x;
            }
            if (keyLeft && !keyRight && !keyUp && !keyDown) {
                float angle = rotation.y - 90;
                Vector3f newPosition = new Vector3f(position);
                float schuine = (walkingSpeed * 0.0002f) * delta;
                float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
                float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
                newPosition.z += aanliggende;
                newPosition.x -= overstaande;
                position.z = newPosition.z;
                position.x = newPosition.x;
            }
            if (keyRight && !keyLeft && !keyUp && !keyDown) {
                float angle = rotation.y + 90;
                Vector3f newPosition = new Vector3f(position);
                float schuine = (walkingSpeed * 0.0002f) * delta;
                float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
                float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
                newPosition.z += aanliggende;
                newPosition.x -= overstaande;
                position.z = newPosition.z;
                position.x = newPosition.x;
            }
            if (flyUp && !flyDown) {
                double newPositionY = (walkingSpeed * 0.0002) * delta;
                position.y -= newPositionY;
            }
            if (flyDown && !flyUp) {
                double newPositionY = (walkingSpeed * 0.0002) * delta;
                position.y += newPositionY;
            }
            if (moveFaster && !moveSlower) {
                walkingSpeed /= 4f;
            }
            if (moveSlower && !moveFaster) {
                walkingSpeed *= 10f;
            }
            while (Mouse.next()) {
                if (Mouse.isButtonDown(0)) {
                    Mouse.setGrabbed(true);
                }
                if (Mouse.isButtonDown(1)) {
                    Mouse.setGrabbed(false);
                }

            }
            while (Keyboard.next()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
                    position = new Vector3f(0, 0, 0);
                    rotation = new Vector3f(0, 0, 0);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
                    mouseSpeed += 1;
                    System.out.println("Mouse speed changed to " + mouseSpeed + ".");
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
                    if (mouseSpeed - 1 > 0) {
                        mouseSpeed -= 1;
                        System.out.println("Mouse speed changed to " + mouseSpeed + ".");
                    }
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
                    System.out.println("Walking speed changed to " + walkingSpeed + ".");
                    walkingSpeed += 1;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                    System.out.println("Walking speed changed to " + walkingSpeed + ".");
                    walkingSpeed -= 1;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_F11)) {
                    try {
                        Display.setFullscreen(!Display.isFullscreen());
                        if (!Display.isFullscreen()) {
                            Display.setResizable(true);
                            Display.setDisplayMode(new DisplayMode(800, 600));
                            glViewport(0, 0, Display.getWidth(), Display.getHeight());
                            glMatrixMode(GL_PROJECTION);
                            glLoadIdentity();
                            gluPerspective(fov, (float) Display.getWidth() / (float) Display.getHeight(), zNear, zFar);
                            glMatrixMode(GL_MODELVIEW);
                            glLoadIdentity();
                        } else {
                            glViewport(0, 0, Display.getWidth(), Display.getHeight());
                            glMatrixMode(GL_PROJECTION);
                            glLoadIdentity();
                            gluPerspective(fov, (float) Display.getWidth() / (float) Display.getHeight(), zNear, zFar);
                            glMatrixMode(GL_MODELVIEW);
                            glLoadIdentity();
                        }
                    } catch (LWJGLException ex) {

                    }
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                    if (!Mouse.isGrabbed() || Display.isFullscreen()) {
                        running = false;
                    } else {
                        Mouse.setGrabbed(false);
                    }
                }
            }

            if (Display.wasResized()) {
                glViewport(0, 0, Display.getWidth(), Display.getHeight());
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                gluPerspective(fov, (float) Display.getWidth() / (float) Display.getHeight(), zNear, zFar);
                glMatrixMode(GL_MODELVIEW);
                glLoadIdentity();
            }
            

            updateFPS();
            Display.update();
            if (Display.isCloseRequested()) {
                running = false;
            }
        }
    }
    
    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private int getDelta() {
        long currentTime = getTime();
        int delta = (int) (currentTime - lastFrame);
        lastFrame = getTime();
        return delta;
    }

    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            System.out.println("FPS: " + fps);
            
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
    
    public static void main(String[] args) 
    {
        new MinecraftRedone();
    }
}
