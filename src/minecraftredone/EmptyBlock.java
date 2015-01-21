/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minecraftredone;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Dasty
 */
public class EmptyBlock extends Block
{
    public EmptyBlock(Chunk c, Vector3f l, TextureManager m)
    {
        super(c,l,m,0);
    }
    
    @Override
    public void draw(int x, int y, int z)
    {
        //well, its empty... what were you expecting?
    }
}
