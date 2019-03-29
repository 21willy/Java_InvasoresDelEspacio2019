/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Guillermo Maroto
 */
public class Marciano {
    public Image imagen1, imagen2 = null;
    public int x = 1;
    public int y = 0;
    private int vX = 2;
    public boolean vivo = true; 
              
    public Marciano(){
       
        
    }
    
    public void mueve(){
        x += vX;
    }

    public void setvX(int vX) {
        this.vX = vX;
    }

    public int getvX() {
        return vX;
    }
}
