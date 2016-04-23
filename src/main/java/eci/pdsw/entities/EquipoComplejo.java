/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eci.pdsw.entities;

import eci.pdsw.persistence.PersistenceException;
import java.util.Objects;

/**
 *
 * @author David Useche
 */
public class EquipoComplejo implements Comparable<EquipoComplejo> {
    int id_Eq;
    private boolean asegurado;
    private boolean disponibilidad;
    private String estado;
    private String serial;
    private int placa;
    private String marca;
    private Modelo modelo_Eq;

    public EquipoComplejo(Modelo mod,String mar, String ser)throws PersistenceException {
        if(mod==null) throw new PersistenceException("El equipo no posee modelo");
        marca=mar;
        serial=ser;
        modelo_Eq=mod;
    }

    public EquipoComplejo(int id_Eq, boolean asegurado, boolean disponibilidad, String estado, String serial, int placa, String marca, Modelo modelo_Eq) {
        this.id_Eq = id_Eq;
        this.asegurado = asegurado;
        this.disponibilidad = disponibilidad;
        this.estado = estado;
        this.serial = serial;
        this.placa = placa;
        this.marca = marca;
        this.modelo_Eq = modelo_Eq;
    }

    public EquipoComplejo() {
    }

    public int getId_Eq() {
        return id_Eq;
    }

    public void setId_Eq(int id_Eq) {
        this.id_Eq = id_Eq;
    }
    
    public void setId(int id){
        this.id_Eq=id;
    }
    
    public int getId(){
        return id_Eq;
    }
    
    public void setAsegurado(boolean ase){
        this.asegurado=ase;
    }
    
    public boolean getAsegurado(){
        return asegurado;
    }
    
    public void setDisponibilidad(boolean dis){
        this.disponibilidad=dis;
    }
    
    public boolean getDisponibilidad(){
        return disponibilidad;
    }
    
    public void setEstado(String est){
        this.estado=est;
    }
    
    public String getEstado(){
        return estado;
    }
    
    public void setSerial(String ser){
        this.serial=ser;
    }
    
    public String getSerial(){
        return serial;
    }
    
    public void setPlaca(int pla){
        this.placa=pla;
    }
    
    public int getPlaca(){
        return placa;
    }


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Modelo getModelo_Eq() {
        return modelo_Eq;
    }

    public void setModelo_Eq(Modelo modelo_Eq) {
        this.modelo_Eq = modelo_Eq;
    }
    
    @Override
    public String toString(){
        String res="EquipoComplejo:["+id_Eq+","+asegurado+","+disponibilidad+","+estado+","+serial+","+placa+","+marca+"]\n";
        res+=modelo_Eq.toString();
        return res;
    }

    @Override
    public int compareTo(EquipoComplejo o) {
        if(modelo_Eq.getValorComercial()<o.modelo_Eq.getValorComercial()) return -1;
        else return 1;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EquipoComplejo other = (EquipoComplejo) obj;
        if (this.placa != other.placa) {
            return false;
        }
        if (!Objects.equals(this.serial, other.serial)) {
            return false;
        }
        if (!Objects.equals(this.marca, other.marca)) {
            return false;
        }
        return true;
    }
    
}
