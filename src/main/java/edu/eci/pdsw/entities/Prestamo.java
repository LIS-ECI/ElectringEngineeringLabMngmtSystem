/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.entities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 *
 * @author Hugo Alvarez
 */
public abstract class Prestamo implements Comparable<Prestamo> {

    protected Timestamp fechaInicio;
    protected Timestamp fechaEstimadaDeEntrega;
    protected Timestamp fechaRealEntregada;

    protected long valorTotal;

    protected Set<EquipoComplejo> equiposComplejosPrestados;
    protected Set<EquipoComplejo> equiposComplejosFaltantes;
    protected Set<EquipoSencillo> equiposSencillosPrestados;
    protected Set<EquipoSencillo> equiposSencillosFaltantes;
    protected Persona elQuePideElPrestamo;
    protected String tipo_prestamo;

    @Override
    public boolean equals(Object obj) {
        Prestamo p = (Prestamo) obj;
        boolean check = true;
        check = fechaInicio.equals(p.getFechaInicio());
        check = check && elQuePideElPrestamo.equals(p.getElQuePideElPrestamo());
        if(getEquiposComplejosPrestados()!=null && p.getEquiposComplejosPrestados()!=null && check){
            check=check && getEquiposComplejosPrestados().size()==p.getEquiposComplejosPrestados().size();
            for (EquipoComplejo ec : getEquiposComplejosPrestados()) {
                check=check && p.getEquiposComplejosPrestados().contains(ec);
            }
        }
        if(getEquiposSencillosPrestados()!=null && p.getEquiposSencillosPrestados()!=null && check){
            check=check && getEquiposSencillosPrestados().size()==p.getEquiposSencillosPrestados().size();
            for (EquipoSencillo es : getEquiposSencillosPrestados()) {
                check=check && p.getEquiposSencillosPrestados().contains(es);
            }
        }
        return check;
        //return fechaInicio.equals(p.getFechaInicio()) && elQuePideElPrestamo.equals(p.getElQuePideElPrestamo());
    }

    /**
     * Obj: Conocer la hora actual
     *
     * @return la hora actual
     */
    public static Timestamp currDate() {
        Timestamp timestamp = new Timestamp(Calendar.getInstance(TimeZone.getTimeZone("GMT-5")).getTimeInMillis());
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss.SS");

        fmt.setTimeZone(TimeZone.getTimeZone("GMT-5"));

        //System.out.println(fmt.format(timestamp));
        //timestamp=timestamp.valueOf(fmt.format(timestamp));
        return timestamp;
    }

    @Override
    public abstract String toString();

    /**
     * Obj: Saber si el prestamo esta en mora o no. Pre: Ninguna. Pos: devolver
     * True si el prestamo esta en mora o no.
     *
     * @return True, si el prestamo esta en mora, False d.l.c
     */
    public abstract boolean enMora();

    /**
     * @return the equiposComplejosFaltantes
     */
    public Set<EquipoComplejo> getEquiposComplejosFaltantes() {
        if (equiposComplejosFaltantes == null) {
            equiposComplejosFaltantes = new HashSet<>();
        }
        setEquiposComplejosFaltantes(equiposComplejosPrestados);
        return equiposComplejosFaltantes;
    }

    /**
     * @return the fechaInicio
     */
    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @return the fechaEstimadaDeEntrega
     */
    public Timestamp getFechaEstimadaDeEntrega() {
        return fechaEstimadaDeEntrega;
    }

    /**
     * @return the fechaRealEntregada
     */
    public Timestamp getFechaRealEntregada() {
        return fechaRealEntregada;
    }

    /**
     * @return the equiposComplejosPrestados
     */
    public Set<EquipoComplejo> getEquiposComplejosPrestados() {
        if (equiposComplejosPrestados != null) {
            setEquiposComplejosFaltantes(equiposComplejosPrestados);
        }
        return equiposComplejosPrestados;
    }

    /**
     * Actualiza un equipo complejo prestado
     *
     * @param equipo a actualizar
     * @throws PrestamoException si el equipo no esta prestado
     */
    public void updateEquipoComplejo(EquipoComplejo equipo) throws PrestamoException {
        if (!equiposComplejosPrestados.contains(equipo)) {
            throw new PrestamoException("El equipo no se encuentra en este prestamo");
        }
        equiposComplejosPrestados.remove(equipo);
        equiposComplejosPrestados.add(equipo);
    }

    /**
     * @param equiposComplejosPrestados the equiposComplejosFaltantes to set
     */
    public void setEquiposComplejosFaltantes(Set<EquipoComplejo> equiposComplejosPrestados) {
        //OBTENER LOS EQUIPOS DE DB
        equiposComplejosFaltantes = new HashSet<>();
        if (equiposComplejosPrestados != null) {
            for (EquipoComplejo ec : equiposComplejosPrestados) {
                if (ec.getEstado().equalsIgnoreCase(EquipoComplejo.diario) || ec.getEstado().equalsIgnoreCase(EquipoComplejo.indefinido) || ec.getEstado().equalsIgnoreCase(EquipoComplejo.p24h) || ec.getEstado().equalsIgnoreCase(EquipoComplejo.semestre)) {
                    equiposComplejosFaltantes.add(ec);
                }
            }
        }
        //if(terminado())fechaRealEntregada=new Timestamp(Calendar.getInstance(TimeZone.getTimeZone("GMT-5")).getTimeInMillis());
    }

    /**
     * @return the elQuePideElPrestamo
     */
    public Persona getElQuePideElPrestamo() {
        return elQuePideElPrestamo;
    }

    /**
     *
     * @param equiposComplejosP
     */
    public void setEquiposComplejosPrestados(Set<EquipoComplejo> equiposComplejosP) {
        Set<EquipoComplejo> tmp = new HashSet<>();
        tmp.addAll(equiposComplejosP);
        equiposComplejosP.addAll(tmp);
        this.equiposComplejosPrestados = equiposComplejosP;
        setEquiposComplejosFaltantes(equiposComplejosPrestados);
    }

    /**
     *
     * @param elQuePideElPrestamo
     */
    public void setElQuePideElPrestamo(Persona elQuePideElPrestamo) {
        this.elQuePideElPrestamo = elQuePideElPrestamo;
    }

    /**
     *
     * @param equiposSencillosP
     */
    public void setEquiposSencillosPrestados(Set<EquipoSencillo> equiposSencillosP) {
        Set<EquipoSencillo> tmp = new HashSet<>();
        tmp.addAll(equiposSencillosP);
        equiposSencillosP.addAll(tmp);
        this.equiposSencillosPrestados = equiposSencillosP;
        setEquiposSencillosFaltantes(equiposSencillosPrestados);
        //Collections.sort(equiposSencillosFaltantes2);
    }

    /**
     *
     * @return el dueño del prestamo
     */
    public String getTipo_prestamo() {
        return tipo_prestamo;
    }

    /**
     *
     * @param tipo_prestamo
     */
    public void setTipo_prestamo(String tipo_prestamo) {
        this.tipo_prestamo = tipo_prestamo;
    }

    @Override
    public abstract int compareTo(Prestamo o);

    /**
     * @return the equiposSencillosFaltantes
     */
    public Set<EquipoSencillo> getEquiposSencillosFaltantes() {
        
        if (equiposSencillosFaltantes == null) {
            equiposSencillosFaltantes = new HashSet<>();
        }
        //Collections.sort(equiposSencillosFaltantes2);
        setEquiposSencillosFaltantes(equiposSencillosPrestados);
        return equiposSencillosFaltantes;
    }

    /**
     * @param equiposSencillosFaltantes the equiposSencillosFaltantes to set
     */
    public void setEquiposSencillosFaltantes(Set<EquipoSencillo> equiposSencillosFaltantes) {
        this.equiposSencillosFaltantes = new HashSet<>();
        //System.out.println(">>>>>>>>>>>>>>>>>>>><si entro al set");
        if (equiposSencillosFaltantes != null) {
            //System.out.println(Arrays.toString(equiposSencillosFaltantes.toArray()));
            for (EquipoSencillo es : equiposSencillosFaltantes) {
                //System.out.println("si esta en el for "+es.getNombre()+" "+es.getCantidadTotal());
                if (es.getCantidadTotal() > 0) {
                    this.equiposSencillosFaltantes.add(es);
                }
            }
        }
    }

    /**
     *
     * @return return the equiposSencillosPrestados
     */
    public Set<EquipoSencillo> getEquiposSencillosPrestados() {
        if (equiposSencillosPrestados != null) {
            setEquiposSencillosFaltantes(equiposSencillosPrestados);
        }
        return equiposSencillosPrestados;
    }

    /**
     * Me dice si un prestamo ha terminado
     *
     * @return si el prestamo ya termino
     */
    public boolean terminado() {
        if (equiposSencillosFaltantes == null) {
            equiposSencillosFaltantes = new HashSet<>();
        }
        if (equiposComplejosFaltantes == null) {
            equiposComplejosFaltantes = new HashSet<>();
        }
        return equiposComplejosFaltantes.isEmpty() && equiposSencillosFaltantes.isEmpty();
    }

    /**
     *
     * @param fechaInicio
     */
    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     *
     * @param fechaEstimadaDeEntrega
     */
    public void setFechaEstimadaDeEntrega(Timestamp fechaEstimadaDeEntrega) {
        this.fechaEstimadaDeEntrega = fechaEstimadaDeEntrega;
    }

    /**
     *
     * @param fechaRealEntregada
     */
    public void setFechaRealEntregada(Timestamp fechaRealEntregada) {
        this.fechaRealEntregada = fechaRealEntregada;
    }

    /**
     * Me dice si un equipo complejo falta por entregar en este prestamo
     *
     * @param equipo a buscar
     * @return si un equipo complejo falta por entregar en este prestamo
     */
    public boolean isFaltante(EquipoComplejo equipo) {
        return getEquiposComplejosFaltantes().contains(equipo);
    }

    /**
     * Me dice si un equipo sencillo falta por entregar en este prestamo
     *
     * @param equipo a buscar
     * @return si un equipo sencillo falta por entregar en este prestamo
     */
    public boolean isFaltante(EquipoSencillo equipo) {
        return getEquiposSencillosFaltantes().contains(equipo);
    }

    /**
     * Obtiene un equipo sencillo
     *
     * @param equipo a obtener
     * @return el equipo que esta en el prestamo
     * @throws PrestamoException si el equipo no esta en los faltantes del
     * prestamo
     */
    public EquipoSencillo getSencillo(EquipoSencillo equipo) throws PrestamoException {
        EquipoSencillo ans = null;
        if (!equiposSencillosFaltantes.contains(equipo)) {
            throw new PrestamoException(PrestamoException.OBJETO_SENCILLO_NO_ENCONTRADO);
        }
        for (EquipoSencillo es : equiposSencillosFaltantes) {
            if (es.equals(equipo)) {
                ans = es;
            }
        }
        return ans;
    }

    /**
     * @return the valorTotal
     */
    public long getValorTotal() {
        setValorTotal(0);
        return valorTotal;
    }

    /**
     * @param valorTotal the valorTotal to set
     */
    public void setValorTotal(long valorTotal) {
        this.valorTotal = 0;
        for (EquipoComplejo ec : getEquiposComplejosFaltantes()) {
            this.valorTotal += ec.getModelo_Eq().getValorComercial();
        }
        for (EquipoSencillo es : getEquiposSencillosFaltantes()) {
            this.valorTotal += es.getValorComercial() * es.getCantidadTotal();
        }
    }

    /**
     * Calcular la fecha estimada
     *
     * @param tipo
     * @return la fecha estimada
     */
    public static Timestamp calcularFechaEstimada(String tipo) {
        Timestamp fecha = null;
        Calendar calen = Calendar.getInstance();
        calen.setTime(currDate());
        if (tipo.equalsIgnoreCase(EquipoComplejo.p24h)) {
            calen.add(Calendar.DAY_OF_MONTH, 1);
            fecha = new Timestamp(calen.getTimeInMillis());
        } else if (tipo.equalsIgnoreCase(EquipoComplejo.diario)) {
            calen.set(Calendar.HOUR, 19);
            fecha = new Timestamp(calen.getTimeInMillis());
        } else if (tipo.equalsIgnoreCase(EquipoComplejo.semestre)) {
            if (Prestamo.currDate().getMonth() >= 1 && Prestamo.currDate().getMonth() < 5) {
                calen.set(Calendar.MONTH, 5);
            } else if (Prestamo.currDate().getMonth() == 6 || Prestamo.currDate().getMonth() == 7) {
                calen.set(Calendar.MONTH, 7);
            } else if (Prestamo.currDate().getMonth() >= 8 && Prestamo.currDate().getMonth() < 12) {
                calen.set(Calendar.MONTH, 12);
            }
            fecha = new Timestamp(calen.getTimeInMillis());
        }
        return fecha;
    }

    /**
     * Revisa si un prestamo esta activo o ya se cumlpio
     *
     * @return true si esta activo de lo contrario false
     */
    public boolean prestamoActivo() {
        boolean res = false;
        if (fechaRealEntregada==null) {
            res = true;
        } 
        return res;
    }
}
