/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.persistence;

import edu.eci.pdsw.entities.EquipoComplejo;
import edu.eci.pdsw.entities.EquipoSencillo;
import edu.eci.pdsw.entities.Prestamo;
import edu.eci.pdsw.entities.PrestamoException;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Hugo Alvarez
 */
public interface DAOPrestamo {

    /**
     * Obj: Carga los prestamos de un estudiante identificado por el carne en
     * una fecha dada. pre: la fecha(no debe ser mayor a la actual) y el
     * carne(no debe ser null) del estudiante para saber que prestamos realizo
     * el estudiante pos: los prestamos del estudiante en la fecha dada
     *
     * @param fecha, la fecha a revisar
     * @param carne, el carne a revisar
     * @return la lista de prestamos del estudiante
     * @throws PersistenceException Hubo un error al cargar los prestamos
     */
    public abstract Prestamo load(Timestamp fecha, String carne) throws PersistenceException;

    /**
     * Obj: Guarda un prestamo. pre: el prestamo a guardar, no debe ser null
     *
     * @param prestamo, el prestamo a guardar
     * @throws PersistenceException Hubo un error al guardar el prestamo
     */
    public abstract void save(Prestamo prestamo) throws PersistenceException;

    /**
     * Obj: Actualiza un prestamo dado. pre: el prestamo, no debe ser null
     *
     * @param prestamo, el prestamo a actualizar
     * @throws PersistenceException Hubo un error al actualizar el prestamo
     */
    public abstract void update(Prestamo prestamo) throws PersistenceException;

    /**
     * Obj: carga todos los prestamos de la base de datos. pos: la lista de
     * Prestamos de la base de datos.
     *
     * @return La lista de Prestamos que estan en la base de datos
     * @throws PersistenceException Hubo un error al cargar los prestamos
     */
    public abstract List<Prestamo> loadAll() throws PersistenceException;

    /**
     * Obj: Cargar todos los prestamos por la fecha dada. pre: la fecha a
     * revisar, no ser mayor a la fecha actual y no debe ser null pos: la lista
     * de prestamos dada la fecha
     *
     * @param fecha, la fecha dada a revisar
     * @return La lista de prestamos cargados de la base de datos
     * @throws PersistenceException Hubo un error al cargar de la base de datos
     */
    public abstract List<Prestamo> loadByFecha(Timestamp fecha) throws PersistenceException;

    /**
     * Obj: Cargar todos los prestamos por el carne dado. pre: el carne a
     * revisar, no debe ser null pos: la lista de prestamos dado el carne
     *
     * @param carne, el carne a revisar
     * @return La lista de Prestamos cargados por carne
     * @throws PersistenceException Hubo un error al cargar de la base de datos
     */
    public abstract List<Prestamo> loadByCarne(String carne) throws PersistenceException;

    /**
     * Obj: Cargar todos los prestamos que estan morosos. pos: la lista de
     * prestamos morosos
     *
     * @return la lista de prestamos que estan en mora
     * @throws PersistenceException Hubo un error al cargar de la base de datos
     */
    public abstract List<Prestamo> loadMorosos() throws PersistenceException;

    /**
     * Obj: Cargar todos los prestamos dado el EquipoComplejo. pre: el
     * EquipoComplejo, no debe ser null pos: la lista de prestamos dada la fecha
     *
     * @param equipocomplejo, el EquipoComplejo a revisar
     * @return la lista de prestamos que se obtuvo por el EquipoComplejo
     * @throws PersistenceException Hubo un error al cargar de la base de datos
     */
    public abstract List<Prestamo> loadByEquipoComplejo(EquipoComplejo equipocomplejo) throws PersistenceException;

    
    /**
     * Carga la fecha actual de la base de datos
     * @return la fecha actual de la base de datos
     */
    public abstract Timestamp currDate();
    
    /**
     * Carga el prestamo actual de un equipo
     * 
     * @param equipo a buscar
     * @return prestamo actual de ese equipo
     * @throws PersistenceException si no hay prestamos activos o el equipo no existe
     */
    public Prestamo loadPrestamoActual(EquipoComplejo equipo)throws PersistenceException;
    
    /**
     * Calcula la diferencia en horas entre la fecha actual y la fecha de fin estimada del prestamo
     * 
     * @param prestamo el prestamo que se usará para consultar
     * @return la diferencia en horas entre la fecha actual y la fecha de fin estimada del prestamo
     */
    public long diffHours(Prestamo prestamo);
    
    /**
     * Carga el prestamo actual de una persona
     * 
     * @param carnet de la persona
     * @return el prestamo activo de esa persona
     * @throws PersistenceException si la persona no existe o no tiene prestamo activo
     */
    public Prestamo loadActualPersona(String carnet)throws PersistenceException;
}
