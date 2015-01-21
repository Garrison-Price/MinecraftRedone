/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minecraftredone;

import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Dasty
 */
public class Block 
{
    public int textureID;
    private Chunk myChunk;
    private Vector3f location;
    private TextureManager tm;
    private float transparency;
    private boolean blockTop = false, blockBottom = false, blockFront = false, blockBack = false, blockLeft = false, blockRight = false;
    public Block(Chunk c, Vector3f l, TextureManager m, int t)
    {
        myChunk = c;
        location = l;
        textureID = t;
        tm = m;
    }
    
    public Vector3f getLocation()
    {
        return location;
    }
    
    private void checkSurroundings(int x, int y, int z)
    {
        if(x == myChunk.maxWidth-1)
        {
            blockRight = false;
            System.out.println(myChunk.getBlocks()[x-1][y][z].textureID != tm.EMPTY);
            if(myChunk.getBlocks()[x-1][y][z].textureID != tm.EMPTY)
            {
                blockLeft = true;
            }
        }
        else if(x == 0)
        {
            blockLeft = false;
            if(myChunk.getBlocks()[x+1][y][z].textureID != tm.EMPTY)
            {
                blockRight = true;
            }
        }
        else
        {
            if(myChunk.getBlocks()[x+1][y][z].textureID != tm.EMPTY)
            {
                blockRight = true;
            }
            if(myChunk.getBlocks()[x-1][y][z].textureID != tm.EMPTY)
            {
                blockLeft = true;
            }
        }
        if(y == myChunk.maxHeight-1)
        {
            blockTop = false;
            if(myChunk.getBlocks()[x][y-1][z].textureID != tm.EMPTY)
            {
                blockBottom = true;
            }
        }
        else if(y == 0)
        {
            blockBottom = false;
            if(myChunk.getBlocks()[x][y+1][z].textureID != tm.EMPTY)
            {
                blockTop = true;
            }
        }
        else
        {
            if(myChunk.getBlocks()[x][y+1][z].textureID != tm.EMPTY)
            {
                blockTop = true;
            }
            if(myChunk.getBlocks()[x][y-1][z].textureID != tm.EMPTY)
            {
                blockBottom = true;
            }
        }
        if(z == myChunk.maxWidth-1)
        {
            blockFront = false;
            if(myChunk.getBlocks()[x][y][z-1].textureID != tm.EMPTY)
            {
                blockBack = true;
            }
        }
        else if(z == 0)
        {
            blockBack = false;
            if(myChunk.getBlocks()[x][y][z+1].textureID != tm.EMPTY)
            {
                blockFront = true;
            }
        }
        else
        {
            if(myChunk.getBlocks()[x][y][z+1].textureID != tm.EMPTY)
            {
                blockFront = true;
            }
            if(myChunk.getBlocks()[x][y][z-1].textureID != tm.EMPTY)
            {
                blockBack = true;
            }
        }
    }
    
    public void draw(int x, int y, int z)
    {
        checkSurroundings(x,y,z);
        /* 6 faces */

        tm.getTexture(textureID).bind();
            
        glBegin(GL_QUADS);            // Draw A Quad
        
        
        if(!blockTop)
        {
            glVertex3f(location.x+1.0f, location.y+1.0f, location.z+-1.0f);   // Top Right Of The Quad (Top)
            glTexCoord2f(0f, 1f);
            glVertex3f(location.x+-1.0f, location.y+1.0f, location.z+-1.0f);  // Top Left Of The Quad (Top)
            glTexCoord2f(1f, 1f);
            glVertex3f(location.x+-1.0f, location.y+1.0f, location.z+1.0f);   // Bottom Left Of The Quad (Top)
            glTexCoord2f(1f, 0f);
            glVertex3f(location.x+1.0f, location.y+1.0f, location.z+1.0f);    // Bottom Right Of The Quad (Top)
            glTexCoord2f(0f, 0f);
        }

        if(!blockBottom)
        {
            glVertex3f(location.x+1.0f, location.y+-1.0f, location.z+1.0f);   // Top Right Of The Quad (Bottom)
            glTexCoord2f(0f, 1f);
            glVertex3f(location.x+-1.0f, location.y+-1.0f, location.z+1.0f);  // Top Left Of The Quad (Bottom)
            glTexCoord2f(1f, 1f);
            glVertex3f(location.x+-1.0f, location.y+-1.0f, location.z+-1.0f); // Bottom Left Of The Quad (Bottom)
            glTexCoord2f(1f, 0f);
            glVertex3f(location.x+1.0f, location.y+-1.0f, location.z+-1.0f);  // Bottom Right Of The Quad (Bottom)
            glTexCoord2f(0f, 0f);
        }
        
        if(!blockFront)
        {
            glVertex3f(location.x+1.0f, location.y+1.0f, location.z+1.0f);    // Top Right Of The Quad (Front)
            glTexCoord2f(0f, 1f);
            glVertex3f(location.x+-1.0f, location.y+1.0f, location.z+1.0f);   // Top Left Of The Quad (Front)
            glTexCoord2f(1f, 1f);
            glVertex3f(location.x+-1.0f, location.y+-1.0f, location.z+1.0f);  // Bottom Left Of The Quad (Front)
            glTexCoord2f(1f, 0f);
            glVertex3f(location.x+1.0f, location.y+-1.0f, location.z+1.0f);   // Bottom Right Of The Quad (Front)
            glTexCoord2f(0f, 0f);
        }

        if(!blockBack)
        {
            glVertex3f(location.x+1.0f, location.y+-1.0f, location.z+-1.0f);  // Bottom Left Of The Quad (Back)
            glTexCoord2f(0f, 1f);
            glVertex3f(location.x+-1.0f, location.y+-1.0f, location.z+-1.0f); // Bottom Right Of The Quad (Back)
            glTexCoord2f(1f, 1f);
            glVertex3f(location.x+-1.0f, location.y+1.0f, location.z+-1.0f);  // Top Right Of The Quad (Back)
            glTexCoord2f(1f, 0f);
            glVertex3f(location.x+1.0f, location.y+1.0f, location.z+-1.0f);   // Top Left Of The Quad (Back)
            glTexCoord2f(0f, 0f);
        }

        if(!blockLeft)
        {
            glVertex3f(location.x+-1.0f, location.y+1.0f, location.z+1.0f);   // Top Right Of The Quad (Left)
            glTexCoord2f(0f, 1f);
            glVertex3f(location.x+-1.0f, location.y+1.0f, location.z+-1.0f);  // Top Left Of The Quad (Left)
            glTexCoord2f(1f, 1f);
            glVertex3f(location.x+-1.0f, location.y+-1.0f, location.z+-1.0f); // Bottom Left Of The Quad (Left)
            glTexCoord2f(1f, 0f);
            glVertex3f(location.x+-1.0f, location.y+-1.0f, location.z+1.0f);  // Bottom Right Of The Quad (Left)
            glTexCoord2f(0f, 0f);
        }

        if(!blockRight)
        {
            glVertex3f(location.x+1.0f, location.y+1.0f, location.z+-1.0f);   // Top Right Of The Quad (Right)
            glTexCoord2f(0f, 1f);
            glVertex3f(location.x+1.0f, location.y+1.0f, location.z+1.0f);    // Top Left Of The Quad (Right)
            glTexCoord2f(1f, 1f);
            glVertex3f(location.x+1.0f, location.y+-1.0f, location.z+1.0f);   // Bottom Left Of The Quad (Right)
            glTexCoord2f(1f, 0f);
            glVertex3f(location.x+1.0f, location.y+-1.0f, location.z+-1.0f);  // Bottom Right Of The Quad (Right)
            glTexCoord2f(0f, 0f);
        }
        glEnd();
    }
}
