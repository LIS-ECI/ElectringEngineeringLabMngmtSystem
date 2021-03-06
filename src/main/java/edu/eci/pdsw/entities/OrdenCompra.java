/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.entities;

import java.sql.Timestamp;

/**
 *
 * @author German
 */
public class OrdenCompra implements Comparable<OrdenCompra> {

    private Timestamp adquisicion;
    private Timestamp garantia;
    private String proveedor;
    private String activo;
    private String codigo;

    public OrdenCompra() {
    }

    /**
     * Constructor de orden de compra
     *
     * @param adq
     * @param gar
     * @param provee
     * @param act
     * @param cod
     * @throws EquipoException
     */
    public OrdenCompra(Timestamp adq, Timestamp gar, String provee, String act, String cod) throws EquipoException {
        if (cod == null || cod.length() == 0) {
            throw new EquipoException("Favor colocar un código de orden de compra adecuado");
        }
        if (act == null || act.length() == 0) {
            throw new EquipoException("Favor colocar un código activo adecuado");
        }
        if (provee == null || provee.length() == 0) {
            throw new EquipoException("Favor colocar un proveedor en la orden de compra del equipo adecuado");
        }
        if (adq == null) {
            throw new EquipoException("Favor colocar una fecha de adquisición adecuada");
        }
        if (gar == null) {
            throw new EquipoException("Favor colocar una fecha de garantía adecuada");
        }
        if (gar.before(adq)) {
            throw new EquipoException("Fecha de garatía inválida");
        }
        this.adquisicion = adq;
        this.garantia = gar;
        this.proveedor = provee;
        this.activo = act;
        this.codigo = cod;
    }

    //Por implementar 
    @Override
    public int compareTo(OrdenCompra o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        OrdenCompra oc = (OrdenCompra) obj;
        return oc.getAdquisicion().equals(adquisicion) && oc.getGarantia().equals(garantia) && oc.getProveedor().equals(proveedor) && oc.getCodigo().equals(codigo) && oc.getActivo().equals(activo);
    }

    /**
     * @return the adquisicion
     */
    public Timestamp getAdquisicion() {
        return adquisicion;
    }

    /**
     * @param adquisicion the adquisicion to set
     */
    public void setAdquisicion(Timestamp adquisicion) {
        this.adquisicion = adquisicion;
    }

    /**
     * @return the garantia
     */
    public Timestamp getGarantia() {
        return garantia;
    }

    /**
     * @param garantia the garantia to set
     */
    public void setGarantia(Timestamp garantia) {
        this.garantia = garantia;
    }

    /**
     * @return the proveedor
     */
    public String getProveedor() {
        return proveedor;
    }

    /**
     * @param proveedor the proveedor to set
     */
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public String toString() {
        String res = "Datos Generales:[" + adquisicion + "," + garantia + "," + proveedor
                + "," + activo + "," + codigo + "]\n";
        return res;
    }

    /**
     * @return the activo
     */
    public String getActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(String activo) {
        this.activo = activo;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
