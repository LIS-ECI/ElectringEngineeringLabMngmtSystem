/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eci.pdsw.servicios;

import eci.pdsw.entities.EquipoComplejo;
import eci.pdsw.entities.Persona;
import eci.pdsw.entities.Prestamo;
import eci.pdsw.persistence.DAOFactory;
import eci.pdsw.persistence.DAOPrestamo;
import eci.pdsw.persistence.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2105684
 */
public class ServiciosPrestamoPersistence extends ServiciosPrestamo {

    private DAOPrestamo basePaciente;
    private DAOFactory daoF;
    
    public ServiciosPrestamoPersistence() {
    	try {
            
            InputStream input = getClass().getClassLoader().getResource("applicationconfig.properties").openStream();
            
            Properties properties=new Properties();
            properties.load(input);
            daoF = DAOFactory.getInstance(properties);
          
        } catch (IOException ex) {
            Logger.getLogger(ServiciosPrestamoPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Prestamo> consultarPrestamosMorosos() throws ExcepcionServicios {
        List<Prestamo> morosos=new LinkedList<>();
        try{
            daoF.beginSession();
            basePaciente=daoF.getDaoPrestamo();
            morosos=basePaciente.loadMorosos();
            Collections.sort(morosos);
        }catch(PersistenceException e){
            throw new ExcepcionServicios("");
        }finally{
            daoF.endSession();
            return morosos;
        }
        
    }

    @Override
    public List<Prestamo> consultarPrestamosPersona(Persona p) {
        List<Prestamo> prestamos=new LinkedList<>();
        try{
            daoF.beginSession();
            basePaciente=daoF.getDaoPrestamo();
            prestamos=basePaciente.loadByCarne(p.getCarnet());
            Collections.sort(prestamos);
        }catch(PersistenceException e){
            throw new ExcepcionServicios("");
        }finally{
            daoF.endSession();
            return prestamos;
        }
    }

    @Override
    public List<Prestamo> consultarPrestamosEquipoComplejo(EquipoComplejo ec) {
        List<Prestamo> prestamos=new LinkedList<>();
        try{
            daoF.beginSession();
            basePaciente=daoF.getDaoPrestamo();
            prestamos=basePaciente.loadByEquipoComplejo(ec);
            Collections.sort(prestamos);
        }catch(PersistenceException e){
            throw new ExcepcionServicios("");
        }finally{
            daoF.endSession();
            return prestamos;
        }
    }

    @Override
    public List<Prestamo> consultarTodos() {
        List<Prestamo> prestamos=new LinkedList<>();
        try{
            daoF.beginSession();
            basePaciente=daoF.getDaoPrestamo();
            prestamos=basePaciente.loadAll();
            Collections.sort(prestamos);
        }catch(PersistenceException e){
            throw new ExcepcionServicios("");
        }finally{
            daoF.endSession();
            return prestamos;
        }
    }
    
}
