package org.seguro.model;

// Generated 11-ago-2015 18:28:13 by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * DetalleTasaCondicion generated by hbm2java
 */
@Entity
@Table(name = "detalle_tasa_condicion"
      , schema = "public")
public class DetalleTasaCondicion implements java.io.Serializable
{

   private int idDetalleTasaCondicion;
   private Condicion condicion;
   private Tasa tasa;
   private String observaciones;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;

   public DetalleTasaCondicion()
   {
   }

   public DetalleTasaCondicion(int idDetalleTasaCondicion)
   {
      this.idDetalleTasaCondicion = idDetalleTasaCondicion;
   }

   public DetalleTasaCondicion(int idDetalleTasaCondicion, Condicion condicion, Tasa tasa, String observaciones, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado)
   {
      this.idDetalleTasaCondicion = idDetalleTasaCondicion;
      this.condicion = condicion;
      this.tasa = tasa;
      this.observaciones = observaciones;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
   }

   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   @Column(name = "id_detalle_tasa_condicion", unique = true, nullable = false, insertable=false)
   public int getIdDetalleTasaCondicion()
   {
      return this.idDetalleTasaCondicion;
   }

   public void setIdDetalleTasaCondicion(int idDetalleTasaCondicion)
   {
      this.idDetalleTasaCondicion = idDetalleTasaCondicion;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_condicion")
   public Condicion getCondicion()
   {
      return this.condicion;
   }

   public void setCondicion(Condicion condicion)
   {
      this.condicion = condicion;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_tasa")
   public Tasa getTasa()
   {
      return this.tasa;
   }

   public void setTasa(Tasa tasa)
   {
      this.tasa = tasa;
   }

   @Column(name = "observaciones", length = 200)
   public String getObservaciones()
   {
      return this.observaciones;
   }

   public void setObservaciones(String observaciones)
   {
      this.observaciones = observaciones;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_reg", length = 29)
   public Date getFechaReg()
   {
      return this.fechaReg;
   }

   public void setFechaReg(Date fechaReg)
   {
      this.fechaReg = fechaReg;
   }

   @Column(name = "usuario_reg", length = 30)
   public String getUsuarioReg()
   {
      return this.usuarioReg;
   }

   public void setUsuarioReg(String usuarioReg)
   {
      this.usuarioReg = usuarioReg;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_mod", length = 29)
   public Date getFechaMod()
   {
      return this.fechaMod;
   }

   public void setFechaMod(Date fechaMod)
   {
      this.fechaMod = fechaMod;
   }

   @Column(name = "usuario_mod", length = 30)
   public String getUsuarioMod()
   {
      return this.usuarioMod;
   }

   public void setUsuarioMod(String usuarioMod)
   {
      this.usuarioMod = usuarioMod;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_borrado", length = 29)
   public Date getFechaBorrado()
   {
      return this.fechaBorrado;
   }

   public void setFechaBorrado(Date fechaBorrado)
   {
      this.fechaBorrado = fechaBorrado;
   }

   @Column(name = "usuario_borrado", length = 30)
   public String getUsuarioBorrado()
   {
      return this.usuarioBorrado;
   }

   public void setUsuarioBorrado(String usuarioBorrado)
   {
      this.usuarioBorrado = usuarioBorrado;
   }

}
