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
 * CentroSalud generated by hbm2java
 */
@Entity
@Table(name = "centro_salud"
      , schema = "public")
public class CentroSalud implements java.io.Serializable
{

   private int idCentroSalud;
   private String nombre;
   private String flagEstado;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;
   private Set<RecuperacionMedica> recuperacionMedicas = new HashSet<RecuperacionMedica>(0);

   public CentroSalud()
   {
   }

   public CentroSalud(int idCentroSalud)
   {
      this.idCentroSalud = idCentroSalud;
   }

   public CentroSalud(int idCentroSalud, String nombre, String flagEstado, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado, Set<RecuperacionMedica> recuperacionMedicas)
   {
      this.idCentroSalud = idCentroSalud;
      this.nombre = nombre;
      this.flagEstado = flagEstado;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
      this.recuperacionMedicas = recuperacionMedicas;
   }

   @Id
   @Column(name = "id_centro_salud", unique = true, nullable = false)
   public int getIdCentroSalud()
   {
      return this.idCentroSalud;
   }

   public void setIdCentroSalud(int idCentroSalud)
   {
      this.idCentroSalud = idCentroSalud;
   }

   @Column(name = "nombre", length = 200)
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

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "centroSalud")
   public Set<RecuperacionMedica> getRecuperacionMedicas()
   {
      return this.recuperacionMedicas;
   }

   public void setRecuperacionMedicas(Set<RecuperacionMedica> recuperacionMedicas)
   {
      this.recuperacionMedicas = recuperacionMedicas;
   }

}
