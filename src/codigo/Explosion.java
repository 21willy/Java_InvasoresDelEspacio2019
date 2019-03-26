/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author guill
 */
public class Explosion {
   
     public Image imagen1, imagen2 = null;
    public int x = 1;
    public int y = 0;
    private int vX = 1;
    public boolean vivo = true; 
              
    public Explosion(){
       
        
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
