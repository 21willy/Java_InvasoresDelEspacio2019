/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;

/**
 *
 * @author Jorge Cisneros
 */
public class VentanaJuego extends javax.swing.JFrame {

    
    static int ANCHOPANTALLA = 600;
    static int ALTOPANTALLA = 450;
    
    
    //numero de marcianos que van a aparecer
    int filas = 5;
    int columnas = 7;
    
    BufferedImage buffer = null;
    
    Nave miNave = new Nave();
    Disparo miDisparo = new Disparo();
//    Marciano miMarciano = new Marciano();
    Marciano [][] ListaMarciano = new Marciano[filas][columnas];
    boolean direccionMarciano = false;
    boolean gameOver = false;
 
   
    //el contador sirve para decidir que imagen del marciano toca poner
    int contador = 0;
    //image para cargar el spritesheet con todos los sprites del juego
    BufferedImage plantilla = null;
    Image [][] imagenes;
    Image [][] imagenesNave;
    Image [][] imagenesDisparo;
    Image muerto;
    
    
    Timer temporizador = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            bucleDelJuego();
        }
    });
    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();
        //para cargar el archivo de imagenes:
        // primero la ruta al archivo
        // segundo numero de filas 
        // tercero numero de columnas
        //cuarto lo que mide de ancho cada sprite
        //quinto lo que mide de alto cada sprite
        // sexto la escala
        imagenes = cargaImagenes("/imagenes/Mexicano.png", 1, 2, 165, 258, 5);
        
        imagenesNave = cargaImagenes("/imagenes/Trump.png", 4, 6, 100, 100, 2);
        
        imagenesDisparo = cargaImagenes("/imagenes/muro.png", 3, 6, 203, 174, 6);
        
        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        buffer  = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);
        buffer.createGraphics();
        
        temporizador.start();
        
        miNave.imagen = imagenesNave[3][2];
        miDisparo.imagen = imagenesDisparo [1][5];
        //inicializo la posición inicial de la nave
        miNave.x = ANCHOPANTALLA /2 - miNave.imagen.getWidth(this) /2;
        miNave.y = ALTOPANTALLA - miNave.imagen.getHeight(this) - 40;
        
        
        //inicializo el array de marcianos
        
        //primero numero de fila de marcianos que estoy creando
        //segundo fila dentro del spritesheet del marciano que quiero pintar
        //tercero columna dentro del spritesheet del marciano que quiero pintar
        
        creaFilaMarcianos(0, 0, 0);
        creaFilaMarcianos(1, 0, 0);
        creaFilaMarcianos(2, 0, 0);
        creaFilaMarcianos(3, 0, 0);
        creaFilaMarcianos(4, 0, 0);
       
        
    }
    
    private void creaFilaMarcianos(int numFila, int spriteFila, int spriteColumna){
        for(int j = 0; j < columnas; j++){
                ListaMarciano [numFila][j] = new Marciano();
                ListaMarciano [numFila][j].imagen1 = imagenes[spriteFila][spriteColumna];
                ListaMarciano [numFila][j].imagen2 = imagenes[spriteFila][spriteColumna + 1];
                ListaMarciano [numFila][j].x = j*(15 + ListaMarciano [numFila][j].imagen1.getWidth(null));
                ListaMarciano [numFila][j].y = numFila*(15 + ListaMarciano [numFila][j].imagen1.getHeight(null));
            }
    }
    
    
    private void reproduce (String cancion){
           try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream( getClass().getResource(cancion) ));
            clip.loop(0);
            Thread one = new Thread() {
                    public void run() {
                            while(clip.getFramePosition()<clip.getFrameLength())
                                Thread.yield();
                             
                    }  
                };
            one.start();
        } catch (Exception e) {      
        } 
   }
    
    //este metod va a servir para crear el array de imagenes con todas las imagenes
    //del sprite. Devolverá un array de dos dimensiones con las imagenes colocadas
    private Image [][] cargaImagenes (String nombreArchivoImagen, int numFilas, int numColumnas, int ancho, int alto,int escala ){
        
        try {
            plantilla = ImageIO.read(getClass().getResource(nombreArchivoImagen));
        } catch (IOException ex) {
            
        }
        
        Image [][] arrayImagenes = new Image[numFilas][numColumnas];
        // cargo las imagenes de forma individual en cada imagen del array de imagenes
        for(int i = 0;i < numFilas; i++){
            for(int j = 0; j < numColumnas; j++){
                arrayImagenes [i][j] = plantilla.getSubimage(j * ancho, i * alto, ancho, alto);
                arrayImagenes [i][j] = arrayImagenes [i][j].getScaledInstance(ancho/escala, alto/escala, Image.SCALE_SMOOTH);
            }
        }
        return arrayImagenes;
    }
    
    private void finPartida (Graphics2D muerto) throws IOException{
        try{
            Image gameOver1 = ImageIO.read(getClass().getResource("/imagenes/gameOver.png"));
            muerto.drawImage(gameOver1, 0, 0, ANCHOPANTALLA, ALTOPANTALLA, null);
        }catch (IOException ex){
            
        }
    }


    private void bucleDelJuego(){
        // gobierna el redibujado de los objetos en el jPanel1
        // primero borro lo que hay en el buffer
        contador++;
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        if(!gameOver){
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
      
        ///////////////////////////////////////////////////
        //redibujamos aqui cada elemento
        g2.drawImage(miDisparo.imagen, miDisparo.x, miDisparo.y, null);
        g2.drawImage(miNave.imagen, miNave.x, miNave.y, null);
        pintaMarcianos(g2);
        chequeaColision();
        miDisparo.mueve();
        miNave.mueve();
        }
        else{
            try {
                finPartida(g2);
            } catch (IOException ex) {
                
            }
        }
        
        
        
        
        ////////////////////////////////////////////////////
        //*****************  fase final  *****************//
        //****  el buffer de golpe sobre el jPanel1  *****//
        
        
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, null);    
    }
    
    private void chequeaColision(){
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();
        Rectangle2D.Double rectangulomiNave = new Rectangle2D.Double();
        
        rectanguloDisparo.setFrame(miDisparo.x,
                                           miDisparo.y,
                                           miDisparo.imagen.getWidth(null),
                                           miDisparo.imagen.getHeight(null));
        
        rectangulomiNave.setFrame(miNave.x,
                                           miNave.y,
                                           miNave.imagen.getWidth(null),
                                           miNave.imagen.getHeight(null));
        
        for(int i = 0;i < filas; i++){
            for(int j = 0; j < columnas; j++){
                if(ListaMarciano[i][j].vivo){
                rectanguloMarciano.setFrame(ListaMarciano[i][j].x,
                                           ListaMarciano[i][j].y,
                                           ListaMarciano[i][j].imagen1.getWidth(null),
                                           ListaMarciano[i][j].imagen1.getHeight(null));
                if(rectanguloDisparo.intersects(rectanguloMarciano)){
                    ListaMarciano[i][j].vivo = false;
                    miDisparo.y = 2000;
                    miDisparo.disparado = false;
                    
                }
                
                if(rectanguloMarciano.intersects(rectangulomiNave)){
                    gameOver = true;
                }
            }
            }
        }
       
    }
    
    
    private void cambiaDireccionMarcianos (){
        for(int i = 0;i < filas; i++){
            for(int j = 0; j < columnas; j++){
                ListaMarciano[i][j].y += 10;
                ListaMarciano[i][j].setvX(ListaMarciano[i][j].getvX() * -1);
            }        
        }
    }
    
    private void pintaMarcianos(Graphics2D _g2){
        
        int anchoMarciano = ListaMarciano[0][0].imagen1.getWidth(null);
        
        
        for(int i = 0;i < filas; i++){
            for(int j = 0; j < columnas; j++){
                if(ListaMarciano[i][j].vivo){
                ListaMarciano [i][j].mueve();
                //chequeo si el marciano ha chocado comtra la pared para cambiar la dirección
                //de todos los marcianos
                if(ListaMarciano[i][j].x + anchoMarciano == ANCHOPANTALLA){
                    direccionMarciano = true;
                    
                }
                if(ListaMarciano[i][j].x == 0 ){
                    direccionMarciano = true;
                   
                }
                
                
                if(contador < 50){
                    
                    _g2.drawImage(ListaMarciano[i][j].imagen1, ListaMarciano[i][j].x, ListaMarciano[i][j].y, null);
                    
                }
                else{
                    if(contador < 100){
                        
                    _g2.drawImage(ListaMarciano[i][j].imagen2, ListaMarciano[i][j].x, ListaMarciano[i][j].y, null);
                        
                }
                    else{
                        contador = 0;
                    }
                }
            }
                
                
             
            }
        }
        
        if(direccionMarciano){
            cambiaDireccionMarcianos();
            direccionMarciano = false; 
        }
        
        
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 103, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 72, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch (evt.getKeyCode()){
            case KeyEvent.VK_LEFT: miNave.setPulsadoIzquierda(true);
            break ;
            case KeyEvent.VK_RIGHT: miNave.setPulsadoDerecha(true);
            break;
            case KeyEvent.VK_SPACE: miDisparo.posicionaDisparo(miNave);
            miDisparo.disparado = true;
            reproduce("/sonidos/disparo.wav");
            break;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch (evt.getKeyCode()){
            case KeyEvent.VK_LEFT: miNave.setPulsadoIzquierda(false);
            break ;
            case KeyEvent.VK_RIGHT: miNave.setPulsadoDerecha(false); 
            break;
        }
    }//GEN-LAST:event_formKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
