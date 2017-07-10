/*
	Solo muestra una ventana con los botones, al presionar uno se comunica con la clase ModificaPropiedades
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GridSonidos extends JFrame{
    //crea un objeto de una clase interna anonima que se agraga como escuhador a los botones, esta clase esta en este archivo	
    ManejadorBoton manejadorBotones = new ManejadorBoton();
    config con = new config();
    private JButton boton[];
    private String origenSonido[];
    private String origenImagen[];
    private JButton botonModificar, botonAgregar;
    private String nombre="", tip="";
    
    private int totalBotones = 2, botonesSonidos = 0, botonesMod = 2;
            
    private GridLayout gridCentral;
    
    private JPanel panelCentral;

    // es la interfaz grafica parecida a la caja de sonidos solo que se usa un arreglo con las rutas a la imagen
    // ademas el escuchador de los botones envia los paramateros de los botones a otra clase para ser modificados
    public GridSonidos() throws IOException{
        super("SELECCIONE EL BOTON A MODIFICAR");
        
        botonModificar = new JButton("Modificar botones");
        botonModificar.setToolTipText("Modifica las propiedades de los botones");
        botonModificar.setEnabled(false);
        
        botonAgregar = new JButton("Nuevo boton");
        botonAgregar.setToolTipText("Agrega un nuevo boton a la cuadricula");
        botonAgregar.setEnabled(false);
        
        //si se encuentra el archivo config.config carga los botones segun el sistema operativo
        if (!(con.getConfig("Botones").equals("NO_config.config"))){
            botonesSonidos = Integer.parseInt(con.getConfig("Botones"));
            
            if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
                boton = con.getBotonesWindows();
                origenSonido = con.getOrigenSonidosWindows();
                origenImagen = con.getOrigenImagenesWindows();
            }
            else{
                boton = con.getBotonesUnix();
                origenSonido = con.getOrigenSonidosUnix();
                origenImagen = con.getOrigenImagenesUnix();
            }
            
            totalBotones = botonesMod + botonesSonidos;
        }
        
        gridCentral = new GridLayout( (int) Math.round(Math.sqrt(totalBotones)), (int) Math.ceil(Math.sqrt(totalBotones)),2,2 );
        
        panelCentral = new JPanel(); 
        panelCentral.setLayout(gridCentral);
        
        if (!(con.getConfig("Botones").equals("NO_config.config"))){
            for(int i=0; i < botonesSonidos; i++){
                if (!(origenSonido[i].equals("NADA"))){
                    panelCentral.add (boton[i]);
                    boton[i].addActionListener(manejadorBotones);
                }
            }
        }
        
        panelCentral.add(botonModificar);
        panelCentral.add(botonAgregar);
        add( panelCentral, BorderLayout.CENTER);
        
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );   
        setSize( 900, 700 );
        setVisible( true );
        setLocationRelativeTo(null);
    }
        
    private class ManejadorBoton implements ActionListener{

        public void actionPerformed( ActionEvent evento ){
            //recorre el arreglo de botones buscando cual se presiono para mandar sus propiedades a otra clase, no es tan tardado como se pensaria
            for(int i = 0; i < botonesSonidos; i++){
                if (evento.getSource() == boton[i]){
                    try {
                        
                        nombre = con.getConfig("Nombre"+Integer.toString(i));
                        tip = con.getConfig("Tooltip"+Integer.toString(i));
                        ModificaPropiedades newprops = new ModificaPropiedades(origenSonido[i], nombre, origenImagen[i], tip, Integer.toString(i));
                    } catch (IOException ex) {
                        Logger.getLogger(GridSonidos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        }
    }
    
}
