package org.seguro.model;

// Generated 11-ago-2015 18:28:13 by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Tasa generated by hbm2java
 */
@Entity
@Table(name = "tasa"
      , schema = "public")
public class Tasa implements java.io.Serializable
{

   private int idTasa;
   private Compania compania;
   private Tipo tipo;
   private Integer mayorQue;
   private Integer menorQue;
   private BigDecimal primaContado;
   private BigDecimal primaCredito;
   private BigDecimal valorExtra;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;
   private Set<Poliza> polizas = new HashSet<Poliza>(0);
   private Set<DetalleTasaCondicion> detalleTasaCondicions = new HashSet<DetalleTasaCondicion>(0);

   public Tasa()
   {
   }

   public Tasa(int idTasa)
   {
      this.idTasa = idTasa;
   }

   public Tasa(int idTasa, Compania compania, Tipo tipo, Integer mayorQue, Integer menorQue, BigDecimal primaContado, BigDecimal primaCredito, BigDecimal valorExtra, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado, Set<Poliza> polizas, Set<DetalleTasaCondicion> detalleTasaCondicions)
   {
      this.idTasa = idTasa;
      this.compania = compania;
      this.tipo = tipo;
      this.mayorQue = mayorQue;
      this.menorQue = menorQue;
      this.primaContado = primaContado;
      this.primaCredito = primaCredito;
      this.valorExtra = valorExtra;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
      this.polizas = polizas;
      this.detalleTasaCondicions = detalleTasaCondicions;
   }

   @Id
   @Column(name = "id_tasa", unique = true, nullable = false)
   public int getIdTasa()
   {
      return this.idTasa;
   }

   public void setIdTasa(int idTasa)
   {
      this.idTasa = idTasa;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_compania")
   public Compania getCompania()
   {
      return this.compania;
   }

   public void setCompania(Compania compania)
   {
      this.compania = compania;
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

   @Column(name = "mayor_que")
   public Integer getMayorQue()
   {
      return this.mayorQue;
   }

   public void setMayorQue(Integer mayorQue)
   {
      this.mayorQue = mayorQue;
   }

   @Column(name = "menor_que")
   public Integer getMenorQue()
   {
      return this.menorQue;
   }

   public void setMenorQue(Integer menorQue)
   {
      this.menorQue = menorQue;
   }

   @Column(name = "prima_contado", precision = 18)
   public BigDecimal getPrimaContado()
   {
      return this.primaContado;
   }

   public void setPrimaContado(BigDecimal primaContado)
   {
      this.primaContado = primaContado;
   }

   @Column(name = "prima_credito", precision = 18)
   public BigDecimal getPrimaCredito()
   {
      return this.primaCredito;
   }

   public void setPrimaCredito(BigDecimal primaCredito)
   {
      this.primaCredito = primaCredito;
   }

   @Column(name = "valor_extra", precision = 18)
   public BigDecimal getValorExtra()
   {
      return this.valorExtra;
   }

   public void setValorExtra(BigDecimal valorExtra)
   {
      this.valorExtra = valorExtra;
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

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "tasa")
   public Set<Poliza> getPolizas()
   {
      return this.polizas;
   }

   public void setPolizas(Set<Poliza> polizas)
   {
      this.polizas = polizas;
   }

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "tasa")
   public Set<DetalleTasaCondicion> getDetalleTasaCondicions()
   {
      return this.detalleTasaCondicions;
   }

   public void setDetalleTasaCondicions(Set<DetalleTasaCondicion> detalleTasaCondicions)
   {
      this.detalleTasaCondicions = detalleTasaCondicions;
   }

}