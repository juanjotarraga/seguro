package org.seguro.model;

// Generated 11-ago-2015 18:28:13 by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TallerMecanico generated by hbm2java
 */
@Entity
@Table(name = "taller_mecanico"
      , schema = "public")
public class TallerMecanico implements java.io.Serializable
{

   private int idTallerMecanico;
   private String nombre;
   private String flagEstado;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;
   private Set<RestauracionMecanica> restauracionMecanicas = new HashSet<RestauracionMecanica>(0);

   public TallerMecanico()
   {
   }

   public TallerMecanico(int idTallerMecanico)
   {
      this.idTallerMecanico = idTallerMecanico;
   }

   public TallerMecanico(int idTallerMecanico, String nombre, String flagEstado, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado, Set<RestauracionMecanica> restauracionMecanicas)
   {
      this.idTallerMecanico = idTallerMecanico;
      this.nombre = nombre;
      this.flagEstado = flagEstado;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
      this.restauracionMecanicas = restauracionMecanicas;
   }

   @Id
   @Column(name = "id_taller_mecanico", unique = true, nullable = false)
   public int getIdTallerMecanico()
   {
      return this.idTallerMecanico;
   }

   public void setIdTallerMecanico(int idTallerMecanico)
   {
      this.idTallerMecanico = idTallerMecanico;
   }

   @Column(name = "nombre", length = 100)
   public String getNombre()
   {
      return this.nombre;
   }

   public void setNombre(String nombre)
   {
      this.nombre = nombre;
   }

   @Column(name = "flag_estado", length = 2)
   public String getFlagEstado()
   {
      return this.flagEstado;
   }

   public void setFlagEstado(String flagEstado)
   {
      this.flagEstado = flagEstado;
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

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "tallerMecanico")
   public Set<RestauracionMecanica> getRestauracionMecanicas()
   {
      return this.restauracionMecanicas;
   }

   public void setRestauracionMecanicas(Set<RestauracionMecanica> restauracionMecanicas)
   {
      this.restauracionMecanicas = restauracionMecanicas;
   }

}