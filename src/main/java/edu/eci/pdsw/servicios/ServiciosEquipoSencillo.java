/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.servicios;

import edu.eci.pdsw.entities.EquipoSencillo;
import java.util.List;

/**
 *
 * @author David Useche
 */
public abstract class ServiciosEquipoSencillo {

    private static ServiciosEquipoSencillo instance = new ServiciosEquipoSencilloPersistence();

    public static ServiciosEquipoSencillo getInstance() {
        return instance;
    }
    
    /**
     * Registra un equipo sencillo
     * @param equipo a registrar
     * @throws ExcepcionServicios si el equipo ya esta registrado 
     */
    public abstract void registrarEquipoSencillo(EquipoSencillo equipo) throws ExcepcionServicios;

    /**
     * Consulta un equipo sencillo desde el nombre
     * @param nombre del equipo que se quiere consultar
     * @return El equipo sencillo que tiene el nombre
     * @throws ExcepcionServicios Si el Equipo sencillo con el nombre no esta registrado
     */
    public abstract EquipoSencillo consultarPorNombre(String nombre) throws ExcepcionServicios;

    /**
     * Consulta todos los equipos sencillos
     * @return Una lista con todos los equipos sencillos
     * @throws ExcepcionServicios Si existe algun error consultando
     */
    public abstract List<EquipoSencillo> consultarTodo() throws ExcepcionServicios;

    /**
     * Consulta los elementos que se pueden prestar de un equipo a partir del nombre
     * @param nombre del equipo
     * @return La cantidad de elementos que se pueden prestar
     * @throws ExcepcionServicios Si el equipo con ese nombre no esta registrado
     */
    public abstract EquipoSencillo ConsultarDisponibilidadPorNombre(String nombre) throws ExcepcionServicios;

    /**
     * Actualiza un equipo sencillo
     * @param equipo actualizado que se va a guardar de nuevo
     * @throws ExcepcionServicios si el equipo no esta registrado
     */
    public abstract void actualizar(EquipoSencillo equipo) throws ExcepcionServicios;
    
    /**
     * Consulta la cantidad disponible de un equipo sencillo 
     * para hacer prestamos
     * @param nombre del equipo a consultar
     * @return la cantidad que se puede prestar
     * @throws ExcepcionServicios si el equipo sencillo no existe
     */
    public abstract int consultarCantidadDisponibleEqSencillo(String nombre) throws ExcepcionServicios;
    
    /**
     * Busca un equipo sencillo por su nombre aproximado
     * 
     * @param busqueda a hacer
     * @return una lista con los equiṕos sencillos que cumplen la condicion
     * @throws ExcepcionServicios si la cadena esta vacia
     */
    public abstract List<String> consultarPorNombreAproximado(String busqueda) throws ExcepcionServicios;
}
