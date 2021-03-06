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
 * SiniestroFoto generated by hbm2java
 */
@Entity
@Table(name = "siniestro_foto"
      , schema = "public")
public class SiniestroFoto implements java.io.Serializable
{

   private int idSiniestroFoto;
   private Siniestro siniestro;
   private String fotoFrontal;
   private String fotoLateralDerecho;
   private String fotoLateralIzquierdo;
   private String fotoTrasera;
   private String fotoAccesorio;
   private String fotoOtros;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;

   public SiniestroFoto()
   {
   }

   public SiniestroFoto(int idSiniestroFoto)
   {
      this.idSiniestroFoto = idSiniestroFoto;
   }

   public SiniestroFoto(int idSiniestroFoto, Siniestro siniestro, String fotoFrontal, String fotoLateralDerecho, String fotoLateralIzquierdo, String fotoTrasera, String fotoAccesorio, String fotoOtros, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado)
   {
      this.idSiniestroFoto = idSiniestroFoto;
      this.siniestro = siniestro;
      this.fotoFrontal = fotoFrontal;
      this.fotoLateralDerecho = fotoLateralDerecho;
      this.fotoLateralIzquierdo = fotoLateralIzquierdo;
      this.fotoTrasera = fotoTrasera;
      this.fotoAccesorio = fotoAccesorio;
      this.fotoOtros = fotoOtros;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
   }

   @Id
   @Column(name = "id_siniestro_foto", unique = true, nullable = false)
   public int getIdSiniestroFoto()
   {
      return this.idSiniestroFoto;
   }

   public void setIdSiniestroFoto(int idSiniestroFoto)
   {
      this.idSiniestroFoto = idSiniestroFoto;
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

   @Column(name = "foto_frontal", length = 200)
   public String getFotoFrontal()
   {
      return this.fotoFrontal;
   }

   public void setFotoFrontal(String fotoFrontal)
   {
      this.fotoFrontal = fotoFrontal;
   }

   @Column(name = "foto_lateral_derecho", length = 200)
   public String getFotoLateralDerecho()
   {
      return this.fotoLateralDerecho;
   }

   public void setFotoLateralDerecho(String fotoLateralDerecho)
   {
      this.fotoLateralDerecho = fotoLateralDerecho;
   }

   @Column(name = "foto_lateral_izquierdo", length = 200)
   public String getFotoLateralIzquierdo()
   {
      return this.fotoLateralIzquierdo;
   }

   public void setFotoLateralIzquierdo(String fotoLateralIzquierdo)
   {
      this.fotoLateralIzquierdo = fotoLateralIzquierdo;
   }

   @Column(name = "foto_trasera", length = 200)
   public String getFotoTrasera()
   {
      return this.fotoTrasera;
   }

   public void setFotoTrasera(String fotoTrasera)
   {
      this.fotoTrasera = fotoTrasera;
   }

   @Column(name = "foto_accesorio", length = 200)
   public String getFotoAccesorio()
   {
      return this.fotoAccesorio;
   }

   public void setFotoAccesorio(String fotoAccesorio)
   {
      this.fotoAccesorio = fotoAccesorio;
   }

   @Column(name = "foto_otros", length = 200)
   public String getFotoOtros()
   {
      return this.fotoOtros;
   }

   public void setFotoOtros(String fotoOtros)
   {
      this.fotoOtros = fotoOtros;
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
