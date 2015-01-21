/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minecraftredone;

import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Dasty
 */
public class Chunk 
{
    private Block[][][] blocks;
    private TextureManager tm;
    public int maxWidth = 16;
    public int maxHeight = 256;
    public Chunk(TextureManager m)
    {
        blocks = new Block[maxWidth][maxHeight][maxWidth];
        tm = m;
        for(int x = 0; x < maxWidth; x++)
        {
            for(int y = 0; y < maxHeight; y++)
            {
                for(int z = 0; z < maxWidth; z++)
                {
                    if(z%2 == 0)
                    {
                        if(x%2 == 0)
                        {
                            if(y%2 == 1)
                            {
                                blocks[x][y][z] = new Block(this,new Vector3f((float)x+1f,(float)y+1f,(float)z+1f),tm,tm.DIRT);
                            }
                            else
                            {
                                blocks[x][y][z] = new Block(this,new Vector3f((float)x+1f,(float)y,(float)z+1f),tm,tm.DIRT);
                            }
                        }
                        else if(y%2 == 1)
                        {
                            blocks[x][y][z] = new Block(this,new Vector3f((float)x,(float)y+1f,(float)z+1f),tm,tm.DIRT);
                        }
                        else
                        {
                            blocks[x][y][z] = new Block(this,new Vector3f((float)x,(float)y,(float)z+1f),tm,tm.DIRT);
                        }
                    }
                    else
                    {
                        if(x%2 == 0)
                        {
                            if(y%2 == 1)
                            {
                                blocks[x][y][z] = new Block(this,new Vector3f((float)x+1f,(float)y+1f,(float)z),tm,tm.DIRT);
                            }
                            else
                            {
                                blocks[x][y][z] = new Block(this,new Vector3f((float)x+1f,(float)y,(float)z),tm,tm.DIRT);
                            }
                        }
                        else if(y%2 == 1)
                        {
                            blocks[x][y][z] = new Block(this,new Vector3f((float)x,(float)y+1f,(float)z),tm,tm.DIRT);
                        }
                        else
                        {
                            blocks[x][y][z] = new Block(this,new Vector3f((float)x,(float)y,(float)z),tm,tm.DIRT);
                        }
                    }
                    
                    if(Math.random()*20.0 <= 18.0)
                    {
                        EmptyBlock b = new EmptyBlock(this,blocks[x][y][z].getLocation(),tm);
                        blocks[x][y][z] = b;
                    }
                    else
                    {
                        if(Math.random() <= .5)
                        {
                            blocks[x][y][z].textureID = 2;
                        }
                    }
                }
            }
        }
    }
    
    public Block[][][] getBlocks()
    {
        return blocks;
    }
    
    public void drawBlocks(Vector3f p)
    {
        //System.out.println("Start Draw");
        for(int x = 0; x < maxWidth; x++)
        {
            for(int y = 0; y < maxHeight; y++)
            {
                for(int z = 0; z < maxWidth; z++)
                {
                    if(Math.pow(blocks[x][y][z].getLocation().x+p.x, 2) + Math.pow(blocks[x][y][z].getLocation().y+p.y, 2) + Math.pow(blocks[x][y][z].getLocation().z+p.z, 2) < 40*40)
                        blocks[x][y][z].draw(x,y,z);
                }
            }
        }
        //System.out.println("End Draw");
    }
}
