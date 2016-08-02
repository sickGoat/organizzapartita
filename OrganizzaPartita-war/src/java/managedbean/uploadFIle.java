/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import net.coobird.thumbnailator.Thumbnails;

/**
 *
 * @author antonio
 */
public class uploadFIle implements Serializable{
    
    
    private static final String GESTORE_PATH = "/home/antonio/webapp/images/campi/";
    private static final String GIOCATORE_PATH = "/home/antonio/webapp/images/";
    private Part file;
    private List<Part> files  = new ArrayList<Part>();
    
    /**
     * Creates a new instance of uploadFIle
     */
    public uploadFIle() {
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
    
    public List<Part> getFiles() {
        return files;
    }

    public void setFiles(List<Part> files) {
        this.files = files;
    }
    
    public void uploadSingleFile(String username){
        
        try{
            salvaFotoProfilo(username);
        }catch(Exception ex){
            System.out.println("e = " + ex.getCause());
        }
        
    }
    
    public void saveIntoList(AjaxBehaviorEvent e) throws ServletException, IOException{
        
        System.out.println("SAve into list" );
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();
        for (Part p : request.getParts()){
            if ( p.getContentType() != null )
                if( p.getContentType().startsWith("image"))
                        files.add(p);
        }    
        
    }
    
    public void uploadAllFiles(String nome_cartella) throws IOException{
        System.out.println("upload all files  " );
        System.out.println("nome_cartella = " + nome_cartella);
        System.out.println("files.isEmpty() = " + files.isEmpty());
        int count = 0;
        if ( !files.isEmpty() ){
            for ( Part p : files ){
                System.out.println("p = " + p.getName());
                salvaImmagineCampo(p,nome_cartella,count);
                count++;
            }
        }
    }
    
    private void salvaImmagineCampo(Part part,String nome_cartella,int count) throws FileNotFoundException, IOException{
            
            FileOutputStream stream = null;   
            File f = new File(GESTORE_PATH+"/"+nome_cartella);
            if ( !f.exists())
                f.mkdir();
            File nuovo_file = new File(f, Integer.toString(count));
            Files.copy(part.getInputStream(), nuovo_file.toPath());

    }
    
    private void salvaFotoProfilo(String username) throws FileNotFoundException, IOException{
        
        File uplatd = new File(GIOCATORE_PATH+"/"+username);
        if (uplatd.exists())
            uplatd.mkdir();
        
        File foto_profilo = new File(uplatd,username);
        Files.copy(file.getInputStream(), foto_profilo.toPath());
        
            
    }
    
}
