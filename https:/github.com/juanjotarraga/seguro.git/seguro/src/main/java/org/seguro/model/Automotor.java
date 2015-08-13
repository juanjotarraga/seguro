package org.seguro.model;

// Generated 11-ago-2015 18:28:13 by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Automotor generated by hbm2java
 */
@Entity
@Table(name = "automotor"
      , schema = "public")
public class Automotor implements java.io.Serializable
{

   private int idAutomotor;
   private Modelo modelo;
   private Tipo tipo;
   private String ano;
   private String placa;
   private String color;
   private String traccion;
   private String chasis;
   private String motor;
   private Integer cc;
   private BigDecimal valorAsegurado;
   private String flagEstado;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;
   private Set<Poliza> polizas = new HashSet<Poliza>(0);

   public Automotor()
   {
   }

   public Automotor(int idAutomotor)
   {
      this.idAutomotor = idAutomotor;
   }

   public Automotor(int idAutomotor, Modelo modelo, Tipo tipo, String ano, String placa, String color, String traccion, String chasis, String motor, Integer cc, BigDecimal valorAsegurado, String flagEstado, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado, Set<Poliza> polizas)
   {
      this.idAutomotor = idAutomotor;
      this.modelo = modelo;
      this.tipo = tipo;
      this.ano = ano;
      this.placa = placa;
      this.color = color;
      this.traccion = traccion;
      this.chasis = chasis;
      this.motor = motor;
      this.cc = cc;
      this.valorAsegurado = valorAsegurado;
      this.flagEstado = flagEstado;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
      this.polizas = polizas;
   }

   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   @Column(name = "id_automotor", unique = true, nullable = false, insertable=false)
   public int getIdAutomotor()
   {
      return this.idAutomotor;
   }

   public void setIdAutomotor(int idAutomotor)
   {
      this.idAutomotor = idAutomotor;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_modelo")
   public Modelo getModelo()
   {
      return this.modelo;
   }

   public void setModelo(Modelo modelo)
   {
      this.modelo = modelo;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_tipo")
   public Tipo getTipo()
   {
      return this.tipo;
   }

   public void setTipo(Tipo tipo)
   {
      this.tipo = tipo;
   }

   @Column(name = "ano", length = 5)
   public String getAno()
   {
      return this.ano;
   }

   public void setAno(String ano)
   {
      this.ano = ano;
   }

   @Column(name = "placa", length = 30)
   public String getPlaca()
   {
      return this.placa;
   }

   public void setPlaca(String placa)
   {
      this.placa = placa;
   }

   @Column(name = "color", length = 30)
   public String getColor()
   {
      return this.color;
   }

   public void setColor(String color)
   {
      this.color = color;
   }

   @Column(name = "traccion", length = 30)
   public String getTraccion()
   {
      return this.traccion;
   }

   public void setTraccion(String traccion)
   {
      this.traccion = traccion;
   }

   @Column(name = "chasis", length = 30)
   public String getChasis()
   {
      return this.chasis;
   }

   public void setChasis(String chasis)
   {
      this.chasis = chasis;
   }

   @Column(name = "motor", length = 30)
   public String getMotor()
   {
      return this.motor;
   }

   public void setMotor(String motor)
   {
      this.motor = motor;
   }

   @Column(name = "cc")
   public Integer getCc()
   {
      return this.cc;
   }

   public void setCc(Integer cc)
   {
      this.cc = cc;
   }

   @Column(name = "valor_asegurado", precision = 18)
   public BigDecimal getValorAsegurado()
   {
      return this.valorAsegurado;
   }

   public void setValorAsegurado(BigDecimal valorAsegurado)
   {
      this.valorAsegurado = valorAsegurado;
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

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "automotor")
   public Set<Poliza> getPolizas()
   {
      return this.polizas;
   }

   public void setPolizas(Set<Poliza> polizas)
   {
      this.polizas = polizas;
   }

}
