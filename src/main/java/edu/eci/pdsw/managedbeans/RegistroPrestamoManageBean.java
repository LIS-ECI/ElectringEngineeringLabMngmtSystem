/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.managedbeans;

import edu.eci.pdsw.entities.EquipoComplejo;
import edu.eci.pdsw.entities.EquipoSencillo;
import edu.eci.pdsw.entities.Persona;
import edu.eci.pdsw.entities.Prestamo;
import edu.eci.pdsw.entities.PrestamoException;
import edu.eci.pdsw.entities.PrestamoIndefinido;
import edu.eci.pdsw.entities.PrestamoTerminoFijo;
import edu.eci.pdsw.entities.Rol;
import edu.eci.pdsw.log.Registro;
import edu.eci.pdsw.servicios.ExcepcionServicios;
import edu.eci.pdsw.servicios.ServiciosEquipoComplejo;
import edu.eci.pdsw.servicios.ServiciosEquipoComplejoPersistence;
import edu.eci.pdsw.servicios.ServiciosEquipoSencillo;
import edu.eci.pdsw.servicios.ServiciosPrestamo;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author German Lopez
 */
@ManagedBean(name = "RegistroPrestamo")
@SessionScoped
public class RegistroPrestamoManageBean implements Serializable {

    private final ServiciosPrestamo PRESTAMO;
    private final ServiciosEquipoComplejo EQCOMPLEJO;
    private final ServiciosEquipoSencillo EQSENCILLO;
    //Atributos de prestamo
    private Timestamp fechaEstimadaDeEntrega;
    //Consultar persona
    private String carne;
    //Equipo Sencillo prestamo termino fijo
    private EquipoSencillo selectEquipoSencillo;

    //Consultar equipo sencillo por nombre prestamo termino fijo
    private String nombre;
    //Cantidad de equipo sencillo de prestamo termino fijo
    private int cantidad;

    //Atributos de prestamo
    //Prestamo termino fijo
    private Set<EquipoComplejo> equiposComplejosPrestados;
    private Set<EquipoSencillo> equiposSencillosPrestados;

    private List<Integer> equiposSencillosPrestadosCantidad;
    private Persona elQuePideElPrestamo;
    private int tipo_prestamo;
    //Pantallas
    private boolean showPanelRegistro = false;
    private boolean showPanelRegistrado = false;
    private boolean showPanelPersona = true;
    private boolean showPanelInfo = false;
    private boolean showPanelOtroRegistro=false;
    private boolean showPanelMoroso=true;
    private String selectEqSe;

    //Lista de equipo sencillo para consultar los equipos prestamo termino fijo
    private List<EquipoSencillo> eqS;
    //Mapa de los tiempos que posee un prestamo, equipo complejo prestamo termino fijo
    private Map<String, String> tipoPrestamo;
    //Tiempo de vida del prestamo, equipo complejo prestamo termino fijo
    private String fechaTipoPrestamo;
    //lista para saber la cantidad de los equipos
    private List<EquipoSencillo> es;
    //cantidad disponible del equipo sencillo
    private int cantidadDisponible;
    private Prestamo prestamo;
    private String laPersona;
    private String placa;
    private String eqcompl;
    //Avisa si a la persona toca agregarle un prestamo o si se le pueden agregar equipos a uno activo que ya tiene
    private boolean pres;
    //Prestamo que ya tiene equipos prestados y se le va a agregar mas
    private Prestamo prestamoAgregarle;

    public boolean showPanelInformacion() {
        return showPanelInfo;
    }

    public List<String> modelosAproximados(String query) {
        List<String> aproximados = new ArrayList<>();
        try {
            aproximados = EQCOMPLEJO.consultarAproximado(query);
        } catch (ExcepcionServicios ex) {
            facesError(ex.getMessage());
            Registro.anotar(ex);
        }
        return aproximados;
    }
    
    public List<String> herramientasAproximadas(String query){
        List<String> aproximados = new ArrayList<>();
        try {
            aproximados = EQSENCILLO.consultarPorNombreAproximado(query);
        } catch (ExcepcionServicios ex) {
            facesError(ex.getMessage());
            Registro.anotar(ex);
        }
        return aproximados;
    }

    public RegistroPrestamoManageBean() {

        PRESTAMO = ServiciosPrestamo.getInstance();
        EQCOMPLEJO = ServiciosEquipoComplejoPersistence.getInstance();
        EQSENCILLO = ServiciosEquipoSencillo.getInstance();
        equiposComplejosPrestados = new LinkedHashSet<>();
        equiposSencillosPrestados = new LinkedHashSet<>();
        fechaTipoPrestamo = "";
        nombre="";
        placa="";
        cantidadDisponible = 0;
        fechaEstimadaDeEntrega = Prestamo.currDate();
        tipoPrestamo = new HashMap<>();
        tipoPrestamo.put(EquipoComplejo.p24h, EquipoComplejo.p24h);
        tipoPrestamo.put(EquipoComplejo.diario, EquipoComplejo.diario);
        tipoPrestamo.put(EquipoComplejo.semestre, EquipoComplejo.semestre);
        tipoPrestamo.put(EquipoComplejo.indefinido, EquipoComplejo.indefinido);
        es = new ArrayList<>();
        eqS=new ArrayList<>();
    }
    
    /**
     * Revisa si una persona tiene algún prestamo moroso
     * @param revisar
     * @return true si tiene un prestamo moroso de lo contrario false
     */
    public boolean revisarMoroso(List<Prestamo> revisar){
        boolean res=false;
        boolean ban=true;
        for(int i=0;i<revisar.size() && ban==true;i++){
            if(revisar.get(i).prestamoMoroso()){
                res=true;
                ban=false;
            }
        }
        return res;
    }

    /**
     * Consulta una persona por su carne para realizar un prestamo hacia el
     */
    public void consultarPersona() {
        try {
            registrarOtroPrestamo();
            elQuePideElPrestamo = PRESTAMO.personaCarne(carne);
            List<Prestamo> prestados=PRESTAMO.consultarPrestamosPersona(elQuePideElPrestamo.getCarnet());
            showPanelPersona=true;
            showPanelInfo=true;
            if(revisarMoroso(prestados)){
                facesError("La persona con nombre "+ elQuePideElPrestamo.getNombre() +" y con carné "+ elQuePideElPrestamo.getCarnet() +" tiene un prestamo moroso");
                showPanelRegistro=false;
                showPanelOtroRegistro=false;
                showPanelMoroso=false;
            }
            else{
                if(prestados.isEmpty()){
                    pres=true;
                    showPanelRegistro = true;
                }else{
                    boolean ban=true;
                    for(int i=0;i<prestados.size() && ban==true;i++){
                        if(prestados.get(i).prestamoActivo()){
                            showPanelOtroRegistro=true;
                            pres=false;
                            setPrestamoAgregarle(prestados.get(i));
                            /*equiposComplejosPrestados=prestamoAgregarle.getEquiposComplejosPrestados();
                            equiposSencillosPrestados=prestamoAgregarle.getEquiposSencillosPrestados();
                            for(EquipoComplejo ecp:equiposComplejosPrestados){
                                if(ecp.getEstado()!=null){
                                    fechaTipoPrestamo=ecp.getEstado();
                                    break;
                                }
                            }*/
                            fechaTipoPrestamo=prestamoAgregarle.getTipo_prestamo();
                            ban=false;                        
                        }
                    }
                    if(ban){
                        pres=true;
                        showPanelRegistro = true;
                    }
              }
            }  
        } catch (ExcepcionServicios ex) {
            facesError(ex.getMessage());
            showPanelInfo = false;

        }
    }


    public List<EquipoSencillo> sacarEqS() {
        return eqS;
    }


    /**
     * Consulta un equipo sencillo por su nombre
     */
    public void consultarEqSNombre() {
        List<EquipoSencillo> equiposS = new ArrayList<>();
        try {
            equiposS.add(EQSENCILLO.ConsultarDisponibilidadPorNombre(nombre));
        } catch (ExcepcionServicios ex) {
            facesError(ex.getMessage());
        }
        eqS = equiposS;
        if (!eqS.isEmpty()) {
            cantidadDisponibleEqs(eqS.get(0).getNombre());
        }
    }

    /**
     * Agrega equipos complejos al prestamo termino fijo
     */
    public void agregarEquipoC() {
       if(fechaTipoPrestamo== null || fechaTipoPrestamo.length()==0){
            facesError("Favor seleccionar un tipo de préstamo");
       }else{
            EquipoComplejo ec;
            try{
                ec=EQCOMPLEJO.consultarEquipoEnAlmacenPorPlaca(placa);
                equiposComplejosPrestados.add(ec);
            }catch(ExcepcionServicios ex){
                facesError(ex.getMessage());
            }             
        }
    }
    
    /**
     * Actualiza los equipos despues de haber sufrido un cambio en su estado
     *
     * @param nuevo
     */
    public void actualizarEquipoComplejo(EquipoComplejo nuevo) {
        try {
            EQCOMPLEJO.actualizarEquipo(nuevo);
        } catch (ExcepcionServicios ex) {
            facesError(ex.getMessage());
        }

    }


    /**
     * Agrega equipos sencillos al prestamo termino fijo
     */
    public void agregarEquipoS() {
        if (selectEquipoSencillo != null) {
            cantidadDisponibleEqs(selectEquipoSencillo.getNombre());
            selectEquipoSencillo.setCantidadTotal(cantidad);
            equiposSencillosPrestados.add(selectEquipoSencillo);
            consultarEqSNombre();
        }
    }

    public void cantidadDisponibleEqs(String nom) {
        try {
            cantidadDisponible = EQSENCILLO.consultarCantidadDisponibleEqSencillo(nom);
            for (EquipoSencillo eqse : equiposSencillosPrestados) {
                if (eqse.getNombre().equals(nom)) {
                    cantidadDisponible -= eqse.getCantidadTotal();
                }
            }
        } catch (ExcepcionServicios ex) {
            facesError(ex.getMessage());
        }
    }

    public void superSeteo(){
        for (EquipoComplejo equiposComplejosPrestado : equiposComplejosPrestados) {
            equiposComplejosPrestado.setEstado(fechaTipoPrestamo);
            actualizarEquipoComplejo(equiposComplejosPrestado);
        }
    }
    /**
     * Registra un prestamo dependiendo de la persona que haga el prestamo se
     * obtiene el tipo del prestamo si es indefinido o termino fijo
     */
    public void registrarPrestamo() {
        try {
            if(fechaTipoPrestamo == null || fechaTipoPrestamo.length() <= 0) {
              facesError("Debe seleccionar un tipo de prestamo para poder continuar");
            }
            fechaEstimadaDeEntrega = Prestamo.calcularFechaEstimada(fechaTipoPrestamo);
            if(pres){
                
                if (elQuePideElPrestamo.rolMasValioso().equalsIgnoreCase(Rol.estudiante) && (fechaTipoPrestamo != null || fechaTipoPrestamo.length() > 0)) {
                    superSeteo();
                    prestamo = new PrestamoTerminoFijo(elQuePideElPrestamo, equiposComplejosPrestados, equiposSencillosPrestados, fechaEstimadaDeEntrega, fechaTipoPrestamo);
                } else if (getElQuePideElPrestamo().rolMasValioso().equalsIgnoreCase(Rol.laboratorista) || getElQuePideElPrestamo().rolMasValioso().equalsIgnoreCase(Rol.profesor)) {
                    if (fechaTipoPrestamo.equalsIgnoreCase(EquipoComplejo.indefinido)) {
                        prestamo= new PrestamoIndefinido(elQuePideElPrestamo, equiposComplejosPrestados, equiposSencillosPrestados);
                    } else if(fechaTipoPrestamo != null || fechaTipoPrestamo.length() > 0){
                        superSeteo();
                        prestamo = new PrestamoTerminoFijo(elQuePideElPrestamo, equiposComplejosPrestados, equiposSencillosPrestados, fechaEstimadaDeEntrega, fechaTipoPrestamo);
                    }
                }
                PRESTAMO.registrarPrestamo(prestamo);
                facesInfo("El prestamo ha sido registrado satisfactoriamente");
                showPanelRegistro=false;
                showPanelRegistrado = true;
            }else{
                prestamoAgregarle.setEquiposComplejosPrestados(equiposComplejosPrestados);
                prestamoAgregarle.setEquiposSencillosPrestados(equiposSencillosPrestados);
                PRESTAMO.actualizarPrestamo(prestamoAgregarle);
                facesInfo("El prestamo ha sido registrado satisfactoriamente");
                showPanelOtroRegistro=false;
                showPanelRegistrado = true;
            } 
            

        } catch (PrestamoException | ExcepcionServicios ex) {
            facesError(ex.getMessage());
        } 
    }

    /**
     * Reinicia todas las variables para realizar otro prestamo
     */
    public void registrarOtroPrestamo() {
        setFechaEstimadaDeEntrega(Prestamo.currDate());
        equiposComplejosPrestados = new LinkedHashSet<>();
        equiposSencillosPrestados = new LinkedHashSet<>();
        setElQuePideElPrestamo(null);
        setPrestamo(null);
        nombre="";
        placa="";
        cantidadDisponible = 0;
        showPanelRegistro = false;
        showPanelRegistrado = false;
        showPanelOtroRegistro=false;
        showPanelMoroso=true;
        fechaTipoPrestamo = "";
        cantidad = 0;
        eqS.clear();
    }
     /**
     * @return the showPanelRegistro
     */
    public boolean showPanelRegistro() {
        return showPanelRegistro;
    }
     /**
     * @return the showPanelRegistrado
     */
    public boolean showPanelRegistrado() {
        return showPanelRegistrado;
    }

    /**
     * @return the showPanelMoroso
     */
    public boolean showPanelMoroso() {
        return showPanelMoroso;
    }
    /**
     * restriccion(); Muestra un mensaje de error en la vista
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
     * @return the fechaEstimadaDeEntrega
     */
    public Timestamp getFechaEstimadaDeEntrega() {
        return fechaEstimadaDeEntrega;
    }

    /**
     * @param fechaEstimadaDeEntrega the fechaEstimadaDeEntrega to set
     */
    public void setFechaEstimadaDeEntrega(Timestamp fechaEstimadaDeEntrega) {
        this.fechaEstimadaDeEntrega = fechaEstimadaDeEntrega;
    }

    /**
     * @return the equiposComplejosPrestados
     */
    public Set<EquipoComplejo> getEquiposComplejosPrestados() {
        return equiposComplejosPrestados;
    }

    /**
     * @param equiposComplejosPrestados the equiposComplejosPrestados to set
     */
    public void setEquiposComplejosPrestados(Set<EquipoComplejo> equiposComplejosPrestados) {
        this.equiposComplejosPrestados = equiposComplejosPrestados;
    }

    /**
     * @return the equiposSencillosPrestados
     */
    public Set<EquipoSencillo> getEquiposSencillosPrestados() {
        return equiposSencillosPrestados;
    }

    /**
     * @param equiposSencillosPrestados the equiposSencillosPrestados to set
     */
    public void setEquiposSencillosPrestados(Set<EquipoSencillo> equiposSencillosPrestados) {
        this.equiposSencillosPrestados = equiposSencillosPrestados;
    }

    /**
     * @return the equiposSencillosPrestadosCantidad2
     */
    public List<Integer> getEquiposSencillosPrestadosCantidad2() {
        return equiposSencillosPrestadosCantidad;
    }

    /**
     * @param equiposSencillosPrestadosCantidad2 the
     * equiposSencillosPrestadosCantidad2 to set
     */
    public void setEquiposSencillosPrestadosCantidad2(List<Integer> equiposSencillosPrestadosCantidad2) {
        this.equiposSencillosPrestadosCantidad = equiposSencillosPrestadosCantidad2;
    }

    /**
     * @return the elQuePideElPrestamo
     */
    public Persona getElQuePideElPrestamo() {
        return elQuePideElPrestamo;
    }

    /**
     * @param elQuePideElPrestamo the elQuePideElPrestamo to set
     */
    public void setElQuePideElPrestamo(Persona elQuePideElPrestamo) {
        this.elQuePideElPrestamo = elQuePideElPrestamo;
    }

    /**
     * @return the tipo_prestamo
     */
    public int getTipo_prestamo() {
        return tipo_prestamo;
    }

    /**
     * @param tipo_prestamo the tipo_prestamo to set
     */
    public void setTipo_prestamo(int tipo_prestamo) {
        this.tipo_prestamo = tipo_prestamo;
    }

    /**
     * @return the prestamo
     */
    public Prestamo getPrestamo() {
        return prestamo;
    }

    /**
     * @param prestamo the prestamo to set
     */
    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public void setCarne(String car) {
        this.carne = car;
    }

    public String getCarne() {
        return carne;
    }

    public void setNombre(String n) {
        this.nombre = n;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * @return the showPanelPersona
     */
    public boolean isShowPanelPersona() {
        return showPanelPersona;
    }

    public String getLaPersona() {
        return laPersona;
    }

    public void setLaPersona(String laPersona) {
        this.laPersona = laPersona;
    }

    

    public EquipoSencillo getSelectEquipoSencillo() {
        return selectEquipoSencillo;
    }

    public void setSelectEquipoSencillo(EquipoSencillo equiS) {
        this.selectEquipoSencillo = equiS;
    }
    
    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the tipoPrestamo
     */
    public Map<String, String> getTipoPrestamo() {
        return tipoPrestamo;
    }

    /**
     * @param tipoPrestamo the tipoPrestamo to set
     */
    public void setTipoPrestamo(Map<String, String> tipoPrestamo) {
        this.tipoPrestamo = tipoPrestamo;
    }

    /**
     * @return the fechaTipoPrestamo
     */
    public String getFechaTipoPrestamo() {
        return fechaTipoPrestamo;
    }

    /**
     * @param fechaTipoPrestamo the fechaTipoPrestamo to set
     */
    public void setFechaTipoPrestamo(String fechaTipoPrestamo) {
        this.fechaTipoPrestamo = fechaTipoPrestamo;
    }

    public String getSelectEqSe() {
        return selectEqSe;
    }

    public void setSelectEqSe(String selectEqSe) {
        this.selectEqSe = selectEqSe;
    }

    //////////////////////////Devolucion
    

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /**
     * @return the cantidadDisponible
     */
    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    /**
     * @param cantidadDisponible the cantidadDisponible to set
     */
    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getEqcompl() {
        return eqcompl;
    }

    public void setEqcompl(String eqcompl) {
        this.eqcompl = eqcompl;
    }
    
    public boolean getShowPanelOtroRegistro(){
        return showPanelOtroRegistro;
    }

    /**
     * @return the prestamoAgregarle
     */
    public Prestamo getPrestamoAgregarle() {
        return prestamoAgregarle;
    }

    /**
     * @param prestamoAgregarle the prestamoAgregarle to set
     */
    public void setPrestamoAgregarle(Prestamo prestamoAgregarle) {
        this.prestamoAgregarle = prestamoAgregarle;
    }

}
