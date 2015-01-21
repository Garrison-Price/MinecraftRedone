/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minecraftredone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Dasty
 */
public class TextureManager 
{
    public final int EMPTY = 0;
    public final int DIRT = 1;
    public final int STONE = 2;
    private ArrayList<Texture> textures;
    File directory = new File(".");
    public TextureManager()
    {
        String workingDir = directory.getAbsolutePath().substring(0, directory.getAbsolutePath().indexOf("\\lib"));
        textures = new ArrayList();
        try 
        {
            textures.add(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(workingDir+"\\res\\dirt.png"), GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(workingDir+"\\res\\dirt.png"), GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(workingDir+"\\res\\stone.png"), GL_NEAREST));
            
            for(int i = 0; i < textures.size(); i++)
            {
                System.out.println("Texture loaded: "+textures.get(i));
                System.out.println(">> Image width: "+textures.get(i).getImageWidth());
                System.out.println(">> Image height: "+textures.get(i).getImageHeight());
                System.out.println(">> Texture width: "+textures.get(i).getTextureWidth());
                System.out.println(">> Texture height: "+textures.get(i).getTextureHeight());
                System.out.println(">> Texture ID: "+textures.get(i).getTextureID());
            }
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public Texture getTexture(int i)
    {
        return textures.get(i);
    }
}
