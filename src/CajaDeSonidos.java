/*
	Esta es la ventana principal que reproduce el sonido y se comunica con las clases GridSonidos y AgregarSonido
    Copyright (C) 2017  JuanMX

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javazoom.jl.player.Player;
import javazoom.jl.decoder.JavaLayerException;

public class CajaDeSonidos extends JFrame{
    //crea un objeto de una clase interna anonima que se agraga como escuhador a los botones, esta clase esta en este archivo	
    ManejadorBoton manejadorBotones = new ManejadorBoton();
    config con = new config();
    
    private JButton boton[]; // en este arreglo estan los botones que se ven en la cuadricula
    private String origenSonido[]; // en este arreglo esta la ruta al sonido asociado a cada boton
    
    private JButton botonModificar, botonAgregar;// son los botones modificar y agregar que se encuentran por defecto en el programa
    
    private int botonesSonidos = 0, botonesMod = 2, totalBotones = 2;
            
    private GridLayout gridCentral;
    
    private JPanel panelCentral;


    public CajaDeSonidos() throws IOException{
        super("Caja de sonidos << BETA >>");
        
        botonModificar = new JButton("Modificar botones");
        botonModificar.setToolTipText("Modifica las propiedades de los botones");
        
        botonAgregar = new JButton("Nuevo boton");
        botonAgregar.setToolTipText("Agrega un nuevo boton a la cuadricula");
        
        //si se encuentra el archivo config.config carga los botones segun el sistema operativo
        if (!(con.getConfig("Botones").equals("NO_config.config"))){
            botonesSonidos = Integer.parseInt(con.getConfig("Botones"));
            
            if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
                boton = con.getBotonesWindows();
                origenSonido = con.getOrigenSonidosWindows();
            }
            else{
                boton = con.getBotonesUnix();
                origenSonido = con.getOrigenSonidosUnix();
            }
            
            totalBotones = botonesMod + botonesSonidos;
        }
        else{
            JOptionPane.showMessageDialog( null, "Parece que hay un problema al leer el archivo config.config o no se encuentran las carpetas Imagenes y Sonidos","AVISO", JOptionPane.WARNING_MESSAGE );
        }
        
        //los parametros del GridLayout es la manera de como se redimenciona automaticamente la cuadricula al quitar o poner botones
        gridCentral = new GridLayout( (int) Math.round(Math.sqrt(totalBotones)), (int) Math.ceil(Math.sqrt(totalBotones)),2,2 );
        
        panelCentral = new JPanel(); 
        panelCentral.setLayout(gridCentral);
        
        // si se encuentra el archivo config.config pone los botones en la ventana con su escuchador
        if (!(con.getConfig("Botones").equals("NO_config.config"))){
            for(int i=0; i < botonesSonidos; i++){
                if (!(origenSonido[i].equals("NADA"))){
                    panelCentral.add (boton[i]);
                    boton[i].addActionListener(manejadorBotones);
                }
            }
        }
        
        //ponel los botones modificar y agregar a la ventana
        panelCentral.add(botonModificar);
        panelCentral.add(botonAgregar);
        add( panelCentral, BorderLayout.CENTER);
        botonModificar.addActionListener(manejadorBotones);
        botonAgregar.addMouseListener( new ManejadorClick() );
        botonAgregar.addActionListener(manejadorBotones);
    }
    
 
// estos dos metodos estan hechos para desactivar los botones cuando se reproduce un sonido, es una posible mejora a futuro
public void desactivarBotones(){
    for (int i=0;i<botonesSonidos;i++){
        boton[i].setEnabled(false);
    }
}
public void activarBotones(){
    for (int i=0;i<botonesSonidos;i++){
        boton[i].setEnabled(true);
    }
}

public static void main(String[] args) throws IOException {
/*Este try-catch es para poner a la interfaz grafica una "skin" de "nimbus" (hace que se vea "bonita" la interfaz).
        Esta manera de hacerlo se encuentra en la pagina de oracle
        */
    try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
            }
    } 
    catch (Exception e) {
    // If Nimbus is not available, you can set the GUI to another look and feel.
    }

    CajaDeSonidos sistema = new CajaDeSonidos();
    sistema.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    sistema.setSize( 900, 700 );
    sistema.setVisible( true );
    sistema.setLocationRelativeTo(null);

}

    private class ManejadorClick extends MouseAdapter{
        public void mouseClicked( MouseEvent evento ){
            
            //SwingUtilities.isRightMouseButton(evento) 
            //SwingUtilities.isLeftMouseButton(evento)
            //Scroll click
            if (evento.isAltDown()/*SwingUtilities.isMiddleMouseButton(evento)*/ ){             
                JOptionPane.showMessageDialog( null, "Al menos la primer version de esta caja de sonidos su ubica en:\n\ngithub.com/JuanMX/caja-de-sonidos\n\nCon una licencia GPL-3.0","Soy un mensaje escondido !", JOptionPane.WARNING_MESSAGE );
            }
            //else if  ( evento.isMetaDown() ){}
            
            /*
            else{
                AgregarSonido nuevoSonido = new AgregarSonido();
            }*/
        }
    }


    private class ManejadorBoton implements ActionListener{

        public void actionPerformed( ActionEvent evento ){
            
            //recorre el arreglo de botones buscando cual se presiono para reproducir el sonido, no es tan tardado como se pensaria
            for(int i=0; i < botonesSonidos; i++){
                desactivarBotones();
                if (evento.getSource() == boton[i]){
                    
                    try{
                        /*
                        // esta es una manera de reproducir un sonido que al final no se uso
                        InputStream ubicacionSonido;
                        ubicacionSonido = getClass().getResourceAsStream(origenSonido[i] );
                        Player player;
                        BufferedInputStream bufferEntrada = new BufferedInputStream(ubicacionSonido);
                        player = new Player(bufferEntrada);
                        player.play();
                        */
                        File file = new File (origenSonido[i]);
                        FileInputStream fileInput = new FileInputStream(file.getCanonicalPath());
                        BufferedInputStream bufferInput = new BufferedInputStream(fileInput);
                        
                        Player player = new Player (bufferInput);
                        player.play();
                        
                            
                    }
                    catch(JavaLayerException e){
                        e.printStackTrace();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(CajaDeSonidos.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(CajaDeSonidos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                activarBotones();
            }
            //evento para los botones modificar y agregar botones
            
            if (evento.getSource()==botonAgregar){
                desactivarBotones();
                AgregarSonido nuevoSonido = new AgregarSonido();
                activarBotones();
            }
            
            if (evento.getSource()==botonModificar){
                //String nada[] = null;
                desactivarBotones();
                try {
                    GridSonidos grid = new GridSonidos();
                    //main(nada);
                    //System.exit(1);
                } catch (IOException ex) {
                    Logger.getLogger(CajaDeSonidos.class.getName()).log(Level.SEVERE, null, ex);
                }
                activarBotones();
            }
        }
    }
}
