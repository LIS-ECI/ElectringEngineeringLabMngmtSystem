/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.managedbeans;

import edu.eci.pdsw.entities.EquipoComplejo;
import edu.eci.pdsw.entities.Prestamo;
import edu.eci.pdsw.log.Registro;
import edu.eci.pdsw.servicios.ExcepcionServicios;
import edu.eci.pdsw.servicios.ServiciosEquipoComplejo;
import edu.eci.pdsw.servicios.ServiciosPrestamo;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author amoto
 */
@ManagedBean(name = "FichaEquipo")
@SessionScoped
public class FichaEquipoManagedBean implements Serializable {

    private String placaAConsultar;
    private ServiciosEquipoComplejo SEC = ServiciosEquipoComplejo.getInstance();
    private ServiciosPrestamo SEP = ServiciosPrestamo.getInstance();
    private EquipoComplejo consultado;
    private boolean consulto;
    private Timestamp fecha;

    /**
     * Consulta un equipo por placa, se usa la paca guardada
     */
    public void ConsultarEquipoByPlaca() {
        if (getPlacaAConsultar() != null && getPlacaAConsultar().length() > 0) {

            try {
                setConsultado(SEC.consultarPorPlaca(getPlacaAConsultar()));
            } catch (ExcepcionServicios ex) {
                facesError(ex.getMessage());
            }
            if (getConsultado() != null) {
                setConsulto(true);
                setFecha(currDate());
            } else {
                facesError("La placa no corresponde a ningún equipo registrado");
            }
        } else {
            facesError("Por favor coloque una placa para consultar");
        }
    }

    /**
     * Consulta los prestamos de un equipo
     *
     * @return los prestamos de un equipo
     */
    public List<Prestamo> consultarPrestamosEquipo() {
        List<Prestamo> lista = null;
        try {
            lista = SEP.consultarPrestamosEquipoComplejo(consultado);
        } catch (ExcepcionServicios ex) {
            Registro.anotar(ex);
            facesFatal("Ups! ha ocurrido un error inesperado");
        }
        return lista;
    }

    /**
     * Muestra un mensaje de error en la vista
     *
     * @param message Mensaje de error
     */
    private void facesError(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + message, null));
    }

    /**
     * Muestra un mensaje de informacion en la vista
     *
     * @param message Mensaje de informativo
     */
    public void facesInfo(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    /**
     * Muestra un mensaje de alarma en la vista
     *
     * @param message Mensaje de Alarma
     */
    public void facesWarn(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
    }

    /**
     * Muestra un mensaje de error grave en la vista
     *
     * @param message Mensaje fatal
     */
    public void facesFatal(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, message, null));
    }

    /**
     * @return the placaAConsultar
     */
    public String getPlacaAConsultar() {
        return placaAConsultar;
    }

    /**
     * @param placaAConsultar the placaAConsultar to set
     */
    public void setPlacaAConsultar(String placaAConsultar) {
        this.placaAConsultar = placaAConsultar;
    }

    /**
     * @return the consulto
     */
    public boolean isConsulto() {
        return consulto;
    }

    /**
     * @param consulto the consulto to set
     */
    public void setConsulto(boolean consulto) {
        this.consulto = consulto;
    }

    /**
     * @return the consultado
     */
    public EquipoComplejo getConsultado() {
        return consultado;
    }

    /**
     * @param consultado the consultado to set
     */
    public void setConsultado(EquipoComplejo consultado) {
        this.consultado = consultado;
    }

    public Timestamp currDate() {
        Timestamp now = null;
        try {
            now = SEP.currDate();
        } catch (ExcepcionServicios ex) {
            Registro.anotar(ex);
            facesFatal("Ups! ha ocurrido un error inesperado");
        }
        return now;
    }

    /**
     * @return the fecha
     */
    public Timestamp getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

}
