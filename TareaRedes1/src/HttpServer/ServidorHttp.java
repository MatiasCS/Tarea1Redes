/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package HttpServer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matias
 */
public class ServidorHttp implements Runnable{
    
    //Variables Estaticas
    static final int puerto = 8080;
    static final File directorio_raiz = new File(".");
    static final String inicio = "index.html";
    
    Socket conexion;

    public ServidorHttp(Socket conexion){
        this.conexion = conexion;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            //Creacion del servidor y espera de clientes
            ServerSocket servidor = new ServerSocket(puerto);
            while(true){
                ServidorHttp cliente = new ServidorHttp(servidor.accept());
                Thread hebra = new Thread(cliente);
                hebra.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorHttp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void run() {
        // Mensaje cliente, archivo que se pide, metodo POST o GET
        BufferedReader entradacliente;
        String archivoPedido;
        String metodo;
        BufferedOutputStream salidaArchivo = null;
        try {
            //Lectura mensaje enviado por el cliente
            entradacliente = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String entrada = entradacliente.readLine();
            StringTokenizer token = new StringTokenizer(entrada);
            metodo = token.nextToken();
            archivoPedido = token.nextToken();
            salidaArchivo = new BufferedOutputStream(conexion.getOutputStream());
            
            if(archivoPedido.equals("/"))
                archivoPedido += inicio;
            
            File archivo = new File(directorio_raiz,archivoPedido);
            int pesoArchivo = (int) archivo.length();
            
            //Implementacion GET
            if(metodo.equals("GET")){
                FileInputStream stream;
                byte[] buffer = new byte[pesoArchivo];
               
                stream = new FileInputStream(archivo);
                stream.read(buffer);
                
                salidaArchivo.write(buffer,0,pesoArchivo);
                salidaArchivo.flush();
            }
          
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (IOException ex) {
            Logger.getLogger(ServidorHttp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
