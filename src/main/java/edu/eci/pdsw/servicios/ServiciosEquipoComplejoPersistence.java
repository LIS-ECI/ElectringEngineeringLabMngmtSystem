/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.servicios;

import edu.eci.pdsw.entities.EquipoComplejo;
import edu.eci.pdsw.entities.EquipoException;
import edu.eci.pdsw.entities.Modelo;
import edu.eci.pdsw.log.Registro;
import edu.eci.pdsw.persistence.DAOEquipoComplejo;
import edu.eci.pdsw.persistence.DAOFactory;
import edu.eci.pdsw.persistence.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David Useche
 */
public class ServiciosEquipoComplejoPersistence extends ServiciosEquipoComplejo {

    private final DAOFactory dao;
    private DAOEquipoComplejo complejoPersistencia;

    public ServiciosEquipoComplejoPersistence() {
        InputStream input = null;
        Properties properties = new Properties();
        try {
            input=ServiciosEquipoComplejoPersistence.class.getClassLoader().getResource("applicationconfig.properties").openStream();
            properties.load(input);
        } catch (IOException ex) {
            Registro.anotar(ex);
        }
        dao = DAOFactory.getInstance(properties);
    }

    @Override
    public void registrarModelo(Modelo model) throws ExcepcionServicios {
        try {
            dao.beginSession();
            complejoPersistencia=dao.getDaoEquipoComplejo();
            complejoPersistencia.saveModelo(model);
            dao.commitTransaction();
            dao.endSession();
        } catch (PersistenceException  ex) {
            dao.rollbackTransaction();
            throw new ExcepcionServicios(ex,ex.getLocalizedMessage());
        }
    }

    @Override
    public void registrarEquipoComplejo(EquipoComplejo equipo) throws ExcepcionServicios {
        try {
            dao.beginSession();
            complejoPersistencia=dao.getDaoEquipoComplejo();
            complejoPersistencia.save(equipo);
            dao.commitTransaction();
            dao.endSession();
        } catch (PersistenceException ex) {
            dao.rollbackTransaction();
            throw new ExcepcionServicios(ex,ex.getLocalizedMessage());
        }
    }

    @Override
    public List<EquipoComplejo> consultarTodo() throws ExcepcionServicios {
        ArrayList<EquipoComplejo> ans=new ArrayList<>();
        try {
            dao.beginSession();
            complejoPersistencia=dao.getDaoEquipoComplejo();
            ans=complejoPersistencia.loadAll();
            dao.endSession();
        } catch (PersistenceException ex) {
            throw new ExcepcionServicios(ex,ex.getLocalizedMessage());
        }
        return ans;
    }

    @Override
    public List<EquipoComplejo> consultarPorModelo(String modelo) throws ExcepcionServicios {
        ArrayList<EquipoComplejo> ans=new ArrayList<>();
        try {
            dao.beginSession();
            complejoPersistencia=dao.getDaoEquipoComplejo();
            ans=complejoPersistencia.loadByModelo(modelo);
            dao.endSession();
        } catch (PersistenceException ex) {
            throw new ExcepcionServicios(ex,ex.getLocalizedMessage());
        }
        return ans;
    }

    @Override
    public EquipoComplejo consultarPorPlaca(String numPlaca) throws ExcepcionServicios {
        EquipoComplejo ans;
        try {
            dao.beginSession();
            complejoPersistencia=dao.getDaoEquipoComplejo();
            ans=complejoPersistencia.load(numPlaca);
            dao.commitTransaction();
            dao.endSession();
            return ans;
        } catch (PersistenceException ex) {
            throw new ExcepcionServicios(ex,ex.getLocalizedMessage());
        }
    }

    @Override
    public EquipoComplejo consultarPorSerial(String modelo,String serial) throws ExcepcionServicios {
        EquipoComplejo ans;
        try {
            dao.beginSession();
            complejoPersistencia=dao.getDaoEquipoComplejo();
            ans=complejoPersistencia.load(modelo,serial);
            dao.endSession();
            return ans;
        } catch (PersistenceException ex) {
            throw new ExcepcionServicios(ex,ex.getLocalizedMessage());
        }
    }

    @Override
    public List<EquipoComplejo> consultarDisponibles() throws ExcepcionServicios {
        ArrayList<EquipoComplejo> ans=new ArrayList<>();
        try {
            dao.beginSession();
            complejoPersistencia=dao.getDaoEquipoComplejo();
            ans=complejoPersistencia.loadDisponibles();
            dao.endSession();
        } catch (PersistenceException ex) {
            throw new ExcepcionServicios(ex,ex.getLocalizedMessage());
        }
        return ans;
    }

    @Override
    public void actualizarEquipo(EquipoComplejo toUpdate) throws ExcepcionServicios {
        try {
            dao.beginSession();
            complejoPersistencia=dao.getDaoEquipoComplejo();
            complejoPersistencia.update(toUpdate);
            dao.commitTransaction();
            dao.endSession();
        } catch (PersistenceException ex) {
            dao.rollbackTransaction();
            throw new ExcepcionServicios(ex,ex.getLocalizedMessage());
        }
    }

    @Override
    public Modelo consultarModelo(String nombre) throws ExcepcionServicios {
        if(nombre.length()==0)throw new ExcepcionServicios(ExcepcionServicios.NOMBRE_INVALIDO);
        try {
            dao.beginSession();
            complejoPersistencia=dao.getDaoEquipoComplejo();
            Modelo ans=complejoPersistencia.loadModelo(nombre);
            dao.commitTransaction();
            dao.endSession();
            return ans;
        } catch (PersistenceException ex) {
            throw new ExcepcionServicios(ex,ex.getLocalizedMessage());
        }
    }

    @Override
    public List<String> consultarAproximado(String cadena) throws ExcepcionServicios{
        if(cadena.length()==0)throw new ExcepcionServicios("Cadena de longitud invalida");
        dao.beginSession();
        complejoPersistencia=dao.getDaoEquipoComplejo();
        List<String> ans=complejoPersistencia.loadModelosAproximados(cadena);
        dao.endSession();
        return ans;
    }

    @Override
    public List<EquipoComplejo> consultarEnAlmacenModelo(String modelo) throws ExcepcionServicios {
        if(modelo.length()==0) throw new ExcepcionServicios("Modelo invalido");
        List<EquipoComplejo> ans=new ArrayList<>();
        try{
        dao.beginSession();
        complejoPersistencia=dao.getDaoEquipoComplejo();
        ans=complejoPersistencia.loadEnAlmacenByModelo(modelo);
        dao.commitTransaction();
        dao.endSession();
        }catch(PersistenceException ex){
            throw new ExcepcionServicios(ex,ex.getLocalizedMessage());
        }
        return ans;
    }

    @Override
    public EquipoComplejo consultarEquipoEnAlmacenPorPlaca(String placa) throws ExcepcionServicios {
        if(placa.length()==0) throw new ExcepcionServicios("Placa invalida");
        EquipoComplejo eq=null;
        try{
            dao.beginSession();
            complejoPersistencia=dao.getDaoEquipoComplejo();
            eq=complejoPersistencia.loadEnAlmacenPorPlaca(placa);
            dao.commitTransaction();
            dao.endSession();
        }catch(PersistenceException ex){
            throw new ExcepcionServicios(ex,ex.getLocalizedMessage());
        }
        return eq;
    }

    @Override
    public Date currDate() throws ExcepcionServicios {
        dao.beginSession();
        complejoPersistencia=dao.getDaoEquipoComplejo();
        Date fecha=complejoPersistencia.currDate();
        dao.endSession();
        if(fecha==null)
            throw new ExcepcionServicios("No se ha podido calcular la fecha actual");
        return fecha;
    }

}
