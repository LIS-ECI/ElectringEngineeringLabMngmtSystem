/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.servicios;

/**
 *
 * @author David Useche
 */
public class ExcepcionServicios extends Exception{
    
    public static final String NOMBRE_INVALIDO="El nombre del modelo es invalido.";

    ExcepcionServicios(String message) {
        super(message);
    }

    public ExcepcionServicios(Throwable e, String localizedMessage) {
        super(localizedMessage,e);
    }
    
    
}
