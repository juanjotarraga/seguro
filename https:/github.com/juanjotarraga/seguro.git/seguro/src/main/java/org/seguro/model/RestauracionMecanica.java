package org.seguro.model;

// Generated 11-ago-2015 18:28:13 by Hibernate Tools 4.3.1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RestauracionMecanica generated by hbm2java
 */
@Entity
@Table(name = "restauracion_mecanica"
      , schema = "public")
public class RestauracionMecanica implements java.io.Serializable
{

   private int idRestauracionMecanica;
   private ServicioMecanico servicioMecanico;
   private Siniestro siniestro;
   private TallerMecanico tallerMecanico;
   private String descripcion;
   private Double monto;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;

   public RestauracionMecanica()
   {
   }

   public RestauracionMecanica(int idRestauracionMecanica)
   {
      this.idRestauracionMecanica = idRestauracionMecanica;
   }

   public RestauracionMecanica(int idRestauracionMecanica, ServicioMecanico servicioMecanico, Siniestro siniestro, TallerMecanico tallerMecanico, String descripcion, Double monto, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado)
   {
      this.idRestauracionMecanica = idRestauracionMecanica;
      this.servicioMecanico = servicioMecanico;
      this.siniestro = siniestro;
      this.tallerMecanico = tallerMecanico;
      this.descripcion = descripcion;
      this.monto = monto;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
   }

   @Id
   @Column(name = "id_restauracion_mecanica", unique = true, nullable = false)
   public int getIdRestauracionMecanica()
   {
      return this.idRestauracionMecanica;
   }

   public void setIdRestauracionMecanica(int idRestauracionMecanica)
   {
      this.idRestauracionMecanica = idRestauracionMecanica;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_servicio_mecanico")
   public ServicioMecanico getServicioMecanico()
   {
      return this.servicioMecanico;
   }

   public void setServicioMecanico(ServicioMecanico servicioMecanico)
   {
      this.servicioMecanico = servicioMecanico;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_siniestro")
   public Siniestro getSiniestro()
   {
      return this.siniestro;
   }

   public void setSiniestro(Siniestro siniestro)
   {
      this.siniestro = siniestro;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_taller_mecanico")
   public TallerMecanico getTallerMecanico()
   {
      return this.tallerMecanico;
   }

   public void setTallerMecanico(TallerMecanico tallerMecanico)
   {
      this.tallerMecanico = tallerMecanico;
   }

   @Column(name = "descripcion", length = 500)
   public String getDescripcion()
   {
      return this.descripcion;
   }

   public void setDescripcion(String descripcion)
   {
      this.descripcion = descripcion;
   }

   @Column(name = "monto", precision = 17, scale = 17)
   public Double getMonto()
   {
      return this.monto;
   }

   public void setMonto(Double monto)
   {
      this.monto = monto;
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
